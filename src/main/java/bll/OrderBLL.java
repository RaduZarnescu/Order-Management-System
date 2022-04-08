package bll;

import DAO.OrderDAO;
import DAO.ProductDAO;
import Model.Order_Table;
import Model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * The business logic layer for order operations. This class is the top layer used to execute the operations.
 */

public class OrderBLL {
    private OrderDAO orderDAO = new OrderDAO();

    public OrderBLL() {
    }

    public List<Order_Table> findAll() {
        ArrayList<Order_Table> orders = (ArrayList<Order_Table>) orderDAO.findAll();
        return orders;
    }

    public Order_Table insert(Order_Table o){
        Order_Table insertedOrder = orderDAO.insert(o);
        if(insertedOrder == null){
            throw new NoSuchElementException("Product cannot be inserted!");
        }
        return insertedOrder;
    }

    public Order_Table deleteById(int id){
        Order_Table order = orderDAO.deleteById(id);
        return null;
    }

    public Order_Table update(Order_Table o){
        Order_Table res = orderDAO.update(o);
        return res;
    }
}
