package com.example.venkatneehar.mainpage;


import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues; import android.content.Context; import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper {
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

    private static final String TABLE_MYEVENTS = "MyEvents";
    private static final String MyEvent_ID = "eventId";
    private static final String MyEvent_NAME = "eventName";
    private static final String MyEvent_DECISION = "decision";

    private static final String TABLE_EVENTS = "Events";
    private static final String Event_ID = "eventId";
    private static final String Event_NAME = "eventName";
    private static final String Event_FRIENDS_NAME = "friendsName";
    private static final String Event_DECISION = "decision";



    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
// Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_IMAGE+" BLOB,"+
                KEY_NAME + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);

        CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_MESSEGES + "("
                + MESSAGE_ID + " INTEGER PRIMARY KEY," +
                SENDER_ID + " TEXT," +
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
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int
            oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
// Create tables again
onCreate(db);

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSEGES);
        // Create tables again
        onCreate(db);
    }
    /**
     * Inserting new label into labels table
     * */
    public void insertContact(String label,Integer image){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, label);
        values.put(KEY_IMAGE, image);
// Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        MainActivity.CustAdpt.add(new Person(label,image));
      // MainActivity.person.add(new Person(label,image));
        db.close(); // Closing database connection
    }

    public void insertMessage(String senderID,String receiverID,String label)
    {
        Log.d("DATABASE", senderID + " " + receiverID + " " + label);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SENDER_ID, senderID);
        values.put(RECEIVER_ID, receiverID);
        values.put(TEXT, label);
        // Inserting Row
        db.insert(TABLE_MESSEGES, null, values);
        db.close(); // Closing database connection
    }

    public void insertMyEvent(String eventNAME,String decision)
    {
        Log.d("Event DATABASE",  eventNAME + " " + decision);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Event_NAME, eventNAME);
        values.put(Event_DECISION, decision);
        // Inserting Row
        db.insert(TABLE_MYEVENTS, null, values);
        db.close(); // Closing database connection
    }

    public void insertEvent(String eventNAME,String FriendNAME,String decision)
    {
        Log.d("Event DATABASE",  eventNAME + " " + decision);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Event_NAME, eventNAME);
        values.put(Event_FRIENDS_NAME, FriendNAME);
        values.put(Event_DECISION, decision);
        // Inserting Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }

    public Cursor getAllEvents(String EventName){
        List<String> labels = new ArrayList<String>();
        List<byte[]> images = new ArrayList<byte[]>();

        String selectQuery = "SELECT * FROM " +
                TABLE_EVENTS+ " WHERE "+Event_NAME+" like "+EventName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getAllMyEvents(){
        List<String> labels = new ArrayList<String>();
        List<byte[]> images = new ArrayList<byte[]>();

        String selectQuery = "SELECT * FROM " +
                TABLE_MYEVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    /**
     * Getting all labels
     * returns list of labels
     * */
    public Cursor getAllContacts(){
        List<String> labels = new ArrayList<String>();
        List<byte[]> images = new ArrayList<byte[]>();

        String selectQuery = "SELECT * FROM " +
                TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor checkContact(String str){
        List<String> labels = new ArrayList<String>();
        List<byte[]> images = new ArrayList<byte[]>();

        String selectQuery = "SELECT * FROM "+
                TABLE_CONTACTS +
                "  WHERE "+KEY_NAME+"="+str;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public List<String> getAllMessages(String ownerID,String friendID)
    {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //
        String rawQuery = "SELECT * FROM " + TABLE_MESSEGES + " WHERE " +  SENDER_ID +" like " + friendID  +
                " OR " + RECEIVER_ID +" like " + friendID+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Log.d("CURSOR:", cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        cursor.moveToFirst();

        if (cursor.moveToFirst())
        {
            do
            {
                labels.add(cursor.getString(3));
            }
            while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning labels
      //  Log.d("LABELS:MESSAGES :",labels.toString());

        return labels;
    }

    public List<List<String>> getAllSenders(String ownerID,String friendID)
    {
        List<String> labels = new ArrayList<String>();
        List<List<String>> result=new ArrayList<>();
        // Select All Query
        //
        String rawQuery = "SELECT * FROM " + TABLE_MESSEGES + " WHERE " +  SENDER_ID +" like " + friendID  +
                " OR " + RECEIVER_ID +" like " + friendID+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                labels = new ArrayList<String>();
                labels.add(cursor.getString(1));
                labels.add(cursor.getString(2));
                labels.add(cursor.getString(3));
                result.add(labels);
            }
            while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning labels
      //  Log.d("LABELS:SENDERS :",labels.toString());

    //    return labels;
        return result;
    }

}