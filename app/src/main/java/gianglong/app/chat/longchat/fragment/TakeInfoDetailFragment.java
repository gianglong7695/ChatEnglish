package gianglong.app.chat.longchat.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.MainActivity;
import gianglong.app.chat.longchat.custom.SweetDialog;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.utils.MyUtils;
import gianglong.app.chat.longchat.utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class TakeInfoDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.etName)
    MaterialEditText etName;
    @BindView(R.id.spCountry)
    Spinner spCountry;
    @BindView(R.id.spGender)
    Spinner spGender;
    @BindView(R.id.layout_avatar)
    LinearLayout layout_avatar;
    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.btComplete)
    Button btComplete;


    private View view;
    private UserEntity userEntity;
    private Context mContext;


    private String[] arrGender;
    private int genderPos = 0;
    private String[] arrCountry;
    private int countryPos = 0;

    private static final int CAM_REQUEST = 1;
    private static final int SELECTED_PICTURE_REQUEST = 2;
    private SweetDialog mSweetDialog;
    private Bitmap bAvatar;
    private DatabaseReference mDatabase;
    private SessionManager mSessionManager;


    @SuppressLint("ValidFragment")
    public TakeInfoDetailFragment(UserEntity userEntity, Context context) {
        this.userEntity = userEntity;
        this.mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_take_info_detail, container, false);
        ButterKnife.bind(this, view);

        init();
        handleEvent();


        return view;
    }


    public void init() {
        mSweetDialog = new SweetDialog(getActivity());
        mSessionManager = new SessionManager(getActivity());

        arrGender = getResources().getStringArray(R.array.arrGender);
        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.item_simple_list, arrGender) {

                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        ((TextView) v).setTextColor(
                                getResources().getColorStateList(R.color.white)
                        );

                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);

                        ((TextView) v).setTextColor(
                                getResources().getColorStateList(R.color.text_color_default_spinner)
                        );
                        ((TextView) v).setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

                        return v;
                    }
                };
        spGender.setAdapter(genderAdapter);


        arrCountry = getResources().getStringArray(R.array.arrCountry);
        ArrayAdapter<String> countryAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.item_simple_list, arrCountry) {

                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        ((TextView) v).setTextColor(
                                getResources().getColorStateList(R.color.white)
                        );

                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);

                        ((TextView) v).setTextColor(
                                getResources().getColorStateList(R.color.text_color_default_spinner)
                        );
                        ((TextView) v).setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

                        return v;
                    }
                };
        spCountry.setAdapter(countryAdapter);


        // Firebase
//        mAuth = FirebaseAuth.getInstance();


        btComplete.setOnClickListener(this);
        layout_avatar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_avatar:
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Choose your avatar");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_single_dialog);
                arrayAdapter.add("Camera");
                arrayAdapter.add("Library");

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePicFromCamera();
                                break;
                            case 1:
                                takePicFromGallery();
                                break;
                        }

                    }
                });
                builderSingle.show();

                break;
            case R.id.btComplete:
                updateUserInfo();
                break;

        }
    }


    public void handleEvent() {
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void takePicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAM_REQUEST);
    }


    public void takePicFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAM_REQUEST && resultCode != 0) {
            bAvatar = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(bAvatar);
        }

        if (requestCode == SELECTED_PICTURE_REQUEST) {
            try {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filepath = cursor.getString(columnIndex);
                cursor.close();


                bAvatar = MyUtils.decodeUri(uri, mContext);
                Drawable drawable = new BitmapDrawable(bAvatar);
                ivAvatar.setImageDrawable(drawable);


            } catch (Exception e) {
                Logs.e(e.toString());
            }
        }

    }


    public void updateUserInfo() {
        mSweetDialog.showProgress("Please wait ...");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(etName.getText().toString())
                .setPhotoUri(Uri.parse("https://www.timeshighereducation.com/sites/default/files/byline_photos/default-avatar.png"))
                .build();


        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userEntity.setName(etName.getText().toString());
                            userEntity.setGender(arrGender[genderPos]);
                            userEntity.setCountry(arrCountry[countryPos]);
                            userEntity.setAvatar("Avatar link");
                            userEntity.setIntrodution("introduce");

                            mDatabase.child(Constants.NODE_MASTER).child(Constants.NODE_USER).child(userEntity.getId()).setValue(userEntity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mSweetDialog.dismiss();



                                    // Saving basic user entity to preference
                                    SharedPreferences pref = mContext.getSharedPreferences(Constants.KEY_USER, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = pref.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(userEntity);
                                    edit.putString(Constants.KEY_USER_ENTITY, json);
                                    edit.commit();






                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    mSessionManager.setLogin(true);
                                    getActivity().finish();

                                }
                            });
                        }else{
                            Toast.makeText(mContext, "Update fail !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
