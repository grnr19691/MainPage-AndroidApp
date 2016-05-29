package com.example.venkatneehar.mainpage;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    final Context context = this;
    final static List<Person> person = new ArrayList<>();
    final static List<Person> favorites=new ArrayList();

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ChatApp";
    // Labels table name
    private static final String TABLE_CONTACTS = "Contacts";
    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    public static String SENDER_ID = "748551893309";

    private static final String TABLE_MESSEGES = "Messages";
    // Labels Table Columns names
    private static final String MESSAGE_ID = "messageId";
    private static final String SENDER_ID1 = "senderID";
    private static final String RECEIVER_ID = "receiverID";
    private static final String TEXT = "message";


    private static final String TABLE_MYEVENTS = "MyEvents";
    private static final String MyEvent_ID = "eventId";
    private static final String MyEvent_NAME = "eventName";
    private static final String MyEvent_DECISION = "decision";

    private static final String TABLE_EVENTS = "Events";
    private static final String Event_ID = "eventId";
    private static final String Event_NAME = "eventName";
    private static final String Event_FRIENDS_NAME = "friendsName";
    private static final String Event_DECISION = "decision";



    ListView listView;
    int flag=0;
    static CustomAdapter CustAdpt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity

            startActivity(new Intent(MainActivity.this, Register.class));
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
        }

        Log.d("PHONE SENDER ID:",getPhoneId(this.getApplicationContext()));
        SENDER_ID=getPhoneId(this.getApplicationContext());

           SQLiteDatabase db = new DataBaseHandler(this).getWritableDatabase();
            createDataBase(db);



        listView = (ListView) findViewById(R.id.listView);
        CustAdpt=new CustomAdapter(this, R.layout.custom_row,
                person);
        updateFromDb();
        listView.setAdapter(CustAdpt);





//        fetchContacts();

        //   SQLiteDatabase db = new DataBaseHandler(this).getWritableDatabase();
        //    createDataBase(db);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView <? > arg0,View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ChatInstance.class);

               Log.d("ID_phoneNumber::::",person.get(position).getName());
                i.putExtra("ID_phoneNumber",person.get(position).getName());
                startActivity(i);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onResume(){
   super.onResume();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contacts) {

            Intent i = new Intent(getApplicationContext(), Contacts.class);
            i.putExtra("Description","Concert of Shakira on April 25. For bookings click call");
            startActivity(i);
            return true;
        }
        else if(id==R.id.action_events){
            Intent i = new Intent(getApplicationContext(), Events.class);
            i.putExtra("Description","Concert of Shakira on April 25. For bookings click call");
            startActivity(i);
            return true;
        }
  /*      else if (id==R.id.action_favorites){
            Intent i = new Intent(getApplicationContext(), favorites.class);
            startActivity(i);

            return true;
        }
        else if(id == R.id.action_uninstall){
            Uri packageURI = Uri.parse("package:"+"com.example.venkatneehar.popartists");
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
            uninstallIntent.setData(packageURI);
            startActivity(uninstallIntent);
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }

    public void createDataBase(SQLiteDatabase db){
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_IMAGE+" BLOB,"+
                KEY_NAME + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_MESSEGES + "("
                + MESSAGE_ID + " INTEGER PRIMARY KEY," +
                SENDER_ID1 + " TEXT," +
                RECEIVER_ID + " TEXT," +
                TEXT + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_MYEVENTS + "("
                + MyEvent_ID + " INTEGER PRIMARY KEY," +
                MyEvent_NAME + " TEXT," +
                MyEvent_DECISION + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_EVENTS + "("
                + Event_ID + " INTEGER PRIMARY KEY," +
                Event_NAME + " TEXT," +
                Event_FRIENDS_NAME + " TEXT," +
                Event_DECISION + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    public void updateFromDb(){
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        Cursor cursor=db.getAllContacts();
           person.clear();
        if (cursor.moveToFirst()) {
            do {
                person.add(new Person(cursor.getString(2),cursor.getInt(1)));
                Log.d("Check database:",cursor.getString(2));
            } while (cursor.moveToNext());
        }

    }


    private String getPhoneId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String phoneId = prefs.getString(GCMNotificationIntentService.PROPERTY_PHONE_ID, "");
        if (phoneId.isEmpty()) {
            Log.i(GCMNotificationIntentService.TAG, "Registration not found.");
            return "";
        }
        return phoneId;
    }
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

}