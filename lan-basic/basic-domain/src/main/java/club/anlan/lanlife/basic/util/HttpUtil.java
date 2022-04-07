package club.anlan.lanlife.basic.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final String HTTP_CONTENT_CHARSET = "utf-8";

    public static final int MAX_TIME_OUT = 1000; //最大连接时间
    public static final int MAX_IDLE_TIME_OUT = 6000; //最长超时时间

    /**
     * get方法获取网页
     *
     * @param tempurl 网页链接
     * @return
     */
    public static String doSimpleGet(String tempurl) {
        return doSimpleGet(tempurl, "utf-8", null, 0, null, null, null, null);
    }

    /**
     * get方法获取网页
     *
     * @param tempurl
     * @param referer
     * @return
     */
    public static String doSimpleGet(String tempurl, String referer) {
        return doSimpleGet(tempurl, "utf-8", null, 0, null, null, referer, null);
    }

    /**
     * 配置请求头,get方法获取网页
     *
     * @param tempurl
     * @param referer
     * @param XRequestedWith
     * @return
     */
    public static String doSimpleGet(String tempurl, String referer, String xRequestedWith) {
        return doSimpleGet(tempurl, "utf-8", null, 0, null, null, referer, xRequestedWith);
    }


    public static String doSimpleGet(String tempurl, String charSet, String proxyHost, int proxyPort, String cookie,
                                     String userAgent, String referer, String xRequestedWith) {
        HttpClient client = new HttpClient();
        if (StringUtils.isNotEmpty(proxyHost)) {
            client.getHostConfiguration().setProxy(proxyHost, proxyPort);
        }
        client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
        client.getHttpConnectionManager().getParams().setSoTimeout(120000);
        StringBuilder sb = new StringBuilder();
        GetMethod method = new GetMethod(tempurl);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.getParams().setContentCharset(charSet);
        method.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        method.setRequestHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        method.setRequestHeader("Connection", "close");
        if (StringUtils.isNotEmpty(xRequestedWith)) {
            method.setRequestHeader("X-Requested-With", xRequestedWith);
        }
        if (StringUtils.isNotEmpty(userAgent)) {
            method.setRequestHeader("User-Agent", userAgent);
        } else {
            method.setRequestHeader("User-Agent",
                    "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:20.0) Gecko/20100101 Firefox/20.0");
        }
        if (StringUtils.isNotEmpty(cookie)) {
            method.setRequestHeader("Cookie", cookie);
        }

        if (StringUtils.isNotEmpty(referer)) {
            method.setRequestHeader("Referer", referer);
        }

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),
                        charSet));
                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    sb.append(tmp);
                    sb.append("\r\n");
                }
            } else {
                System.err.println("Response Code: " + statusCode);
            }
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        return sb.toString();
    }

    public static String doSimplePost(String url, Map<String, Object> params) {
        return doPost(url, params, null);
    }

    public static String doPost(String url, Map<String, Object> params, Map<String, String> headerParams) {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
        HttpClient httpClient = new HttpClient(httpConnectionManager);
        PostMethod postMethod = new PostMethod(url);
        try {
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, HTTP_CONTENT_CHARSET);
            if (!CollectionUtils.isEmpty(params)) {
                for (Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        postMethod.addParameter(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
            postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            postMethod.addRequestHeader(new Header("Connection", "close"));
            if (!CollectionUtils.isEmpty(headerParams)) {
                for (Entry<String, String> entry : headerParams.entrySet()) {
                    if (entry.getValue() != null) {
                        postMethod.addRequestHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            httpClient.executeMethod(postMethod);
            if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
                return postMethod.getResponseBodyAsString();
            } else {
                System.out.println(postMethod.getStatusCode());
                postMethod.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }

    public static String doPost(String url, String jsonStr, Map<String, String> headerParams) {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
        HttpClient httpClient = new HttpClient(httpConnectionManager);
        PostMethod postMethod = new PostMethod(url);
        try {
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, HTTP_CONTENT_CHARSET);
            if (!StringUtils.isEmpty(jsonStr)) {
                RequestEntity requestEntity = new StringRequestEntity(jsonStr, "text/xml", "UTF-8");
                postMethod.setRequestEntity(requestEntity);
            }
            postMethod.addRequestHeader(new Header("Connection", "close"));
            if (!CollectionUtils.isEmpty(headerParams)) {
                for (Entry<String, String> entry : headerParams.entrySet()) {
                    if (entry.getValue() != null) {
                        postMethod.addRequestHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            Header contentTypeHeader = postMethod.getRequestHeader("Content-Type");
            if (contentTypeHeader == null) {
                postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            }
            httpClient.executeMethod(postMethod);
            if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
                return postMethod.getResponseBodyAsString();
            } else {
                System.out.println(postMethod.getStatusCode());
                postMethod.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }

    public static String doPostWithLog(String url, String jsonStr, Map<String, String> headerParams) {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
        HttpClient httpClient = new HttpClient(httpConnectionManager);
        PostMethod postMethod = new PostMethod(url);
        try {
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, HTTP_CONTENT_CHARSET);
            if (!StringUtils.isEmpty(jsonStr)) {
                RequestEntity requestEntity = new StringRequestEntity(jsonStr, "text/xml", "UTF-8");
                postMethod.setRequestEntity(requestEntity);
            }
            postMethod.addRequestHeader(new Header("Connection", "close"));
            if (!CollectionUtils.isEmpty(headerParams)) {
                for (Entry<String, String> entry : headerParams.entrySet()) {
                    if (entry.getValue() != null) {
                        postMethod.addRequestHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            Header contentTypeHeader = postMethod.getRequestHeader("Content-Type");
            if (contentTypeHeader == null) {
                postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            }
            httpClient.executeMethod(postMethod);
            String responseStr = postMethod.getResponseBodyAsString();
            if (postMethod.getStatusCode() != HttpStatus.SC_OK) {
                logger.error("发送post请求失败，url-【{}】，返回码-【{}】，返回信息-【{}】", url, postMethod.getStatusCode(), responseStr);
                postMethod.abort();
                return null;
            } else {
                return postMethod.getResponseBodyAsString();
            }
        } catch (Exception e) {
            throw new RuntimeException("发送post请求失败，url-【" + url + "】", e);
        } finally {
            postMethod.releaseConnection();
        }
    }

    @SuppressWarnings("deprecation")
    public static String doXmlPost(String url, String xmlContext) {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
        HttpClient httpClient = new HttpClient(httpConnectionManager);
        PostMethod postMethod = new PostMethod(url);

        try {
            postMethod.addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
            postMethod.addRequestHeader("Pragma", "no-cache");
            postMethod.addRequestHeader("Cache-Control:", "no-cache");
            postMethod.setRequestBody(xmlContext);
            httpClient.executeMethod(postMethod);
            if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
                return postMethod.getResponseBodyAsString();
            } else {
                postMethod.abort();
            }
        } catch (Exception e) {
        } finally {
            postMethod.releaseConnection();
        }
        return null;
    }


    public static String doSimplePut(String url, Map<String, Object> params) {
        return doPut(url, params, null);
    }


    public static String doPut(String url, Map<String, Object> params, Map<String, String> headerParams) {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
        HttpClient httpClient = new HttpClient(httpConnectionManager);
        PutMethod putMethod = new PutMethod(url);
        try {
            if (!CollectionUtils.isEmpty(params)) {
                for (Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        putMethod.getParams().setParameter(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(headerParams)) {
                for (Entry<String, String> entry : headerParams.entrySet()) {
                    if (entry.getValue() != null) {
                        putMethod.addRequestHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            putMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            putMethod.addRequestHeader(new Header("Connection", "close"));
            httpClient.executeMethod(putMethod);
            if (putMethod.getStatusCode() == HttpStatus.SC_OK) {
                return putMethod.getResponseBodyAsString();
            } else {
                putMethod.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            putMethod.releaseConnection();
        }
        return null;
    }


    public static String doOssImgPost(String url, File file, String fileName, Map<String, String> headerParams) {
        try {
            MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
            httpConnectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
            HttpClient httpClient = new HttpClient(httpConnectionManager);
            if (file.exists()) {
                PostMethod postMethod = new PostMethod(url);
                if (!CollectionUtils.isEmpty(headerParams)) {
                    for (Entry<String, String> entry : headerParams.entrySet()) {
                        if (entry.getValue() != null) {
                            postMethod.addRequestHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
                FilePart filePart = new FilePart("file", fileName, file);
                Part[] parts = {filePart};
                MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, postMethod.getParams());
                postMethod.setRequestEntity(requestEntity);
                httpClient.executeMethod(postMethod);
                if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
                    Header objectUriHeader = postMethod.getResponseHeader("X-Dss-Object-Uri");
                    String objectUri = objectUriHeader.getValue();
                    Header reauestIdHeader = postMethod.getResponseHeader("X-Dss-Request-Id");
                    String resuestId = reauestIdHeader.getValue();
                    return objectUri;
                } else {
                    postMethod.abort();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean doOssImageDelete(String url) {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.closeIdleConnections(MAX_IDLE_TIME_OUT);
        HttpClient httpClient = new HttpClient(httpConnectionManager);
        DeleteMethod deleteMethod = new DeleteMethod(url);
        try {
            httpClient.executeMethod(deleteMethod);
            if (deleteMethod.getStatusCode() == HttpStatus.SC_OK) {
                Header objectUriHeader = deleteMethod.getResponseHeader("X-Dss-errcode");
                String errorCode = objectUriHeader.getValue();
                return StringUtils.equals(errorCode, "0");
            } else {
                deleteMethod.abort();
            }
        } catch (Exception e) {
        } finally {
            deleteMethod.releaseConnection();
        }
        return false;
    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader bufferReader = null;
        try {
            inputStream = request.getInputStream();
            bufferReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            char[] buffer = new char[1024];
            int len = -1;
            while ((len = bufferReader.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, len);
                sb.append(chunk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
			/*byte[] bytes = FileUtil.getBytes("D:\\111111A8JZ0.jpg");
			String byteContent = Base64CoderUtil.encode(bytes);
			String fileName = "浙A8JZ0.jpg";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("byteContent", byteContent);
			params.put("fileName", fileName);
			Map<String, String> headerParams = new HashMap<String, String>();
			headerParams.put("Content-Type","application/x-www-form-urlencoded");
			String result = HttpUtil.doPost("http://localhost/kingdo/integration/image/upload/byte",params, headerParams);
			System.out.println(result);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
