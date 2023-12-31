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
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.App;
import project.FireStoreContext;
import javafx.geometry.Insets;

/**
 *
 * @author amnasajid
 */
public class HomeController implements Initializable {

    @FXML
    private FlowPane flowPane;
    @FXML
    private Button uploadFormButton;
    @FXML
    private ImageView pfp;
    @FXML
    private ImageView ppfp;
    @FXML
    private Label labelWelcome;
    @FXML
    private TextField tfSearch;
    @FXML
    private Button refreshItemsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button accountDetailsButton;
    @FXML
    private Button clothesBtn;
    @FXML
    private Button textbookBtn;
    @FXML
    private Button electronicsBtn;

    private boolean isActionInProgress = false;

    private Timer searchTimer = new Timer();

    //Constructor
    public HomeController() {
        //FireStoreContext initializes FirebaseApp
        FireStoreContext fireStoreContext = new FireStoreContext();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Fetch and display user details for Welcome message
        fetchUserDetails();

        //Calls the method to load the images as soon as home screen is loaded
        showImage(null);

        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (searchTimer != null) {
                //Cancel previous timer
                searchTimer.cancel();
            }

            searchTimer = new Timer();
            searchTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        if (!newValue.trim().isEmpty()) {
                            performLiveSearch(newValue.trim());
                        } else {
                            showImage(null);
                        }
                    });
                }
                //Adjust this delay to make more responsive, but will trigger duplicates if typed fast
            }, 500);
        });
    }

    @FXML
    private void uploadButton() throws IOException {
        App.switchScene("productform");
    }

    @FXML
    private void clothesButton(ActionEvent event) {
        showImagesByCategory("Wearable");
    }

    @FXML
    private void textbooksButton(ActionEvent event) {
        showImagesByCategory("Textbook");
    }

    @FXML
    private void electronicsButton(ActionEvent event) {
        showImagesByCategory("Electronic");
    }

    @FXML
    private void logoutButton(ActionEvent event) {
        try {
            //Clear current user's session data
            CurrentUser.logout();

            //Go back to login screen
            App.switchScene("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToAccountDetails(ActionEvent event) throws IOException {
        App.setRoot("accountDetails");
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

    @FXML
    private void showImage(ActionEvent event) {
        if (isActionInProgress) {
            return;
        }
        isActionInProgress = true;

        //Clears flowpane before pulling images from database
        flowPane.getChildren().clear();

        Task<Void> loadImageTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Firestore db = FirestoreClient.getFirestore();
                    CollectionReference items = db.collection("Items");

                    //Retrieve all items
                    ApiFuture<QuerySnapshot> querySnapshot = items.get();

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
                        String ownerId = (String) item.get("RamID");

                        Image image = new Image(imageUrl);
                        Platform.runLater(() -> {
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            imageView.setOnMouseClicked(event -> {
                                double priceValue = Double.parseDouble(productPrice);
                                Item itemClass = new Item(category, comments, condition, description, priceValue, imageUrl, productName, ownerId);
                                openItemDetail(itemClass);
                            });

                            Label nameLabel = new Label("Name: " + productName);
                            Label priceLabel = new Label("Price: $" + productPrice);
                            Label conditionLabel = new Label("Condition: " + condition);
                            Label categoryLabel = new Label("Category: " + category);

                            VBox imageAndDescription = new VBox(imageView, nameLabel, priceLabel, conditionLabel, categoryLabel);
                            imageAndDescription.setPadding(new Insets(10));
                            addItemToFlowPane(imageAndDescription);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isActionInProgress = false;
                }
                return null;
            }
        };
        Thread thread = new Thread(loadImageTask);
        thread.setDaemon(true);
        thread.start();
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
                    String profilePicUrl = document.getString("ProfilePicUrl");
                    Platform.runLater(() -> {
                        labelWelcome.setText("Welcome, " + firstName + " " + lastName + " (RAMID: " + currentUserId + ")");
                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                            Image profileImage = new Image(profilePicUrl, true);
                            //Set the profile image in the ImageView
                            pfp.setImage(profileImage);
                        }
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
        if (isActionInProgress) {
            return;
        }
        isActionInProgress = true;

        flowPane.getChildren().clear();

        Task<Void> loadCategoryImagesTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Firestore db = FirestoreClient.getFirestore();
                    CollectionReference items = db.collection("Items");

                    //Query items by category
                    Query query = items.whereEqualTo("category", category);
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
                        String ownerId = (String) item.get("RamID");
                        Image image = new Image(imageUrl);
                        Platform.runLater(() -> {
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            imageView.setOnMouseClicked(event -> {
                                double priceValue = Double.parseDouble(productPrice);
                                Item itemClass = new Item(category, comments, condition, description, priceValue, imageUrl, productName, ownerId);
                                openItemDetail(itemClass);
                            });

                            Label nameLabel = new Label("Name: " + productName);
                            Label priceLabel = new Label("Price: $" + productPrice);
                            Label conditionLabel = new Label("Condition: " + condition);
                            Label categoryLabel = new Label("Category: " + category);
                            VBox imageAndDescription = new VBox(imageView, nameLabel, priceLabel, conditionLabel, categoryLabel);
                            imageAndDescription.setPadding(new Insets(10));
                            addItemToFlowPane(imageAndDescription);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isActionInProgress = false;
                }
                return null;
            }
        };
        Thread thread = new Thread(loadCategoryImagesTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void performLiveSearch(String searchText) {

        if (isActionInProgress) {
            return;
        }
        isActionInProgress = true;

        //debug line
        System.out.println("Searching for: " + searchText);
        //Clear existing content in the flowPane
        flowPane.getChildren().clear();

        Task<Void> searchTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Firestore db = FirestoreClient.getFirestore();
                    CollectionReference items = db.collection("Items");

                    //Adjust this query to match your search criteria
                    Query query = items.orderBy("productName").startAt(searchText).endAt(searchText + "\uf8ff");
                    ApiFuture<QuerySnapshot> querySnapshot = query.get();

                    for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                        Map<String, Object> item = document.getData();
                        //Existing code to handle and display each item...
                        //imageUrl and other metadata are stored in the document
                        String imageUrl = (String) item.get("imageUrl");
                        String productName = (String) item.get("productName");
                        String productPrice = String.format("%.2f", (Double) item.get("price"));
                        String condition = (String) item.get("condition");
                        String category = (String) item.get("category");
                        String comments = (String) item.get("comments");
                        String description = (String) item.get("description");
                        String ownerId = (String) item.get("RamID");

                        Image image = new Image(imageUrl);
                        Platform.runLater(() -> {
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(200);
                            imageView.setPreserveRatio(true);

                            imageView.setOnMouseClicked(event -> {
                                double priceValue = Double.parseDouble(productPrice);
                                Item itemClass = new Item(category, comments, condition, description, priceValue, imageUrl, productName, ownerId);
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
                } finally {
                    isActionInProgress = false;
                }
                return null;
            }
        };
        Thread thread = new Thread(searchTask);
        thread.setDaemon(true);
        thread.start();
    }

    private synchronized void addItemToFlowPane(VBox itemBox) {
        Platform.runLater(() -> {
            flowPane.getChildren().add(itemBox);
        });
    }
}
