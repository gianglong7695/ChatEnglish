package gianglong.app.chat.longchat.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.adapter.MessageAdapter;
import gianglong.app.chat.longchat.custom.ProgressWheel;
import gianglong.app.chat.longchat.entity.MessageItemEntity;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.MessageService;
import gianglong.app.chat.longchat.service.listener.ICallBack;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.utils.MyUtils;

import static gianglong.app.chat.longchat.utils.Constants.NODE_MASTER;
import static gianglong.app.chat.longchat.utils.Constants.NODE_MESSAGE;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SingleChatFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.btSend)
    ImageButton btSend;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.activity_chat)
    View activityRootView;
    @BindView(R.id.progressWheel)
    ProgressWheel progressWheel;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.iv_emoji)
    ImageView iv_emoji;


    private DatabaseReference databaseReference;
    private String room_id;
    private ArrayList<MessageItemEntity> alMsg = new ArrayList<>();
    private MessageAdapter msgAdapter;
    private ICallBack iCallBack;
    private UserEntity user_guest;
    private UserEntity userEntity;
    private boolean isSend = false;
    private View view;
    private boolean isListener;


    @SuppressLint("ValidFragment")
    public SingleChatFragment(UserEntity id_guest) {
        this.user_guest = id_guest;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        if (getContext() instanceof ICallBack) iCallBack = (ICallBack) getContext();


        initConfig();
        event();

        return view;
    }


    public void initConfig() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(NODE_MASTER).child(NODE_MESSAGE);
        progressWheel.setDefaultStyle();

        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        btSend.setOnClickListener(this);


        ivMore.setImageResource(R.drawable.ic_more_white);
        ivMore.setVisibility(View.VISIBLE);

        tvTitle.setText(user_guest.getName());


        userEntity = ((MainActivity) getActivity()).getUserEntity();
        databaseReference.child(Constants.NODE_ROOM_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_guest.getId() + "-" + userEntity.getId())) {
                    room_id = user_guest.getId() + "-" + userEntity.getId();
                    Logs.i("Join to room : " + room_id);
                } else if (dataSnapshot.hasChild(userEntity.getId() + "-" + user_guest.getId())) {
                    room_id = userEntity.getId() + "-" + user_guest.getId();
                    Logs.i("Join to room : " + room_id);
                } else {
                    room_id = userEntity.getId() + "-" + user_guest.getId();
                    databaseReference.child(Constants.NODE_ROOM_ID).child(room_id).setValue("");
                    Logs.i("Create succcess : " + room_id);
                }


                listener(room_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logs.e(databaseError.getMessage().toString());
            }
        });

        rvMessage.setLayoutManager(MyUtils.getLinearLayoutManager(getContext(), 1));
        msgAdapter = new MessageAdapter(getActivity(), alMsg);
        msgAdapter.setUserEntity(userEntity);
        rvMessage.setAdapter(msgAdapter);


    }


    public void listener(String room_id) {
        databaseReference.child(Constants.NODE_ROOM_ID).child(room_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isListener) {

                    MessageItemEntity entity = dataSnapshot.getValue(MessageItemEntity.class);
                    if (entity != null) {
                        alMsg.add(entity);
//                            if(databaseHandler.addMessage(entity) != -1){
//                                Logs.e("Save msg to database successed!");
//                            }else{
//                                Logs.e("Can't saving msg");
//                            }

                        msgAdapter.notifyItemChanged(alMsg.size() - 1);
                        rvMessage.scrollToPosition(alMsg.size() - 1);


                        if (alMsg.size() > 1) {
//                            if(entity.getSenderID() == alMsg.get(alMsg.size() - 2).getSenderID()){
//                                alMsg.get(alMsg.size() - 2).setHideTime(true);
//                                alMsg.get(alMsg.size() - 2).setHideAvatar(true);
//                                msgAdapter.notifyItemChanged(alMsg.size() - 2);
//                            }


                            if (entity.getSenderID().equals(user_guest.getId())) {
                                if (entity.getSenderID().equals(alMsg.get(alMsg.size() - 2).getSenderID())) {
                                    alMsg.get(alMsg.size() - 2).setHideTime(true);
                                    alMsg.get(alMsg.size() - 2).setHideAvatar(true);
                                    msgAdapter.notifyItemChanged(alMsg.size() - 2);
                                }
                            }
                        }

                    }


                } else {
                    isListener = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void event() {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etMessage.getText().length() > 0) {
                    setActiveButtonSend(true);
                } else {
                    setActiveButtonSend(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void setActiveButtonSend(boolean isActive) {
        if (isActive) {
            if (!isSend) {
                btSend.setImageResource(R.drawable.ic_send_activate);
                isSend = true;
            }
        } else {
            if (isSend) {
                btSend.setImageResource(R.drawable.ic_send_deactivate);
                isSend = false;
            }
        }
    }


    public void sendEvent() {
        if (isSend) {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("hh:mm");
            String hour = df.format(date);

            MessageItemEntity messageItem = new MessageItemEntity();
            messageItem.setMessage(etMessage.getText().toString());
            messageItem.setTime(hour);
            messageItem.setStatusType(0);
            messageItem.setHideAvatar(true);
            messageItem.setHideTime(true);
            messageItem.setSenderID(userEntity.getId());
            messageItem.setReceiverID(user_guest.getId());

//            if(databaseHandler.addMessage(messageItem) != -1){
//                Logs.e("Saved ! " +  messageItem.toString());
//
//            }else{
//                Logs.e("Can't saving msg");
//            }

            if (room_id != null) {
                pushMessage(messageItem);
            }
            etMessage.setText("");
        }

    }


    public void pushMessage(final MessageItemEntity messageItem) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DataNotify.DATA_SUCCESS) {
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Logs.e("getUserInfo error!");
                }
            }
        };
        if (room_id != null) {
            new MessageService(getActivity()).pushMessage(handler, messageItem, room_id);
        } else {
            Toast.makeText(getActivity(), "RoomID = null", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;
            case R.id.ivMore:
                iCallBack.onAddFragment(new OptionsFragment(), 1);
                break;
            case R.id.iv_emoji:
                Toast.makeText(getActivity(), "Emoji click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btSend:
                sendEvent();
                break;

        }
    }

}
