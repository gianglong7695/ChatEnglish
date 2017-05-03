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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.utils.RippleView;

public class SignUpDetailActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private MaterialEditText etName, etAge;
    private Spinner spCountry, spGender;
    private LinearLayout layout_avatar;
    private RippleView rippleBtnSignin;
    private CircleImageView ivAvatar;
    private String[] arrGender;
    private String[] arrCountry;

    private static final int CAM_REQUEST = 1;
    private static final int SELECTED_PICTURE_REQUEST = 2;

    private FirebaseAuth mAuth;
    private String email, password;
    private SweetAlertDialog pDialog;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Bitmap bAvatar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_detail);
        screenConfigPopup();
        initUI();

    }

    public void screenConfigPopup(){
        getSupportActionBar().setHomeButtonEnabled(true);
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


    public void initUI() {
        etName = (MaterialEditText) findViewById(R.id.etName);
        etAge = (MaterialEditText) findViewById(R.id.etAge);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        spGender = (Spinner) findViewById(R.id.spGender);
        layout_avatar = (LinearLayout) findViewById(R.id.layout_avatar);
        rippleBtnSignin = (RippleView) findViewById(R.id.rippleBtnSignin);
        ivAvatar = (CircleImageView) findViewById(R.id.ivAvatar);

        // Set dialog
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.purple));
        pDialog.setTitleText("Please wait ...");
        pDialog.setCancelable(false);


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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(SignUpDetailActivity.this);
                builderSingle.setTitle("Choose your avatar");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignUpDetailActivity.this, R.layout.item_single_dialog);
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
//        email = getIntent().getExtras().getString("email");
//        password = getIntent().getExtras().getString("password");

        rippleBtnSignin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                updateUserInfo();

            }
        });
    }


    public void updateUserInfo(){
        showDialog();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child(Constants.NODE_MASTER).child(Constants.NODE_USER).child()
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
                Log.d(TAG, e.toString());
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


    public void showDialog() {
        pDialog.show();
    }

    public void hideDialog() {
        pDialog.cancel();
    }


    public void uploadAvatar(Bitmap bm) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://longchat-1bc37.appspot.com");

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
                Toast.makeText(SignUpDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                Toast.makeText(SignUpDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                hideDialog();
//                Log.d(TAG, downloadUrl.toString());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
