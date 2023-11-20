package project;

import com.google.cloud.firestore.Firestore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.scene.control.Alert;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Firestore fstore; //access firestore

    @Override
    public void start(Stage stage) {
        try {
            // Initialize Firebase Firestore through the FireStoreContext
            FireStoreContext contxtFirebase = new FireStoreContext();
            // Access Firestore instance
            fstore = contxtFirebase.firebase();

            // Load scene
            scene = new Scene(loadFXML("login"), 954, 654);
            stage.setScene(scene);
            stage.setTitle("RamThrift");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
            showAlert("Error", "An error occurred while starting the application.");
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
            showAlert("Error", "An unexpected error occurred.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        //  "/View/" -> name of package where fxml file is located 
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/View/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
