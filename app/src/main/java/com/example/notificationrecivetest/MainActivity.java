package com.example.notificationrecivetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private User user;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser Fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Fuser = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("users").child(Fuser.getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                user = Objects.requireNonNull(task.getResult()).getValue(User.class);

            }
        });
        final CheckBox reciveOrNot=findViewById(R.id.ReciveOrNot);
        // ReciveOhrNot is the CheckBox id
        reciveOrNot.setChecked(user.getR_n()==1);
        reciveOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(reciveOrNot.isChecked()){
                    User.SubscribeNotification(ref);
                }else{
                    User.unSubscribeNotification(ref);
                }
            }
        });

    }

}