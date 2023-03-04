package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class AccountService {
    private final String baseUrl;
    RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);
    private final ConsoleService consoleService = new ConsoleService();


    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;

    }

    public BigDecimal getBalance(AuthenticatedUser user) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<BigDecimal> response = null;


        try {
             response = restTemplate.exchange(baseUrl + "balance/" + user.getUser().getId(), HttpMethod.GET, entity, BigDecimal.class);
            //System.out.printf("$%,.2f", response.getBody());


        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getMessage());
        }

        return response.getBody();
    }

    public void sendBucks(AuthenticatedUser user) {

        Transfer transfer = new Transfer();

        //set up transfer object
        transfer.setTransferTypeId(2);
        transfer.setAccountFrom(user.getUser().getId());

        while(true) { // let user select who to send money to

            System.out.println("");
            System.out.println("");
            System.out.println("*********************");
            List<Integer> validIds = listUsers(user);
            System.out.println("*********************");
            int selection = consoleService.promptForInt("Please enter the ID of a user to send money to: ");

            if(validIds.contains(selection)){
                transfer.setAccountTo(selection);
                break;
            } else{
                System.out.println("Please enter a valid user ID");
            }

        }
        while (true) { // let user select amount to send
            System.out.println("");
            System.out.println("");
            System.out.println("*********************");
            System.out.printf("Current Balance: $%,.2f\n", getBalance(user));
            System.out.println("*********************");
            BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount to send: ");

            if((amount.compareTo(BigDecimal.ZERO) > 0) && (amount.compareTo(getBalance(user)) != 1)){
                transfer.setAmount(amount);
                break;

            } else if (amount.compareTo(BigDecimal.ZERO) < 0){
                System.out.println("Please enter a positive amount");
            } else if (amount.compareTo(getBalance(user)) == 1){
                System.out.println("Must be less than our equal to your current balance");
            }

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        Transfer returnedUser = restTemplate.postForObject(baseUrl + "transfer", entity, Transfer.class);





    }

    public List<Integer> listUsers(AuthenticatedUser user) {

        List<Integer> validIds = new ArrayList<>();

        User[] users = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, entity, User[].class);
        users = response.getBody();
        for (User userU : users) {


            if (userU.getId() == user.getUser().getId()){ //Skips listing the current logged in user
                continue;
            }
            validIds.add(userU.getId());
            System.out.println(userU.getUsername() + " ID: "+ userU.getId());



        }
        return validIds;


    }

    public void getFullTransferHistory(AuthenticatedUser user){
        Transfer[] transfers = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, entity, Transfer[].class);
        transfers = response.getBody();
        for (Transfer transfer : transfers) {

            System.out.println(transfer.toString());
        }
    }
}
