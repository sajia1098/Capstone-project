/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import project.App;

/**
 * FXML Controller class
 *
 * @author amnasajid
 */
public class Registration implements Initializable {

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
        App.setRoot("login");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code when the controller is created
    }

    // Asynchronously retrieve all documents
    ApiFuture<QuerySnapshot> future = App.fstore.collection("Users").get();

    public boolean registerUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        CreateRequest request = new CreateRequest()
                .setUid(ramid.getText().trim()) // using school ID as the UID
                .setPassword(password.getText().trim());

        try {
            UserRecord userRecord = auth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating new user: " + e.getMessage());
            return false;
        }
    }

    @FXML
    private void signupButtonClicked(javafx.event.ActionEvent event) throws IOException {
    if (areFieldsEmpty()) {
        // If any fields are empty, show an alert
        showAlert("Validation Error", "Please fill in all fields.");
    } else if (password.getText().length() < 6) {
        // If password is too short, show an alert
        showAlert("Validation Error", "Password must be at least 6 characters long.");
    } else if (!doPasswordsMatch()) {
        // If passwords do not match, show an alert
        showAlert("Validation Error", "Passwords do not match.");
    } else {
        // Otherwise, proceed with registration
        registerUser();
        addData();
        // Show success message
        showAlert("Registration Successful", "You have successfully registered!");
        // Go back to login screen
        App.setRoot("login");
    }
}

// Helper method to show an alert
private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}
    public void addData() {
        DocumentReference docRef = App.fstore.collection("Users").document(ramid.getText().trim());
        // Add document data with ID using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("Name", first_name.getText().trim() + " " + last_name.getText().trim());
        data.put("ID", ramid.getText().trim());
        data.put("PasswordHash", hashPassword(password.getText().trim()));  // Adding password hash
        // Asynchronously write data
        try {
            ApiFuture<WriteResult> result = docRef.set(data);
            WriteResult writeResult = result.get();
            System.out.println("Write result: " + writeResult);
        } catch (Exception e) {
            System.err.println("Error writing to Firestore: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        // Implement password hashing here
        // This is a simplistic example, use a stronger hash function in a real application
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean areFieldsEmpty() {
        return first_name.getText().trim().isEmpty()
                || last_name.getText().trim().isEmpty()
                || ramid.getText().trim().isEmpty()
                || password.getText().trim().isEmpty()
                || confirm_password.getText().trim().isEmpty();
    }

    private boolean doPasswordsMatch() {
        return password.getText().equals(confirm_password.getText());
    }


}
