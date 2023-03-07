package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;


public class AccountService {
    private final String baseUrl;
    RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);
    private final ConsoleService consoleService = new ConsoleService();
    private final int send = 2;
    private final int request =1;


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
        List<User> validUsers = userList(user);
        List<Integer> validIds = new ArrayList<>();

        Transfer transfer = new Transfer();

        //set up transfer object
        transfer.setTransferTypeId(send);
        transfer.setAccountFrom(user.getUser().getId());

        while(true) { // let user select who to send money to

            System.out.println("");
            System.out.println("");
            System.out.println("-------------------------------------------");
            System.out.printf("User\n");
            System.out.printf("%-10s%s\n", "IDs", "Name");
            System.out.println("-------------------------------------------");
            for (int i = 0; i < validUsers.size(); i++){
                System.out.printf("%-10d%s\n", validUsers.get(i).getId(), validUsers.get(i).getUsername());
                validIds.add(validUsers.get(i).getId());

            }
            System.out.println("---------");
            System.out.printf("Current Balance: $%,.2f\n", getBalance(user));
            System.out.println("---------\n");

            int selection = consoleService.promptForInt("Please enter the ID of a user to send money to (0 to cancel): ");

            if(validIds.contains(selection)) {
                transfer.setAccountTo(selection);
                break;
            }else if (selection == 0){
                return;
            } else{
                System.out.println("Please enter a valid user ID");
            }

        }
        while (true) { // let user select amount to send

            BigDecimal amount = consoleService.promptForBigDecimal("Enter Amount (0 to cancel): ");

            if(amount.compareTo(BigDecimal.ZERO) == 0){
                return;
            }

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

    public List<User> userList(AuthenticatedUser user) {

        List<User> validUsers = new ArrayList<>();

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
            validUsers.add(userU);

        }
        return validUsers;


    }


    public void getFullTransferHistory(AuthenticatedUser user){

        Transfer[] transfers = null;
        Map<Integer, Integer> transferMap = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/user/" + user.getUser().getId(),
                HttpMethod.GET, entity, Transfer[].class);
        transfers = response.getBody();

//        for (Transfer transfer : transfers) {
//            //System.out.println(transfer.getId());
//            System.out.println(transfer.toString());
//        }

        System.out.println("");
        System.out.println("");
        System.out.println("-------------------------------------------");
        System.out.printf("%-5s\n","Transfer");
        System.out.printf("%-10s%-23s%s\n", "IDs", "From/To", "Amount");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < transfers.length; i++){
            System.out.printf("%-10d", transfers[i].getId());
            if(transfers[i].getAccountToUserId() == user.getUser().getId()){
                System.out.printf("%-6s%-17s", "From:", transfers[i].getAccountFromUN());
            } else {
                System.out.printf("%-6s%-17s", "To:", transfers[i].getAccountToUN());
            }
            System.out.printf("$%.2f\n",transfers[i].getAmount());
            transferMap.put(transfers[i].getId(), i);

            }
        System.out.println("---------");

        while (true) {
            int choice = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");

            if (choice == 0) {
                return;
            }
            if (transferMap.containsKey(choice)) {
                System.out.println("\n" + transfers[transferMap.get(choice)].toString());
            } else {
                System.out.println("Please select valid transfer");
            }
        }
//            validIds.add(validUsers.get(i).getId());

        }



    public void requestBucks(AuthenticatedUser user){
        /*

        Make a transfer request from list of users use the sendBucks Listing method
        analyze the sendBucks
        Create new transfer that has a request set to pending
        make sure the purchase can't go through unless user accepts it and stuff
         */


        List<User> validUsers = userList(user);
        List<Integer> validIds = new ArrayList<>();

        Transfer transfer = new Transfer();

        //set up transfer object
        transfer.setTransferTypeId(request);
        transfer.setAccountFrom(user.getUser().getId());

        while(true) { // let user select who to request  money from

            System.out.println("");
            System.out.println("");
            System.out.println("-------------------------------------------");
            System.out.printf("User\n");
            System.out.printf("%-10s%s\n", "IDs", "Name");
            System.out.println("-------------------------------------------");
            for (int i = 0; i < validUsers.size(); i++){
                System.out.printf("%-10d%s\n", validUsers.get(i).getId(), validUsers.get(i).getUsername());
                validIds.add(validUsers.get(i).getId());

            }
            System.out.println("---------");

            int selection = consoleService.promptForInt("Please enter the ID of a user to request money from (0 to cancel): ");

            if(validIds.contains(selection)) {
                transfer.setAccountTo(selection);
                break;
            }else if (selection == 0){
                return;
            } else{
                System.out.println("Please enter a valid user ID");
            }

        }
        while (true) { // let user select amount to request

            BigDecimal amount = consoleService.promptForBigDecimal("Enter Amount (0 to cancel): ");

            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                return;
            }

            if ((amount.compareTo(BigDecimal.ZERO) > 0)) {
                transfer.setAmount(amount);
                break;

            } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Please enter a positive amount");


            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        //Transfer returnedUser =
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
    public int viewReceivedRequests(AuthenticatedUser user){
        Map<Integer, Integer> transferMap = new HashMap<>();
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers/user/" + user.getUser().getId() + "/pending-received", HttpMethod.GET, entity, Transfer[].class);

        transfers = response.getBody();
        List<Transfer> transferList = Arrays.asList(transfers);

//        for(Transfer transfer : transfers){
//            //maybe include counter
//            System.out.println(transfer.toString());
//        }
//        //System.out.println(transferList);

        System.out.println("");
        System.out.println("");
        System.out.println("-------------------------------------------");
        System.out.printf("%-5s\n","Pending Transfer");
        System.out.printf("%-10s%-23s%s\n", "IDs", "From/To", "Amount");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < transfers.length; i++){
            System.out.printf("%-10d", transfers[i].getId());
            if(transfers[i].getAccountToUserId() == user.getUser().getId()){
                System.out.printf("%-6s%-17s", "From:", transfers[i].getAccountFromUN());
            } else {
                System.out.printf("%-6s%-17s", "To:", transfers[i].getAccountToUN());
            }
            System.out.printf("$%.2f\n",transfers[i].getAmount());
            transferMap.put(transfers[i].getId(), i);

        }
        System.out.println("---------");

        while (true) {
            int choice = consoleService.promptForInt("Please enter transfer ID to approve/reject  (0 to cancel): ");

            if (choice == 0) {
                return 0;
            }
            if (transferMap.containsKey(choice)) {
                System.out.println("\n" + transfers[transferMap.get(choice)].toString());
                return choice;
            } else {
                System.out.println("Please select valid transfer");
            }
        }

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


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(headers);

        //approve transfer and edit it in the server side?
        //restTemplate.exchange(baseUrl + "transfers/" +id + "/approve-transfer", HttpMethod.PUT, entity, Void.class);
        restTemplate.exchange(baseUrl+"transfers/approve/" + id, HttpMethod.PUT,entity,Void.class);

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
