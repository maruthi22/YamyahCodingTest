package com.example.codingtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.datatransport.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {

    //Declarations
    private Button addProduct;
    private Button buttonShowProducts;
    private Button buttonDeals;
    private Button buttonMaps;
    private Button logout;
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

        //Assigning address (reference)
        db = FirebaseFirestore.getInstance();
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        addProduct = (Button) findViewById(R.id.buttonSignin);
        logout = (Button) findViewById(R.id.logoutbutton);
        buttonShowProducts = (Button) findViewById(R.id.buttonShowProducts);
        buttonDeals = (Button) findViewById(R.id.buttonDeals);
        buttonMaps = (Button) findViewById(R.id.buttonMaps);

        //Setting Click Listener
        buttonMaps.setOnClickListener(this);
        buttonShowProducts.setOnClickListener(this);
        buttonDeals.setOnClickListener(this);
        addProduct.setOnClickListener(this);
        logout.setOnClickListener(this);

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
                            Toast.makeText(homeActivity.this, "Error Getting Documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //When a button is pressed this method is called
    public void onClick(View view) {
        if (view == addProduct) {
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

        if (view == logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
