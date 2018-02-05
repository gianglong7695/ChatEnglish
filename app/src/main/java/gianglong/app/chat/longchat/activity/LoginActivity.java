package gianglong.app.chat.longchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.custom.SweetDialog;
import gianglong.app.chat.longchat.custom.TransactionHelperFragment;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.fragment.TakeInfoDetailFragment;
import gianglong.app.chat.longchat.service.UserService;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.DataNotify;
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.tvForget)
    TextView tvForget;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.etUser)
    EditText etUser;
    @BindView(R.id.etPassword)
    EditText etPass;
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.ivFacebook)
    ImageView ivFacebook;
    @BindView(R.id.ivGoogle)
    ImageView ivGoogle;
    @BindView(R.id.layout_bottom)
    LinearLayout layout_bottom;
    @BindView(R.id.frame_layout)
    FrameLayout frame_layout;

    private FirebaseAuth mAuth;
    private SessionManager mSessionManager;
    private SweetDialog mSweetDialog;
    private DatabaseHandler databaseHandler;
    private TransactionHelperFragment transHelperFragment;
    private String user_name, user_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        runAnimation();
    }


    public void init() {
        mSessionManager = new SessionManager(getApplicationContext());
        // redirect to MainActivity if have signed in
        if (mSessionManager.isLoggedIn()) {
            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
            finish();
        }


        databaseHandler = new DatabaseHandler(this);
        mAuth = FirebaseAuth.getInstance();
        transHelperFragment = new TransactionHelperFragment(this, R.id.frame_layout);
        mSweetDialog = new SweetDialog(this);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // Underline in text
        tvForget.setPaintFlags(tvForget.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSignUp.setPaintFlags(tvSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Set font default
        etPass.setTypeface(Typeface.DEFAULT);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name = etUser.getText().toString().trim();
                user_password = etPass.getText().toString().trim();
                signIn();
            }
        });


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(it);

            }
        });


    }


    public void signIn() {
        mSweetDialog.showProgress("Please wait ...");
        mAuth.signInWithEmailAndPassword(user_name, user_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UserEntity userEntity = new UserEntity();
                            userEntity.setId(firebaseUser.getUid());
                            userEntity.setEmail(firebaseUser.getEmail());
                            userEntity.setPassword(user_password);


                            getDetailUserInfo(userEntity);

                        } else {
                            mSweetDialog.dismiss();
                            mSweetDialog.showError("Oops...", "Email or password is incorrect!");
                        }
                    }
                });
    }


    public void runAnimation() {
        // For logo
        Animation animLogo = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.down_from_top);

        animLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivLogo.setAnimation(animLogo);

        // for layout bottom
        Animation animBottom = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_bottom_to_up_card_login);
        animBottom.setDuration(900);
        layout_bottom.setAnimation(animBottom);


        // for et account
        Animation animAccount = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_left_to_right_in_fb);
        animAccount.setDuration(1300);
        etUser.setAnimation(animAccount);


        // for et password
        Animation animPassword = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_right_to_left_in_gmail);
        animPassword.setDuration(1300);
        etPass.setAnimation(animPassword);

    }


    public void getDetailUserInfo(final UserEntity userEntity) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mSweetDialog.dismiss();


                if (msg.what == DataNotify.DATA_SUCCESS) {
                    mSessionManager.setLogin(true);
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it);
                    finish();

                    // Saving basic user entity to preference
                    SharedPreferences pref = getSharedPreferences(Constants.KEY_USER, MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(msg.obj);
                    edit.putString(Constants.KEY_USER_ENTITY, json);
                    edit.commit();


                } else if (msg.what == DataNotify.DATA_SUCCESS_WITH_NO_DATA) {
                    // If haven't user info, redirect to takeInfoDetailFragment
                    transHelperFragment.addFragment(new TakeInfoDetailFragment(userEntity, getApplicationContext()), true, 2);
                } else if (msg.what == DataNotify.DATA_UNSUCCESS) {
                    Logs.e("getUserInfo error!");
                }

            }
        };

        new UserService(this).getUserInfo(handler, userEntity.getId());
    }

}
