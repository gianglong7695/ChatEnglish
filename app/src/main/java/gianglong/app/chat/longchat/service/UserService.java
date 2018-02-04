package gianglong.app.chat.longchat.service;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.entity.BasicUserInfoEntity;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;

/**
 * Created by VCCORP on 5/8/2017.
 */

public class UserService {
    private Context context;
    private FirebaseUser user;
    private DatabaseReference database;


    public UserService(Context context) {
        this.context = context;
    }

    public void getBasicUserInfo(Handler handler) {
        final Message msg = new Message();

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Id of the provider (ex: google.com)
            String providerId = user.getProviderId();

            // UID specific to the provider
            String uid = user.getUid();

            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            boolean isEmailVerified = user.isEmailVerified();

            BasicUserInfoEntity entity = new BasicUserInfoEntity(providerId, uid, name, email, photoUrl, isEmailVerified);

            // Send ....
            msg.what = DataNotify.DATA_SUCCESS;
            msg.obj = entity;
            handler.sendMessage(msg);

        } else {
            msg.what = DataNotify.DATA_UNSUCCESS;
            msg.obj = null;
            handler.sendMessage(msg);
        }

    }


    public void getUserInfo(final Handler handler, String id) {
        final Message msg = new Message();

        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(Constants.NODE_MASTER).child(Constants.NODE_USER).child(id);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserEntity entity = dataSnapshot.getValue(UserEntity.class);

                if (dataSnapshot.getValue() != null) {
                    msg.what = DataNotify.DATA_SUCCESS;
                    msg.obj = entity;
                    handler.sendMessage(msg);
                } else {
                    msg.what = DataNotify.DATA_SUCCESS_WITH_NO_DATA;
                    msg.obj = null;
                    handler.sendMessage(msg);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                msg.what = DataNotify.DATA_UNSUCCESS;
                msg.obj = null;
                handler.sendMessage(msg);
            }
        });


    }


    public void getAllUser(final Handler handler) {
        final Message msg = new Message();
        final ArrayList<UserEntity> alPeople = new ArrayList<>();

        database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child(Constants.NODE_MASTER).child(Constants.NODE_USER);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot1) {

                ref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                        if(!entity.getId().equals(MainActivity.basicUser.getId())){
                            alPeople.add(entity);
                        }


                        if (alPeople.size() == dataSnapshot1.getChildrenCount() - 1) {
                            msg.what = DataNotify.DATA_SUCCESS;
                            msg.obj = alPeople;
                            handler.sendMessage(msg);
                        }


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        msg.what = DataNotify.DATA_UNSUCCESS;
                        msg.obj = null;
                        handler.sendMessage(msg);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
