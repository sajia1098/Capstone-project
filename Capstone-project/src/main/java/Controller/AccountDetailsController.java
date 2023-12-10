/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Model.CurrentUser;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
//import model.DisplayItem;
import project.App;
/**
 *
 * @author thren
 */
public class AccountDetailsController{
        @FXML
    private Button changeProfilePictureButton;

    @FXML
    private GridPane itemContainer;


    @FXML
    private Button requestPasswordResetButton;

    @FXML
    private Button returnToHomePageButton;

    @FXML
    private Label userFirstNameLabel;

    @FXML
    private Label userLastNameLabel;

    @FXML
    private Label userRAMIDLabel;
    @FXML
    private VBox box;
    @FXML
    private Button inbox;

    @FXML
    void changeProfilePictureButtonHandler(ActionEvent event) {

    }

    @FXML
    void requestPasswordResetHandler(ActionEvent event) {

    }
   
    @FXML
    public void openInboxButton(){
        ItemDescriptionController x = new ItemDescriptionController();
        x.openMessaging();
        
    }
   
    @FXML
    void returnToHomePageHandler(ActionEvent event) throws IOException{
        App.setRoot("home");

    }
    
private void fetchUserDetails() {
        String currentUserId = CurrentUser.getInstance().getId();
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("Users").document(currentUserId);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        future.addListener(() -> {
            try {
                DocumentSnapshot document = future.get();
                if (document.exists()) {
                    String firstName = document.getString("FirstName");
                    String lastName = document.getString("LastName");

                    Platform.runLater(() -> {
                        userFirstNameLabel.setText(firstName);
                        userLastNameLabel.setText(lastName);
                        userRAMIDLabel.setText(currentUserId);
                    });
                } else {
                    System.out.println("User not found with ID: " + currentUserId);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }, Executors.newSingleThreadExecutor());
    }
        
    public void initialize() {
        fetchUserDetails();
    }
    

}
