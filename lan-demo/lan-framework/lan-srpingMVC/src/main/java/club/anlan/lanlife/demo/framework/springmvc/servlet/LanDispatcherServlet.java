package club.anlan.lanlife.demo.framework.springmvc.servlet;

import club.anlan.lanlife.demo.framework.springmvc.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * DispatcherServlet
 *
 * @author lan
 * @version 1.0
 * @date 2021/9/6 22:40
 */
@Slf4j
public class LanDispatcherServlet extends HttpServlet {

    /**
     * 保存 application.properties 配置文件中内容
     */
    private Properties contextConfig = new Properties();

    /**
     * 保存扫描到的类名
     */
    private List<String> classNames = new ArrayList<String>();

    /**
     * IOC 容器
     */
    private Map<String, Object> ioc = new HashMap<String, Object>();

    /**
     * 保存 method 和 url 对应关系
     */
    private List<HandlerMapping> handlerMapping = new ArrayList<HandlerMapping>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("error: " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }


    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {


        HandlerMapping handlerMapping = getHandler(req);
        if (handlerMapping == null) {
            resp.getWriter().write("404 not found !!!");
            return;
        }

        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        Object[] paramValues = new Object[paramTypes.length];
        Map<String, String[]> params = req.getParameterMap();

        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue())
                    .replaceAll("\\[\\]", "")
                    .replaceAll("\\s", ",");

            if (handlerMapping.paramIndexMapping.containsKey(param.getKey())) {
                paramValues[handlerMapping.paramIndexMapping.get(param.getKey())] = value;
            }
        }

        paramValues[handlerMapping.paramIndexMapping.get(HttpServletRequest.class.getName())] = req;
        paramValues[handlerMapping.paramIndexMapping.get(HttpServletResponse.class.getName())] = resp;

        handlerMapping.method.invoke(handlerMapping.getController(), paramValues);
    }

    private HandlerMapping getHandler(HttpServletRequest req) {
        if (handlerMapping.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url.replaceAll(contextPath, "").replaceAll("/+", "/");
        for (HandlerMapping mapping : this.handlerMapping) {
            if (url.equals(mapping.getUrl())) {
                return mapping;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        // 1. 加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 2. 扫描相关类
        doScanner(contextConfig.getProperty("scanPackage"));

        // 3. 初始化扫描的类， 并且将它们放道 IOC 容器中
        doInstance();

        // 4. 完成依赖注入
        doAutowired();

        // 5. 初始化 HandlerMapping
        initHandlerMapping();


        System.out.println("LanDispatcherServlet 初始化完毕");
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if (!clazz.isAnnotationPresent(LanController.class)) {
                continue;
            }

            String baseUrl = "";
            if (clazz.isAnnotationPresent(LanRequestMapping.class)) {
                LanRequestMapping lanRequestMapping = clazz.getAnnotation(LanRequestMapping.class);
                baseUrl = lanRequestMapping.value();
            }

            for (Method method : clazz.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(LanRequestMapping.class)) {
                    continue;
                }

                LanRequestMapping lanRequestMapping = method.getAnnotation(LanRequestMapping.class);
                String url = (baseUrl + "/" + lanRequestMapping.value()).replaceAll("/+", "/");

                handlerMapping.add(new HandlerMapping(url, method, entry.getValue()));
                System.out.println("Mapped url-method, " + url + "-" + method);
            }

        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : fields) {
                if (!field.isAnnotationPresent(LanAutowired.class)) {
                    continue;
                }
                LanAutowired autowired = field.getAnnotation(LanAutowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                try {
                    field.setAccessible(true);
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    System.out.println("赋值失败：{}" + e.getMessage());
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(LanController.class)) {
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(LanService.class)) {
                    LanService service = clazz.getAnnotation(LanService.class);
                    String beanName = service.value();
                    if ("".endsWith(beanName.trim())) {
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);

                    // 根据类型自动赋值
                    for (Class<?> cla : clazz.getInterfaces()) {
                        if (ioc.containsKey(cla.getName())) {
                            throw new Exception(" The " + cla.getName() + " is exists!!");
                        }
                        ioc.put(cla.getName(), instance);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("实例化失败 ：{}" + e.getMessage());
        }
    }

    /**
     * 转化字符串第一个字幕为小写
     *
     * @param simpleName clazz name
     * @return bean name
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classFiles = new File(url.getFile());
        for (File file : classFiles.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = scanPackage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }
    }


    private void doLoadConfig(String contextConfigLocation) {

        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            contextConfig.load(fis);
        } catch (IOException e) {
            System.out.println("加载配置失败 ：{}" + e.getMessage());
        }
    }


    class HandlerMapping {

        private String url;

        private Method method;

        private Object controller;

        /**
         * 参数-位置
         */
        private Map<String, Integer> paramIndexMapping;

        public HandlerMapping(String url, Method method, Object controller) {
            this.url = url;
            this.method = method;
            this.controller = controller;
            paramIndexMapping = new HashMap<String, Integer>();
            putParamIndexMapping(method);
        }

        private void putParamIndexMapping(Method method) {

            Annotation[][] pa = method.getParameterAnnotations();
            for (int j = 0; j < pa.length; j++) {
                for (Annotation an : pa[j]) {
                    if (an instanceof LanRequestParam) {
                        String paramName = ((LanRequestParam) an).value();
                        if (!"".equals(paramName.trim())) {
                            paramIndexMapping.put(paramName, j);
                        }
                    }
                }
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> type = parameterTypes[i];
                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramIndexMapping.put(type.getName(), i);
                }
            }
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }

        public Map<String, Integer> getParamIndexMapping() {
            return paramIndexMapping;
        }

        public void setParamIndexMapping(Map<String, Integer> paramIndexMapping) {
            this.paramIndexMapping = paramIndexMapping;
        }
    }
}
