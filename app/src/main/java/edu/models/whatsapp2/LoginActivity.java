package edu.models.whatsapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import whatsapp_sequential.machine3;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth firebaseAuth;
    private TextInputLayout nicknameLayout;
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
        nicknameLayout = findViewById(R.id.nickname_layout);
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
        String nickname = String.valueOf(nicknameText.getText());
        if (nickname == null || nickname.isEmpty()) {
            nicknameLayout.setError("This field should be filled");
            return;
        }
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Integer userId = App.getMachine().get_nextUserIndex();
                        if (userId == null) userId = 1;

                        FirebaseDatabase.getInstance().getReference()
                                .child("users_names")
                                .child(userId.toString()).setValue(nickname);

                        App.getMachine().get_add_user().run_add_user(userId);

                        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this)
                                .edit()
                                .putInt("user_id", userId)
                                .apply();

                        Toast
                                .makeText(LoginActivity.this, String.format("Welcome, %s!", nickname), Toast.LENGTH_SHORT)
                                .show();

                        startActivity(new Intent(LoginActivity.this, ChatsActivity.class));

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
