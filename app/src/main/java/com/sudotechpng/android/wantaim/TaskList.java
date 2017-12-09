package com.sudotechpng.android.wantaim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by syagi on 08/16/2017.
 */

public class TaskList extends ArrayAdapter<Task> {

    private Activity context;
    private List<Task> taskList;

    public TaskList(Activity context, List<Task> taskList) {
        super(context, R.layout.list_layout, taskList);
        this.context = context;
        this.taskList = taskList;
    }
    @NonNull
    @Override
    public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewTask = (TextView) listViewItem.findViewById(R.id.textViewTaskName);
        CheckBox checkBoxTask = (CheckBox) listViewItem.findViewById(R.id.taskCompletedCheckbox);
        Task task = taskList.get(position);
        textViewTask.setText(task.getTaskName());
        checkBoxTask.setChecked(task.getCompleted());
        checkBoxTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Task mTask = taskList.get(position);
                String mTaskName = mTask.getTaskName();
                String mTaskID = mTask.getTaskId();
                mTask.setCompleted(isChecked);
//                Firebase transactions and authentications
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                assert mUser != null;
                String mUserID = mUser.getUid();
//                Database instantiation, reference, transactions
                FirebaseDatabase fdbInstance = FirebaseDatabase.getInstance();
                DatabaseReference dbRefTasks = fdbInstance.getReference("tasks");
                DatabaseReference dbRefUserTasks = dbRefTasks.child(mUserID).child(mTaskID);
                Map<String, Object> taskUpdates = new HashMap<>();
                taskUpdates.put("completed",isChecked);
                dbRefUserTasks.updateChildren(taskUpdates);
//                Toast.makeText(getContext(),mTaskName+" Completed!",Toast.LENGTH_SHORT).show();
            }
        });
        return listViewItem;
    }
}
