package gianglong.app.chat.longchat.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.utils.RippleView;
import gianglong.app.chat.longchat.utils.SessionManager;

public class SignUpActivity extends AppCompatActivity {
    private EditText etUser, etPass, etRePass;
    private Button btSignUp;
    private HTextView hTextView;
    private RippleView rippleBtnSignin;
    private SweetAlertDialog pDialog;
    private FirebaseAuth mAuth;
    private SessionManager mSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        initUI();


    }


    public void initUI() {
        btSignUp = (Button) findViewById(R.id.btSignUp);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPassword);
        etRePass = (EditText) findViewById(R.id.etRePassword);
        hTextView = (HTextView) findViewById(R.id.text);
        rippleBtnSignin = (RippleView) findViewById(R.id.rippleBtnSignin);

        etPass.setTypeface(Typeface.DEFAULT);
        etRePass.setTypeface(Typeface.DEFAULT);


        // Set dialog
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.purple));
        pDialog.setTitleText("Please wait ...");
        pDialog.setCancelable(false);


        /* Title animation */
        hTextView.setTypeface(Typeface.createFromAsset(getAssets(), "I believe in life before death.ttf"));
        //hTextView.setTypeface(Typeface.createFromAsset(getAssets(), "victoria.ttf"));
        hTextView.setAnimateType(HTextViewType.ANVIL);
        hTextView.animateText("Sign Up"); // animate




        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mSessionManager = new SessionManager(getApplicationContext());



        rippleBtnSignin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                String user = etUser.getText().toString();
                String pass = etPass.getText().toString();
                String repass = etRePass.getText().toString();
                if(user.length() > 0 && pass.length() > 0 && repass.length() > 0){
                    showDialog();
                    signUp(user, pass);

                }else{
                    Toast.makeText(SignUpActivity.this, "Không thể để rỗng!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Fail!",
                                    Toast.LENGTH_SHORT).show();
                            hideDialog();
                        } else {
                            hideDialog();
                            SweetAlertDialog progressSweetDialog = new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            progressSweetDialog.setTitleText("Success!");
                            progressSweetDialog.setContentText("You have successfully registered. Login now ?");
                            progressSweetDialog.setCancelText("No");
                            progressSweetDialog.setConfirmText("Yes");
                            progressSweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    mSessionManager.setLogin(true);
                                    Intent it = new Intent(SignUpActivity.this, LoginActivity.class);
                                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(it);
                                }
                            });

                            progressSweetDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    finish();
                                }
                            });
                            progressSweetDialog.show();
                        }

                    }
                });

//        if(bAvatar != null){
//            uploadAvatar(bAvatar);
//        }
    }


    public void showDialog() {
        pDialog.show();
    }

    public void hideDialog() {
        pDialog.dismiss();
    }

}
