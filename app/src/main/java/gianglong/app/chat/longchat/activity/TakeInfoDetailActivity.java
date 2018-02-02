package gianglong.app.chat.longchat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.utils.SweetDialog;

import static gianglong.app.chat.longchat.utils.Constants.STORAGE_URL;

public class TakeInfoDetailActivity extends AppCompatActivity {
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


    private String[] arrGender;
    private int genderPos = 0;
    private String[] arrCountry;
    private int countryPos = 0;

    private static final int CAM_REQUEST = 1;
    private static final int SELECTED_PICTURE_REQUEST = 2;

    private FirebaseAuth mAuth;
    private String email, password;
    private SweetDialog mSweetDialog;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Bitmap bAvatar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_detail);
        ButterKnife.bind(this);

        screenConfigPopup();
        init();
        handleEvent();
    }

    public void screenConfigPopup() {
        setTitle("Add your info");

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int) (width*.95), (int) (height*.95));
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }


    public void init() {
        mSweetDialog = new SweetDialog(this);


        arrGender = getResources().getStringArray(R.array.arrGender);
        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<String>(this, R.layout.item_simple_list, arrGender) {

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
                new ArrayAdapter<String>(this, R.layout.item_simple_list, arrCountry) {

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


        layout_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(TakeInfoDetailActivity.this);
                builderSingle.setTitle("Choose your avatar");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TakeInfoDetailActivity.this, R.layout.item_single_dialog);
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
            }
        });


        // Firebase
        mAuth = FirebaseAuth.getInstance();


        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

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


    public void updateUserInfo() {
        mSweetDialog.showProgress("Please wait ...");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(etName.getText().toString())
                .setPhotoUri(Uri.parse("https://www.timeshighereducation.com/sites/default/files/byline_photos/default-avatar.png"))
                .build();


        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            UserEntity userEntity = new UserEntity(MainActivity.basicUser.getId(),
                                    MainActivity.basicUser.getEmail(),
                                    etName.getText().toString(),
                                    MainActivity.basicUser.getPassword(),
                                    arrGender[genderPos],
                                    arrCountry[countryPos],
                                    "avatar",
                                    "introduce",
                                    1.5,
                                    10
                            );
                            mDatabase.child(Constants.NODE_MASTER).child(Constants.NODE_USER).child(UserEntity.getInstance().getId()).setValue(userEntity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mSweetDialog.dismiss();
                                    finish();
                                    Toast.makeText(TakeInfoDetailActivity.this, "Update success!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAM_REQUEST && resultCode != 0) {
            bAvatar = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(bAvatar);
        }

        if (requestCode == SELECTED_PICTURE_REQUEST) {
            try {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filepath = cursor.getString(columnIndex);
                cursor.close();


                bAvatar = decodeUri(uri);
                Drawable drawable = new BitmapDrawable(bAvatar);
                ivAvatar.setImageDrawable(drawable);


            } catch (Exception e) {
                Logs.e(e.toString());
            }
        }

    }

    public void takePicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAM_REQUEST);
    }


    public void takePicFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE_REQUEST);
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);
    }


    public void uploadAvatar(Bitmap bm) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl(STORAGE_URL);

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("mountains.png");

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.png");

        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false


//        // Get the data from an ImageView as bytes
//        ivAvatar.setDrawingCacheEnabled(true);
//        ivAvatar.buildDrawingCache();
//        Bitmap bitmap = ivAvatar.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(TakeInfoDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                mSweetDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                Toast.makeText(TakeInfoDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                hideDialog();
//                Log.d(TAG, downloadUrl.toString());
            }
        });
    }

}
