/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.capstone.project;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author amnasajid
 */
public class SecondaryController implements Initializable {

    @FXML
    private TextField first_name;
    @FXML
    private TextField last_name;
    @FXML
    private TextField ramid;
    @FXML
    private TextField password;
    @FXML
    private TextField confirm_password;
    @FXML
    private Button signup_button;

    private boolean key;
    private ObservableList<User> listOfUsers = FXCollections.observableArrayList();
    private User user;
    @FXML
    private Button backToLogin;

    public ObservableList<User> getListOfUsers() {
        return listOfUsers;
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    public void switchToPrimary() throws IOException {
        // Switch to the primary FXML view
        App.setRoot("primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code when the controller is created
    }

    // Asynchronously retrieve all documents
    ApiFuture<QuerySnapshot> future = App.fstore.collection("Users").get();

    public boolean registerUser() {
        // Initialize Firebase Realtime Database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users");
        // Create a User object with data
        User newUser = new User(first_name.getText(), last_name.getText(), password.getText(), ramid.getText());
        // Push the new user data to the database
        DatabaseReference newUserRef = databaseRef.push();
        newUserRef.setValue(newUser, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                // Data could not be written to the database
                System.err.println("Data could not be written: " + databaseError.getMessage());
            } else {
                // Data was successfully written
                System.out.println("Successfully created new user with ID: " + newUserRef.getKey());
            }
        });
        return true;
    }

    @FXML
    private void signupButtonClicked(javafx.event.ActionEvent event) {
        // Handle the signup button click event
        registerUser();
        addData();
    }

    public void addData() {
        DocumentReference docRef = App.fstore.collection("Users").document(UUID.randomUUID().toString());
        // Add document data with ID using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("Name", first_name.getText() + " " + last_name.getText());
        data.put("ID", ramid.getText());
        // Asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }
}
