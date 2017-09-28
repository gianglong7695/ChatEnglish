package gianglong.app.chat.longchat.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.adapter.ListMessageAdapter;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.KeyValueEntity;
import gianglong.app.chat.longchat.entity.MessageEntity;
import gianglong.app.chat.longchat.entity.MessageItemEntity;
import gianglong.app.chat.longchat.service.MessageService;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.LogUtil;
import gianglong.app.chat.longchat.utils.ProgressWheel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    @BindView(R.id.rvListMessage)
    RecyclerView rvListMessage;
    @BindView(R.id.progressWheel)
    ProgressWheel progressWheel;


    private View v;
    private ArrayList<MessageEntity> alListMessage;
    private ListMessageAdapter messageAdapter;
    private ArrayList<KeyValueEntity> listRoom;
    private ArrayList<MessageItemEntity> alMsg;
    private DatabaseHandler databaseHandler;


    public MessageFragment() {
        // Required empty public constructor
        databaseHandler = DatabaseHandler.getInstance(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, v);

        initConfig();
        alMsg = (ArrayList<MessageItemEntity>) databaseHandler.getAllLastMsg();

        messageAdapter = new ListMessageAdapter(getActivity(), alMsg);
        rvListMessage.setAdapter(messageAdapter);


        return v;
    }


    public void initConfig(){
        rvListMessage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        // progress wheel config
        progressWheel.setDefaultStyle();
    }


    public void getMessageHistory(){
        progressWheel.setVisibility(View.VISIBLE);
        rvListMessage.setVisibility(View.GONE);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressWheel.setVisibility(View.GONE);
                rvListMessage.setVisibility(View.VISIBLE);

                if (msg.what == DataNotify.DATA_SUCCESS) {

//                    alListMessage = (ArrayList<MessageEntity>) msg.obj;
//                    messageAdapter = new ListMessageAdapter(getActivity(), alListMessage);
//                    rvListMessage.setAdapter(messageAdapter);

                    listRoom = (ArrayList<KeyValueEntity>) msg.obj;
                    LogUtil.e(listRoom.size() + "");


                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    LogUtil.e("getUserInfo error!");
                }
            }
        };

        new MessageService(getActivity()).getMessageHistory(handler);
    }

}

