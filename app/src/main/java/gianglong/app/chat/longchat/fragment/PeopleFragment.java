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
import gianglong.app.chat.longchat.adapter.ListPeopleAdapter;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.UserService;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.LogUtil;
import gianglong.app.chat.longchat.utils.ProgressWheel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {
    @BindView(R.id.rvListPeople)
    RecyclerView rvListPeople;
    @BindView(R.id.progressWheel)
    ProgressWheel progressWheel;

    private View v;
    private ArrayList<UserEntity> alPeople;
    private ListPeopleAdapter peopleAdapter;

    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_people, container, false);
        ButterKnife.bind(this, v);
        initUI();

        getPeople();

        return v;
    }


    public void initUI(){
        rvListPeople.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // progress wheel config
        progressWheel.setDefaultStyle();
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
                    LogUtil.e("getUserInfo error!");
                }
            }
        };

        new UserService(getActivity()).getAllUser(handler);
    }



}
