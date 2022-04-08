package Controller;

import Model.Client;
import bll.ClientBLL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * The controller for the home page. It is used to navigate through the client, product, order windows.
 */

public class MainController {

    @FXML private Button btn1;
    @FXML private Button btn2;
    @FXML private Button btn3;

    /**
     * The method used to switch the windows of the GUI.
     * @param event occurs at a button press
     * @param fxml name of the next window
     * @param btn button pressed
     * @throws IOException
     */

    public void changeSceneOnButtonAction(ActionEvent event, String fxml, Button btn) throws IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(fxml)));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The method which handles the press of the button btn1
     * @param event button press
     * @throws IOException
     */

    public void btn1OnAction(ActionEvent event) throws IOException {
        changeSceneOnButtonAction(event, "a3client.fxml", btn1);
    }

    /**
     * The method which handles the press of the button btn2
     * @param event button press
     * @throws IOException
     */

    public void btn2OnAction(ActionEvent event) throws IOException {
        changeSceneOnButtonAction(event, "a3product.fxml", btn1);
    }

    /**
     * The method which handles the press of the button btn3
     * @param event button press
     * @throws IOException
     */

    public void btn3OnAction(ActionEvent event) throws IOException {
        changeSceneOnButtonAction(event, "a3order.fxml", btn1);
    }
}
