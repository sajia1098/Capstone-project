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
    //Reference to main stage
    private static Stage primaryStage;
    //Access firestore
    public static Firestore fstore;

    @Override
    public void start(Stage stage) {
        try {
            //Initialize Firebase Firestore through the FireStoreContext
            FireStoreContext contxtFirebase = new FireStoreContext();
            //Access Firestore instance
            fstore = contxtFirebase.firebase();
            
            primaryStage = stage;
            
            //Load scene
            scene = new Scene(loadFXML("login"), 954, 654);
            //Set primary stage as login screen
            primaryStage.setScene(scene);
            primaryStage.setTitle("RamThrift");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while starting the application.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred.");
        }
    }

    //Method to switch scenes
    public static void switchScene(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
    }

    //Old way to switch scenes, use switchscene instead
    @Deprecated
    public static void setRoot(String fxml) throws IOException {
        switchScene(fxml);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        //"/View/" -> name of package where fxml file is located 
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/View/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
