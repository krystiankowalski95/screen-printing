package pl.lodz.it.sitodruk;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SitodrukApplicationTests {

    @Value("${thesis.app.payu.client.id}")
    private String client_id;
    @Value("${thesis.app.payu.client.secret}")
    private String client_secret;
    @Value("${thesis.app.payu.url}")
    private String url;




    @Test
    public void hasAPIGeneratedToken()   {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String postingString = "grant_type=client_credentials&client_id="+client_id+"&client_secret="+client_secret;
        HttpEntity<?> entity = new HttpEntity<>(postingString, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url,HttpMethod.POST,entity,Map.class);
        Assertions.assertEquals(HttpStatus.OK.toString(),responseEntity.getStatusCode().toString());
    }

}
