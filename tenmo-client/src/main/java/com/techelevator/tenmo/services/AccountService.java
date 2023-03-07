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
import java.util.Arrays;
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
        //Transfer returnedUser =
        restTemplate.postForObject(baseUrl + "transfer", entity, Transfer.class);





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

    //TODO the methods I've added
    public void getFullTransferHistory(AuthenticatedUser user){

        Transfer[] transfers = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/user/" + user.getUser().getId(),
                HttpMethod.GET, entity, Transfer[].class);
        transfers = response.getBody();

        for (Transfer transfer : transfers) {
            //System.out.println(transfer.getId());
            System.out.println(transfer.toString());
        }
    }

    public void requestBucks(AuthenticatedUser user){
        /*
        TODO
        Make a transfer request from list of users use the sendBucks Listing method
        analyze the sendBucks
        Create new transfer that has a request set to pending
        make sure the purchase can't go through unless user accepts it and stuff
         */

        //////////////////copy and paste from sendBucks////////////////////////////////////////
        Transfer transfer = new Transfer();

        //set up transfer object
        transfer.setTransferTypeId(1);
        transfer.setAccountFrom(user.getUser().getId());

        //FIXME EDIT 1
        transfer.setTransferStatusId(1); //sets the transfer status to pending.

        while(true) { // let user select who to send money to

            System.out.println("");
            System.out.println("");
            System.out.println("*********************");
            List<Integer> validIds = listUsers(user);
            System.out.println("*********************");
            int selection = consoleService.promptForInt("Please enter the ID of a user to request money from: ");

            if(validIds.contains(selection)){
                transfer.setAccountTo(selection);
                break;
            } else{
                System.out.println("Please enter a valid user ID");
            }

        }
        //Fixme add a check in the accept/deny script checking how much money they have.


        while (true) { // let user select amount to send
            System.out.println("");
            System.out.println("");
            System.out.println("*********************");
            System.out.printf("Current Balance: $%,.2f\n", getBalance(user));
            System.out.println("*********************");
            BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount to request: ");

            if((amount.compareTo(BigDecimal.ZERO) > 0) /* && (amount.compareTo(getBalance(user)) != 1)*/){
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

       restTemplate.postForObject(baseUrl + "transfer", entity, Transfer.class);


    }

    //TODO
    public void viewSentRequests(AuthenticatedUser user){
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ///transfers/user/{userId}/pending-sent2
        ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/user/" + user.getUser().getId() + "/pending-sent",

                HttpMethod.GET, entity, Transfer[].class);
        transfers = response.getBody();
        for(Transfer transfer : transfers){
            System.out.println(transfer.toString());
        }

    }

    //TODO was experimenting with making this a list to help w/ approving and rejecting requests
    public List<Transfer> viewReceivedRequests(AuthenticatedUser user){
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/user/" + user.getUser().getId() + "/pending-received",

                HttpMethod.GET, entity, Transfer[].class);
        transfers = response.getBody();
        List<Transfer> transferList = Arrays.asList(transfers);
        for(Transfer transfer : transfers){
            //maybe include counter
            System.out.println(transfer.toString());
        }
        //System.out.println(transferList);
        return transferList;
    }
    public void viewAllPendingRequests(AuthenticatedUser user){
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/user/" + user.getUser().getId() + "/pending",

                HttpMethod.GET, entity, Transfer[].class);
        transfers = response.getBody();
        for(Transfer transfer : transfers){
            System.out.println(transfer.toString());
        }


    }
    //TODO unfinished, couldn't figure out the best way to use the .put method for updating an entry.
    public void approveTransfer(AuthenticatedUser user, int id){
        //FIXME work in progress 3:42.
        Transfer transfer = new Transfer();
        transfer.setId(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        //may not need the transfer in header, seeing where this goes
        HttpEntity<Transfer> entity = new HttpEntity<>(headers);

        //approve transfer and edit it in the server side?
        //restTemplate.exchange(baseUrl + "transfers/" +id + "/approve-transfer", HttpMethod.PUT, entity, Void.class);
        restTemplate.exchange(baseUrl+"/transfers/user/"+ user.getUser().getId() +"/pending/"+ id +"/approve", HttpMethod.PUT,entity,Void.class);

    }
    ///transfers/user/{userId}/pending-received/{transferId}/0

    public void denyTransfer(AuthenticatedUser user, int id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(headers);

        //should deny transfer?
        restTemplate.put(baseUrl + "transfers/" +id + "/reject-transfer", entity);
    }
    public List<Integer> listOfTransferIds(List<Transfer> transferList){
        List<Integer> transferIds = new ArrayList<>();
        for(Transfer transfer:transferList){
            //FIXME add transfer ids to a list to chose from.
            transferIds.add(transfer.getId());
        }
        return  transferIds;
    }
}
