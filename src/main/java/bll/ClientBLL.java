package bll;

import DAO.ClientDAO;
import Model.Client;

import javax.xml.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * The business logic layer for client operations. This class is the top layer used to execute the operations.
 */

public class ClientBLL {

    private ClientDAO clientDAO;

    public ClientBLL() {
        new EmailValidator();
        clientDAO = new ClientDAO();
    }

    public List<Client> findAll(){
        ArrayList<Client>clients = (ArrayList<Client>) clientDAO.findAll();
        if(clients == null){
            throw new NoSuchElementException("No clients in the table!");
        }
        return clients;
    }

    public Client insert(Client c){
        Client insertedClient = clientDAO.insert(c);
        if(insertedClient == null){
            throw new NoSuchElementException("Client cannot be inserted!");
        }
        return  insertedClient;
    }

    public Client deleteById(int id){
        Client client = clientDAO.deleteById(id);
        return null;
    }

    public Client update(Client c){
        Client res = clientDAO.update(c);
        return res;
    }
}
