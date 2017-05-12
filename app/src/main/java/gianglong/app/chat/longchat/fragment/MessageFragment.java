package gianglong.app.chat.longchat.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.adapter.ListMessageAdapter;
import gianglong.app.chat.longchat.entity.MessageEntity;
import gianglong.app.chat.longchat.service.MessageService;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.ProgressWheel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    String TAG = getClass().getSimpleName();
    View v;
    RecyclerView rvListMessage;
    ArrayList<MessageEntity> alListMessage;
    ListMessageAdapter messageAdapter;
    ProgressWheel progressWheel;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_message, container, false);
        initUI();
        initConfig();

        alListMessage = new ArrayList<>();
        alListMessage.add(new MessageEntity("0", "Giang Long", "12:34", "Hello. We can talk together ???", true, 1, true));
        alListMessage.add(new MessageEntity("1", "Giang Long", "12:34", "Hello. We can talk together ???", false, 0, true));
        alListMessage.add(new MessageEntity("0", "Giang Long", "12:34", "Hello. We can talk together ???", true, 1, false));
        alListMessage.add(new MessageEntity("1", "Giang Long", "12:34", "Hello. We can talk together ???", false, 0, true));
        alListMessage.add(new MessageEntity("1", "Giang Long", "12:34", "Hello. We can talk together ???", false, 1, false));
        alListMessage.add(new MessageEntity("0", "Giang Long", "12:34", "Hello. We can talk together ???", true, 1, true));


        messageAdapter = new ListMessageAdapter(getActivity(), alListMessage);
        rvListMessage.setAdapter(messageAdapter);


        getMessageHistory();

        return v;
    }


    public void initUI(){
        rvListMessage = (RecyclerView) v.findViewById(R.id.rvListMessage);
        progressWheel = (ProgressWheel) v.findViewById(R.id.progressWheel);
    }


    public void initConfig(){
        rvListMessage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        // progress wheel config
        progressWheel.setProgress(0.5f);
        progressWheel.setBarWidth(7);
        progressWheel.setBarColor(getResources().getColor(R.color.purple));
        progressWheel.setCallback(new ProgressWheel.ProgressCallback() {
            @Override
            public void onProgressUpdate(float progress) {
                if (progress == 0) {
                    progressWheel.setProgress(1.0f);
                } else if (progress == 1.0f) {
                    progressWheel.setProgress(0.0f);
                }
            }
        });
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


                    Log.e(TAG, msg.obj + "");


                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Log.e(TAG, "getUserInfo error!");
                }
            }
        };

        new MessageService(getActivity()).getMessageHistory(handler);
    }

}
