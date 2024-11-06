package jccom.example.appbantra;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;

    Button signUp;

    TextView signIn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

//        signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email, password;
//                email = String.valueOf(editTextEmail.getText());
//                password = String.valueOf(editTextPassword.getText());
//
//                if(TextUtils.isEmpty(email)) {
//                    Toast.makeText(RegisterPage.this, "Enter Email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(TextUtils.isEmpty(email)) {
//                    Toast.makeText(RegisterPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                firebaseAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()){
//                                    Toast.makeText(RegisterPage.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                                else {
//                                    Toast.makeText(RegisterPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email, password;
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterPage.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUserInDatabase(user);
                        }else {
                            Toast.makeText(RegisterPage.this, "Đăng ký thất bại: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private void createUserInDatabase(FirebaseUser user) {
                String userId = user.getUid();
                DatabaseReference userRef = mDatabase.child("users").child(userId);

                userRef.child("email").setValue(user.getEmail());
                userRef.child("role").setValue(user.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterPage.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterPage.this, "Lỗi khi lưu thông tin người dùng" + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
}