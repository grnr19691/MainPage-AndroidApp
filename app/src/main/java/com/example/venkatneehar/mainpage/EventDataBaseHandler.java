package com.example.venkatneehar.mainpage;

/**
 * Created by RajaNageswaraRao on 5/19/2015.
 */
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context; import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
class popDetail
{
    String description;
    String name;

    public popDetail(String description,String name)
    {
        this.name=name;
        this.description=description;
    }
    public String getDescription()
    {
        return description;
    }
    public String getName()
    {
        return name;
    }
}
public class EventDataBaseHandler extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "spinnerExample";
    // Labels table name
    private static final String TABLE_LABELS = "label";
    // Labels Table Columns names
    private static final String EVENT_ID = "eventId";
    private static final String FRIEND_ID = "friendID";
    private static final String RECEIVER_ID = "receiverID";
    private static final String TEXT = "message";
    public EventDataBaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                TABLE_LABELS + "("
                + EVENT_ID + " INTEGER PRIMARY KEY," +
                FRIEND_ID + " TEXT," +
                RECEIVER_ID + " TEXT," +
                TEXT + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
        // Create tables again
        onCreate(db);
    }
    /**
     * Inserting new label into labels table
     * */
    public void insertLabel(String senderID,String receiverID,String label)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FRIEND_ID, senderID);
        values.put(RECEIVER_ID, receiverID);
        values.put(TEXT, label);
        // Inserting Row
        db.insert(TABLE_LABELS, null, values);
        db.close(); // Closing database connection
    }
    /**
     * Getting all labels
     * returns list of labels
     * */
    public List<String> getAllLabels(String ownerID,String friendID)
    {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //
        String rawQuery = "SELECT * FROM " + TABLE_LABELS + " WHERE (" +  FRIEND_ID +" = ? AND " + RECEIVER_ID +" = ?) OR (" + RECEIVER_ID +" = ? AND " + FRIEND_ID +" = ?);";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, new String[] {ownerID,friendID,ownerID,friendID});
        // looping through all rows and adding to list
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
        return labels;
    }
    public List<String> getAllSenders(String ownerID,String friendID)
    {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //
        String rawQuery = "SELECT * FROM " + TABLE_LABELS + " WHERE (" +  FRIEND_ID +" = ? AND " + RECEIVER_ID +" = ?) OR (" + RECEIVER_ID +" = ? AND " + FRIEND_ID +" = ?);";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(rawQuery, new String[] {ownerID,friendID,ownerID,friendID});
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                labels.add(cursor.getString(1));
            }
            while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning labels
        return labels;
    }
    public void drop()
    {

    }
}