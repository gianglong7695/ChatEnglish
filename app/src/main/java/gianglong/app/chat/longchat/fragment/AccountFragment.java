package gianglong.app.chat.longchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.LoginActivity;
import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.activity.ProfileActivity;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.ProgressWheel;
import gianglong.app.chat.longchat.utils.RippleViewLinear;
import gianglong.app.chat.longchat.utils.SweetDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    @BindView(R.id.row_item1)
    LinearLayout row_item1;
    @BindView(R.id.row_item2)
    LinearLayout row_item2;
    @BindView(R.id.row_item3)
    LinearLayout row_item3;
    @BindView(R.id.row_item4)
    LinearLayout row_item4;
    @BindView(R.id.layout_signout)
    LinearLayout layout_signout;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvIntroduce)
    TextView tvIntroduce;
    @BindView(R.id.progressWheel)
    ProgressWheel progressWheel;

    private View v;
    private SweetAlertDialog mSweetAlertDialog;
    private SweetDialog mSweetDialog;
    private UserEntity user;
    private DatabaseHandler databaseHandler;


    public AccountFragment() {
        databaseHandler = DatabaseHandler.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, v);

        initUI();
        eventHandle();


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetUserEntity(UserEntity userEntity) {
        user = userEntity;
        tvName.setText(user.getName());
        tvIntroduce.setText(user.getIntrodution());
    }

    public void initUI() {
        mSweetDialog = new SweetDialog(getActivity());
        // progress wheel config
        progressWheel.setDefaultStyle();
    }


    public void eventHandle() {
        row_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), ProfileActivity.class);
                startActivity(it);
            }
        });

        layout_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                mSweetDialog.showError("Confirm logout", "You will not be able to receive the message. Are you sure ?");

                mSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                mSweetAlertDialog.setTitleText("Confirm logout");
                mSweetAlertDialog.setContentText("You will not be able to receive the message. Are you sure ?");
                mSweetAlertDialog.setCancelText("No");
                mSweetAlertDialog.setConfirmText("Yes");
                mSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        databaseHandler.deleteAllTable();
//                        EventBus.getDefault().postSticky(new Boolean(false));
                        MainActivity.mSessionManager.setLogin(false);
                        Intent it = new Intent(getActivity(), LoginActivity.class);
                        startActivity(it);
                        getActivity().finish();
                    }
                });
                mSweetAlertDialog.show();
            }
        });

    }

}
