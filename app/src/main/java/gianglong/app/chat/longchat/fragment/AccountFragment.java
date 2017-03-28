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
import gianglong.app.chat.longchat.utils.RippleViewLinear;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    View v;
    RippleViewLinear rippleViewLinear1, rippleViewLinear2, rippleViewLinear3, rippleViewLinear4;
    Button btSignOut;
    SweetAlertDialog mSweetAlertDialog;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account, container, false);
        initUI();
        eventHandle();


        return v;
    }


    public void initUI() {
        rippleViewLinear1 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear1);
        rippleViewLinear2 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear2);
        rippleViewLinear3 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear3);
        rippleViewLinear4 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear4);
        btSignOut = (Button) v.findViewById(R.id.btSignOut);
    }


    public void eventHandle() {
        rippleViewLinear1.setOnRippleCompleteListener(new RippleViewLinear.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleViewLinear rippleView) {
                Intent it = new Intent(getActivity(), ProfileActivity.class);
                startActivity(it);
            }
        });


        btSignOut.setOnClickListener(new View.OnClickListener() {
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
