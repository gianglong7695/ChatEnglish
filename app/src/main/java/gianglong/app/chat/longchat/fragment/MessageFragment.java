package gianglong.app.chat.longchat.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.custom.ProgressWheel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BaseFragment {
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
    protected int getLayoutRes() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);





        initConfig();
        alMsg = (ArrayList<MessageItemEntity>) databaseHandler.getAllLastMsg();

        messageAdapter = new ListMessageAdapter(getActivity(), alMsg);
        rvListMessage.setAdapter(messageAdapter);
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
                    Logs.e(listRoom.size() + "");


                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Logs.e("getUserInfo error!");
                }
            }
        };

        new MessageService(getActivity()).getMessageHistory(handler);
    }

}

