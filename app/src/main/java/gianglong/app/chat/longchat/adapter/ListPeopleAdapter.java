package gianglong.app.chat.longchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.ChatActivity;
import gianglong.app.chat.longchat.entity.UserEntity;
import gianglong.app.chat.longchat.utils.Constants;
import gianglong.app.chat.longchat.utils.RippleViewLinear;

/**
 * Created by VCCORP on 4/28/2017.
 */

public class ListPeopleAdapter extends RecyclerView.Adapter<ListPeopleAdapter.MyViewHolder> {
    Context context;
    ArrayList<UserEntity> alListPeople;


    public ListPeopleAdapter(Context context, ArrayList<UserEntity> people) {
        this.context = context;
        this.alListPeople = people;
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



            holder.layout_item.setOnRippleCompleteListener(new RippleViewLinear.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleViewLinear rippleView) {
                    Intent it = new Intent(context, ChatActivity.class);
                    it.putExtra(Constants.KEY_USER, obj);
                    context.startActivity(it);
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
        RippleViewLinear layout_item;

        public MyViewHolder(View v) {
            super(v);

            civAvatar = (CircleImageView) v.findViewById(R.id.civAvatar);
            vUserStatus = v.findViewById(R.id.vUserStatus);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvIntroduce = (TextView) v.findViewById(R.id.tvIntroduce);
            ivFlag = (ImageView) v.findViewById(R.id.ivFlag);
            layout_item = (RippleViewLinear) v.findViewById(R.id.layout_item);

        }
    }
}
