package project;

import com.google.cloud.firestore.Firestore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    public static Firestore fstore;

    @Override
    public void start(Stage stage) {
        try {
            FireStoreContext contxtFirebase = new FireStoreContext();
            fstore = contxtFirebase.firebase();

            primaryStage = stage;

            scene = new Scene(loadFXML("login"), 954, 654);
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

    public static void switchScene(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
    }

    @Deprecated
    public static void setRoot(String fxml) throws IOException {
        switchScene(fxml);
    }

    private static Parent loadFXML(String fxml) throws IOException {
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
