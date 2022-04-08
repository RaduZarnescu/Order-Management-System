package Controller;

import Model.Client;
import Model.Product;
import View.Alert;
import bll.ClientBLL;
import bll.ProductBLL;
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
 * Controller used for the product operations window.
 */

public class ProductsController {

    @FXML private Button viewProductsBtn;
    @FXML private Button insertProductBtn;
    @FXML private Button deleteProductBtn;
    @FXML private Button updateProductBtn;
    @FXML private Button backBtn;

    @FXML private TableView<Product> productsTable = new TableView<>();
    @FXML private TableColumn<Product, Integer> idCol;
    @FXML private TableColumn<Product, String> nameCol;
    @FXML private TableColumn<Product, Integer> priceCol;
    @FXML private TableColumn<Product, Integer> stockCol;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;

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
     * Method to display the products on the table view
     * @param event button press
     */

    public void viewProducts(ActionEvent event){

        idCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("product_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("product_name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("product_price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("product_stock"));

        productsTable.getItems().clear();
        productsTable.getItems().addAll(getProducts());
    }

    /**
     * Method to insert a new product
     * @param event button press
     */

    public void insertProduct(ActionEvent event){

        if(nameField.getText().equals("") || priceField.getText().equals("") || stockField.getText().equals("")){
            Alert alert = new Alert();
            alert.errorAlert("Empty fields", "To add a client you need to insert name, price and stock!");
            return;
        }
        ObservableList<Product>allProducts = getProducts();
        int insertID = allProducts.get(allProducts.size() - 1).getProduct_id() + 1;
        Product insertProduct = new Product(insertID, nameField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(stockField.getText()));
        ProductBLL productBLL = new ProductBLL();
        productBLL.insert(insertProduct);
        viewProducts(event);
    }

    /**
     * method to delete the selected product
     * @param event button press
     */

    public void deleteProduct(ActionEvent event){
        if(idField.getText().equals("")){
            Alert alert = new Alert();
            alert.errorAlert("Empty field", "You need to specify the id of the client you want to delete!");
            return;
        }
        int idToDelete = Integer.parseInt(idField.getText());
        ProductBLL productBLL = new ProductBLL();
        productBLL.deleteById(idToDelete);
        viewProducts(event);
    }

    /**
     * method to update a product
     * @param event button press
     */

    public void updateProduct(ActionEvent event){
        if(idField.getText().equals("") && (!nameField.getText().equals("") || !priceField.getText().equals("") || !stockField.getText().equals(""))){
            Alert alert = new Alert();
            alert.errorAlert("Empty fields", "To update a client you need to specify the id and modify\nat least one of the fields!");
            return;
        }
        Product product = new Product();
        ObservableList<Product>allProducts = getProducts();
        for(Product p: allProducts){
            if(p.getProduct_id() == Integer.parseInt(idField.getText())){
                product = p;
            }
        }
        if(!nameField.getText().equals("")){
            product.setProduct_name(nameField.getText());
        }
        if(!priceField.getText().equals("")){
            product.setProduct_price(Integer.parseInt(priceField.getText()));
        }
        if(!stockField.getText().equals("")){
            product.setProduct_stock(Integer.parseInt(stockField.getText()));
        }
        ProductBLL productBLL = new ProductBLL();
        productBLL.update(product);
        viewProducts(event);
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
