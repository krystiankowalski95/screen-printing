package pl.lodz.it.sitodruk.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.dto.ProductDTO;
import pl.lodz.it.sitodruk.dto.payu.*;
import pl.lodz.it.sitodruk.model.moz.UserEntity;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
@SessionScope
@Data
public class PayUController {

    @Value("${thesis.app.payu.client.id}")
    private String client_id;
    @Value("${thesis.app.payu.client.secret}")
    private String client_secret;
    @Value("${thesis.app.payu.url}")
    private String OauthUrl;
    @Value("${thesis.app.payu.api.url}")
    private String payuApiUrl;

    private String payuToken;

    @PostConstruct
    private void init() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String postingString = "grant_type=client_credentials&client_id=" + client_id + "&client_secret=" + client_secret;
        HttpEntity<?> entity = new HttpEntity<>(postingString, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(OauthUrl, HttpMethod.POST, entity, Map.class);
        payuToken = responseEntity.getBody().get("access_token").toString();
    }


    public String payuTransaction(UserEntity userEntity, OrderDTO orderDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders paymentHeaders = new HttpHeaders();
        paymentHeaders.setContentType(MediaType.APPLICATION_JSON);
        paymentHeaders.setBearerAuth(payuToken);
        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
        OrderPayU order = new OrderPayU();
        order.setCurrencyCode("PLN");
        String totalValue = String.valueOf(orderDTO.getTotalValue() * 100);
        order.setTotalAmount(totalValue.substring(0,totalValue.indexOf(".")));
        order.setDescription("Payment");
        order.setNotifyUrl("https://screen-printing-application.herokuapp.com/platnosci");
        order.setCustomerIp(orderDTO.getIpAddress());
        order.setMerchantPosId(client_id);
        order.setBuyer( new BuyerPayU("",userEntity.getEmail(),userEntity.getPhoneNumber(),userEntity.getFirstname(),userEntity.getLastname(),"pl"));
        for(ProductDTO prod : orderDTO.getProducts()){
            ProductPayU productPayU = new ProductPayU();
            productPayU.setName(prod.getName());
            productPayU.setQuantity(prod.getQuantity().toString());
            String unitPrice = String.valueOf(prod.getPrice() * 100);
            order.setTotalAmount(unitPrice.substring(0,unitPrice.indexOf(".")));
            productPayU.setUnitPrice(unitPrice);
            order.getProducts().add(productPayU);
        }
        PayMethodPayU payMethod = new PayMethodPayU();
        payMethod.setBlikData(new BlikData(true));
        payMethod.setAuthorizationCode(orderDTO.getBlikCode().toString());
        payMethod.setType("BLIK_TOKEN");
        order.setPayMethods(new PayMethodsPayU());
        order.getPayMethods().setPayMethod(payMethod);
        HttpEntity<?> paymentEntity = new HttpEntity<>(order, paymentHeaders);
        ResponseEntity<Map> paymentResponseEntity = restTemplate.exchange(payuApiUrl + "/orders", HttpMethod.POST, paymentEntity, Map.class);
        return (String) paymentResponseEntity.getBody().get("orderId");
    }


}