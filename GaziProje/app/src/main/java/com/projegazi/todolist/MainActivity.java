package com.projegazi.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projegazi.todolist.Adapter.ToDoAdapter;
import com.projegazi.todolist.Model.ToDoModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.projegazi.todolist.Users.loginActivity;
import com.projegazi.todolist.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener{

    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private ActivityMainBinding binding;
    private ToDoAdapter adapter;
    private List<ToDoModel> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    TextView exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        actionBar.hide();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleview);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        exit=findViewById(R.id.btnexit);



        //Hesaptan çıkış
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, loginActivity.class));
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

      binding.addicon.setOnClickListener(view -> {
         AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
            Log.d("Add Button", "onClick: I am Add Button");
        });

      list = new ArrayList<>();
      adapter = new ToDoAdapter(MainActivity.this,list);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        showData();
      recyclerView.setAdapter(adapter);
    }

    //Görev Listesini veri tabanından ekranına getirme
    private void showData(){
       query =  firestore.collection("task").document(firebaseUser.getUid())
               .collection("myToDo").orderBy("time", Query.Direction.DESCENDING);
              listenerRegistration = query .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange: value.getDocumentChanges()){
                    if(documentChange.getType()==DocumentChange.Type.ADDED){
                        String id = documentChange.getDocument().getId();
                        ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);

                        list.add(toDoModel);
                        adapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {

        list.clear();
        showData();
        adapter.notifyDataSetChanged();
    }
}