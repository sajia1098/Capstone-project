package Controller;

import Model.CurrentUser;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import project.App;

/**
 * FXML Controller class
 *
 * @author Elan
 */
public class ProductformController implements Initializable {

    @FXML
    private TextField tfProductname;
    @FXML
    private ComboBox<String> cbCategory;
    @FXML
    private RadioButton rbNew;
    @FXML
    private RadioButton rbUsed;
    @FXML
    private RadioButton rbRefurbished;
    @FXML
    private TextField tfPrice;
    @FXML
    private Button bnSubmit;
    @FXML
    private Button uploadProductImage;
    @FXML
    private TextArea productDescription;
    @FXML
    private TextArea productComments;
    @FXML
    private ImageView productImage;
    @FXML
    private ToggleGroup radioGroup;
    @FXML
    private Button bnClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbCategory.setItems(FXCollections.observableArrayList("Wearable", "Textbook", "Electronic"));
        radioGroup = new ToggleGroup();
        rbNew.setToggleGroup(radioGroup);
        rbUsed.setToggleGroup(radioGroup);
        rbRefurbished.setToggleGroup(radioGroup);
    }

    //This method is called when the "Upload Image" button is clicked
    @FXML
    private void handleUploadImage() {
        try {
            //Get image file from the user
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );
            fileChooser.setTitle("Select Image Files");

            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

            if (selectedFiles != null && !selectedFiles.isEmpty()) {
                //Assuming you want to use only the first selected file
                File file = selectedFiles.get(0);
                uploadFileToFirebaseStorage(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method is called when the "Submit" button is clicked
    @FXML
    private void handleSubmit() throws IOException {
        //Get the product information from the form
        String productName = tfProductname.getText();
        String category = cbCategory.getValue();
        RadioButton selectedCondition = (RadioButton) radioGroup.getSelectedToggle();
        String priceText = tfPrice.getText();
        Image productImageValue = productImage.getImage();

        //Validate the product name
        if (productName == null || productName.trim().isEmpty()) {
            showAlert("Validation Error", "Product name is required.");
            return;
        }

        //Validate the category
        if (category == null || category.trim().isEmpty()) {
            showAlert("Validation Error", "Please select a category.");
            return;
        }

        //Validate the condition
        String condition = selectedCondition == null ? null : selectedCondition.getText();
        if (condition == null) {
            showAlert("Validation Error", "Please select the condition of the product.");
            return;
        }

        //Validate the price
        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid price.");
            return;
        }

        //Validate the image
        if (productImageValue == null) {
            showAlert("Validation Error", "Please upload an image of the product.");
            return;
        }
        String imageUrl = productImageValue.getUrl();
        String currentUserId = CurrentUser.getInstance().getId();

        //Create a map with the product information
        Map<String, Object> productData = new HashMap<>();
        productData.put("productName", productName);
        productData.put("category", category);
        productData.put("condition", condition);
        productData.put("price", price);
        productData.put("description", productDescription.getText());
        productData.put("comments", productComments.getText());
        productData.put("imageUrl", imageUrl);
        productData.put("RamID", currentUserId);

        //Add the product data to Firestore
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference products = db.collection("Items");
        DocumentReference newProductRef = products.document();
        newProductRef.set(productData, SetOptions.merge());

        //Show success message
        showAlert("Product Form", "Product Added!\nClick OK to go back to the home screen!");
        //Go back to home screen
        App.setRoot("home");
    }

    @FXML
    private void handleClose(ActionEvent event) throws IOException {
        App.switchScene("home");
    }

    private void uploadFileToFirebaseStorage(File file) throws IOException {
        //Check if Firebase has been initialized and get the app
        if (FirebaseApp.getApps().isEmpty()) {
            System.out.println("Firebase has not been initialized");
            return;
        }

        //Get default app instance
        FirebaseApp firebaseApp = FirebaseApp.getInstance();

        //Get storage instance from Firebase app
        Storage storage = StorageClient.getInstance(firebaseApp).bucket("csc325-capstone.appspot.com").getStorage();

        //Prepare file to be uploaded
        String objectName = UUID.randomUUID().toString();
        String contentType = Files.probeContentType(file.toPath());
        BlobId blobId = BlobId.of("csc325-capstone.appspot.com", objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        //Upload the file to Firebase Storage
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        //Generate a signed URL for the blob with a long expiration time
        URL signedUrl = storage.signUrl(blobInfo, 7, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature());

        //Use Platform.runLater to update the UI on the JavaFX Application Thread
        Platform.runLater(() -> {
            try {
                //Use storage client to get download URL
                String imageUrl = signedUrl.toString();

                //Update the UI
                Image image = new Image(imageUrl);
                productImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Exception occurred while setting image to ImageView: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
