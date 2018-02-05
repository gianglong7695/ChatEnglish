package gianglong.app.chat.longchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.custom.TransactionHelperFragment;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.MessageEvent;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.fragment.AccountFragment;
import gianglong.app.chat.longchat.fragment.FriendFragment;
import gianglong.app.chat.longchat.fragment.MessageFragment;
import gianglong.app.chat.longchat.fragment.PeopleFragment;
import gianglong.app.chat.longchat.service.FirebaseService;
import gianglong.app.chat.longchat.service.listener.ICallBack;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.utils.SessionManager;

public class MainActivity extends RuntimePermissionsActivity implements View.OnClickListener, ICallBack {
    private static final int REQUEST_PERMISSIONS = 20;

    private int[] tabDrawableOn = {R.drawable.ic_chat_selected, R.drawable.ic_friends_selected,
            R.drawable.ic_people_selected, R.drawable.ic_account_selected};
    private int[] tabDrawableOff = {R.drawable.ic_chat, R.drawable.ic_friends,
            R.drawable.ic_people, R.drawable.ic_account};

    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.layout_chat)
    RelativeLayout layout_chat;
    @BindView(R.id.layout_friend)
    RelativeLayout layout_friend;
    @BindView(R.id.layout_people)
    RelativeLayout layout_people;
    @BindView(R.id.layout_account)
    RelativeLayout layout_account;

    @BindView(R.id.img_chat)
    ImageView img_chat;
    @BindView(R.id.img_friend)
    ImageView img_friend;
    @BindView(R.id.img_people)
    ImageView img_people;
    @BindView(R.id.img_account)
    ImageView img_account;

    @BindView(R.id.tv_chat)
    TextView tv_chat;
    @BindView(R.id.tv_friend)
    TextView tv_friend;
    @BindView(R.id.tv_people)
    TextView tv_people;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.frame_layout)
    FrameLayout frame_layout;


    private MessageFragment messageFragment;
    private FriendFragment friendFragment;
    private PeopleFragment peopleFragment;
    private AccountFragment accountFragment;

    private MyViewPagerAdapter viewPagerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static SessionManager mSessionManager;
    private DatabaseHandler databaseHandler;
    private SharedPreferences pref;
    private UserEntity userEntity;
    public static UserEntity basicUser;
    private SweetAlertDialog mSweetAlertDialog;
    private TransactionHelperFragment transHelperFragment;
    private int entryCount;


    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        //Start service
        startService();

        init();
        initFragment();
    }


    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }


