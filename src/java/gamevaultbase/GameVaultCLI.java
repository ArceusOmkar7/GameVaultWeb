package gamevaultbase;

import gamevaultbase.management.GameVaultManagement;
import gamevaultbase.management.CartManagement;
import gamevaultbase.management.GameManagement;
import gamevaultbase.management.OrderManagement;
import gamevaultbase.management.TransactionManagement;
import gamevaultbase.management.UserManagement;
import gamevaultbase.storage.CartStorage;
import gamevaultbase.storage.GameStorage;
import gamevaultbase.storage.OrderStorage;
import gamevaultbase.storage.TransactionStorage;
import gamevaultbase.storage.UserStorage;
import gamevaultbase.helpers.DBUtil;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

public class GameVaultCLI {

    public static void main(String[] args) {
        try {
            // Initialize Storages (but don't connect to DB yet)
            UserStorage userStorage = new UserStorage();
            GameStorage gameStorage = new GameStorage();
            CartStorage cartStorage = new CartStorage(gameStorage);
            OrderStorage orderStorage = new OrderStorage();
            TransactionStorage transactionStorage = new TransactionStorage();

            // Initialize Managements
            UserManagement userManagement = new UserManagement(userStorage);
            GameManagement gameManagement = new GameManagement(gameStorage);
            CartManagement cartManagement = new CartManagement(cartStorage);
            TransactionManagement transactionManagement = new TransactionManagement(transactionStorage);
            OrderManagement orderManagement = new OrderManagement(orderStorage, cartStorage, userStorage,
                    transactionManagement);

            // Create GameVaultManagement but DO NOT initialize data yet (which would
            // trigger DB connection)
            GameVaultManagement vaultManager = new GameVaultManagement(userManagement, gameManagement, orderManagement,
                    transactionManagement);


        } finally {
            // Connection closing is now handled by the GUI
        }
    }
}