/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import Model.CurrentUser;
import Model.Item;
import Model.ItemDetails;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
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
    private FlowPane flowPane;


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
        public void initialize() {
        fetchUserDetails();
            showImagesByCategory(CurrentUser.getInstance().getId());
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

private void showImagesByCategory(String category) {
        flowPane.getChildren().clear();

        Task<Void> loadCategoryImagesTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Firestore db = FirestoreClient.getFirestore();
                    CollectionReference items = db.collection("Items");

                    //Query items by category
                    Query query = items.whereEqualTo("RamID", category);
                    ApiFuture<QuerySnapshot> querySnapshot = query.get();

                    for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                        Map<String, Object> item = document.getData();

                        //imageUrl and other metadata are stored in the document
                        String imageUrl = (String) item.get("imageUrl");
                        String productName = (String) item.get("productName");
                        String productPrice = String.format("%.2f", (Double) item.get("price"));
                        String condition = (String) item.get("condition");
                        String category = (String) item.get("category");
                        String comments = (String) item.get("comments");
                        String description = (String) item.get("description");

                        Image image = new Image(imageUrl);
                        Platform.runLater(() -> {
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            imageView.setOnMouseClicked(event -> {
                                double priceValue = Double.parseDouble(productPrice);
                                Item itemClass = new Item(category, comments, condition, description, priceValue, imageUrl, productName);
                                openItemDetail(itemClass);
                            });

                            Label nameLabel = new Label("Name: " + productName);
                            Label priceLabel = new Label("Price: $" + productPrice);
                            Label conditionLabel = new Label("Condition: " + condition);
                            Label categoryLabel = new Label("Category: " + category);
                            VBox imageAndDescription = new VBox(imageView, nameLabel, priceLabel, conditionLabel, categoryLabel);
                            addItemToFlowPane(imageAndDescription);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        Thread thread = new Thread(loadCategoryImagesTask);
        thread.setDaemon(true);
        thread.start();
    }
        

    private void openItemDetail(Item itemDetails) {
        try {
            //Set item details in ItemDetails class
            ItemDetails.getInstance().setCurrentItem(itemDetails);

            //Use App class to switch scenes
            App.switchScene("itemdescription");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private synchronized void addItemToFlowPane(VBox itemBox) {
        Platform.runLater(() -> {
            flowPane.getChildren().add(itemBox);
        });
    }
}
    