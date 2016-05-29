package com.example.venkatneehar.mainpage;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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


public class Contacts extends ActionBarActivity {
    final Context context = this;
    final List<Person> person = new ArrayList<>();
    final static List<Person> favorites=new ArrayList();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ChatApp";
    // Labels table name
    private static final String TABLE_CONTACTS = "Contacts";
    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";

    private static final String TABLE_MESSEGES = "Messages";
    // Labels Table Columns names
    private static final String MESSAGE_ID = "messageId";
    private static final String SENDER_ID = "senderID";
    private static final String RECEIVER_ID = "receiverID";
    private static final String TEXT = "message";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);

        fetchContacts();

        //   SQLiteDatabase db = new DataBaseHandler(this).getWritableDatabase();
        //    createDataBase(db);

        //update persons
//        updateFromDb();

        listView.setAdapter(new CustomAdapter(this, R.layout.custom_row,
                person));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView <? > arg0,View view, final int position, long id) {
                // When clicked, show a toast with the TextView text
           /* Toast.makeText(getApplicationContext(), "lets see"+position,
                    Toast.LENGTH_SHORT).show();
*/

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(person.get(position).getName());
                alertDialogBuilder
                        .setMessage("Click yes to add to Chat!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!checkInContact(person.get(position).getName())) {
                                    insertintoDB(person.get(position));
                               //     favorites.add(person.get(position));
                                    finish();
                                    Toast.makeText(context, "Added to Chat!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(context, "Already in Chat!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }


        });

        // registerForContextMenu(listView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_Concert_Info) {

            Intent i = new Intent(getApplicationContext(), concerts_info.class);
            i.putExtra("Description","Concert of Shakira on April 25. For bookings click call");
            startActivity(i);
            return true;
        }
        else if (id==R.id.action_favorites){
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
    }

    public void insertintoDB(Person p1){
        DataBaseHandler db1 = new DataBaseHandler( getApplicationContext());
        String str=p1.getName().replaceAll("[^0-9]+", "");
        db1.insertContact(str,R.drawable.arai);
    }



    public void updateFromDb(){
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        Cursor cursor=db.getAllContacts();

        if (cursor.moveToFirst()) {
            do {
                person.add(new Person(cursor.getString(2),cursor.getInt(1)));
                Log.d("Check database:",cursor.getString(2));
            } while (cursor.moveToNext());
        }

    }

    public boolean checkInContact(String str){
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
       // Cursor cursor=db.getAllContacts();

        Cursor cursor =db.checkContact(str.replaceAll( "[^0-9]+", ""));
        if(cursor.getCount()> 0 ) return true;

/*
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(2).equals(str)) return true;
            } while (cursor.moveToNext());
        }  */
            return false;
    }



    public void fetchContacts() {

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        Uri ImageCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String imageCONTACT_ID = ContactsContract.CommonDataKinds.Photo.CONTACT_ID;
        String Photo = ContactsContract.CommonDataKinds.Photo.PHOTO;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        Person p1;
        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);


                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                        if(phoneNumber!="" && phoneNumber!=null) {
                            p1 = new Person(phoneNumber, R.drawable.arai);
                            person.add(p1);
                        }
                    }

                    phoneCursor.close();

                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\nEmail:" + email);

                    }

                    emailCursor.close();
                }

                output.append("\n");

            }


            //     outputText.setText(output);
        }
    }
}