package gianglong.app.chat.longchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.LoginActivity;
import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.activity.ProfileActivity;
import gianglong.app.chat.longchat.utils.ProgressWheel;
import gianglong.app.chat.longchat.utils.RippleViewLinear;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    View v;
    RippleViewLinear rippleViewLinear1, rippleViewLinear2, rippleViewLinear3, rippleViewLinear4, layout_signout;
    SweetAlertDialog mSweetAlertDialog;
    ProgressWheel progressWheel;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account, container, false);
        initUI();
        initConfig();
        eventHandle();


        return v;
    }


    public void initUI() {
        rippleViewLinear1 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear1);
        rippleViewLinear2 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear2);
        rippleViewLinear3 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear3);
        rippleViewLinear4 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear4);
        layout_signout = (RippleViewLinear) v.findViewById(R.id.layout_signout);
        progressWheel = (ProgressWheel) v.findViewById(R.id.progressWheel);
    }


    public void initConfig(){
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
