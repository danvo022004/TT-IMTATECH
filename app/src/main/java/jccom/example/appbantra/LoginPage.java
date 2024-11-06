package jccom.example.appbantra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class LoginPage extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;

    Button signIn;

    TextView signUp;

//    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static  final String SHARED_PREFS = "sharedPrefs";

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
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


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                      loginUser();
            }
        });


//        signIn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                String email, password;
//                email = String.valueOf(editTextEmail.getText());
//                password = String.valueOf(editTextPassword.getText());
//
//                if(TextUtils.isEmpty(email)) {
//                    Toast.makeText(LoginPage.this, "Enter Email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if(TextUtils.isEmpty(email)) {
//                    Toast.makeText(LoginPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                firebaseAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful())
//                                {
//                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                                    editor.putString("email", "true");
//                                    editor.apply();
//
//                                    Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(LoginPage.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });

    }

    private  void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkUserRole(user);
                        } else {
                            Toast.makeText(LoginPage.this, "Đăng nhập thất bại: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserRole(FirebaseUser user) {
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String role = dataSnapshot.child("role").getValue(String.class);
                            if ("admin".equals(role)) {
                                Intent intent = new Intent(LoginPage.this, RevennueActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginPage.this, "Không tìm thấy thông tin người dùng",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                       Toast.makeText(LoginPage.this, "Lỗi khi đọc dữ liệu: " + databaseError.getMessage(),
                               Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

// checkbox lưu tài khoản

//    private void checkBox() {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        String check = sharedPreferences.getString("email", "");
//        if(check.equals("true")) {
//            Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(LoginPage.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}