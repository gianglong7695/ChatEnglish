package gianglong.app.chat.longchat.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gianglong.app.chat.longchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {
    String TAG = getClass().getSimpleName();
    View v;
    RecyclerView rvListPeople;

    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_people, container, false);
        initUI();




        return v;
    }


    public void initUI(){
        rvListPeople = (RecyclerView) v.findViewById(R.id.rvListPeople);
    }

}
