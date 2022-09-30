package com.example.demo.rest;

import com.example.demo.object.PaymentResult;
import com.example.demo.security.Authentication;
import com.example.demo.security.TokenAuthenticationController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockLevelRestTest {

    private static final ObjectMapper om = new ObjectMapper();
    int randomServerPort;
    int timeout = 10_000;
    static String t;

    @Test
    public void testProductDetail_Vx0_success() {
        RestTemplate restTemplate = new RestTemplate();
        Authentication authInput = new Authentication();
        PaymentResult input = new PaymentResult();
        long threshold = 12345L;

        final String userName = "mmm7029";
        final String passWord = "137029@Sms";
        authInput.setUser(userName);
        authInput.setPassword(passWord);

        t = TokenAuthenticationController.getJWTToken(authInput);

        System.out.printf("Auth : " + authInput);
        authInput.setPassword("*******");
        printJSON(authInput);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", t);

        input.setW_id("1");
        input.setC_balance(25.0);
        input.setC_credit_lim(50.0);
        input.setC_first("Austen");
        HttpEntity<String> entity = new HttpEntity(input, headers);
        String url1 = "http://localhost:8080/productDetail_Vx0/";

        Long output = restTemplate.postForObject(url1, entity, Long.class);

        System.out.printf("outPut : " + output);
        printJSON(output);
    }

    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}