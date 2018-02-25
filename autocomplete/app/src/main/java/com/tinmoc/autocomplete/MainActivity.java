package com.tinmoc.autocomplete;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference database;
    String suggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize firebase database
        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance().getReference().child("AutoCompleteOptions");
        //Create a new ArrayAdapter with your context and the simple layout for the dropdown menu provided by Android
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        //Child the root before all the push() keys are found and add a ValueEventListener()
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                autoComplete.clear();
                //Basically, this says "For each DataSnapshot *Data* in dataSnapshot, do what's inside the method.
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    //Get the suggestion by childing the key of the string you want to get.
                    suggestion = suggestionSnapshot.child("Interest").getValue(String.class);
                    //Add the retrieved string to the list
                    autoComplete.add(suggestion);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AutoCompleteTextView ACTV = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        //users have to have at least 1 character before the list starts showing suggestions
        ACTV.setThreshold(1);
        ACTV.setAdapter(autoComplete);
    }

    //button press
    protected void aah(View view) {
        AutoCompleteTextView ACTV = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        final String interestVal = ACTV.getText().toString().toLowerCase().trim();
        if(!TextUtils.isEmpty(interestVal))
        {
            database.child(interestVal).child("Interest").setValue(interestVal);
            ACTV.setText("");
        }
    }
}
