/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.capstone.project;

import com.google.api.gax.paging.Page;
import java.io.File;
import javafx.fxml.Initializable;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import java.nio.file.Files;
import java.util.UUID;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import project.App;
import project.FireStoreContext;

/**
 *
 * @author amnasajid
 */
public class UploadImages_database implements Initializable{

    @FXML
    private FlowPane flowPane;
    @FXML
    private ComboBox comboBox;
    @FXML
    private ImageView pfp;
   

    // Constructor
    public UploadImages_database() {
        // Assuming FireStoreContext initializes FirebaseApp
        FireStoreContext fireStoreContext = new FireStoreContext();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> items = FXCollections.observableArrayList(
                "View Profile",
                "Settings"            
        );
        comboBox.setItems(items);
        comboBox.setValue("......");
        flowPane.setHgap(20); // Horizontal gap between images
        flowPane.setVgap(15); // Vertical gap between images
        
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        fileChooser.setTitle("Select Image Files");

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                uploadFileToFirebaseStorage(file);
            }
        }

    }

    private void uploadFileToFirebaseStorage(File file) {
        //Check if firebase has been initalized and get app
        if (FirebaseApp.getApps().isEmpty()) {
            System.out.println("Firebase has not been initalized");
            return;
        }
        try {
            //get default app instance
            FirebaseApp firebaseApp = FirebaseApp.getInstance();

            //get storage instance from firebaseapp
            Storage storage = StorageClient.getInstance(firebaseApp).bucket("csc325-capstone.appspot.com").getStorage();

            // Prepare file to be uploaded
            String objectName = UUID.randomUUID().toString();
            String contentType = Files.probeContentType(file.toPath());
            BlobId blobId = BlobId.of("csc325-capstone.appspot.com", objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

            // Upload the file to Firebase Storage
            Blob blob = storage.create(blobInfo, Files.readAllBytes(file.toPath()));

            // Generate a signed URL for the blob with a long expiration time
            URL signedUrl = storage.signUrl(blobInfo, 7, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature());

            //use storageclient to get download URL
            String imageUrl = signedUrl.toString();

            //Update the UI
            javafx.application.Platform.runLater(() -> {
                Image image = new Image(imageUrl);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(250);
                imageView.setPreserveRatio(true);
                Label descriptionLabel = new Label("Description goes here");
                VBox imageAndDescription = new VBox(imageView, descriptionLabel);
                flowPane.getChildren().add(imageAndDescription);
            });

        } catch (Exception e) {
            System.err.println("Exception occurred while uploading file to Firebase Storage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showImage(ActionEvent event) {
        //Clears flowpane before pulling images from database
        flowPane.getChildren().clear();
        
        // Get the Firebase Storage bucket
        FirebaseApp firebaseApp = FirebaseApp.getInstance();
        Storage storage = StorageClient.getInstance(firebaseApp).bucket("csc325-capstone.appspot.com").getStorage();

        // List all the blobs in the bucket (this is your images)
        Page<Blob> blobs = storage.list("csc325-capstone.appspot.com", Storage.BlobListOption.pageSize(100));
        for (Blob blob : blobs.iterateAll()) {
            // For each blob (image), generate a signed URL
            BlobId blobId = blob.getBlobId();
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            URL signedUrl = storage.signUrl(blobInfo, 7, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature());

            // Create an image view and add it to the flow pane
            String imageUrl = signedUrl.toString();
            javafx.application.Platform.runLater(() -> {
                Image image = new Image(imageUrl);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(250);
                imageView.setPreserveRatio(true);
                Label descriptionLabel = new Label(blob.getName()); // Use the blob name as the description or any other metadata you have
                VBox imageAndDescription = new VBox(imageView, descriptionLabel);
                flowPane.getChildren().add(imageAndDescription);
            });
        }
    }

    @FXML
    private void switchToForm(ActionEvent event) throws IOException {
        App.setRoot("productform");
    }
}
