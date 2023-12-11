package Controller;

import Model.CurrentUser;
import Model.Message;
import Model.SharedResource;
import java.io.IOException;
import java.util.Date;
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
    private Button messageToHomeButton;

    private final Messaging_sr messagingService;
    private String senderId = CurrentUser.getInstance().getId();
    private String receiverId;

    //ObservableList to store messages for the ListView
    private ObservableList<String> messageList = FXCollections.observableArrayList();

    public MessageController() {
        this.messagingService = new Messaging_sr(App.fstore);
    }

    public void initialize() {
        this.receiverId = SharedResource.getInstance().getOwnerId();
        messageListView.setItems(messageList);
        loadMessages(senderId, receiverId);
    }

    public void sendMessage() {
        String content = messageTextField.getText();
        if (!content.isEmpty() && receiverId != null) {
            //Current date and time
            Date timestamp = new Date();
            Message message = new Message(senderId, receiverId, content, timestamp);
            messagingService.sendMessage(message);
            loadMessages(senderId, receiverId);
            //Clear the message input field after sending
            messageTextField.clear();
        }
    }

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

                //Add the message to the ObservableList
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
        try {
            App.switchScene("home");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
        loadMessages(senderId, this.receiverId);
    }
}
