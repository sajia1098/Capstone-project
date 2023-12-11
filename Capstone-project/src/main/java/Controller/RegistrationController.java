/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import project.App;
import project.FireStoreContext;

/**
 * FXML Controller class
 *
 * @author amnasajid
 */
public class RegistrationController {

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
    @FXML
    private Button backToLogin;

    private Firestore db;
    @FXML
    private Circle circle;
    @FXML
    private ImageView defaultImageView1;
    @FXML
    private ImageView defaultImageView2;
    @FXML
    private ImageView defaultImageView3;
    @FXML
    private ImageView defaultImageView4;
    @FXML
    private Button uploadProfile;
    @FXML
    private Button cycleProfile;

    public RegistrationController() {
        FireStoreContext fireStoreContext = new FireStoreContext();
        this.db = fireStoreContext.firebase();
    }

    public void initialize() {
        //Change to whatever the image name is called in the Assets/Profile is called
        Image img1 = new Image("/Assets/Profile/default_image.png");
        defaultImageView1.setImage(img1);

        Image img2 = new Image("/Assets/Profile/default_image1.png");
        defaultImageView2.setImage(img2);
        Image img3 = new Image("/Assets/Profile/pic2.png");
        defaultImageView3.setImage(img3);
        
        Image img4 = new Image("/Assets/Profile/pic3.png");
        defaultImageView4.setImage(img4);

        //Initialize with one image visible
        defaultImageView1.setVisible(true);
    }

    private void toggleImage() {
        if (defaultImageView1.isVisible()) {
            defaultImageView1.setVisible(false);
            defaultImageView2.setVisible(true);
            defaultImageView3.setVisible(false);
            defaultImageView4.setVisible(false);

        } 
        else if(defaultImageView2.isVisible())
        {
           defaultImageView1.setVisible(false);
           defaultImageView2.setVisible(false);
           defaultImageView3.setVisible(true);
           defaultImageView4.setVisible(false);

        }
        else if (defaultImageView3.isVisible()){
           defaultImageView1.setVisible(false);
           defaultImageView2.setVisible(false);
           defaultImageView3.setVisible(false);
           defaultImageView4.setVisible(true);
        }
        else if (defaultImageView4.isVisible()){
           defaultImageView1.setVisible(true);
           defaultImageView2.setVisible(false);
           defaultImageView3.setVisible(false);
           defaultImageView4.setVisible(false);
        }
    }

    @FXML
    private void cycleProfilePictureButtonClicked() {
        toggleImage();
    }

    @FXML
    private void uploadProfileButtonClicked() {
        //TODO UPLOAD PROFILE FROM DESKTOP
        //ADD CODE TO UPLOAD TOGETHER WITH LOGIN INFORMATION TO DATABASE
    }

    
    @FXML
    private void signupButtonClicked() throws IOException {
        if (areFieldsValid()) {
            try {
                if (registerUser()) {
                    showAlert("Registration Successful", "You have successfully registered!");
                    App.switchScene("login");
                }
            } catch (InterruptedException | ExecutionException e) {
                showAlert("Registration Error", "Error during registration: " + e.getMessage());
            }
        }
    }

    @FXML
    private void switchToLogin() throws IOException {
        //Switch to the login scene
        App.switchScene("login");
    }

    private boolean areFieldsValid() {
        //Validate that fields are not empty, password length, password matching
        if (first_name.getText().isEmpty() || last_name.getText().isEmpty()
                || ramid.getText().isEmpty() || password.getText().isEmpty()
                || confirm_password.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return false;
        }

        if (!password.getText().equals(confirm_password.getText())) {
            showAlert("Validation Error", "Passwords do not match.");
            return false;
        }

        if (password.getText().length() < 6) {
            showAlert("Validation Error", "Password must be at least 6 characters long.");
            return false;
        }
        return true;
    }

    private boolean registerUser() throws InterruptedException, ExecutionException {
        //Check if a user with the same ID already exists
        DocumentReference docRef = db.collection("Users").document(ramid.getText().trim());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            //User with this ID already exists
            showAlert("Registration Error", "A user with this ID already exists.");
            return false;
        }

        //Proceed with registration if user does not exist
        String hashedPassword = hashPassword(password.getText().trim());

        //Create user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("FirstName", first_name.getText().trim());
        userData.put("LastName", last_name.getText().trim());
        userData.put("RamID", ramid.getText().trim());
        userData.put("PasswordHash", hashedPassword);

        //Add user to the database
        docRef.set(userData);
        return true;
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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void uploadProfileButtonClicked(ActionEvent event) {
    }
}
