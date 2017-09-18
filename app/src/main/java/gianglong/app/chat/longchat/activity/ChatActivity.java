package gianglong.app.chat.longchat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.adapter.MessageAdapter;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.MessageItemEntity;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.MessageService;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.LogUtil;
import gianglong.app.chat.longchat.utils.Utils;

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.btSend)
    ImageButton btSend;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.activity_chat)
    View activityRootView;

    private boolean isScroll = false;
    private UserEntity entity; // Reciever!

    private DatabaseReference database;
    private DatabaseReference ref;
    private String roomID;

    private LinearLayoutManager linearLayoutManager;
    private boolean isSend = false;
    private ArrayList<MessageItemEntity> alMsg;
    private MessageAdapter msgAdapter;
    private Random rd = new Random();
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        databaseHandler = DatabaseHandler.getInstance(this);
        initConfig();
        event();
        initialData();
        getRoomID();
    }



    public void initConfig(){
        database = FirebaseDatabase.getInstance().getReference();
//        database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_BOX).child(roomID).setValue(1);
        if(getIntent().getExtras() != null){
            entity = (UserEntity) getIntent().getSerializableExtra(Constants.KEY_USER);
            setTitle(entity.getName());
        }else{
            setTitle(getResources().getString(R.string.title_room_default));
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
                    setActiveButtonSend(true);
                }else{
                    setActiveButtonSend(false);
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

                    MessageItemEntity messageItem = new MessageItemEntity(etMessage.getText().toString(), hour, 0 , false , false, MainActivity.basicUser.getId(), entity.getId());
                    alMsg.add(messageItem);

                    if(databaseHandler.addMessage(messageItem) != -1){
                        LogUtil.e("Save msg to database successed!");
                    }else{
                        LogUtil.e("Can't saving msg");
                    }

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




    public void setActiveButtonSend(boolean isActive){
        if(isActive){
            if(!isSend){
                btSend.setImageResource(R.drawable.ic_send_activate);
                isSend = true;
            }
        }else{
            if(isSend){
                btSend.setImageResource(R.drawable.ic_send_deactivate);
                isSend = false;
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
                    LogUtil.e("getUserInfo error!");
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
                    LogUtil.e("getUserInfo error!");
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
                    try {
                        ref = database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_BOX).child(roomID);

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                MessageItemEntity entity = dataSnapshot.getValue(MessageItemEntity.class);
                                if(entity != null){
                                    if(!entity.getSenderID().equals(MainActivity.basicUser.getId())){
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
                    }catch (Exception e){
//                        database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_BOX).child(roomID).setValue(1);
                        getRoomID();
                    }



                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    LogUtil.e("getUserInfo error!");
                }
            }
        };

        new MessageService(this).getRoomID(handler, entity.getId());
    }

}

