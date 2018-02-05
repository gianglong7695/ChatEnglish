package gianglong.app.chat.longchat.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import gianglong.app.chat.longchat.entity.MessageEvent;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.Logs;

import static gianglong.app.chat.longchat.utils.Constants.NODE_BOX;
import static gianglong.app.chat.longchat.utils.Constants.NODE_MASTER;
import static gianglong.app.chat.longchat.utils.Constants.NODE_MESSAGE;
import static gianglong.app.chat.longchat.utils.Constants.NODE_USER;

/**
 * Created by VCCORP on 2/2/2018.
 */

public class FirebaseService extends Service{
    private DatabaseReference database;
    private DatabaseReference databaseUser;
    private List<UserEntity> listUser;
    public UserEntity basicUser;
    private SharedPreferences pref;



    private static FirebaseService instance;


    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }




    @Override
    public void onCreate() {
        super.onCreate();


        database = FirebaseDatabase.getInstance().getReference().child(NODE_MASTER).child(NODE_MESSAGE).child(NODE_BOX);
        databaseUser = FirebaseDatabase.getInstance().getReference().child(NODE_MASTER).child(NODE_USER);

        listUser = new ArrayList<>();
        pref = getSharedPreferences(Constants.KEY_USER, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(Constants.KEY_USER_ENTITY, DataNotify.NO_DATA);
        basicUser = gson.fromJson(json, UserEntity.class);
    }


    public DatabaseReference getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseReference database) {
        this.database = database;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                Toast.makeText(getApplicationContext(), "You have new message", Toast.LENGTH_SHORT).show();
//
//
////                Logs.e(dataSnapshot.getValue().toString() + "");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Logs.e(databaseError.getMessage().toString());
//            }
//        });




        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Logs.e("onChildAdded " + dataSnapshot.toString() + " - " + s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Logs.e("onChildChanged " + dataSnapshot.toString() + " - " + s);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Logs.e("onChildRemoved " + dataSnapshot.toString());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Logs.e("onChildMoved " + dataSnapshot.toString() + " - " + s);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logs.e("onChildAdded " + databaseError.toString());
            }
        });



        databaseUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                if(!entity.getId().equals(basicUser.getId())){
                    EventBus.getDefault().post(new MessageEvent("add_list_people", entity));
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                if(!entity.getId().equals(basicUser.getId())){
                    EventBus.getDefault().post(new MessageEvent("update_list_people", entity));
                }else{
                    EventBus.getDefault().post(new MessageEvent("update_profile", entity));
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                EventBus.getDefault().post(new MessageEvent("remove_list_people", entity));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
