package gianglong.app.chat.longchat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.adapter.ListPeopleAdapter;
import gianglong.app.chat.longchat.adapter.MessageAdapter;
import gianglong.app.chat.longchat.entity.BasicUserInfoEntity;
import gianglong.app.chat.longchat.entity.MessageItemEntity;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.MessageService;
import gianglong.app.chat.longchat.service.UserService;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.Utils;

public class ChatActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    RecyclerView rvMessage;
    ImageButton btSend;
    boolean isSend = false;
    EditText etMessage;
    ArrayList<MessageItemEntity> alMsg;
    MessageAdapter msgAdapter;
    Random rd = new Random();
    LinearLayoutManager linearLayoutManager;
    View activityRootView;
    boolean isScroll = false;
    UserEntity entity;

    DatabaseReference database;
    DatabaseReference ref;
    String roomID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();
        initConfig();
        event();
        initialData();

        getRoomID();
    }


    public void initUI(){
        rvMessage = (RecyclerView) findViewById(R.id.rvMessage);
        btSend = (ImageButton) findViewById(R.id.btSend);
        etMessage = (EditText) findViewById(R.id.etMessage);
        activityRootView = findViewById(R.id.activity_chat);
    }


    public void initConfig(){
        database = FirebaseDatabase.getInstance().getReference();

        if(getIntent().getExtras() != null){
            entity = (UserEntity) getIntent().getSerializableExtra(Constants.KEY_USER);
            setTitle(entity.getName());

        }else{
            setTitle("Not yet");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvMessage.setLayoutManager(linearLayoutManager);
    }


    public void event(){
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etMessage.getText().length() > 0){
                    setActiveButtonSend(1);
                }else{
                    setActiveButtonSend(0);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSend){
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("hh:mm");
                    String hour = df.format(date);
                    int type = rd.nextInt(2);

                    MessageItemEntity messageItem = new MessageItemEntity(String.valueOf(type), etMessage.getText().toString(), hour, 0 , false , false, BasicUserInfoEntity.getInstance().getUid());
                    alMsg.add(messageItem);

                    if(roomID == null){
                        createRelationship(messageItem, entity.getId());
                    }else{
                        pushMessage(messageItem, alMsg.size() - 1);
                    }





                    if(alMsg.size() > 1){
                        if(messageItem.getSenderID() == alMsg.get(alMsg.size() - 2).getSenderID()){
                            alMsg.get(alMsg.size() - 2).setHideTime(true);

                            if(messageItem.getSenderID() != alMsg.get(alMsg.size() - 2).getSenderID()){
                                alMsg.get(alMsg.size() - 2).setHideAvatar(true);
                            }
                            msgAdapter.notifyItemChanged(alMsg.size() - 2);
                        }
                    }







                    msgAdapter.notifyItemChanged(alMsg.size() - 1);
                    rvMessage.scrollToPosition(alMsg.size() - 1);
                    etMessage.setText("");
                }
            }
        });



        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > Utils.dpToPx(getApplicationContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
                    if(!isScroll){
                        rvMessage.scrollToPosition(alMsg.size() - 1);
                        isScroll = true;
                    }else{
                        isScroll = false;
                    }

                }
            }
        });

    }




    public void setActiveButtonSend(int type){
        if(type == 0){
            if(isSend){
                btSend.setImageResource(R.drawable.ic_send_deactivate);
                isSend = false;
            }
        }else{
            if(!isSend){
                btSend.setImageResource(R.drawable.ic_send_activate);
                isSend = true;
            }
        }
    }


    public void initialData(){
        alMsg = new ArrayList<>();
        msgAdapter = new MessageAdapter(getApplicationContext(), alMsg);
        rvMessage.setAdapter(msgAdapter);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void pushMessage(final MessageItemEntity messageItem, final int position){
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                if (msg.what == DataNotify.DATA_SUCCESS) {
//                    messageItem.setStatusType(DataNotify.DATA_SUCCESS);
//                    msgAdapter.notifyItemChanged(position);
                    Toast.makeText(ChatActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Log.e(TAG, "getUserInfo error!");
//                    messageItem.setStatusType(DataNotify.DATA_UNSUCCESS);
//                    msgAdapter.notifyItemChanged(position);
                }
            }
        };
        if(roomID != null){
            new MessageService(this).pushMessage(handler, messageItem, roomID);
        }else{
            Toast.makeText(this, "RoomID = null", Toast.LENGTH_SHORT).show();
        }

    }


    public void createRelationship(final MessageItemEntity messageItem, String guestID){
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                if (msg.what == DataNotify.DATA_SUCCESS) {
                    roomID = (String) msg.obj;

                    pushMessage(messageItem, alMsg.size());
                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Log.e(TAG, "getUserInfo error!");
                }
            }
        };

        new MessageService(this).createRelationship(handler, guestID);
    }


    public void getRoomID(){
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                if (msg.what == DataNotify.DATA_SUCCESS) {
                    roomID = (String) msg.obj;

                    ref = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_BOX).child(roomID);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            MessageItemEntity entity = dataSnapshot.getValue(MessageItemEntity.class);
                            if(entity != null){
                                if(!entity.getSenderID().equals(BasicUserInfoEntity.getInstance().getUid())){
                                    alMsg.add(entity);

                                    msgAdapter.notifyItemChanged(alMsg.size() - 1);
                                    rvMessage.scrollToPosition(alMsg.size() - 1);


                                    if(alMsg.size() > 1){
                                        if(entity.getSenderID() == alMsg.get(alMsg.size() - 2).getSenderID()){
                                            alMsg.get(alMsg.size() - 2).setHideTime(true);
                                            msgAdapter.notifyItemChanged(alMsg.size() - 2);
                                        }
                                    }
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Log.e(TAG, "getUserInfo error!");
                }
            }
        };

        new MessageService(this).getRoomID(handler, entity.getId());
    }

}

