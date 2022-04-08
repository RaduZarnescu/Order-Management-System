package Model;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * This class is the representation of the orders, used to build and store new orders.
 * It's fields are: id, client's id, product's id and the quantity.
 */

public class Order_Table {

    private int order_id;
    private int order_client;
    private int order_prod;
    private int order_quantity;

    public Order_Table(int order_id, int order_client, int order_prod, int order_quantity) {
        this.order_id = order_id;
        this.order_client = order_client;
        this.order_prod = order_prod;
        this.order_quantity = order_quantity;
    }

    public Order_Table() {
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_client() {
        return order_client;
    }

    public void setOrder_client(int order_client) {
        this.order_client = order_client;
    }

    public int getOrder_prod() {
        return order_prod;
    }

    public void setOrder_prod(int order_prod) {
        this.order_prod = order_prod;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }
}
