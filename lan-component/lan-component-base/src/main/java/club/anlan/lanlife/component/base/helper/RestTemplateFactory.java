package club.anlan.lanlife.component.base.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * RestTemplateFactory
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/30 1:02
 */
@Slf4j
public class RestTemplateFactory {

    private RestTemplateFactory() {
    }

    public static RestTemplate getClient(int timeoutMilliseconds) {
        return wrapper(timeoutMilliseconds);
    }

    private static RestTemplate wrapper(int timeoutMilliseconds) {
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30L, TimeUnit.SECONDS);
        pollingConnectionManager.setMaxTotal(500);
        pollingConnectionManager.setDefaultMaxPerRoute(500);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        //默认不重试
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, true));
        httpClientBuilder.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE);
        httpClientBuilder.setSSLSocketFactory(getSslSocketFactory());
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"));
        headers.add(new BasicHeader("Connection", "keep-alive"));
        httpClientBuilder.setDefaultHeaders(headers);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectTimeout(timeoutMilliseconds);
        clientHttpRequestFactory.setReadTimeout(timeoutMilliseconds);
        clientHttpRequestFactory.setConnectionRequestTimeout(200);
        clientHttpRequestFactory.setBufferRequestBody(false);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new GsonHttpMessageConverter());
        messageConverters.add(new ByteArrayHttpMessageConverter());
        RestTemplate restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    private static SSLConnectionSocketFactory getSslSocketFactory() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            return null;
        }
        return new SSLConnectionSocketFactory(sslContext);
    }

}
