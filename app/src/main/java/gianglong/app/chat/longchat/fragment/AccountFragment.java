package gianglong.app.chat.longchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.ProfileActivity;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.ProgressWheel;
import gianglong.app.chat.longchat.utils.RippleViewLinear;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    View v;
    @BindView(R.id.rippleViewLinear1)
    RippleViewLinear rippleViewLinear1;
    @BindView(R.id.rippleViewLinear2)
    RippleViewLinear rippleViewLinear2;
    @BindView(R.id.rippleViewLinear3)
    RippleViewLinear rippleViewLinear3;
    @BindView(R.id.rippleViewLinear4)
    RippleViewLinear rippleViewLinear4;
    @BindView(R.id.layout_signout)
    RippleViewLinear layout_signout;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvIntroduce)
    TextView tvIntroduce;
    @BindView(R.id.progressWheel)
    ProgressWheel progressWheel;

    private SweetAlertDialog mSweetAlertDialog;
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


    public void eventHandle() {
        rippleViewLinear1.setOnRippleCompleteListener(new RippleViewLinear.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleViewLinear rippleView) {
                Intent it = new Intent(getActivity(), ProfileActivity.class);
                startActivity(it);
            }
        });

        layout_signout.setOnRippleCompleteListener(new RippleViewLinear.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleViewLinear rippleView) {
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
                        EventBus.getDefault().post(new Boolean(false));
                    }
                });
                mSweetAlertDialog.show();
            }
        });

    }

}
