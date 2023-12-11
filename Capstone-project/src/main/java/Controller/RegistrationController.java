/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
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

    //default image URL
    private String currentProfilePic = "/Assets/Profile/default_image.png";

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
            currentProfilePic = "/Assets/Profile/default_image1.png";
        } else if (defaultImageView2.isVisible()) {
            defaultImageView1.setVisible(false);
            defaultImageView2.setVisible(false);
            defaultImageView3.setVisible(true);
            defaultImageView4.setVisible(false);
            currentProfilePic = "/Assets/Profile/pic2.png";
        } else if (defaultImageView3.isVisible()) {
            defaultImageView1.setVisible(false);
            defaultImageView2.setVisible(false);
            defaultImageView3.setVisible(false);
            defaultImageView4.setVisible(true);
            currentProfilePic = "/Assets/Profile/pic3.png";
        } else if (defaultImageView4.isVisible()) {
            defaultImageView1.setVisible(true);
            defaultImageView2.setVisible(false);
            defaultImageView3.setVisible(false);
            defaultImageView4.setVisible(false);
            currentProfilePic = "/Assets/Profile/default_image.png";
        }
    }

    @FXML
    private void cycleProfilePictureButtonClicked() {
        toggleImage();
        System.out.println("Current Profile Picture is " + currentProfilePic);
    }

    @FXML
    private void uploadProfileButtonClicked() {
        try {
            //Get image file from the user
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );
            fileChooser.setTitle("Select Profile Picture");

            File file = fileChooser.showOpenDialog(null);

            if (file != null) {
                uploadFileToFirebaseStorage(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        userData.put("ProfilePicUrl", currentProfilePic);

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

    private void uploadFileToFirebaseStorage(File file) throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            System.out.println("Firebase has not been initialized");
            return;
        }

        FirebaseApp firebaseApp = FirebaseApp.getInstance();
        Storage storage = StorageClient.getInstance(firebaseApp).bucket("csc325-capstone.appspot.com").getStorage();

        String objectName = "profilepictures/" + UUID.randomUUID().toString();
        String contentType = Files.probeContentType(file.toPath());
        BlobId blobId = BlobId.of("csc325-capstone.appspot.com", objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        URL signedUrl = storage.signUrl(blobInfo, 7, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature());

        Platform.runLater(() -> {
            try {
                currentProfilePic = signedUrl.toString(); // Update current profile picture URL

                //Update all defaultImageViews to be invisible
                defaultImageView1.setVisible(false);
                defaultImageView2.setVisible(false);
                defaultImageView3.setVisible(false);
                defaultImageView4.setVisible(false);

                //Load and display the uploaded image in one of the ImageViews, let's use defaultImageView1 for example
                Image image = new Image(currentProfilePic);
                defaultImageView1.setImage(image);
                defaultImageView1.setVisible(true);
            } catch (Exception e) {
                System.err.println("Exception occurred while setting image to ImageView: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
