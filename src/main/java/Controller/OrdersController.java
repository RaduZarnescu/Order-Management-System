package Controller;

import Model.Client;
import Model.Order_Table;
import Model.Product;
import View.Alert;
import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import org.graalvm.compiler.nodes.java.ArrayLengthNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * Controller used for the order operations window.
 */

public class OrdersController {

    @FXML private Button addBtn;
    @FXML private Button backBtn;
    @FXML private TextField clientIdField;
    @FXML private TextField productIdField;
    @FXML private TextField quantityField;

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
     * Method which gets all the products from the database using ProductBLL
     * @return list with all the clients
     */

    public ObservableList<Product> getProducts(){
        ObservableList<Product>products = FXCollections.observableArrayList();
        ProductBLL productBLL = new ProductBLL();
        ArrayList<Product> allProducts = (ArrayList<Product>) productBLL.findAll();
        products.addAll(allProducts);

        return products;
    }

    /**
     * Method which gets all the orders from the database using OrderBLL
     * @return list with all the clients
     */

    public ObservableList<Order_Table> getOrders(){
        ObservableList<Order_Table>orders = FXCollections.observableArrayList();
        OrderBLL orderBLL = new OrderBLL();
        ArrayList<Order_Table> allOrders = (ArrayList<Order_Table>) orderBLL.findAll();
        if(allOrders == null){
            return null;
        }
        orders.addAll(allOrders);

        return orders;
    }

    /**
     * method to generate bills for each order
     * @param o order
     * @param clients list of all the clients
     * @param products list of all the products
     * @throws IOException
     */

    public void generateBill(Order_Table o, ObservableList<Client>clients, ObservableList<Product> products) throws IOException {

        String fileName = "bill" + o.getOrder_id() +".txt";
        FileWriter file = new FileWriter(fileName);
        file.write("Bill for order " + o.getOrder_id() + ":\n\n");
        for(Client c: clients){
            if(c.getClient_id() == o.getOrder_client()){
                file.write("Name: " + c.getClient_name() + "\n");
                file.write("Email: " + c.getClient_email() + "\n");
                file.write("Address: " + c.getClient_address() + "\n");
                break;
            }
        }
        file.write("\n");
        for(Product p: products){
            if(p.getProduct_id() == o.getOrder_prod()){
                file.write(p.getProduct_name() + " x " + o.getOrder_quantity() + "\n");
                file.write("Total: " + p.getProduct_price()*o.getOrder_quantity());
                break;
            }
        }
        file.close();
    }

    /**
     * method to create a new order and insert it into the database
     * @param event button press
     * @throws IOException
     */

    public void addOrder(ActionEvent event) throws IOException {

        if(clientIdField.getText().equals("") || productIdField.getText().equals("") || quantityField.getText().equals("")){
            Alert alert = new Alert();
            alert.errorAlert("Empty Fields", "Please complete all fields!");
            return;
        }
        int clientID = Integer.parseInt(clientIdField.getText());
        int productID = Integer.parseInt(productIdField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        ObservableList<Client>clients = getClients();
        ObservableList<Product>products = getProducts();
        ObservableList<Order_Table>orders = getOrders();

        boolean clientIdFound = false;
        boolean productIdFound = false;
        for(Client c: clients){
            if(c.getClient_id() == clientID){
                clientIdFound = true;
                break;
            }
        }
        for(Product p: products){
            if(p.getProduct_id() == productID){
                productIdFound = true;
                break;
            }
        }
        if(!clientIdFound || !productIdFound){
            Alert alert = new Alert();
            alert.errorAlert("Wrong ID", "Please insert a valid ID for both Client and Product!");
        }
        Order_Table order = new Order_Table();
        if(orders.size() == 0){
            order = new Order_Table(1, clientID, productID, quantity);
        }
        else {
            order = new Order_Table(orders.get(orders.size() - 1).getOrder_id() + 1, clientID, productID, quantity);
        }
        OrderBLL orderBLL = new OrderBLL();
        Product product = new Product();
        for(Product p: products){
            if(p.getProduct_id() == productID){
                product = p;
                break;
            }
        }
        product.setProduct_stock(product.getProduct_stock() - quantity);
        if(product.getProduct_stock() >= 0) {
            ProductBLL productBLL = new ProductBLL();
            productBLL.update(product);
            orderBLL.insert(order);
            generateBill(order, clients, products);
        }
        else{
            Alert alert = new Alert();
            alert.errorAlert("Not enough Products", "There are not enough products in stock!");
        }

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
