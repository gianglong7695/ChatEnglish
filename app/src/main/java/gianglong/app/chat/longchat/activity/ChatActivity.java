package gianglong.app.chat.longchat.activity;

import android.os.Bundle;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.adapter.MessageAdapter;
import gianglong.app.chat.longchat.entity.MessageItemEntity;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();
        initConfig();
        event();
        initialData();
    }


    public void initUI(){
        rvMessage = (RecyclerView) findViewById(R.id.rvMessage);
        btSend = (ImageButton) findViewById(R.id.btSend);
        etMessage = (EditText) findViewById(R.id.etMessage);
        activityRootView = findViewById(R.id.activity_chat);
    }


    public void initConfig(){
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

                    MessageItemEntity entity = new MessageItemEntity(String.valueOf(type), etMessage.getText().toString(), hour, 1 , type, false);
                    alMsg.add(entity);


                    if(alMsg.size() > 1){
                        if(entity.getTypeView() == alMsg.get(alMsg.size() - 2).getTypeView()){
                            alMsg.get(alMsg.size() - 2).setHideTime(true);
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
}

