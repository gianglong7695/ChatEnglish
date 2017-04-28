package gianglong.app.chat.longchat.fragment;


import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    String TAG = getClass().getSimpleName();
    View v;
    RecyclerView rvListMessage;
    ArrayList<MessageEntity> alListMessage;
    ListMessageAdapter messageAdapter;

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
        return v;
    }


    public void initUI(){
        rvListMessage = (RecyclerView) v.findViewById(R.id.rvListMessage);
    }


    public void initConfig(){
        rvListMessage.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

}
