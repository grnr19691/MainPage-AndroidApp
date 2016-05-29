package com.example.venkatneehar.mainpage;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
public class MainActivity1 extends ActionBarActivity
{
    private String owner_id="owner";
    private String friend_id="friend";

    private DiscussArrayAdapter adapter;
    private ListView lv;
    private EditText editText1;
    private static Random random;
    private Button btnAdd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_app_layout);

        Intent x=getIntent();
        friend_id=x.getStringExtra("ID_phoneNumber");
        random = new Random();
        // ipsum = new LoremIpsum();
        EventDataBaseHandler db=new EventDataBaseHandler(this);

        lv = (ListView) findViewById(R.id.listView1);
        lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv.setStackFromBottom(true);

        adapter = new DiscussArrayAdapter(getApplicationContext(), R.layout.listitem_discuss);

        lv.setAdapter(adapter);

        editText1 = (EditText) findViewById(R.id.editText1);



        addItems();
    }
    public void onClick(View view)
    {

        // Perform action on key press
        if(!editText1.getText().toString().contentEquals(""))
            adapter.add(new OneComment(false, editText1.getText().toString()));
        DataBaseHandler db = new DataBaseHandler( getApplicationContext());
        // inserting new label into database
        if(!editText1.getText().toString().contentEquals(""))
            db.insertMessage("owner", "friend", editText1.getText().toString());
        editText1.setText("");
    }
    private void addItems()
    {
        adapter.add(new OneComment(true, "Wassup brother"));
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getAllMessages(owner_id,friend_id);
        List<List<String>> senders = db.getAllSenders(owner_id, friend_id);
        adapter.add(new OneComment(false, editText1.getText().toString()));
        for (int i = 0; i<lables.size(); i++)
        {
            boolean left=(senders.get(i)).get(1).equalsIgnoreCase(owner_id);

            int word = getRandomInteger(1, 10);
            int start = getRandomInteger(1, 40);
            String words = "it is"+word+start;

            adapter.add(new OneComment(!left, (String)lables.get(i)));
        }
    }

    private void loadListData()
    {
        // database handler
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getAllMessages(owner_id,friend_id);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        lv.setAdapter(dataAdapter);
    }

    private static int getRandomInteger(int aStart, int aEnd) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        long range = (long) aEnd - (long) aStart + 1;
        long fraction = (long) (range * random.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }




/*
    // Spinner element
    Spinner spinner;
    // Add button
    //Button btnAdd;
    // Input text
    EditText inputLabel;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        // add button
        btnAdd = (Button) findViewById(R.id.btn_add);
        // new label input field
        inputLabel = (EditText) findViewById(R.id.input_label);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Loading spinner data from database
        loadSpinnerData();
        /**
            * Add new label button click listener
            * */
   /*     btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                String label = inputLabel.getText().toString();
                if (label.trim().length() > 0)
                {
                    // database handler
                    DataBaseHandler db = new DataBaseHandler( getApplicationContext());
                    // inserting new label into database
                    db.insertMessage("owner","friend",label);
                    // making input filed text to blank
                    inputLabel.setText("");
                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);
                    // loading spinner with newly added data
                    loadSpinnerData();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter label name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * Function to load the spinner data from SQLite database
     * */
 /*   private void loadSpinnerData()
    {
        // database handler
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getAllMessages();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // TODO Auto-generated method stub
    }
    public void onDestroy()
    {
        super.onDestroy();

    }*/
}