package project;

import Model.Message;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Messaging_sr {

    private final Firestore firestore;

    public Messaging_sr(Firestore firestore) {
        this.firestore = firestore;
    }

    public void sendMessage(Message message) {
        // Your code to send a message
        try {
            firestore.collection("messages").add(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessages(String senderId, String receiverId) {
        List<Message> messages = new ArrayList<>();

        try {
            System.out.println("Fetching messages from sender to receiver");
            // Retrieve sent messages
            Query sentQuery = firestore.collection("messages")
                    .whereEqualTo("senderId", senderId)
                    .whereEqualTo("receiverId", receiverId)
                    .orderBy("timestamp", Query.Direction.ASCENDING);

            ApiFuture<QuerySnapshot> sentMessages = sentQuery.get();
            for (QueryDocumentSnapshot document : sentMessages.get().getDocuments()) {
                Message message = document.toObject(Message.class);
                messages.add(message);
            }

            // Retrieve received messages
            System.out.println("Fetching messages from receiver to sender");
            Query receivedQuery = firestore.collection("messages")
                    .whereEqualTo("senderId", receiverId)
                    .whereEqualTo("receiverId", senderId)
                    .orderBy("timestamp", Query.Direction.ASCENDING);

            ApiFuture<QuerySnapshot> receivedMessages = receivedQuery.get();
            for (QueryDocumentSnapshot document : receivedMessages.get().getDocuments()) {
                Message message = document.toObject(Message.class);
                messages.add(message);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Sort messages based on timestamp
        messages = messages.stream()
                .filter(message -> message.getTimestamp() != null)
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());

        System.out.println("Number of messages fetched: " + messages.size());
        return messages;
    }
}