package club.anlan.lanlife.kafka.pdf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2023/8/18 22:10
 */
@Component
@Slf4j
public class HtmlUtil {

    @Resource
    private SpringResourceTemplateResolver resolver;

    public String createHtml(String templateName, String name, Map<String, Object> map) {
        PrintWriter writer = null;
        TemplateEngine templateEngine = new TemplateEngine();
        try {
            // 1. 创建模板解析目录解析器
            Set<ITemplateResolver> templateResolvers = templateEngine.getTemplateResolvers();

            // 无配置模板解析路径,则代码配置
            log.info("context: {}", resolver);
            if (!templateResolvers.iterator().hasNext()) {
                // 2. 创建模板解析器 并设置相关属性
                resolver.setPrefix("classpath:template/");
                resolver.setSuffix(".html");
                resolver.setCharacterEncoding("UTF-8");
                resolver.setCacheable(false);
                // 不允许重复设置 否则会报错
                templateEngine.setTemplateResolver(resolver);
            }
            // 2. 模板上下文 主要存储Model参数
            Context context = new Context();
            if (map.size() > 0) {
                context.setVariables(map);
            }

            // 3. 创建输出文件
            File folder = new File("C:\\Users\\lan\\IdeaProjects\\lan_life\\lan-demo\\kafka-demo\\demo1\\src\\main\\resources\\template\\", name + ".html");

            //如果文件不存在,直接创建
            if (!folder.exists()) {
                folder.createNewFile();
            }

            // 5. 获取输出目标文件输出流
            writer = new PrintWriter(folder, "UTF-8");

            // 6. 生成静态模板参数1:template模板名称  参数2:上下文对象  参数3:目标文件输出流
            templateEngine.process(templateName, context, writer);
            log.info("http path: {}", folder.getAbsolutePath());
            // 返回生成文件路径
            return folder.getAbsolutePath();
        } catch (IOException e) {
            log.error("createHtml error, ", e);
        } finally {
            // flush输出流并关闭
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        return null;
    }
}
