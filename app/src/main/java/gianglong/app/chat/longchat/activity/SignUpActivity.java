package gianglong.app.chat.longchat.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.utils.SessionManager;
import gianglong.app.chat.longchat.custom.SweetDialog;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.etUser)
    EditText etUser;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.etRePass)
    EditText etRePass;
    @BindView(R.id.textLogo)
    HTextView hTextView;
    @BindView(R.id.btSignUp)
    Button btSignUp;

    private SweetDialog mSweetDialog;
    private FirebaseAuth mAuth;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        init();
    }


    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        mSweetDialog = new SweetDialog(this);
        etPass.setTypeface(Typeface.DEFAULT);
        etRePass.setTypeface(Typeface.DEFAULT);


        /* Title animation */
        hTextView.setTypeface(Typeface.createFromAsset(getAssets(), "I believe in life before death.ttf"));
        //hTextView.setTypeface(Typeface.createFromAsset(getAssets(), "victoria.ttf"));
        hTextView.setAnimateType(HTextViewType.ANVIL);
        hTextView.animateText("Sign Up"); // animate


        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mSessionManager = new SessionManager(getApplicationContext());


        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString();
                String pass = etPass.getText().toString();
                String repass = etRePass.getText().toString();

                if (user.length() > 0 && pass.length() > 0 && repass.length() > 0) {
                    mSweetDialog.showProgress("Please wait ...");
                    signUp(user, pass);

                } else {
                    Toast.makeText(SignUpActivity.this, "Can not be empty!", Toast.LENGTH_SHORT).show();
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
                            mSweetDialog.dismiss();
                        } else {
                            mSweetDialog.dismiss();
                            mSweetDialog.showSuccess("Success!", "You have successfully registered. Login now ?");
                            mSweetDialog.getmSweetAlertDialog().setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    mSessionManager.setLogin(true);
                                    Intent it = new Intent(SignUpActivity.this, LoginActivity.class);
                                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(it);
                                }
                            });
                            mSweetDialog.getmSweetAlertDialog().setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    finish();
                                }
                            });
                            mSweetDialog.show();
                        }

                    }
                });

//        if(bAvatar != null){
//            uploadAvatar(bAvatar);
//        }
    }

}
