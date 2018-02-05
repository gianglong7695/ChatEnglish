package gianglong.app.chat.longchat.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.database.DatabaseHandler;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Logs;
import gianglong.app.chat.longchat.utils.MyUtils;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements View.OnClickListener {
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
    CircleImageView ivAvatar;
    @BindView(R.id.spGender)
    Spinner spGender;
    @BindView(R.id.spCountry)
    Spinner spCountry;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivMore)
    ImageView ivMore;


    private View view;
    private UserEntity userEntity;
    private static final int CAM_REQUEST = 1;
    private static final int SELECTED_PICTURE_REQUEST = 2;
    private DatabaseHandler databaseHandler;
    private String[] arrCountry;
    private int countryPos = 0;
    private int genderPos = 0;

    @SuppressLint("ValidFragment")
    public ProfileFragment(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);


        init();
        eventHandle();


        return view;
    }


    public void init() {
        tvTitle.setText("Update profile");
        ivMore.setVisibility(View.VISIBLE);


        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.arrGender, R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);


        arrCountry = getResources().getStringArray(R.array.arrCountry);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_simple_list, arrCountry);
        countryAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(countryAdapter);


        etPass.setTypeface(Typeface.DEFAULT);


        setData();
    }


    public void eventHandle() {


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
        ivAvatar.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etName:
                etName.setFocusable(true);
                etName.requestFocus();
                break;
            case R.id.etPass:

                break;
            case R.id.etEmail:

                break;
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;
            case R.id.ivAvatar:
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setTitle("Change avatar");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_single_dialog);
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


                break;
            case R.id.ivMore:
                Toast.makeText(getActivity(), "Complete", Toast.LENGTH_SHORT).show();
                break;


            default:
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAM_REQUEST && resultCode != 0) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ivAvatar.setImageBitmap(bitmap);
        }

        if (requestCode == SELECTED_PICTURE_REQUEST) {
            try {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String filepath = cursor.getString(columnIndex);
                cursor.close();


                Bitmap bitmap = MyUtils.decodeUri(uri, getContext());
                Drawable drawable = new BitmapDrawable(bitmap);
                ivAvatar.setImageDrawable(drawable);

            } catch (Exception e) {
                Logs.e(e.toString());
            }
        }
    }


    public void setData() {
        if (userEntity.getName() != null) {
            etName.setText(userEntity.getName());
            tvName.setText(userEntity.getName());
        }

        if (userEntity.getEmail() != null) {
            etEmail.setText(userEntity.getEmail());
        }


        if (userEntity.getPassword() != null) {
            etPass.setText(userEntity.getPassword());
        }


        if (userEntity.getGender() != null) {
            if (userEntity.getGender().equals("Male")) {
                spGender.setSelection(0);
            } else {
                spGender.setSelection(1);
            }
        }

        for (int i = 0; i < arrCountry.length; i++) {
            if (userEntity.getCountry().equals(arrCountry[i])) {
                countryPos = i;
                spCountry.setSelection(countryPos);
            }
        }

    }
}
