package Model;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * This class represents the model for the clients and it contains: id, name, address and email.
 */

public class Client {

    private int client_id;
    private String client_name;
    private String client_address;
    private String client_email;

    public Client() {
    }

    /**
     * this is the overloaded constructor which builds a Client object with all it's attributes.
     * @param client_id the id for the new client
     * @param client_name the name of the new client
     * @param client_address the address of the new client
     * @param client_email the email of the new client
     */
    public Client(int client_id, String client_name, String client_address, String client_email) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_address = client_address;
        this.client_email = client_email;
    }

    public Client(String client_name, String client_address, String client_email) {
        this.client_name = client_name;
        this.client_address = client_address;
        this.client_email = client_email;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }
}
