package com.projegazi.todolist.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projegazi.todolist.MainActivity;
import com.projegazi.todolist.R;
import com.projegazi.todolist.SearchActivity;

public class loginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    EditText minputEmaill,minputPasswordL;
    TextView mforgotPassword,mtextViewSignUp, mtxtkullanim;
    Button mbtnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initCompenents();

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Ana sayfaya geçiş
        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(loginActivity.this, MainActivity.class));
        }

        mtxtkullanim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, SearchActivity.class));
            }
        });

        //Giriş yapma
        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=minputEmaill.getText().toString().trim();
                String password=minputPasswordL.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if(task.isSuccessful())
                            {
                                checkmailverfication();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Böyle bir hesap yok!",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });

        //Kaydol sayfasına geçiş
        mtextViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, registerActivity.class));
            }
        });

        //Şifremi unuttum sayfasına geçiş
        mforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, forgotPasswordActivity.class));
            }
        });
    }

    //Tanımlamalar
    private void initCompenents() {
        minputEmaill=findViewById(R.id.inputEmailL);
        minputPasswordL=findViewById(R.id.inputPasswordL);
        mforgotPassword=findViewById(R.id.forgotPassword);
        mtextViewSignUp=findViewById(R.id.textViewSignUp);
        mbtnLogin=findViewById(R.id.btnLogin);
        mtxtkullanim=findViewById(R.id.txtkullanim);
    }


    //E-posta doğrulama
    private void checkmailverfication()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(getApplicationContext(),"Giriş yapıldı",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(loginActivity.this,MainActivity.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Önce e-postanızı doğrulayın.",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}