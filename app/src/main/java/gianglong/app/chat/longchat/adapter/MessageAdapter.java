package gianglong.app.chat.longchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import gianglong.app.chat.longchat.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by VCCORP on 4/28/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
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
