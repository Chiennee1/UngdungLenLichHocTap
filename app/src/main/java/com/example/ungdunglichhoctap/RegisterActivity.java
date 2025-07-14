package com.example.ungdunglichhoctap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import DoiTuong.User;

public class RegisterActivity extends AppCompatActivity {

    private Button btnDangKy, btnGoogleSignUp;
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    TextView txtDangNhap;
    private TextView btnBack;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001; // Request code for Google Sign-In
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        btnDangKy = findViewById(R.id.btnRegister);
        btnGoogleSignUp = findViewById(R.id.btnGoogleSignUp);
        edtEmail = findViewById(R.id.etEmail);
        edtPassword = findViewById(R.id.etPassword);
        edtConfirmPassword = findViewById(R.id.etConfirmPassword);
        txtDangNhap = findViewById(R.id.tvLogin);
        btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, SiginActivity.class);
            startActivity(intent);
        });
        btnDangKy.setOnClickListener(v -> XulyDangKy());
        btnGoogleSignUp.setOnClickListener(v -> {
            XulyDangKyGoogle();
        });
        txtDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, SiginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void XulyDangKyGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount> task =
                    com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                com.google.android.gms.auth.api.signin.GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Đăng ky thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserForFirebase(user);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công với Google", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, SiginActivity.class);
                        intent.putExtra("email", user.getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void XulyDangKy() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(email)) {
            edtEmail.setError("Vui lòng nhập email hợp lệ");
            edtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Mật khẩu không được để trống!");
            edtPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edtPassword.requestFocus();
            return;
        }
        if (mAuth == null) {
            Toast.makeText(this, "Firebase Authentication not initialized", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    btnDangKy.setEnabled(true);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUserForFirebase(user);
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, SiginActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Log.e("RegisterActivity", "Registration error", e);
                            Toast.makeText(RegisterActivity.this, "Lỗi: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                });
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void saveUserForFirebase(FirebaseUser userFirebase) {
        if(userFirebase != null){
            String userId = userFirebase.getUid();
            String email = userFirebase.getEmail();
            String displayName = userFirebase.getDisplayName();
            if(displayName == null || displayName.isEmpty()) {
                displayName = email != null ? email.split("@")[0] : "User";
            }
            String photoUrl = userFirebase.getPhotoUrl() != null ?
                    userFirebase.getPhotoUrl().toString() : "";
            User user = new User(userId, email, displayName, photoUrl);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userRef = database.getReference("users").child(userId);
                    userRef.setValue(user).addOnSuccessListener(aVoid -> {
                        Log.d("RegisterActivity", "User saved successfully");
                    }).addOnFailureListener(e -> {
                        Log.e("RegisterActivity", "Failed to save user: " + e.getMessage());
                    });
        }
    }

}