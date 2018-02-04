package gianglong.app.chat.longchat.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.custom.ProgressWheel;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.FirebaseService;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.MyUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleChatFragment extends Fragment {
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



    private DatabaseReference databaseReference;


    @SuppressLint("ValidFragment")
    public SingleChatFragment(UserEntity id_guest) {
        this.user_guest = id_guest;
    }

    private UserEntity user_guest;

    private View view;

    public SingleChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);



        initConfig();








        return view;
    }



    public void initConfig(){
        progressWheel.setDefaultStyle();
        databaseReference = FirebaseService.getInstance().getDatabase();
//        database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_BOX).child(roomID).setValue(1);
//        if(getIntent().getExtras() != null){
//            entity = (UserEntity) getIntent().getSerializableExtra(Constants.KEY_USER);
//            setTitle(entity.getName());
//        }else{
//            setTitle(getResources().getString(R.string.title_room_default));
//        }
        rvMessage.setLayoutManager(MyUtils.getLinearLayoutManager(getContext(), 1));



    }

}
