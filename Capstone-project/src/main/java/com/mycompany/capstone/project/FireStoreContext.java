package com.mycompany.capstone.project;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;

public class FireStoreContext {
//initialize firebase 

    public Firestore firebase() {
        try {
            // Check if Firebase has already been initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount = new FileInputStream("key.json");

                // Initialize Firebase with the provided credentials and database URL
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://csc325-capstone.firebaseio.com")
                        .setStorageBucket("csc325-capstone.appspot.com")
                        .build();

                // Initialize the Firebase app
                FirebaseApp.initializeApp(options);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
            // Initialize Firestore and return it
            return FirestoreClient.getFirestore();
    }
}