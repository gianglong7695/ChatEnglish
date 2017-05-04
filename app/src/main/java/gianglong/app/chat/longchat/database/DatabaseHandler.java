package gianglong.app.chat.longchat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;

/**
 * Created by Giang Long on 2/22/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper{


    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_PROFILE_TABLE = "CREATE TABLE " + Constants.TABLE_USER_PROFILE + "("
                + Constants.KEY_UID + " TEXT PRIMARY KEY,"
                + Constants.KEY_NAME + " TEXT,"
                + Constants.KEY_EMAIL + " TEXT"
                + Constants.KEY_PASSWORD + " TEXT,"
                + Constants.KEY_GENDER + " TEXT,"
                + Constants.KEY_DATEOFBIRTH + " TEXT,"
                + Constants.KEY_COUNTRY + " TEXT,"
                + Constants.KEY_AVATAR + " TEXT,"
                + Constants.KEY_INTRODUCE + " TEXT,"
                + Constants.KEY_RATE + " TEXT,"
                + Constants.KEY_REVIEWER + " TEXT,"
                + ")";
        db.execSQL(CREATE_USER_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_USER_PROFILE);

        // Create tables again
        onCreate(db);
    }

    // Add, update, delete methods


    public void addUser(UserEntity user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_UID, user.getId());
        values.put(Constants.KEY_NAME, user.getName());
        values.put(Constants.KEY_EMAIL, user.getEmail());
        values.put(Constants.KEY_PASSWORD, user.getPassword());
        values.put(Constants.KEY_GENDER, user.getGender());
        values.put(Constants.KEY_DATEOFBIRTH, user.getDateOfBirth());
        values.put(Constants.KEY_COUNTRY, user.getCountry());
        values.put(Constants.KEY_AVATAR, user.getAvatar());
        values.put(Constants.KEY_INTRODUCE, user.getIntrodution());
        values.put(Constants.KEY_RATE, user.getRate());
        values.put(Constants.KEY_REVIEWER, user.getReviewers());


        // Inserting Row
        db.insert(Constants.TABLE_USER_PROFILE, null, values);
        db.close(); // Closing database connection
    }


    // Getting single user
    public UserEntity getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(Constants.TABLE_USER_PROFILE, new String[] {
                        Constants.KEY_UID,
                        Constants.KEY_NAME,
                        Constants.KEY_EMAIL,
                        Constants.KEY_PASSWORD,
                        Constants.KEY_GENDER,
                        Constants.KEY_DATEOFBIRTH,
                        Constants.KEY_COUNTRY,
                        Constants.KEY_AVATAR,
                        Constants.KEY_INTRODUCE,
                        Constants.KEY_RATE,
                        Constants.KEY_REVIEWER}, Constants.KEY_UID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (c != null)
            c.moveToFirst();

        UserEntity user = new UserEntity(
                c.getString(0),
                c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4),
                c.getString(5),
                c.getString(6),
                c.getString(7),
                c.getString(8),
                0,
                0);
        // return user
        return user;
    }
}
