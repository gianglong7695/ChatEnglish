package gianglong.app.chat.longchat.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.GlobalVars;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.fragment.AccountFragment;
import gianglong.app.chat.longchat.fragment.FriendFragment;
import gianglong.app.chat.longchat.fragment.MessageFragment;
import gianglong.app.chat.longchat.fragment.PeopleFragment;
import gianglong.app.chat.longchat.service.UserService;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.LogUtil;
import gianglong.app.chat.longchat.utils.RippleView;
import gianglong.app.chat.longchat.utils.SessionManager;

public class MainActivity extends RuntimePermissionsActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS = 20;

    private int[] tabDrawableOn = {R.drawable.ic_chat_selected, R.drawable.ic_friends_selected,
            R.drawable.ic_people_selected, R.drawable.ic_account_selected};
    private int[] tabDrawableOff = {R.drawable.ic_chat, R.drawable.ic_friends,
            R.drawable.ic_people, R.drawable.ic_account};

    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.layout_chat)
    RippleView layout_chat;
    @BindView(R.id.layout_friend)
    RippleView layout_friend;
    @BindView(R.id.layout_people)
    RippleView layout_people;
    @BindView(R.id.layout_account)
    RippleView layout_account;

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


    private MessageFragment messageFragment;
    private FriendFragment friendFragment;
    private PeopleFragment peopleFragment;
    private AccountFragment accountFragment;

    private MyViewPagerAdapter viewPagerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SessionManager mSessionManager;
    private DatabaseHandler databaseHandler;
    private SharedPreferences pref;
    private UserEntity user;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        databaseHandler = DatabaseHandler.getInstance(this);
        init();
        initFragment();
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onLogout (Boolean isLogout) {
        mSessionManager.setLogin(false);
    }

    public void init() {
        mSessionManager = new SessionManager(getApplicationContext());

        layout_chat.setOnClickListener(this);
        layout_friend.setOnClickListener(this);
        layout_people.setOnClickListener(this);
        layout_account.setOnClickListener(this);

        pref = getSharedPreferences("user", MODE_PRIVATE);
        userID = pref.getString(DatabaseHandler.KEY_USER_ID, DataNotify.NO_DATA);

        if (userID != null) {
            if (databaseHandler.isCheckExist(DatabaseHandler.TABLE_USER, DatabaseHandler.KEY_USER_ID, userID)) {
                user = databaseHandler.getUserByID(userID);

                EventBus.getDefault().postSticky(user);
                //Saving user entity to global variable
                GlobalVars.setUserEntity(user);
            } else {
                getUserInfo(userID);
            }
        }
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


    public void getUserInfo(String id) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == DataNotify.DATA_SUCCESS) {
                    user = (UserEntity) msg.obj;
                    databaseHandler.addOrUpdateUser(user);

                    // Send user enity to onEvent method of Account fragment
                    EventBus.getDefault().post(user);
                    GlobalVars.setUserEntity(user);
                } else if (msg.what == DataNotify.DATA_SUCCESS_WITH_NO_DATA) {


                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    LogUtil.e("getUserInfo error!");
                }
            }
        };

        new UserService(this).getUserInfo(handler, id);
    }
}
