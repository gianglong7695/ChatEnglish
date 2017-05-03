package gianglong.app.chat.longchat.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.database.UserController;
import gianglong.app.chat.longchat.fragment.AccountFragment;
import gianglong.app.chat.longchat.fragment.FriendFragment;
import gianglong.app.chat.longchat.fragment.MessageFragment;
import gianglong.app.chat.longchat.fragment.PeopleFragment;
import gianglong.app.chat.longchat.utils.RippleView;
import gianglong.app.chat.longchat.utils.SessionManager;
import io.realm.Realm;

public class MainActivity extends RuntimePermissionsActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS = 20;

    private int[] tabDrawableOn = {R.drawable.ic_chat_selected, R.drawable.ic_friends_selected,
            R.drawable.ic_people_selected, R.drawable.ic_account_selected};
    private int[] tabDrawableOff = {R.drawable.ic_chat, R.drawable.ic_friends,
            R.drawable.ic_people, R.drawable.ic_account};


    private ViewPager viewPager;
    private RippleView layout_chat, layout_friend, layout_people, layout_account;
    private ImageView img_chat, img_friend, img_people, img_account;
    private TextView tv_chat, tv_friend, tv_people, tv_account;


    private MessageFragment messageFragment;
    private FriendFragment friendFragment;
    private PeopleFragment peopleFragment;
    private AccountFragment accountFragment;

    private MyViewPagerAdapter viewPagerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = getClass().getSimpleName();
    public static SessionManager mSessionManager;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initFragment();
        config();
    }


    public void config(){
        mSessionManager = new SessionManager(getApplicationContext());
        mRealm = UserController.with(this).getRealm();
    }

    public void initUI() {
        viewPager = (ViewPager) findViewById(R.id.vp_main);

        layout_chat = (RippleView) findViewById(R.id.layout_chat);
        layout_friend = (RippleView) findViewById(R.id.layout_friend);
        layout_people = (RippleView) findViewById(R.id.layout_people);
        layout_account = (RippleView) findViewById(R.id.layout_account);

        img_chat = (ImageView) findViewById(R.id.img_chat);
        img_friend = (ImageView) findViewById(R.id.img_friend);
        img_people = (ImageView) findViewById(R.id.img_people);
        img_account = (ImageView) findViewById(R.id.img_account);

        tv_chat = (TextView) findViewById(R.id.tv_chat);
        tv_friend = (TextView) findViewById(R.id.tv_friend);
        tv_people = (TextView) findViewById(R.id.tv_people);
        tv_account = (TextView) findViewById(R.id.tv_account);


        layout_chat.setOnClickListener(this);
        layout_friend.setOnClickListener(this);
        layout_people.setOnClickListener(this);
        layout_account.setOnClickListener(this);
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
}