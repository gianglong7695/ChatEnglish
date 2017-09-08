package gianglong.app.chat.longchat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.LogUtil;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPass)
    EditText etPass;
    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.ivPenName)
    ImageView ivPenName;
    @BindView(R.id.ivPenPass)
    ImageView ivPenPass;
    @BindView(R.id.ivPenEmail)
    ImageView ivPenEmail;
    @BindView(R.id.ivSortCountry)
    ImageView ivSortCountry;
    @BindView(R.id.ivSortGender)
    ImageView ivSortGender;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.spGender)
    Spinner spGender;
    @BindView(R.id.spCountry)
    Spinner spCountry;


    private static final int CAM_REQUEST = 1;
    private static final int SELECTED_PICTURE_REQUEST = 2;
    private UserEntity entity;
    private DatabaseHandler databaseHandler;
    private String[] arrCountry;
    private int countryPos = 0;
    private int genderPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        databaseHandler = DatabaseHandler.getInstance(this);
        init();
        eventHandle();


    }


    public void init(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Profile");

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.arrGender, R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);


        arrCountry = getResources().getStringArray(R.array.arrCountry);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, R.layout.item_simple_list, arrCountry);
        countryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(countryAdapter);


        etPass.setTypeface(Typeface.DEFAULT);


        setData();
    }



    public void eventHandle(){


        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ivPenName.setImageResource(R.drawable.ic_pen_focus);
                } else {
                    ivPenName.setImageResource(R.drawable.ic_pen);
                }
            }
        });


        etPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ivPenPass.setImageResource(R.drawable.ic_pen_focus);
                } else {
                    ivPenPass.setImageResource(R.drawable.ic_pen);
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ivPenEmail.setImageResource(R.drawable.ic_pen_focus);
                } else {
                    ivPenEmail.setImageResource(R.drawable.ic_pen);
                }
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



        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderPos = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ivPenName.setOnClickListener(this);
        etName.setOnClickListener(this);
        etPass.setOnClickListener(this);
        etEmail.setOnClickListener(this);



        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ProfileActivity.this);
                builderSingle.setTitle("Change avatar");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, R.layout.item_single_dialog);
                arrayAdapter.add("Camera");
                arrayAdapter.add("library");
                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
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
    }

    public void takePicFromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAM_REQUEST);
    }


    public void takePicFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE_REQUEST);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CAM_REQUEST && resultCode != 0 ){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(bitmap);
        }

        if(requestCode == SELECTED_PICTURE_REQUEST){
            try {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filepath = cursor.getString(columnIndex);
                cursor.close();


                Bitmap bitmap = decodeUri(uri);
                Drawable drawable = new BitmapDrawable(bitmap);
                ivAvatar.setImageDrawable(drawable);

            }catch (Exception e){
                LogUtil.e(e.toString());
            }
        }
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etName:
                etName.setFocusable(true);
                etName.requestFocus();
                break;
            case R.id.etPass:

                break;
            case R.id.etEmail:

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.itemCompleted:
                Toast.makeText(this, "Okay", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void setData(){
        entity = databaseHandler.getUserByID(MainActivity.basicUser.getId());
//        entity = UserEntity.getInstance();


        if(entity.getName() != null){
            etName.setText(entity.getName());
            tvName.setText(entity.getName());
        }

        if(entity.getEmail() != null){
            etEmail.setText(entity.getEmail());
        }


        if(entity.getPassword() != null){
            etPass.setText(entity.getPassword());
        }


        if(entity.getGender() != null){
            if(entity.getGender().equals("Male")){
                spGender.setSelection(0);
            }else{
                spGender.setSelection(1);
            }
        }

        for (int i = 0; i < arrCountry.length; i++) {
            if(entity.getCountry().equals(arrCountry[i])){
                countryPos = i;
                spCountry.setSelection(countryPos);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);//Menu Resource, Menu
        return true;
    }

}
