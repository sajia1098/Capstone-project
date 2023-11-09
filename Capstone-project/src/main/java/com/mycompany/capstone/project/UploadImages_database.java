/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.capstone.project;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 *
 * @author amnasajid
 */
public class UploadImages_database {

    @FXML
    private FlowPane flowPane;

    public void initialize() {
        flowPane.setHgap(20); // Horizontal gap between images
        flowPane.setVgap(15); // Vertical gap between images
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
        fileChooser.setTitle("Select Image Files");

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (int i = 0; i < selectedFiles.size(); i += 2) {
                // Create a VBox to contain the image and description
                VBox imageAndDescription = new VBox();

                for (int j = 0; j < 2 && i + j < selectedFiles.size(); j++) {
                    File selectedFile = selectedFiles.get(i + j);

                    // Load the selected image
                    Image image = new Image(selectedFile.toURI().toString());

                    // Create an ImageView for the image
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200);
                    imageView.setPreserveRatio(true);


                    Label descriptionLabel = new Label("Description goes here"); 
                    //Label descriptionLabel = new Label(item.getDescription()); 

                    // Add the ImageView and TextField to the VBox
                    imageAndDescription.getChildren().addAll(imageView, descriptionLabel);
                }

                // Add the VBox containing the image and description to the FlowPane
                flowPane.getChildren().add(imageAndDescription);
            }
             
        }
        
       
    }
    
    

    public void addItemtoDatabase(Item item) {
        // Assuming item.getId() returns a unique identifier for the item
        CollectionReference itemsCollection = App.fstore.collection("Items");

        // Create a map to store the item's data
        Map<String, Object> data = new HashMap<>();
        data.put("Description", item.getDescription());
        data.put("Price", item.getPrice());
        data.put("Condition", item.getCondition());
        
        
        //checks if item is an istance of any other class then gets the attributs and put them in the database
        if (item instanceof TextBook) {
            TextBook textbook = (TextBook) item;

            data.put("Title", textbook.getTitle());
            data.put("ISBN", textbook.getIsbn());
            data.put("Edition", textbook.getEdition());
        } else if (item instanceof Wearable) {
            Wearable wearable = (Wearable) item;
            data.put("Gender", wearable.getGender());
            data.put("Size", wearable.getSize());
            data.put("Brand", wearable.getBrand());
            data.put("Material", wearable.getMaterial());
            data.put("Color", wearable.getColor());
        } // and for all classes 

        // Asynchronously write data
        try {
            ApiFuture<DocumentReference> result = itemsCollection.add(data);
            DocumentReference documentReference = result.get();
            System.out.println("Item added with auto-generated ID: " + documentReference.getId());
        } catch (Exception e) {
            System.err.println("Error adding item data to Firestore: " + e.getMessage());
        }
    }

}
