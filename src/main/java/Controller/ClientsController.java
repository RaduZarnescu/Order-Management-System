package Controller;

import Model.Client;
import View.Alert;
import bll.ClientBLL;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * Controller used for the client operations window.
 */

public class ClientsController {
    @FXML private Button viewClientsBtn;
    @FXML private Button insertClientBtn;
    @FXML private Button deleteClientBtn;
    @FXML private Button updateClientBtn;
    @FXML private Button backBtn;

    @FXML private TableView<Client> clientsTable = new TableView<>();
    @FXML private TableColumn<Client, Integer>idCol;
    @FXML private TableColumn<Client, String>nameCol;
    @FXML private TableColumn<Client, String>addrCol;
    @FXML private TableColumn<Client, String>emailCol;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;

    /**
     * Method which gets all the clients from the database using ClientBLL
     * @return list with all the clients
     */

    public ObservableList<Client> getClients(){
        ObservableList<Client>clients = FXCollections.observableArrayList();
        ClientBLL clientBLL = new ClientBLL();
        ArrayList<Client> allClients = (ArrayList<Client>) clientBLL.findAll();
        clients.addAll(allClients);

        return clients;
    }

    /**
     * Method to display the clients on the table view
     * @param event button press
     */

    public void viewClients(ActionEvent event){

        idCol.setCellValueFactory(new PropertyValueFactory<Client, Integer>("client_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Client, String>("client_name"));
        addrCol.setCellValueFactory(new PropertyValueFactory<Client, String>("client_address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Client, String>("client_email"));

        clientsTable.getItems().clear();
        clientsTable.getItems().addAll(getClients());
    }

    /**
     * Method to insert a new client
     * @param event button press
     */

    public void insertClient(ActionEvent event){

        if(nameField.getText().equals("") || addressField.getText().equals("") || emailField.getText().equals("")){
            Alert alert = new Alert();
            alert.errorAlert("Empty fields", "To add a client you need to insert name, address and email!");
            return;
        }
        ObservableList<Client>allClients = getClients();
        int insertID = allClients.get(allClients.size() - 1).getClient_id() + 1;
        Client insertClient = new Client(insertID, nameField.getText(), addressField.getText(), emailField.getText());
        ClientBLL clientBLL = new ClientBLL();
        clientBLL.insert(insertClient);
        viewClients(event);
    }

    /**
     * method to delete the selected client
     * @param event button press
     */

    public void deleteClient(ActionEvent event){
        if(idField.getText().equals("")){
            Alert alert = new Alert();
            alert.errorAlert("Empty field", "You need to specify the id of the client you want to delete!");
            return;
        }
        int idToDelete = Integer.parseInt(idField.getText());
        ClientBLL clientBLL = new ClientBLL();
        clientBLL.deleteById(idToDelete);
        viewClients(event);
    }

    /**
     * method to update a client
     * @param event button press
     */

    public void updateClient(ActionEvent event){
        if(idField.getText().equals("") && (nameField.getText().equals("") || addressField.getText().equals("") || emailField.getText().equals(""))){
            Alert alert = new Alert();
            alert.errorAlert("Empty fields", "To update a client you need to specify the id and modify\nat least one of the fields!");
            return;
        }
        Client client = new Client();
        ObservableList<Client>allClients = getClients();
        for(Client c: allClients){
            if(c.getClient_id() == Integer.parseInt(idField.getText())){
                client = c;
            }
        }
        if(!nameField.getText().equals("")){
            client.setClient_name(nameField.getText());
        }
        if(!addressField.getText().equals("")){
            client.setClient_address(addressField.getText());
        }
        if(!emailField.getText().equals("")){
            client.setClient_email(emailField.getText());
        }
        ClientBLL clientBLL = new ClientBLL();
        clientBLL.update(client);
        viewClients(event);
    }

    /**
     * method used to switch the windows in the GUI
     * @param event button press
     * @throws IOException
     */

    public void changeSceneOnButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("a3home.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
