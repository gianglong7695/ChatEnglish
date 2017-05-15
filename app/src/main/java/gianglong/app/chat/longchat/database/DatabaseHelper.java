package gianglong.app.chat.longchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;

import gianglong.app.chat.longchat.entity.KeyValueEntity;
import gianglong.app.chat.longchat.entity.UserEntity;

import static android.content.ContentValues.TAG;

/**
 * Created by VCCORP on 5/12/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    // Database Info
    public static final String DATABASE_NAME = "longchat";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USER = "tbl_user";
    public static final String TABLE_ROOM = "tbl_room";


    // User Table Columns
    public static final String KEY_USER_ID = "id";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_COUNTRY = "age";
    public static final String KEY_USER_AVATAR = "avatar";
    public static final String KEY_USER_INTRODUCE = "introduce";
    public static final String KEY_USER_RATE = "rate";
    public static final String KEY_USER_REVIEWER = "reviewer";

    // Room Table Columns
    public static final String KEY_ROOM_ID = "id";
    public static final String KEY_ROOM_VALUE = "value";


    private static DatabaseHelper instance;


    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "(" +
                KEY_USER_ID + " TEXT PRIMARY KEY," + // Define a primary key
                KEY_USER_NAME + " TEXT," +
                KEY_USER_EMAIL + "TEXT," +
                KEY_USER_PASSWORD + " TEXT," +
                KEY_USER_GENDER + " TEXT," +
                KEY_USER_COUNTRY + " TEXT," +
                KEY_USER_AVATAR + " TEXT," +
                KEY_USER_INTRODUCE + " TEXT," +
                KEY_USER_RATE + " TEXT," +
                KEY_USER_REVIEWER + " TEXT" +
                ")";


        String CREATE_TABLE_ROOM = "CREATE TABLE " + TABLE_ROOM + "(" +
                KEY_ROOM_ID + " TEXT PRIMARY KEY," + // Define a primary key
                KEY_ROOM_VALUE + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ROOM);
    }


    public boolean isCheckExist(String tableName, String columnName, String columnValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery("SELECT " + columnName + " FROM " + tableName + " WHERE " + columnName + "='" + columnValue + "'", null);
            if (cursor.moveToFirst()) {
                db.close();
                return true;//record Exists

            }
            db.close();
        } catch (Exception errorException) {
            Log.e("Exception occured", "Exception occured " + errorException);
            db.close();
        }
        return false;
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);
        }
    }


    /* -----------------USER----------------------- */

    public long addOrUpdateUser(UserEntity user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, user.getId());
            values.put(KEY_USER_NAME, user.getName());
            values.put(KEY_USER_EMAIL, user.getEmail());
            values.put(KEY_USER_PASSWORD, user.getPassword());
            values.put(KEY_USER_GENDER, user.getGender());
            values.put(KEY_USER_COUNTRY, user.getCountry());
            values.put(KEY_USER_AVATAR, user.getAvatar());
            values.put(KEY_USER_INTRODUCE, user.getIntrodution());
            values.put(KEY_USER_RATE, user.getRate());
            values.put(KEY_USER_REVIEWER, user.getReviewers());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_USER, values, KEY_USER_ID + "= ?", new String[]{user.getId()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, TABLE_USER, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.getId())});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(TABLE_USER, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }

    // Get all posts in the database
    public ArrayList<UserEntity> getAllUser() {
        ArrayList<UserEntity> alUser = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String USER_SELECT_QUERY ="SELECT * FROM " + TABLE_USER;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    UserEntity user = new UserEntity();
                    user.setId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                    user.setEmail(cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)));
                    user.setPassword(cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)));
                    user.setGender(cursor.getString(cursor.getColumnIndex(KEY_USER_GENDER)));
                    user.setCountry(cursor.getString(cursor.getColumnIndex(KEY_USER_COUNTRY)));
                    user.setAvatar(cursor.getString(cursor.getColumnIndex(KEY_USER_AVATAR)));
                    user.setIntrodution(cursor.getString(cursor.getColumnIndex(KEY_USER_INTRODUCE)));
                    user.setRate(Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_USER_RATE))));
                    user.setReviewers(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_USER_REVIEWER))));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return alUser;
    }

    public int updateUserField(UserEntity user, String columName, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columName, value);

        // Updating profile picture url for user with that userName
        return db.update(TABLE_USER, values, KEY_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Delete all posts and users in the database
    public void deleteTableUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_USER, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }


    /* -------ROOM------- */
    public long addOrUpdateRoom(KeyValueEntity room) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long roomId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, room.getKey());
            values.put(KEY_USER_NAME, room.getValue());

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_ROOM, values, KEY_ROOM_ID + "= ?", new String[]{room.getKey()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ROOM_ID, TABLE_ROOM, KEY_ROOM_ID);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(room.getKey())});
                try {
                    if (cursor.moveToFirst()) {
                        roomId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                roomId = db.insertOrThrow(TABLE_ROOM, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return roomId;
    }



    // Get all posts in the database
    public ArrayList<KeyValueEntity> getAllRoom() {
        ArrayList<KeyValueEntity> alUser = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String USER_SELECT_QUERY ="SELECT * FROM " + TABLE_ROOM;

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    UserEntity user = new UserEntity();
                    user.setId(cursor.getString(cursor.getColumnIndex(KEY_ROOM_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(KEY_ROOM_VALUE)));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return alUser;
    }
}
