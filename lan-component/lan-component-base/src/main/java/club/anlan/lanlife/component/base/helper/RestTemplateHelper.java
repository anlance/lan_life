package club.anlan.lanlife.component.base.helper;

import club.anlan.lanlife.component.base.JsonString;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * rest template helper
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/30 0:51
 */
public interface RestTemplateHelper {

    /**
     * postJson
     */
    String postJson(String url, JsonString body, Map<String, String> header) throws RestClientException;

    /**
     * postJson
     */
    String postJson(String url, JsonString body) throws RestClientException;

    /**
     * get
     */
    String get(String url, Map<String, String> header) throws RestClientException;

    /**
     * get
     */

    String get(String url) throws RestClientException;

    /**
     * post form
     */
    String postForm(String url, Map<String, Object> parameter, Map<String, String> headerMap) throws RestClientException;

    /**
     * post form
     */
    String postForm(String url, Map<String, Object> parameter) throws RestClientException;

    String postMultipartFormData(String url, Map<String, Object> parameter, Map<String, String> headerMap) throws RestClientException;

    String postMultipartFormData(String url, Map<String, Object> parameter) throws RestClientException;
}
