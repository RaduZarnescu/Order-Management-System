package bll;

import DAO.ProductDAO;
import Model.Client;
import Model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * The business logic layer for product operations. This class is the top layer used to execute the operations.
 */

public class ProductBLL {
    private ProductDAO productDAO = new ProductDAO();

    public ProductBLL() {
    }

    public List<Product> findAll() {
        ArrayList<Product> products = (ArrayList<Product>) productDAO.findAll();
        if (products == null) {
            throw new NoSuchElementException("No products in the table!");
        }
        return products;
    }

    public Product insert(Product p){
        Product insertedProduct = productDAO.insert(p);
        if(insertedProduct == null){
            throw new NoSuchElementException("Product cannot be inserted!");
        }
        return insertedProduct;
    }

    public Product deleteById(int id){
        Product product = productDAO.deleteById(id);
        return null;
    }

    public Product update(Product p){
        Product res = productDAO.update(p);
        return res;
    }
}
