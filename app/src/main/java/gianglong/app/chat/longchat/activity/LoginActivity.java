package gianglong.app.chat.longchat.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.RippleView;
import gianglong.app.chat.longchat.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    Button btLogin;
    TextView tvForget, tvSignUp;
    public static EditText etUser, etPass;
    Dialog mDialog;
    ImageView ivFacebook, ivGoogle, ivLogo;
    LinearLayout layout_bottom;
    RippleView rippleBtnSignin;
    private FirebaseAuth mAuth;
    private SweetAlertDialog pDialog;
    private SessionManager mSessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initUI();
        config();
        runAnimation();

    }


    public void initUI() {
        btLogin = (Button) findViewById(R.id.btLogin);
        tvForget = (TextView) findViewById(R.id.tvForget);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPassword);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        rippleBtnSignin = (RippleView) findViewById(R.id.rippleBtnSignin);
//        ivFacebook = (ImageView) findViewById(R.id.ivFacebook);
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        ivGoogle = (ImageView) findViewById(R.id.ivGoogle);
//        btSignIn = (SignInButton) findViewById(R.id.btSignInButton);


    }


    public void config() {
        mSessionManager = new SessionManager(getApplicationContext());


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // Underline in text
        tvForget.setPaintFlags(tvForget.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSignUp.setPaintFlags(tvSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Set font default
        etPass.setTypeface(Typeface.DEFAULT);

        rippleBtnSignin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                signIn(etUser.getText().toString().trim(), etPass.getText().toString().trim());
            }
        });


        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(it);

            }
        });


        // redirect to MainActivity if have signed in
        if (mSessionManager.isLoggedIn()) {
            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
            finish();
        }


        // Set dialog
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.purple));
        pDialog.setTitleText("Please wait ...");
        pDialog.setCancelable(false);

    }


    public void signIn(String email, String password) {
        showDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            hideDialog();
                            mSessionManager.setLogin(true);
                            Intent it = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(it);
                            finish();


                            FirebaseUser fUser = mAuth.getCurrentUser();
                            UserEntity userEntity = new UserEntity();
                            userEntity.setId(fUser.getUid());
                            userEntity.setName(fUser.getDisplayName());



                        } else {
                            hideDialog();
                            showDialogError();
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

    public void showDialog() {
        pDialog.show();
    }

    public void hideDialog() {
        pDialog.dismiss();


    }


    public void showDialogError() {
        SweetAlertDialog alertError = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
        alertError.setTitleText("Oops...");
        alertError.setContentText("Email or password is incorrect!");
        alertError.setCancelable(false);
        alertError.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        });
        alertError.show();
    }
}
