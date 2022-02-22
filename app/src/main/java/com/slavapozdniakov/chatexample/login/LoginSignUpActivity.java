package com.slavapozdniakov.chatexample.login;

//import static com.slavapozdniakov.chatexample.commons.Constants.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.slavapozdniakov.chatexample.MainActivity;
import com.slavapozdniakov.chatexample.R;
import com.slavapozdniakov.chatexample.commons.Constants;

import java.util.HashMap;

public class LoginSignUpActivity extends AppCompatActivity implements Constants {

    private static final String TAG = "LoginSignUpActivity";

    // 1 Vars Globales
    private TextInputEditText etName, etEmail, etPassword, etCofirmPassword;
    private String name, email, password, confirmPassword;
    // 5.1 Ajout de la variable firebase
    private FirebaseUser fuser;
    // 6.1 Ajout de la référence vers la collection Users
    private CollectionReference usersCollectionReference;
    // 7 Ajout de la référence vers le dossier du Storage
    private StorageReference fileReference;
    // 9 Ajout de vars pour le traitement des images
    private Uri localFileUri; // Uri de l'image en local
    private Uri serverFileUri; // Url du fichier stocké dans le storage
    private String urlStorageAvatar; // Url dans le storage en String dans la BDD
    // 10 Localisation du widget container de l'image
    private ImageView ivAddAvatar;
    private String userID;

    // 2 Méthode initUI pour l'initialisation des widgets
    private void initUI(){
        etName = findViewById(R.id.etNameSU);
        etEmail = findViewById(R.id.etEMailSU);
        etPassword = findViewById(R.id.etPasswordSU);
        etCofirmPassword = findViewById(R.id.etConfirmPasswordSignUp);
    }

    // 8 Ajout de la méthode init des outils FB
    private void initFB(){
        // Init de Firestore
        usersCollectionReference = FIRESTORE_INSTANCE.collection(USERS);
        // Init du Storage
        fileReference = STORAGE_INSTANCE.getReference();
        fuser = CURRENT_USER;
    }

    // 4 Gestion du click sur le bouton SignUp
    public void btnSignUpClick(View view){
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPassword = etCofirmPassword.getText().toString().trim();

        Log.i(TAG, "btnSignUpClick: on clique sur le bouton Sign Up");
        
        // 4.1 Les Vérification de base
        // 4.1.1 Si les cases sont vides
        if(name.equals("")){
            etName.setError(getString(R.string.enter_name));
        }else if(email.equals("")){
            etEmail.setError(getString(R.string.login_entrer_email));
        }else if(email.equals("")){
            etPassword.setError(getString(R.string.login_entrer_password));
        }else if(email.equals("")){
            etCofirmPassword.setError(getString(R.string.confirm_password));
            // 4.1.2 Les patterns de vérification
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError(getString(R.string.login_entrer_correct_email));
        }else if(!password.equals(confirmPassword)){
            etCofirmPassword.setError(getString(R.string.login_password_mismatch));
        } else {
            // Connexion à la base et enregistrement des données
            FIREBASE_AUTH.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //équivaut à: if(CURRENT_USER != null { ....
                                fuser = FirebaseAuth.getInstance().getCurrentUser();
                                userID = fuser.getUid();
                                Log.i("TAAG", "onComplete: "+userID);
                                //startActivity(new Intent(LoginSignUpActivity.this, MainActivity.class));
                                if(localFileUri != null){
                                    updateNameAndPhoto();
                                } else {
                                    updateNameOnly();
                                }
                            } else {
                                Toast.makeText(LoginSignUpActivity.this, getString(R.string.failed_signup,task.getException()), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void updateNameAndPhoto() {

    }


    private void updateNameOnly(){
        // Ajout de l'utilisateur dans Authentificatore
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(etName.getText().toString().trim())
                .build();
        fuser.updateProfile(request)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            // Création d'un HashMap pour la gestion des données(ou le modèle)
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put(NAMES, etName.getText().toString().trim());
                            hashMap.put(EMAIL, etEmail.getText().toString().trim());
                            hashMap.put(ONLINE, true);
                            hashMap.put(AVATAR,"");

                            usersCollectionReference.document(userID).set(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(LoginSignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginSignUpActivity.this,SignInActivity.class));
                                        }
                                    });
                        } else {
                            Toast.makeText(LoginSignUpActivity.this, getString(R.string.nameUpdateFailed, task.getException()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    // Cycles de vie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        // 3 appel de la méthode initUI
        initUI();
        initFB();
    }
}