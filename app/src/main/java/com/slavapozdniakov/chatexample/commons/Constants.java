package com.slavapozdniakov.chatexample.commons;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public interface Constants {
    // Constantes pour Firebase
    // Auth
    FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();

    // Current User
    FirebaseUser CURRENT_USER = FIREBASE_AUTH.getCurrentUser();
    // Firestore
    FirebaseFirestore FIRESTORE_INSTANCE = FirebaseFirestore.getInstance();
    // Storage
    FirebaseStorage STORAGE_INSTANCE = FirebaseStorage.getInstance();

    // CONSTANTES POUR LES DOSSIERS DU STORAGE
    // Dossier des avatars utilisateurs
    String AVATARS_FOLDER = "avatars_user";


    // CONSTANTES DES COLLECTIONS ET DE LEURS CHAMPS
    // Collection Users
    String USERS = "Users";

    String NAMES = "name";
    String EMAIL = "email";
    String ONLINE = "online";
    String AVATAR = "avatar";
    // End Collection Users
}
