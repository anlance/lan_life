package club.anlan.lanlife.component.base.helper;

import club.anlan.lanlife.component.base.JsonString;
//import club.anlan.lanlife.component.base.filter.MdcFilter;
import club.anlan.lanlife.component.utils.StringUtil;
import club.anlan.lanlife.component.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/30 0:51
 */
@Slf4j
public abstract class AbstractRestTemplate implements RestTemplateHelper {

    protected abstract RestTemplate getRestTemplate();

    @Override
    public String postJson(String url, JsonString body, Map<String, String> header) throws RestClientException {
        HttpEntity<String> requestEntity = new HttpEntity<>(body.toJsonString(), buildBasicJsonHeaders(header));
        return doExchange(url, HttpMethod.POST, requestEntity);
    }

    @Override
    public String postJson(String url, JsonString body) throws RestClientException {
        HttpEntity<String> requestEntity = new HttpEntity<>(body.toJsonString(), buildBasicJsonHeaders(null));
        return doExchange(url, HttpMethod.POST, requestEntity);
    }

    @Override
    public String get(String url, Map<String, String> header) throws RestClientException {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, buildBasicJsonHeaders(header));
        return doExchange(url, HttpMethod.GET, requestEntity);
    }

    @Override
    public String get(String url) throws RestClientException {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, buildBasicJsonHeaders(null));
        return doExchange(url, HttpMethod.GET, requestEntity);
    }

    @Override
    public String postForm(String url, Map<String, Object> parameter, Map<String, String> headerMap) throws RestClientException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(translate(parameter), buildBasicFormHeaders(headerMap));
        return doExchange(url, HttpMethod.POST, requestEntity);
    }

    @Override
    public String postForm(String url, Map<String, Object> parameter) throws RestClientException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(translate(parameter), buildBasicFormHeaders(null));
        return doExchange(url, HttpMethod.POST, requestEntity);
    }

    @Override
    public String postMultipartFormData(String url, Map<String, Object> parameter, Map<String, String> headerMap) throws RestClientException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(translate(parameter), buildMultipartFORMHeaders(headerMap));
        return doExchange(url, HttpMethod.POST, requestEntity);
    }

    @Override
    public String postMultipartFormData(String url, Map<String, Object> parameter) throws RestClientException {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(translate(parameter), buildMultipartFORMHeaders(null));
        return doExchange(url, HttpMethod.POST, requestEntity);
    }


    private HttpHeaders buildBasicJsonHeaders(Map<String, String> headerMap) {
        return buildHeader(MediaType.APPLICATION_JSON_UTF8, headerMap);
    }


    private HttpHeaders buildBasicFormHeaders(Map<String, String> headerMap) {
        return buildHeader(MediaType.APPLICATION_FORM_URLENCODED, headerMap);
    }

    private HttpHeaders buildMultipartFORMHeaders(Map<String, String> headerMap) {
        return buildHeader(MediaType.MULTIPART_FORM_DATA, headerMap);
    }

    private void setHeader(HttpHeaders headers, Map<String, String> headerMap) {
        if (Objects.nonNull(headerMap) && !headerMap.isEmpty()) {
            for (String key : headerMap.keySet()) {
                String value = headerMap.get(key);
                if (StringUtil.isNotBlank(value)) {
                    headers.set(key, value);
                }
            }
        }
    }

    private HttpHeaders buildHeader(MediaType mediaType, Map<String, String> headerMap) {
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(mediaType);
//        String traceId = MDC.get(MdcFilter.TRACE_ID);
//        if (StringUtil.isBlank(traceId)) {
//            traceId = UUIDUtil.generateUuid();
//        }
//        headers.set(MdcFilter.TRACE_ID, traceId);
//        setHeader(headers, headerMap);
        return headers;
    }

    private String doExchange(String url, HttpMethod method, HttpEntity<?> requestEntity) {
        try {
            ResponseEntity<String> result = getRestTemplate().exchange(url, method, requestEntity, String.class);
            if (!result.getStatusCode().is2xxSuccessful()) {
                log.warn("请求接口返回状态码非2xx:{}", url);
            }
            return result.getBody();
        } catch (ResourceAccessException e) {
            log.warn("网络连接失败:{}", url);
            throw e;
        } catch (RestClientResponseException e) {
            log.warn("请求接口失败:{},{}", url, e.getRawStatusCode());
            return e.getResponseBodyAsString();
        }
    }

    private MultiValueMap<String, Object> translate(Map<String, Object> parameter) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        if (Objects.isNull(parameter) || parameter.isEmpty()) {
            return multiValueMap;
        }
        Set<String> set = parameter.keySet();
        for (String key : set) {
            multiValueMap.add(key, parameter.get(key));
        }
        return multiValueMap;
    }
}
