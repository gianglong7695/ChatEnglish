package gianglong.app.chat.longchat.utils;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gianglong.app.chat.longchat.R;

/**
 * Created by VCCORP on 8/9/2017.
 */

public class SweetDialog {
    private Context mContext;
    private SweetAlertDialog mSweetAlertDialog;

    public SweetDialog(Context mContext) {
        this.mContext = mContext;
    }


    public void showError(String title, String msg) {
        mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);
        mSweetAlertDialog.setTitleText(title);
        mSweetAlertDialog.setContentText(msg);
        mSweetAlertDialog.setCancelable(false);
        mSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        });
        show();
    }

    public void showProgress(String msg){
        mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        mSweetAlertDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.purple));
        mSweetAlertDialog.setTitleText(msg);
        mSweetAlertDialog.setCancelable(false);
        show();
    }


    public void show() {
        if(mSweetAlertDialog != null){
            mSweetAlertDialog.show();
        }
    }

    public void dismiss() {
        if(mSweetAlertDialog != null){
            mSweetAlertDialog.dismiss();
        }
    }

}
