package gianglong.app.chat.longchat.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.LoginActivity;
import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.custom.ProgressWheel;
import gianglong.app.chat.longchat.custom.SweetDialog;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.MessageEvent;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.listener.ICallBack;
import gianglong.app.chat.longchat.utils.PreferenceUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends BaseFragment {
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
    private PreferenceUtil preferenceUtil;
    private ICallBack iCallBack;


    public AccountFragment() {
        databaseHandler = DatabaseHandler.getInstance(getContext());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView(View view) {
        v = view;
        ButterKnife.bind(this, v);
        if (getContext() instanceof ICallBack) iCallBack = (ICallBack) getContext();



        initUI();
        eventHandle();
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
    public void onEvent(MessageEvent event) {
        if (event.getMessage().equals("update_profile")) {
            user = event.getUserEntity();
            preferenceUtil.saveUser(user);
            updateUser(user);
        }

    }


    public void updateUser(UserEntity userEntity) {
        tvName.setText(userEntity.getName());
        tvIntroduce.setText(userEntity.getIntrodution());
    }

    public void initUI() {
        mSweetDialog = new SweetDialog(getActivity());
        // progress wheel config
        progressWheel.setDefaultStyle();
        preferenceUtil = new PreferenceUtil(getActivity());
    }


    public void eventHandle() {
        row_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCallBack.onAddFragment(new ProfileFragment(user));
            }
        });

        layout_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
