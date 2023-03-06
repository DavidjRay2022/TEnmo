package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.model.User;

import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {

        System.out.printf("$%,.2f", accountService.getBalance(currentUser));
	}

	private void viewTransferHistory() {

        accountService.getFullTransferHistory(currentUser);

	}

    //TODO format the menu and toString in the model/Transfer
    //if you want to edit the wording of the menu its in the console service line 49.
	private void viewPendingRequests() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printRequestOptions();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                accountService.viewAllPendingRequests(currentUser);
            } else if (menuSelection == 2) { //FIXME not implemented fully!
                int transferId = -1;
                while(transferId != 0){
                    accountService.viewReceivedRequests(currentUser);
                    transferId = consoleService.promptForInt("Select a transfer id to approve or reject.");
                    if(accountService.listOfTransferIds(accountService.viewReceivedRequests(currentUser)).contains(transferId)){
                        int decision = -1;
                        consoleService.printApproveOrDeny(); //transferId
                        decision=  consoleService.promptForInt("Please select a number.");
                        while(decision != 0){
                            if(decision == 1){
                                //approve
                                accountService.approveTransfer(currentUser,transferId);
                            } else if(decision == 2){
                                //deny
                            } else if(decision == 0){
                                continue;
                            } else{
                                System.out.println("Invalid Selection");
                            }
                            consoleService.pause();
                        }

                    } else if (transferId == 0){
                       continue;
                    } else{
                        System.out.println("Invalid Selection");
                    }
                    consoleService.pause();

                }
                //TODO add more here.
                /*
                Select account id to approve or reject
                check if account id is included in accountService.viewRecieved
                give prompt to approve reject or exit
                if approved
                accountService.acceptRequest(currentUser,(accountid input)
                accountService.rejectRequest(currentUser,(accountid input)
                 */
            } else if (menuSelection == 3) {
                accountService.viewSentRequests(currentUser);
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
		
	}

	private void sendBucks() {

       accountService.sendBucks(currentUser);



		
	}

	private void requestBucks() {

		accountService.requestBucks(currentUser);
	}

}
