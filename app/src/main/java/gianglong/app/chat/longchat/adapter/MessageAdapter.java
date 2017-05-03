package gianglong.app.chat.longchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.entity.MessageItemEntity;

/**
 * Created by VCCORP on 4/28/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    String TAG = getClass().getSimpleName();
    Context context;
    ArrayList<MessageItemEntity> alMsg;


    public final int TYPE_MINE = 1;
    public final int TYPE_YOURS = 0;


    public MessageAdapter(Context context, ArrayList<MessageItemEntity> alMsg) {
        this.context = context;
        this.alMsg = alMsg;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_MINE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
        }

        return new MyViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (alMsg.get(position).getTypeView() == TYPE_MINE) {
            return TYPE_MINE;
        } else {
            return TYPE_YOURS;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageItemEntity entity = alMsg.get(position);

        if (entity != null) {
            holder.tvMsg.setText(entity.getMessage());
            holder.tvTime.setText(entity.getTime());
            if (entity.getAvatarUrl().equals("0")) {
                holder.civAvatar.setImageResource(R.drawable.avatar_male_default);
            } else {
                holder.civAvatar.setImageResource(R.drawable.avatar_female_default);
            }

            if (!entity.isHideTime()) {
                holder.tvTime.setVisibility(View.GONE);
            }


            if (alMsg.size() > 1) {
//                if (entity.getTypeView() == alMsg.get(position - 1).getTypeView()) {
//                    alMsg.get(position - 1).setHideTime(false);
//                    notifyDataSetChanged();
//                }

            }

        }


    }

    @Override
    public int getItemCount() {
        return alMsg.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        LinearLayout layout_body_msg;
        TextView tvMsg, tvTime;

        public MyViewHolder(View v) {
            super(v);
            civAvatar = (CircleImageView) v.findViewById(R.id.civAvatar);
            layout_body_msg = (LinearLayout) v.findViewById(R.id.layout_body_msg);
            tvMsg = (TextView) v.findViewById(R.id.tvMsg);
            tvTime = (TextView) v.findViewById(R.id.tvTime);
        }
    }
}
