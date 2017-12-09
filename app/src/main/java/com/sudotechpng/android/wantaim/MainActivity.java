package com.sudotechpng.android.wantaim;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String mUserID;
    final int RC_SIGN_IN = 123;

    EditText editTextTaskName;
    DatabaseReference dbTasks;
    ListView listViewTasks;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskList = new ArrayList<Task>();
        listViewTasks = (ListView) findViewById(R.id.listViewTasks);
        if (mAuth.getCurrentUser() != null) { // user is registered
            FirebaseUser mUser = mAuth.getCurrentUser();
            String mUserNAME = mUser.getDisplayName();
            mUserID = mUser.getUid();
            dbTasks = database.getReference("tasks");
        } else { // user is not registered
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());
            startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckBox taskCompletedCheckBox = (CheckBox) findViewById(R.id.taskCompletedCheckbox);
        dbTasks.child(mUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskList.clear(); // this ensures when data is updated the list is not appended but a new list is created
                for (DataSnapshot tasksSnapshot : dataSnapshot.getChildren()){
                    Task task = tasksSnapshot.getValue(Task.class);
//                    Toast.makeText(MainActivity.this,task.getTaskName(),Toast.LENGTH_SHORT).show();
                    taskList.add(task);
                }
                TaskList adapter = new TaskList(MainActivity.this, taskList);
                listViewTasks.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Cancelled - DBErr", String.valueOf(databaseError));
            }
        });
    }

    public void addTask(View v){
        editTextTaskName = (EditText) findViewById(R.id.taskName);
        String taskName = editTextTaskName.getText().toString();
        if(!TextUtils.isEmpty(taskName)){
            String mTaskID = dbTasks.push().getKey();
            Task newTask = new Task(mTaskID,taskName,false);
            dbTasks.child(mUserID).child(mTaskID).setValue(newTask);
            editTextTaskName.setText(null);
            Toast.makeText(this, "Added "+taskName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter some text!", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateTask (View v) {
        final TextView taskListItem = (TextView) findViewById(R.id.textViewTaskName);
        taskListItem.performClick();
        taskListItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                taskListItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                Toast.makeText(v.getContext(),"Task Touched",Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
    public void logout(View v) {
        Toast.makeText(MainActivity.this, "Logout!", Toast.LENGTH_SHORT).show();
        mAuth.signOut();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
}
