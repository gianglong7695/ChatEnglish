package gianglong.app.chat.longchat.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gianglong.app.chat.longchat.entity.MessageItemEntity;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;

/**
 * Created by VCCORP on 5/10/2017.
 */

public class MessageService {
    String TAG = getClass().getSimpleName();
    Context context;
    DatabaseReference database;

    public MessageService(Context context) {
        this.context = context;
    }


    public void pushMessage(final Handler handler, MessageItemEntity messageItem, String id){
        final Message msg = new Message();

        database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(UserEntity.getInstance().getId()).child(id);


        ref.setValue(messageItem, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                msg.what = DataNotify.DATA_SUCCESS;
                msg.obj = true;
                handler.sendMessage(msg);
            }

        });

    }
}