//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
//    public void onLogout (Boolean isLogout) {
//        mSessionManager.setLogin(false);
//        Intent it = new Intent(this, LoginActivity.class);
//        startActivity(it);
//        finish();
//    }

    public void init() {
        databaseHandler = DatabaseHandler.getInstance(this);
        mSessionManager = new SessionManager(getApplicationContext());


        layout_chat.setOnClickListener(this);
        layout_friend.setOnClickListener(this);
        layout_people.setOnClickListener(this);
        layout_account.setOnClickListener(this);

        pref = getSharedPreferences(Constants.KEY_USER, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString(Constants.KEY_USER_ENTITY, DataNotify.NO_DATA);
        basicUser = gson.fromJson(json, UserEntity.class);
        userEntity = gson.fromJson(json, UserEntity.class);


//        if (basicUser.getId() != null) {
//            if (databaseHandler.isCheckExist(DatabaseHandler.TABLE_USER, DatabaseHandler.KEY_USER_ID, basicUser.getId())) {
//                userEntity = databaseHandler.getUserByID(basicUser.getId());
//
//                EventBus.getDefault().post(userEntity);
//            }
//        }


        if (userEntity != null) {
            EventBus.getDefault().postSticky(new MessageEvent("update_profile", userEntity));
        }


        transHelperFragment = new TransactionHelperFragment(this, R.id.frame_layout);
        checkEntryCount();
    }


    public void initFragment() {
        messageFragment = new MessageFragment();
        friendFragment = new FriendFragment();
        peopleFragment = new PeopleFragment();
        accountFragment = new AccountFragment();

        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        viewPager.setOffscreenPageLimit(tabDrawableOff.length);
    }


    //method start service
    public void startService() {
        startService(new Intent(getBaseContext(), FirebaseService.class));
    }


    //method destroy service
    public void stopService() {
        stopService(new Intent(getBaseContext(), FirebaseService.class));
    }


    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changeTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void changeTab(int position) {
        Logs.i("Tab selected : " + (position + 1));
        if (position == 0) {
            img_chat.setImageResource(tabDrawableOn[0]);
            tv_chat.setTypeface(null, Typeface.BOLD);
            tv_chat.setTextColor(getResources().getColor(R.color.text_color_tab_selected));
            img_friend.setImageResource(tabDrawableOff[1]);
            tv_friend.setTypeface(null, Typeface.NORMAL);
            tv_friend.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_people.setImageResource(tabDrawableOff[2]);
            tv_people.setTypeface(null, Typeface.NORMAL);
            tv_people.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_account.setImageResource(tabDrawableOff[3]);
            tv_account.setTypeface(null, Typeface.NORMAL);
            tv_account.setTextColor(getResources().getColor(R.color.text_color_tab));
        } else if (position == 1) {
            img_chat.setImageResource(tabDrawableOff[0]);
            tv_chat.setTypeface(null, Typeface.NORMAL);
            tv_chat.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_friend.setImageResource(tabDrawableOn[1]);
            tv_friend.setTypeface(null, Typeface.BOLD);
            tv_friend.setTextColor(getResources().getColor(R.color.text_color_tab_selected));
            img_people.setImageResource(tabDrawableOff[2]);
            tv_people.setTypeface(null, Typeface.NORMAL);
            tv_people.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_account.setImageResource(tabDrawableOff[3]);
            tv_account.setTypeface(null, Typeface.NORMAL);
            tv_account.setTextColor(getResources().getColor(R.color.text_color_tab));
        } else if (position == 2) {
            img_chat.setImageResource(tabDrawableOff[0]);
            tv_chat.setTypeface(null, Typeface.NORMAL);
            tv_chat.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_friend.setImageResource(tabDrawableOff[1]);
            tv_friend.setTypeface(null, Typeface.NORMAL);
            tv_friend.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_people.setImageResource(tabDrawableOn[2]);
            tv_people.setTypeface(null, Typeface.BOLD);
            tv_people.setTextColor(getResources().getColor(R.color.text_color_tab_selected));
            img_account.setImageResource(tabDrawableOff[3]);
            tv_account.setTypeface(null, Typeface.NORMAL);
            tv_account.setTextColor(getResources().getColor(R.color.text_color_tab));
        } else if (position == 3) {
            img_chat.setImageResource(tabDrawableOff[0]);
            tv_chat.setTypeface(null, Typeface.NORMAL);
            tv_chat.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_friend.setImageResource(tabDrawableOff[1]);
            tv_friend.setTypeface(null, Typeface.NORMAL);
            tv_friend.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_people.setImageResource(tabDrawableOff[2]);
            tv_people.setTypeface(null, Typeface.NORMAL);
            tv_people.setTextColor(getResources().getColor(R.color.text_color_tab));
            img_account.setImageResource(tabDrawableOn[3]);
            tv_account.setTypeface(null, Typeface.BOLD);
            tv_account.setTextColor(getResources().getColor(R.color.text_color_tab_selected));
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_chat:
                viewPager.setCurrentItem(0);
                break;
            case R.id.layout_friend:
                viewPager.setCurrentItem(1);
                break;
            case R.id.layout_people:
                viewPager.setCurrentItem(2);
                break;
            case R.id.layout_account:
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onAddFragment(Fragment fragment, int... styleAnimation) {
        if (styleAnimation.length == 0) {
            transHelperFragment.addFragment(fragment, true, 1);
        } else {
            transHelperFragment.addFragment(fragment, true, styleAnimation[0]);
        }
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return messageFragment;
            } else if (position == 1) {
                return friendFragment;
            } else if (position == 2) {
                return peopleFragment;
            } else if (position == 3) {
                return accountFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabDrawableOff.length;
        }
    }


    @Override
    public void onBackPressed() {
        if (entryCount > 0) {
            transHelperFragment.popTopFragment();
        } else {
            mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            mSweetAlertDialog.setTitleText("Confirm exit");
            mSweetAlertDialog.setContentText("Close this application. Are you sure ?");
            mSweetAlertDialog.setCancelText("No");
            mSweetAlertDialog.setConfirmText("Yes");
            mSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    finish();
                }
            });
            mSweetAlertDialog.show();
        }


    }


    private void checkEntryCount() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                entryCount = transHelperFragment.getBackStackEntryCount();
            }
        });
    }
}
