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
import com.projegazi.todolist.R;

public class registerActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    EditText minputEmails,minputPasswords;
    TextView malreadyHaveAccount;
    Button mbtnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponent();

        firebaseAuth=FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Kayıt olma
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=minputEmails.getText().toString().trim();
                String password=minputPasswords.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<7){
                    Toast.makeText(getApplicationContext(), "Şifre en az 7 karakterden oluşmalıdır!", Toast.LENGTH_SHORT).show();
                }
                else{

                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Kayıt yapıldı.", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Kayıt yapılamadı..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //Zaten hesabın var
        malreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this, loginActivity.class));
            }
        });
    }

    //Tanımlamalar
    private void initComponent() {
        minputEmails=findViewById(R.id.inputEmailS);
        minputPasswords=findViewById(R.id.inputPasswordS);
        malreadyHaveAccount=findViewById(R.id.alreadyHaveAccount);
        mbtnRegister=findViewById(R.id.btnRegister);
    }

    //Doğrulama e-postası yollama
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Doğrulama e-postası gönderildi. Doğrulayın ve tekrar giriş yapın.",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(registerActivity.this,loginActivity.class));
                }
            });
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Doğrulama e-postası gönderilemedi.",Toast.LENGTH_SHORT).show();
        }
    }
}