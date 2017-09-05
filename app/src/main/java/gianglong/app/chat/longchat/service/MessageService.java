package gianglong.app.chat.longchat.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import gianglong.app.chat.longchat.entity.BasicUserInfoEntity;
import gianglong.app.chat.longchat.entity.KeyValueEntity;
import gianglong.app.chat.longchat.entity.MessageItemEntity;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;

/**
 * Created by VCCORP on 5/10/2017.
 */

public class MessageService {
    Context context;
    DatabaseReference database;
    DatabaseReference refMine;
    DatabaseReference refYours;

    public MessageService(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference();
    }


    public void pushMessage(final Handler handler, MessageItemEntity messageItem, String roomID) {
        final Message msg = new Message();

        DatabaseReference ref = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_BOX).child(roomID);


        ref.setValue(messageItem, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                msg.what = DataNotify.DATA_SUCCESS;
                msg.obj = true;
                handler.sendMessage(msg);
            }

        });

    }


    public void getRoomID(final Handler handler, final String guestID) {
        final Message msg = new Message();
        DatabaseReference ref1 = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_ROOM);
        final DatabaseReference ref2 = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_ROOM).child(BasicUserInfoEntity.getInstance().getUid());
        final DatabaseReference ref3 = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_ROOM).child(BasicUserInfoEntity.getInstance().getUid()).child(guestID);

        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(BasicUserInfoEntity.getInstance().getUid())) {
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.hasChild(guestID)) {

                                ref3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                        msg.what = DataNotify.DATA_SUCCESS;
                                        msg.obj = dataSnapshot2.getValue().toString();
                                        handler.sendMessage(msg);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        msg.what = DataNotify.DATA_SUCCESS;
                                        msg.obj = null;
                                        handler.sendMessage(msg);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            msg.what = DataNotify.DATA_SUCCESS;
                            msg.obj = null;
                            handler.sendMessage(msg);
                        }
                    });

                } else {
                    msg.what = DataNotify.DATA_SUCCESS;
                    msg.obj = null;
                    handler.sendMessage(msg);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void createRelationship(final Handler handler, final String id) {
        final Message msg = new Message();
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        final String time = df.format(date);

        final DatabaseReference ref = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_ROOM);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(id)) {
                    refMine = ref.child(BasicUserInfoEntity.getInstance().getUid()).child(id);
                    refMine.setValue(time).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            refYours = ref.child(id).child(BasicUserInfoEntity.getInstance().getUid());
                            refYours.setValue(time).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    msg.what = DataNotify.DATA_SUCCESS;
                                    msg.obj = time;
                                    handler.sendMessage(msg);
                                }
                            });


                        }
                    });

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


    public void getMessageHistory(final Handler handler){
        final Message msg = new Message();
        final DatabaseReference ref1 = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_ROOM);
        final ArrayList<KeyValueEntity> listSave = new ArrayList<>();

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(BasicUserInfoEntity.getInstance().getUid())){
                    ref1.child(BasicUserInfoEntity.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                            int length = (int) dataSnapshot.getChildrenCount();
                            int pos = 0;


                            while(pos < length) {
                                DataSnapshot currentPosData = iterator.next();
                                listSave.add(new KeyValueEntity(currentPosData.getKey(), currentPosData.getValue().toString()));
                                pos++;
                            }

                            if(pos == length){
                                msg.what = DataNotify.DATA_SUCCESS;
                                msg.obj = listSave;
                                handler.sendMessage(msg);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
