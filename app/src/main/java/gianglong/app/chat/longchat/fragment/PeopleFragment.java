package gianglong.app.chat.longchat.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.adapter.ListPeopleAdapter;
import gianglong.app.chat.longchat.entity.MessageEvent;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.UserService;
import gianglong.app.chat.longchat.service.listener.ICallBack;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.utils.MyUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends BaseFragment {
    @BindView(R.id.rvListPeople)
    RecyclerView rvListPeople;

    private View v;
    private ArrayList<UserEntity> alPeople = new ArrayList<>();
    private ListPeopleAdapter peopleAdapter;
    private ICallBack iCallBack;

    public PeopleFragment() {
        // Required empty public constructor
        if (getContext() instanceof ICallBack) iCallBack = (ICallBack) getContext();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_people;
    }

    @Override
    protected void initView(View view) {
        v = view;
        ButterKnife.bind(this, v);


        initUI();
//        getPeople();
    }


    public void initUI() {
        rvListPeople.setLayoutManager(MyUtils.getLinearLayoutManager(getContext(), 1));
        rvListPeople.setVisibility(View.VISIBLE);

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(MessageEvent messageEvent) {
        if(messageEvent.getMessage().equals("add_list_people")){
            alPeople.add(messageEvent.getUserEntity());
            if (peopleAdapter == null) {
                peopleAdapter = new ListPeopleAdapter(getActivity(), alPeople);
                rvListPeople.setAdapter(peopleAdapter);
            } else {
                peopleAdapter.notifyItemChanged(alPeople.size() - 1);
            }
        }else if(messageEvent.getMessage().equals("update_list_people")){
            for (int i = 0; i < alPeople.size(); i++){
                if(alPeople.get(i).getId().equals(messageEvent.getUserEntity().getId())){
                    alPeople.set(i, messageEvent.getUserEntity());
                    peopleAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }else if(messageEvent.getMessage().equals("remove_list_people")){
            for (int i = 0; i < alPeople.size(); i++){
                if(alPeople.get(i).getId().equals(messageEvent.getUserEntity().getId())) {
                    alPeople.remove(i);
                    peopleAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }


    public void getPeople() {

        rvListPeople.setVisibility(View.GONE);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                rvListPeople.setVisibility(View.VISIBLE);

                if (msg.what == DataNotify.DATA_SUCCESS) {

                    alPeople = (ArrayList<UserEntity>) msg.obj;
                    peopleAdapter = new ListPeopleAdapter(getActivity(), alPeople);
                    rvListPeople.setAdapter(peopleAdapter);


                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Logs.e("getUserInfo error!");
                }
            }
        };

        new UserService(getActivity()).getAllUser(handler);
    }


}
