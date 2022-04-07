package club.anlan.lanlife.component.base.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 5s
 *
 * @author lan
 * @version 1.0
 * @date 2022/3/30 1:01
 */
@Service
public class RestClient5sService extends AbstractRestTemplate {
    private final RestTemplate restTemplate;

    public RestClient5sService() {
        restTemplate = RestTemplateFactory.getClient(5000);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
