package com.mycompany.capstone.project;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Firestore fstore; //access firestore
    private final FireStoreContext contxtFirebase = new FireStoreContext();

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize Firebase Firestore through the FireStoreContext
        contxtFirebase.firebase();

        // Access Firestore instance
        fstore = FirestoreClient.getFirestore();
        scene = new Scene(loadFXML("login"), 700, 500);
        stage.setScene(scene);
        stage.setTitle("RamThrift");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
