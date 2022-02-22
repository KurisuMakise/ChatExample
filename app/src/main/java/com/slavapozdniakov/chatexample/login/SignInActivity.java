package com.slavapozdniakov.chatexample.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.slavapozdniakov.chatexample.MainActivity;
import com.slavapozdniakov.chatexample.R;

public class SignInActivity extends AppCompatActivity {

    // 1 Vars globales
    private TextInputEditText etEmail, etPassword;
    private String email, password;


    // 2 Méthode d'initUI
    private void initUI() {
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
    }

    // 4 Gestion du bouton login
    public void btnLoginClick(View v) {
        // 4.1 Récupération des données du formulaire
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim(); // trim -> supprime les espaces au début et à la fin
        // 4.2 Vérification du remplissage des champs email et password
        if (email.equals("")) {
            etEmail.setError(getString(R.string.login_entrer_email));
        } else if (password.equals("")) {
            etPassword.setError(getString(R.string.login_entrer_password));
        } else {
            // 4.3 connexion à Authenticator
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, getString(R.string.sign_in_failed) + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    // Méthodes pour la gestion des boutons
    public void btnSignUpActivity(View view){
        startActivity(new Intent(SignInActivity.this,LoginSignUpActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_in);
        // 3 Appel de la méthode initUI
        initUI();
    }
}