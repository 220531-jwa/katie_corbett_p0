package runner;

import dev.corbett.controller.ClientController;
import dev.corbett.model.Account;
import dev.corbett.model.Client;
import dev.corbett.repository.AccountDAO;
import dev.corbett.repository.ClientDAO;
import dev.corbett.controller.ClientController;
import dev.corbett.controller.AccountController;
import dev.corbett.service.AccountService;
import dev.corbett.service.ClientService;
import io.javalin.Javalin;

import java.awt.image.PackedColorModel;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

public class Main {
    public static void main(String[] args){
        ClientDAO cDAO = new ClientDAO();
        AccountDAO aDAO = new AccountDAO();
        ClientService cs = new ClientService(cDAO);
        AccountService as = new AccountService(aDAO);
        ClientController cc = new ClientController(cs);
        AccountController ac = new AccountController(as, cs);

        Javalin app = Javalin.create();
        app.start();

        app.routes(() -> {
            path("/clients", () -> {
                get(ClientController::getAllClients);
                post(ClientController::createClient);
                path("/login", () -> {
                    get(ClientController::loginClient);
                });
                path("/{clientId}", () -> {
                    get(ClientController::getClientById);
                    put(ClientController::updateClient);
                    delete(ClientController::deleteClient);
                    path("/accounts", () -> {
                        post(AccountController::createAccount);
                        get(AccountController::getAllAccounts);
//                        get(AccountController::getAccountsByBalance);
                        path("/{accNum}", () -> {
                            get(AccountController::getAccountByNumber);
                            put(AccountController::updateAccountType);
                            delete(AccountController::deleteAccount);
                            patch(AccountController::updateAccount);
                            path("/transfer", () -> {
                                path("/{clientId2}", () -> {
                                    path("/{accNum2}", () -> {
                                        patch(AccountController::transferBetweenAccounts);
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    }
}
