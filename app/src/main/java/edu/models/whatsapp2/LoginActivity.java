package edu.models.whatsapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth firebaseAuth;
    private EditText nicknameText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, ChatsActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        nicknameText = findViewById(R.id.nickname);
        nicknameText.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                signClick.onClick(null);
                return true;
            }
            return false;
        });
        findViewById(R.id.email_sign_in_button).setOnClickListener(signClick);
    }

    private View.OnClickListener signClick = view -> {
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInAnonymously:success");
                        FirebaseDatabase.getInstance().getReference()
                                .child("users")
                                .child(firebaseAuth.getCurrentUser().getUid()).setValue(nicknameText.getText().toString());
                        Toast
                                .makeText(LoginActivity.this, "Welcome!", Toast.LENGTH_SHORT)
                                .show();
                        startActivity(new Intent(this, ChatsActivity.class));
                        finish();
                    } else {
                        Log.d(TAG, "signInAnonymously:unsuccess");
                        Snackbar
                                .make(findViewById(android.R.id.content), "Something went wrong, try again", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    };

}
