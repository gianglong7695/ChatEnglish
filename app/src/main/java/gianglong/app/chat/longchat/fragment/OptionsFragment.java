package gianglong.app.chat.longchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import gianglong.app.chat.longchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OptionsFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivMore)
    ImageView ivMore;



    private View v;

    public OptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_options, container, false);
        ButterKnife.bind(this, v);


        initView();





        return v;
    }


    public void initView(){
        ivMore.setVisibility(View.GONE);
        tvTitle.setText("Options");


        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                getActivity().onBackPressed();
                break;


        }
    }
}
