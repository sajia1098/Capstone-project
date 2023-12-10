package Controller;

import Model.CurrentUser;
import Model.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import project.Messaging_sr;
import project.App;

import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MessageController {

    @FXML
    private ListView<String> messageListView;
    @FXML
    private Button sendMessageButton;
    @FXML
    private TextField messageTextField;
    @FXML
    private TextField sendMessageTo;

    @FXML
    private final Messaging_sr messagingService;

    String senderId = CurrentUser.getInstance().getId();
    String receiverId;
    @FXML
    private Button messageToHomeButton;

    // ObservableList to store messages for the ListView
    private ObservableList<String> messageList = FXCollections.observableArrayList();

    public MessageController() {
        this.messagingService = new Messaging_sr(App.fstore);
    }

    @FXML
    public void setUIComponents(ListView<String> messageListView, Button sendMessageButton, TextField messageTextField) {
        this.messageListView = messageListView;
        this.sendMessageButton = sendMessageButton;
        this.messageTextField = messageTextField;

        //Set the ObservableList as the items for the ListView
        messageListView.setItems(messageList);
    }

    @FXML
    public void initialize() {
        receiverId = sendMessageTo.getText();
        loadMessages(senderId, receiverId);
    }

    @FXML
    public void sendMessage() {
        String content = messageTextField.getText();
        receiverId = sendMessageTo.getText(); // Update receiverId from the input field
        Message message = new Message(senderId, receiverId, content, null);
        messagingService.sendMessage(message);
        loadMessages(senderId, receiverId);
    }

    @FXML
public void loadMessages(String senderId, String receiverId) {
    System.out.println("In loadMessages method");
    List<Message> messages = messagingService.getMessages(senderId, receiverId);

    Platform.runLater(() -> {
        messageList.clear(); // Clear existing items

        for (Message message : messages) {
            String senderName = message.getSenderId();
            if (senderName == null) {
                senderName = "Unknown Sender";
            }

            String messageText = message.getContent();
            String displayText = senderName + ": " + messageText;

            // Add the message to the ObservableList
            messageList.add(displayText);
            System.out.println("Message added to the ObservableList: " + displayText);
        }
    });

    System.out.println("Number of messages: " + messages.size());
}

    @FXML
    private void sendMessageButton(ActionEvent event) {
        sendMessage();
    }

    @FXML
    public void home() {
        ItemDescriptionController x = new ItemDescriptionController();
        x.backToHome();
    }
}
