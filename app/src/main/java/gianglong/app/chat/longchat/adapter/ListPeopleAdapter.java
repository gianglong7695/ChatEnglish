package gianglong.app.chat.longchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.fragment.SingleChatFragment;
import gianglong.app.chat.longchat.service.listener.ICallBack;

/**
 * Created by VCCORP on 4/28/2017.
 */

public class ListPeopleAdapter extends RecyclerView.Adapter<ListPeopleAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<UserEntity> alListPeople;
    private ICallBack iCallBack;


    public ListPeopleAdapter(Context context, ArrayList<UserEntity> people) {
        this.context = context;
        this.alListPeople = people;
        if (context instanceof ICallBack) iCallBack = (ICallBack) context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_people, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final UserEntity obj = alListPeople.get(position);
        if(obj != null){
            if(obj.getAvatar() != null){
                if(obj.getAvatar().equals("1")){
                    holder.civAvatar.setImageResource(R.drawable.avatar_male_default);
                }else{
                    holder.civAvatar.setImageResource(R.drawable.avatar_female_default);
                }
            }


            if(obj.getName() != null){
                holder.tvName.setText(obj.getName());
            }


            if(obj.getIntrodution() != null){
                holder.tvIntroduce.setText(obj.getIntrodution());
            }

//            if(obj.isUserActive()){
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    holder.vUserStatus.setBackground(context.getDrawable(R.drawable.bg_status_online));
//                }else{
//                    holder.vUserStatus.setBackgroundResource(R.drawable.bg_status_online);
//                }
//            }else{
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    holder.vUserStatus.setBackground(context.getDrawable(R.drawable.bg_status_offline));
//                }else{
//                    holder.vUserStatus.setBackgroundResource(R.drawable.bg_status_offline);
//                }
//            }
//
//            if(obj.isMine()){
//                holder.tvSender.setVisibility(View.VISIBLE);
//            }else{
//                holder.tvSender.setVisibility(View.GONE);
//            }



            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iCallBack.onAddFragment(new SingleChatFragment(obj), 2);
                }
            });








        }
    }

    @Override
    public int getItemCount() {
        return alListPeople.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        View vUserStatus;
        TextView tvName, tvIntroduce;
        ImageView ivFlag;
        LinearLayout layout_item;

        public MyViewHolder(View v) {
            super(v);

            civAvatar = (CircleImageView) v.findViewById(R.id.civAvatar);
            vUserStatus = v.findViewById(R.id.vUserStatus);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvIntroduce = (TextView) v.findViewById(R.id.tvIntroduce);
            ivFlag = (ImageView) v.findViewById(R.id.ivFlag);
            layout_item = (LinearLayout) v.findViewById(R.id.layout_item);

        }
    }
}
