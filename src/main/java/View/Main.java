package View;
import DatabaseConnection.ConnectionFactory;
import Model.Client;
import Model.Product;
import bll.ClientBLL;
import bll.ProductBLL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * This is the main class which is used to start the application.
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("a3home.fxml")));
        primaryStage.setTitle("Home");
        primaryStage.setScene(new Scene(root, 811, 501));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
