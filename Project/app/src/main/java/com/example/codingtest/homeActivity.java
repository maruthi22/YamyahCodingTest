package com.example.codingtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.datatransport.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSignIn;
    private Button buttonShowProducts;
    private Button buttonDeals;
    private Button buttonMaps;
    private EditText editTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    FirebaseFirestore db;
    private static final String TAG = "homeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
      /*  if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), homeActivity.class));
        }*/
        db = FirebaseFirestore.getInstance();
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        buttonShowProducts = (Button) findViewById(R.id.buttonShowProducts);
        buttonDeals = (Button) findViewById(R.id.buttonDeals);
        buttonMaps = (Button) findViewById(R.id.buttonMaps);
        buttonShowProducts.setOnClickListener(this);
        buttonDeals.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

    }


    public void AddProduct() {
//        Toast.makeText(homeActivity.this,"lkk.ufyjdthjrysError",Toast.LENGTH_LONG).show();
        Map<String, Object> city = new HashMap<>();
        city.put("name", editTextPassword.getText());

        db.collection("Products").document()
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void ShowProducts() {

//        DocumentReference docRef = db.collection("Products").document('weQNS0BmyqoUwpVfji9r');
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });


        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Event> eventList = new ArrayList<>();
                            StringBuilder myB = new StringBuilder();
                            for (DocumentSnapshot doc : task.getResult()) {
                                for (String s : doc.getData().keySet()) {
                                    Log.d(TAG, "" + myB.append(doc.get(s) + "\n"));
                                }
                            }

                            new AlertDialog.Builder(homeActivity.this)
                                    .setTitle("Data from Firebase Firestore")
                                    .setMessage(myB.toString())


                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.stat_sys_upload)
                                    .show();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public void onClick(View view) {
        if (view == buttonSignIn) {
            AddProduct();
        }

        if (view == buttonShowProducts) {
            ShowProducts();
        }

        if (view == buttonDeals) {
            startActivity(new Intent(getApplicationContext(), Deals.class));
        }

        if (view == buttonMaps) {
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
    }
}
