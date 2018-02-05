package gianglong.app.chat.longchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gianglong.app.chat.longchat.utils.Logs;

import static gianglong.app.chat.longchat.utils.Constants.NODE_MASTER;
import static gianglong.app.chat.longchat.utils.Constants.NODE_MESSAGE;

/**
 * Created by VCCORP on 2/2/2018.
 */

public class FirebaseService extends Service{
    private DatabaseReference database;




    @Override
    public void onCreate() {
        super.onCreate();


        database = FirebaseDatabase.getInstance().getReference().child(NODE_MASTER).child(NODE_MESSAGE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(getApplicationContext(), "You have new message", Toast.LENGTH_SHORT).show();


//                Logs.e(dataSnapshot.getValue().toString() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logs.e(databaseError.getMessage().toString());
            }
        });



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








        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
