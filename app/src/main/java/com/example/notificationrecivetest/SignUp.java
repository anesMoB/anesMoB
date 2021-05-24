package com.example.notificationrecivetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private TextView sign_in;

    private EditText email;
    private EditText password;
    private Button btnSign_up;

    private FirebaseAuth mAuth;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final Activity thisActivity=this;

        /*sign_in=(TextView) findViewById(R.id.sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(thisActivity,Sign_in.class);
                startActivity(intent);
                finish();
            }
        });*/

        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        btnSign_up=(Button) findViewById(R.id.btnSign_up);

        mAuth=FirebaseAuth.getInstance();
        btnSign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Semail=email.getText().toString();
                final String Spassword=password.getText().toString();
                /*if(isValidData(thisActivity,Semail, Spassword)) {*/
                    mAuth.createUserWithEmailAndPassword(Semail, Spassword)
                            .addOnCompleteListener(thisActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mref= FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid());
                                        User user=new User(mref.getKey(),Semail,1);
                                        User.SubscribeNotification(mref);
                                        mref.setValue(user);
                                        Intent intent = new Intent(thisActivity, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(thisActivity, "Error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            //}
        });
    }
    static boolean isValidData(Activity thisActivity,String email,String password){
        Boolean isValidemail,isValidpassword;
        ArrayList<String> errors=new ArrayList<>();


        isValidemail= Patterns.EMAIL_ADDRESS.matcher(email).matches();
        isValidpassword=password.length()>=6;


        if (!isValidemail) {errors.add("أدخل بريد الكتروني صالح");}
        if (!isValidpassword) {errors.add("كلمة السر قصيرة جدا"); }
        if(!( isValidemail && isValidpassword)){show(thisActivity,errors);}

        return  isValidemail && isValidpassword;
    }
    static void show(Activity thisActivity, ArrayList<String> errors){
        AlertDialog.Builder adb = new AlertDialog.Builder(thisActivity);
        LinearLayout L = new LinearLayout(thisActivity);
        L.setOrientation(LinearLayout.VERTICAL);

        for (int i=0;i<errors.size();i++) {
            TextView Tv = new TextView(thisActivity);
            Tv.setText(errors.get(i));
            L.addView(Tv);
        }
        adb.setView(L);
        Dialog d = adb.create();
        d.show();
    }
}
