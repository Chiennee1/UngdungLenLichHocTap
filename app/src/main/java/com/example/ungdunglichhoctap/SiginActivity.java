package com.example.ungdunglichhoctap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SiginActivity extends AppCompatActivity {
    private TextInputEditText edt_Email, edt_Password;
    private Button btn_SignIn, btn_SingInWithGoogle;
    private TextView txt_DangKy, txt_QuenMatKhau;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001; // Request code for Google Sign-In
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sigin);
        findViews();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();// Nhận dữ liệu từ Intent nếu có
        if (intent != null) {
           Bundle bundle = intent.getExtras();
            if (bundle != null) {
                edt_Email.setText(bundle.getString("email"));
                edt_Password.setText(bundle.getString("password"));
            }
        }
        String email = "";
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XulyDangNhap();
            }
        });
        btn_SingInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XulyDangNhapVoiGoogle();
            }
        });
        txt_DangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SiginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txt_QuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến Activity quên mật khẩu
            }
        });
    }

    private void XulyDangNhapVoiGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // Kết quả đăng nhập Google sau khi chay signInIntent
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Dang nhap that bai!: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //Khi ddanwg nhap thanh cong
                        Toast.makeText(this, "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                        Intent intent = new Intent(SiginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //Khi dang nhap that bai
                        Toast.makeText(this, "Dang nhap that bai: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void XulyDangNhap() {
        String email = edt_Email.getText().toString();
        String password = edt_Password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edt_Email.setError("Email không được để trống");
            edt_Email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edt_Password.setError("Mật khẩu không được để trống!");
            edt_Password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edt_Password.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edt_Password.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SiginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SiginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SiginActivity.this, "Đăng nhập thất bại: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void findViews() {
        edt_Email = findViewById(R.id.etEmail);
        edt_Password = findViewById(R.id.etPassword);
        btn_SignIn = findViewById(R.id.btnSignIn);
        btn_SingInWithGoogle = findViewById(R.id.btnGoogleSignIn);
        txt_DangKy = findViewById(R.id.tvSignUp);
        txt_QuenMatKhau = findViewById(R.id.tvForgotPassword);
    }
}