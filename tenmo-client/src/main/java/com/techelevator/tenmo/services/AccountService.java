package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;


public class AccountService {
    private final String baseUrl;
    RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }


    public void getBalance(AuthenticatedUser user) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "balance/" + user.getUser().getId(), HttpMethod.GET, entity, BigDecimal.class);
            System.out.printf("$%,.2f", response.getBody());

        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        }


    }

    public void sendBucks() {

    }

    public void listUsers(AuthenticatedUser user) {

        User[] users = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, entity, User[].class);
        users = response.getBody();
        for (User userU : users) {
            System.out.println("Username: " + userU.getUsername());

        }


    }
}
