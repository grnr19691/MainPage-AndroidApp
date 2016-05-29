package com.example.venkatneehar.mainpage;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Events extends ActionBarActivity {


    ListView listViewGoing;
    ListView listViewPending;
    int flag=0;
    CustomAdapter CustAdpt;
    CustomAdapter CustAdpt1;
    List<Person> person=new ArrayList<>();
    List<Person> person1=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        listViewGoing = (ListView) findViewById(R.id.listViewGoing);
        listViewPending = (ListView) findViewById(R.id.listViewPending);
        CustAdpt=new CustomAdapter(getApplicationContext(), R.layout.custom_row,person);
        CustAdpt1=new CustomAdapter(getApplicationContext(), R.layout.custom_row,person1);
        listViewGoing.setAdapter(CustAdpt);
        listViewPending.setAdapter(CustAdpt1);
        updateListGoing();
        updateListPending();

    }
    public void updateListGoing(){
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        db.getWritableDatabase();
        db.insertMyEvent("Plan to something","yes");
        db.insertMyEvent("Plan to something1","Pending");
        db.insertMyEvent("Plan to something2","yes");
        db.insertMyEvent("Plan to somethin3","Pending");
        db.getReadableDatabase();
        Cursor cursor=db.getAllMyEvents();

        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(2).equals("yes"))
                    person.add(new Person(cursor.getString(1),R.drawable.arai));
                else
                    person1.add(new Person(cursor.getString(1),R.drawable.arai));

                Log.d("Check database:", cursor.getString(2));
            } while (cursor.moveToNext());
        }
        db.close();

    }

    public void updateListPending(){

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
