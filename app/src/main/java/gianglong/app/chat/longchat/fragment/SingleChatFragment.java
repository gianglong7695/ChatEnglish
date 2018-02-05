package gianglong.app.chat.longchat.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.custom.ProgressWheel;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.FirebaseService;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.MyUtils;
import gianglong.app.chat.longchat.utils.PreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SingleChatFragment extends Fragment implements View.OnClickListener{
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



    private DatabaseReference databaseReference;
    private String room_id;
    private PreferenceUtil preferenceUtil;


    @SuppressLint("ValidFragment")
    public SingleChatFragment(UserEntity id_guest) {
        this.user_guest = id_guest;
    }

    private UserEntity user_guest;
    private UserEntity userEntity;

    private View view;


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
        preferenceUtil = new PreferenceUtil(getActivity());
        databaseReference = FirebaseService.getInstance().getDatabase();
        progressWheel.setDefaultStyle();

        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);


        ivMore.setImageResource(R.drawable.ic_more_white);
        ivMore.setVisibility(View.VISIBLE);

        tvTitle.setText(user_guest.getName());


        userEntity = ((MainActivity)getActivity()).getUserEntity();
        room_id = user_guest.getId() + "-" + userEntity.getId();
        databaseReference.child(Constants.NODE_ROOM_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_id)) {
                    Toast.makeText(getActivity(), "Ahii", Toast.LENGTH_SHORT).show();
                }else{
                    databaseReference.child(room_id).setValue("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//        database.child(Constants.NODE_MASTER).child(Constants.NODE_MESSAGE).child(Constants.NODE_BOX).child(roomID).setValue(1);
//        if(getIntent().getExtras() != null){
//            entity = (UserEntity) getIntent().getSerializableExtra(Constants.KEY_USER);
//            setTitle(entity.getName());
//        }else{
//            setTitle(getResources().getString(R.string.title_room_default));
//        }
        rvMessage.setLayoutManager(MyUtils.getLinearLayoutManager(getContext(), 1));



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;
            case R.id.ivMore:
                Toast.makeText(getActivity(), "Setting", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
