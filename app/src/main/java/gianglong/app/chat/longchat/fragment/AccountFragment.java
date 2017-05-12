package gianglong.app.chat.longchat.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.LoginActivity;
import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.activity.ProfileActivity;
import gianglong.app.chat.longchat.activity.TakeInfoDetailActivity;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.BasicUserInfoEntity;
import gianglong.app.chat.longchat.entity.GlobalVars;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.service.UserService;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.ProgressWheel;
import gianglong.app.chat.longchat.utils.RippleViewLinear;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment{
    String TAG = getClass().getSimpleName();
    View v;
    RippleViewLinear rippleViewLinear1, rippleViewLinear2, rippleViewLinear3, rippleViewLinear4, layout_signout;
    SweetAlertDialog mSweetAlertDialog;
    ProgressWheel progressWheel;
    TextView tvName, tvIntroduce;
    UserEntity user;

    ImageLoader imageLoader;
    DisplayImageOptions options;
    DatabaseHandler databaseHandler;



    public AccountFragment() {
        databaseHandler = MainActivity.databaseHandler;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account, container, false);
        initUI();
        initConfig();
        eventHandle();

        getBasicUserInfo();
        return v;
    }


    public void initUI() {
        rippleViewLinear1 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear1);
        rippleViewLinear2 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear2);
        rippleViewLinear3 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear3);
        rippleViewLinear4 = (RippleViewLinear) v.findViewById(R.id.rippleViewLinear4);
        layout_signout = (RippleViewLinear) v.findViewById(R.id.layout_signout);
        progressWheel = (ProgressWheel) v.findViewById(R.id.progressWheel);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvIntroduce = (TextView) v.findViewById(R.id.tvIntroduce);

    }


    public void initConfig() {
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


        // ImageLoader
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(0) {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        imageAware.setImageBitmap(bitmap);
                        if (loadedFrom == LoadedFrom.NETWORK) {
                            animate(imageAware.getWrappedView(), 300);
                        }
                    }
                }).build();

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


    public void setData() {
        user = UserEntity.getInstance();
        if (user != null) {
            tvName.setText(user.getName());
            tvIntroduce.setText(user.getIntrodution());
        }


    }


    public void getUserInfo(String id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DataNotify.DATA_SUCCESS) {

                    UserEntity userEntity = (UserEntity) msg.obj;
                    GlobalVars.setUserEntity(userEntity);

                    setData();
//                    Toast.makeText(getActivity(), databaseHandler.isUserExist(userEntity.getId()) + "", Toast.LENGTH_SHORT).show();

                } else if (msg.what == DataNotify.DATA_SUCCESS_WITH_NO_DATA) {

                    Intent it = new Intent(getActivity(), TakeInfoDetailActivity.class);
                    startActivity(it);
                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Log.e(TAG, "getUserInfo error!");
                }
            }
        };

        new UserService(getActivity()).getUserInfo(handler, id);
    }


    public void getBasicUserInfo() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DataNotify.DATA_SUCCESS) {

                    BasicUserInfoEntity entity = (BasicUserInfoEntity) msg.obj;
                    GlobalVars.setBasicUserInfoEntity(entity);


                    getUserInfo(entity.getUid());
                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Toast.makeText(getActivity(), "Error!!!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new UserService(getActivity()).getBasicUserInfo(handler);
    }

}
