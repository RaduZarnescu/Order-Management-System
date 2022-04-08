package View;
import java.util.Optional;

/**
 * @author Radu Zarnescu
 * @version 1.0
 * This class is used to display errors in the GUI when the user does something wrong, or when the required quantity isn't available.
 */

public class Alert {

    public Alert() {
    }

    public void errorAlert(String header, String context){
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
}