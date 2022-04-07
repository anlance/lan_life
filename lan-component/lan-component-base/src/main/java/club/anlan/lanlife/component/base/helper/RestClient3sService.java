package club.anlan.lanlife.component.base.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 3s
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/30 1:01
 */
@Service
public class RestClient3sService extends AbstractRestTemplate {
    private final RestTemplate restTemplate;

    public RestClient3sService() {
        restTemplate = RestTemplateFactory.getClient(3000);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
