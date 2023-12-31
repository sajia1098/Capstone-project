package Controller;

import Model.CurrentUser;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import project.App;
import project.FireStoreContext;

public class LoginController {

    @FXML
    private Button bnNeedAccount;
    @FXML
    private TextField tfRamid;
    @FXML
    private Button bnLogin;
    @FXML
    private PasswordField pfPassword;

    private Firestore db;

    public LoginController() {
        FireStoreContext fireStoreContext = new FireStoreContext();
        this.db = fireStoreContext.firebase();
    }

    @FXML
    private void switchToRegistration() throws IOException {
        App.switchScene("registration");
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) throws IOException {
        String ramid = tfRamid.getText().trim();
        String password = pfPassword.getText().trim();

        if (validateLoginInputs(ramid, password)) {
            attemptLogin(ramid, password);
        }
    }

    private boolean validateLoginInputs(String ramid, String password) {
        if (ramid.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Please enter both ID and password.");
            return false;
        }
        return true;
    }

    private void attemptLogin(String ramid, String password) {
        DocumentReference docRef = db.collection("Users").document(ramid);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                String storedPasswordHash = document.getString("PasswordHash");
                String passwordHash = hashPassword(password);
                if (storedPasswordHash.equals(passwordHash)) {
                    //Login successful
                    String firstName = document.getString("FirstName");
                    String lastName = document.getString("LastName");

                    CurrentUser.login(firstName, lastName, "PROTECTED", ramid);
                    //Redirect to home page
                    App.switchScene("home");
                } else {
                    //Incorrect password
                    showAlert("Login Error", "Incorrect password.");
                }
            } else {
                //User not found
                showAlert("Login Error", "User not found.");
            }
        } catch (Exception e) {
            //Failed to get document from database
            showAlert("Login Error", "An error occurred: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        //Password hashing and insert into user collection
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
