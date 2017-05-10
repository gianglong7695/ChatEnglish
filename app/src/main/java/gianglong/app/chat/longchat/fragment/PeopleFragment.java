package gianglong.app.chat.longchat.fragment;


import android.content.Intent;
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
import gianglong.app.chat.longchat.activity.TakeInfoDetailActivity;
import gianglong.app.chat.longchat.adapter.ListPeopleAdapter;
import gianglong.app.chat.longchat.entity.GlobalVars;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.UserService;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.ProgressWheel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {
    String TAG = getClass().getSimpleName();
    View v;
    RecyclerView rvListPeople;
    ArrayList<UserEntity> alPeople;
    ListPeopleAdapter peopleAdapter;
    ProgressWheel progressWheel;

    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_people, container, false);
        initUI();
        initConfig();

        getPeople();

        return v;
    }


    public void initUI(){
        rvListPeople = (RecyclerView) v.findViewById(R.id.rvListPeople);
        progressWheel = (ProgressWheel) v.findViewById(R.id.progressWheel);
    }


    public void getPeople(){
        progressWheel.setVisibility(View.VISIBLE);
        rvListPeople.setVisibility(View.GONE);

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressWheel.setVisibility(View.GONE);
                rvListPeople.setVisibility(View.VISIBLE);

                if (msg.what == DataNotify.DATA_SUCCESS) {

                    alPeople = (ArrayList<UserEntity>) msg.obj;
                    peopleAdapter = new ListPeopleAdapter(getActivity(), alPeople);
                    rvListPeople.setAdapter(peopleAdapter);


                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Log.e(TAG, "getUserInfo error!");
                }
            }
        };

        new UserService(getActivity()).getAllUser(handler);
    }


    public void initConfig(){
        rvListPeople.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

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

}
