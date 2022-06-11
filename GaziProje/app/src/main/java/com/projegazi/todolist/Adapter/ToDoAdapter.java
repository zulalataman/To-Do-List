package com.projegazi.todolist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projegazi.todolist.AddNewTask;
import com.projegazi.todolist.MainActivity;
import com.projegazi.todolist.Model.ToDoModel;
import com.projegazi.todolist.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> todolist;
    private MainActivity activity;
    private FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    public ToDoAdapter(MainActivity activity , List<ToDoModel> todolist){
        this.todolist = todolist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task,parent,false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    public Context getContext(){
        return activity;
    }

    //Görev Silme
    public void deleteTask(int position){
        ToDoModel toDoModel = todolist.get(position);
        firestore.collection("task").document(firebaseUser.getUid()).collection("myToDo")
                .document(toDoModel.TaskId).delete();
        todolist.remove(position);
        notifyItemRemoved(position);
    }

    //Görev Güncellemede Verileri Getirme (Data gönderme)
    public void editTask(int position){
        ToDoModel toDoModel = todolist.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task",toDoModel.getTask());
        bundle.putString("due",toDoModel.getDue());
        bundle.putString("id",toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());

    }

    //Görevin yapılıp yapılmama durumu
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = todolist.get(position);
        holder.checkBox.setText(toDoModel.getTask());
        holder.Duedate.setText("Son Tarih " + toDoModel.getDue());
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        holder.checkBox.setChecked(tobool(toDoModel.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    firestore.collection("task").document(firebaseUser.getUid()).collection("myToDo")
                            .document(toDoModel.TaskId).update("status",1);
                }else {
                    firestore.collection("task").document(firebaseUser.getUid()).collection("myToDo")
                            .document(toDoModel.TaskId).update("status",0);
                }
            }
        });

    }

    private boolean tobool(int status){
        return status!=0;
    }

    @Override
    public int getItemCount() {
        return todolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Duedate;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            Duedate = itemView.findViewById(R.id.due_date);
            checkBox = itemView.findViewById(R.id.check_box);

        }
    }
}
