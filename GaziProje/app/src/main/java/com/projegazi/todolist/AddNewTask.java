package com.projegazi.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewTask  extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private TextView setDueDate;
    private EditText mTaskEdit;
    private Button mSaveBtn;
    private FirebaseFirestore firestore;
    private Context context;
    private String dueDate = "";
    private String id = "";
    private String dueDateUpdate = "";
    private static final int RECOGNIZER_CODE = 1;
    FirebaseUser firebaseUser;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task , container , false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate = view.findViewById(R.id.set_due_tv);
        mTaskEdit = view.findViewById(R.id.task_edittext);
        mSaveBtn = view.findViewById(R.id.button);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        firestore = FirebaseFirestore.getInstance();

       boolean isUpdate = false;

        final Bundle bundle = getArguments();

        //Güncelleme kontrol
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            id = bundle.getString("id");
            dueDateUpdate = bundle.getString("due");

            mTaskEdit.setText(task);
            setDueDate.setText(dueDateUpdate);

            if (task.length() > 0){
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }
        }

        //Görev ekleme kontrol
       mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.material_dynamic_secondary40));
                }else{
                    mSaveBtn.setEnabled(true);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.material_dynamic_secondary40));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Takvim
        setDueDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            int MONTH = calendar.get(Calendar.MONTH);
            int YEAR = calendar.get(Calendar.YEAR);
            int DAY = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view1, year, month, dayOfMonth) -> {
                month = month + 1;
                setDueDate.setText(dayOfMonth + "/" + month + "/" + year);
                dueDate = dayOfMonth + "/" + month +"/"+year;

            }, YEAR , MONTH , DAY);

            datePickerDialog.show();
        });

        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(v -> {

            String task = mTaskEdit.getText().toString();

            //Güncelleme
            if (finalIsUpdate){
                firestore.collection("task").document(firebaseUser.getUid()).collection("myToDo")
                        .document(id).update("task" , task , "due" , dueDate);
                Toast.makeText(context, "Görev Güncellendi", Toast.LENGTH_SHORT).show();

            }

            //Görev Ekleme
            else {
                if (task.isEmpty()) {
                    Toast.makeText(context, "Lütfen Bir Görev Yazınız", Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, Object> taskMap = new HashMap<>();

                    taskMap.put("task", task);
                    taskMap.put("due", dueDate);
                    taskMap.put("status", 0);
                    taskMap.put("time", FieldValue.serverTimestamp());

                    firestore.collection("task").document(firebaseUser.getUid()).collection("myToDo").add(taskMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(context, "Görev Kaydedildi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
            dismiss();
        });
    }

    //Görev Listesi
   @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RECOGNIZER_CODE && resultCode == Activity.RESULT_OK){
            assert data != null;
            ArrayList<String> tasktext = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mTaskEdit.setText(tasktext.get(0).toString());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
