package pl.lodz.it.sitodruk;


import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pl.lodz.it.sitodruk.dto.*;


import java.util.List;
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

    @Test
    public void testPayment(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String postingString = "grant_type=client_credentials&client_id="+client_id+"&client_secret="+client_secret;
        HttpEntity<?> entity = new HttpEntity<>(postingString, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url,HttpMethod.POST,entity,Map.class);
        String token = responseEntity.getBody().get("access_token").toString();
        System.out.println(token);
        HttpHeaders paymentHeaders = new HttpHeaders();
        paymentHeaders.setContentType(MediaType.APPLICATION_JSON);
        paymentHeaders.setBearerAuth(token);
        MultiValueMap<Object, Object> map= new LinkedMultiValueMap<>();
        OrderPayU order = new OrderPayU();
        order.setCurrencyCode("PLN");
        order.setTotalAmount("10");
        order.setDescription("Testowa transakcja");
        order.setNotifyUrl("https://screen-printing-application.herokuapp.com/home");
        order.setCustomerIp("127.0.0.1");
        order.setMerchantPosId(client_id);
        order.setBuyer(new BuyerPayU("ikriss95@gmail.com","","Krystian","Kowalski","PL"));
        ProductPayU productPayU = new ProductPayU();
        productPayU.setName("Siatka 60 10mb");
        productPayU.setQuantity("1");
        productPayU.setUnitPrice("10");
        order.getProducts().add(productPayU);
        PayMethodPayU payMethod = new PayMethodPayU();
        payMethod.setBlikData(new BlikData(true));
        payMethod.setAuthorizationCode("098921");
        payMethod.setType("BLIK_TOKEN");
        order.getPayMethods().add(payMethod);
        HttpEntity<?> paymentEntity = new HttpEntity<>(order, paymentHeaders);
//        ResponseEntity<Map> paymentResponseEntity = restTemplate.exchange("https://secure.snd.payu.com/api/v2_1/orders",HttpMethod.POST,paymentEntity,Map.class);
//        System.out.println(paymentResponseEntity.getBody());
//        System.out.println(paymentResponseEntity.getStatusCode());


    }

}
