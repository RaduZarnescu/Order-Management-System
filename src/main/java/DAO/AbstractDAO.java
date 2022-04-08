package DAO;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import DatabaseConnection.ConnectionFactory;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * The abstract data access object class.
 * This is the main class which executes and sets up queries for the database. It is used for reflection, meaning that the methods in this class should work for every table in the database.
 * @param <T> represents the instance on which the methods will operate. If T is an instance of Client, all the methods below will work with the Client class.
 */

public class AbstractDAO<T> {
    protected final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Method used to create a Select query with a condition.
     * @param field is the column featured in the condition
     * @return the query padded with "?", values which will be added later
     */

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * Method used to create a Delete query with a condition.
     * @param field is the column featured in the condition
     * @return the query padded with "?", values which will be added later
     */

    private String createDeleteQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        sb.append(type.getSimpleName() + "_" + field + " =?");
        return sb.toString();
    }

    /**
     * Method used to create an Insert query with a condition.
     * @param t is the object that is to be added to the database.
     * @return the query padded with "?", values which will be added later
     */

    private String createInsertQuery(T t) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = type.getDeclaredFields();
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName());
        sb.append(" (");
        sb.append(fields[1].getName());
        for(int i = 2; i < fields.length; i++){
            sb.append(", ");
            sb.append(fields[i].getName());
        }
        sb.append(") VALUES (?");
        for(int i = 2; i < fields.length; i++){
            sb.append(", ?");
        }
        sb.append(")");
        //System.out.println(sb);
        return sb.toString();
    }

    /**
     * Method used to create an Update query with a condition.
     * @param t is the object from the database which is to be updated to the database.
     * @return the query padded with "?", values which will be added later
     */

    private String createUpdateQuery(T t){
        StringBuilder sb = new StringBuilder();
        Field[] fields = type.getDeclaredFields();
        sb.append("UPDATE ");
        sb.append(type.getSimpleName());
        sb.append(" SET ");
        sb.append(fields[1].getName());
        sb.append(" = ?");
        for(int i = 2; i < fields.length; i++){
            sb.append(", ");
            sb.append(fields[i].getName());
            sb.append(" = ?");
        }
        sb.append(" WHERE ");
        sb.append(fields[0].getName());
        sb.append(" = ?");
        return sb.toString();
    }

    /**
     * This method reads all the data from the table, and transforms it into multiple objects of the T type.
     * @return list with all the objects formed from the db table (all the rows from the table)
     */

    public List<T> findAll() {
        List<T> list = new ArrayList<T>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Method used to delete from the table represented by T the entry with the id specified.
     * @param id the id of the entry required to delete
     * @return null
     */

    public T deleteById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createDeleteQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int res = statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Method that creates a list of objects of type T from the result set of the query executed.
     * @param resultSet the set returned by the query execution
     * @return list of objects of type T
     */

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();

        try {
            while (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | IntrospectionException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Method that inserts into the required table an object of type T.
     * @param t the object that is to be inserted
     * @return the oject inserted
     */

    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createInsertQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int i = 1;
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals("client_id") && !field.getName().equals("product_id") && !field.getName().equals("order_id") ) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    Object writeData = method.invoke(t);
                    statement.setObject(i, writeData);
                    i++;
                }
            }
            int res = statement.executeUpdate();

        } catch (SQLException | IntrospectionException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * Method used to update the object t from the table.
     * @param t the object required to update from the table, with the new values updated
     * @return the updated object
     */

    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createUpdateQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int i = 1;
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals("client_id") && !field.getName().equals("product_id") && !field.getName().equals("order_id") ) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    Object writeData = method.invoke(t);
                    statement.setObject(i, writeData);
                    i++;
                }
                else{
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    Object writeData = method.invoke(t);
                    statement.setObject(type.getDeclaredFields().length, writeData);
                }
            }
            int res = statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
}

