package pl.lodz.it.sitodruk;

import io.jsonwebtoken.lang.Assert;
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
import pl.lodz.it.sitodruk.dto.payu.*;


import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SitodrukApplicationTests {

    @Value("${thesis.app.payu.client.id}")
    private String client_id;
    @Value("${thesis.app.payu.client.secret}")
    private String client_secret;
    @Value("${thesis.app.payu.url}")
    private String OauthUrl;
    @Value("${thesis.app.payu.api.url}")
    private String payuApiUrl;

    @Test
    public void hasAPIGeneratedToken()   {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String postingString = "grant_type=client_credentials&client_id="+client_id+"&client_secret="+client_secret;
        HttpEntity<?> entity = new HttpEntity<>(postingString, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(OauthUrl,HttpMethod.POST,entity,Map.class);
        Assertions.assertEquals(HttpStatus.OK.toString(),responseEntity.getStatusCode().toString());
    }

    @Test
    public void testPayment(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String postingString = "grant_type=client_credentials&client_id="+client_id+"&client_secret="+client_secret;
        HttpEntity<?> entity = new HttpEntity<>(postingString, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(OauthUrl,HttpMethod.POST,entity,Map.class);
        String token = responseEntity.getBody().get("access_token").toString();
        HttpHeaders paymentHeaders = new HttpHeaders();
        paymentHeaders.setContentType(MediaType.APPLICATION_JSON);
        paymentHeaders.setBearerAuth(token);
        MultiValueMap<Object, Object> map= new LinkedMultiValueMap<>();
        OrderPayU order = new OrderPayU();
        order.setCurrencyCode("PLN");
        order.setTotalAmount("10");
        order.setDescription("Testowa transakcja");
        order.setNotifyUrl("https://screen-printing-application.herokuapp.com/platnosci");
        order.setCustomerIp("127.0.0.1");
        order.setMerchantPosId(client_id);
        order.setBuyer(new BuyerPayU("","ikriss95@gmail.com","","Krystian","Kowalski","pl"));
        ProductPayU productPayU = new ProductPayU();
        productPayU.setName("Siatka 60 10mb");
        productPayU.setQuantity("1");
        productPayU.setUnitPrice("10");
        order.getProducts().add(productPayU);
        PayMethodPayU payMethod = new PayMethodPayU();
        payMethod.setBlikData(new BlikData(true));
        payMethod.setAuthorizationCode("500500");
        payMethod.setType("BLIK_TOKEN");
        order.setPayMethods(new PayMethodsPayU());
        order.getPayMethods().setPayMethod(payMethod);
        HttpEntity<?> paymentEntity = new HttpEntity<>(order, paymentHeaders);
        ResponseEntity<Map> paymentResponseEntity = restTemplate.exchange(payuApiUrl+"/orders",HttpMethod.POST,paymentEntity,Map.class);
        Assert.hasText("statusCode=SUCCESS",paymentResponseEntity.getBody().toString());
    }

    @Test
    public void testRetrieveOrderStatus(){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String postingString = "grant_type=client_credentials&client_id="+client_id+"&client_secret="+client_secret;
        HttpEntity<?> entity = new HttpEntity<>(postingString, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(OauthUrl,HttpMethod.POST,entity,Map.class);
        String token = responseEntity.getBody().get("access_token").toString();
        HttpHeaders orderStatusHeaders = new HttpHeaders();
        orderStatusHeaders.setContentType(MediaType.APPLICATION_JSON);
        orderStatusHeaders.setBearerAuth(token);
        HttpEntity<?> httpEntity = new HttpEntity<>(orderStatusHeaders);
        ResponseEntity<Map> orderStatusResponse = restTemplate.exchange(payuApiUrl+"/orders/MBDVTS34LR210121GUEST000P01",HttpMethod.GET,httpEntity,Map.class);
        String status = orderStatusResponse.getBody().get("orders").toString().substring(orderStatusResponse.getBody().get("orders").toString().indexOf("status="),orderStatusResponse.getBody().get("orders").toString().indexOf(", products=")).replace("status=","");
        Assert.isTrue(status.equalsIgnoreCase("canceled"));

    }

    @Test
    public void testTransactionDetails(){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String postingString = "grant_type=client_credentials&client_id="+client_id+"&client_secret="+client_secret;
        HttpEntity<?> entity = new HttpEntity<>(postingString, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(OauthUrl,HttpMethod.POST,entity,Map.class);
        String token = responseEntity.getBody().get("access_token").toString();
        HttpHeaders orderStatusHeaders = new HttpHeaders();
        orderStatusHeaders.setContentType(MediaType.APPLICATION_JSON);
        orderStatusHeaders.setBearerAuth(token);
        HttpEntity<?> httpEntity = new HttpEntity<>(orderStatusHeaders);
        ResponseEntity<Map> orderStatusResponse = restTemplate.exchange(payuApiUrl+"/orders/C1HQQF9VZZ201115GUEST000P01/transactions",HttpMethod.GET,httpEntity,Map.class);
        Assert.isTrue(orderStatusResponse.getStatusCodeValue()==200);
    }

}
