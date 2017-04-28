package gianglong.app.chat.longchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import gianglong.app.chat.longchat.R;
import gianglong.app.chat.longchat.activity.ChatActivity;
import gianglong.app.chat.longchat.entity.MessageEntity;

/**
 * Created by VCCORP on 4/28/2017.
 */

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.MyViewHolder> {
    Context context;
    ArrayList<MessageEntity> alListMessage;
    ImageLoader imageLoader;
    DisplayImageOptions options;


    public ListMessageAdapter(Context context, ArrayList<MessageEntity> alListMessage) {
        this.context = context;
        this.alListMessage = alListMessage;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(0) {
                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                        imageAware.setImageBitmap(bitmap);
                        if (loadedFrom == LoadedFrom.NETWORK) {
                            animate(imageAware.getWrappedView(), 300);
                        }
                    }
                }).build();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user_message, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageEntity obj = alListMessage.get(position);
        if(obj != null){
            if(obj.getAvatarUrl() != null){
                if(obj.getAvatarUrl().equals("1")){
                    holder.civAvatar.setImageResource(R.drawable.avatar_male_default);
                }else{
                    holder.civAvatar.setImageResource(R.drawable.avatar_female_default);
                }
            }


            if(obj.getName() != null){
                holder.tvName.setText(obj.getName());
            }


            if(obj.getLastMsg() != null){
                holder.tvMesssage.setText(obj.getLastMsg());
            }

            if(obj.getTime() != null){
                holder.tvTime.setText(obj.getTime());
            }

            if(obj.isUserActive()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.vUserStatus.setBackground(context.getDrawable(R.drawable.bg_status_online));
                }else{
                    holder.vUserStatus.setBackgroundResource(R.drawable.bg_status_online);
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.vUserStatus.setBackground(context.getDrawable(R.drawable.bg_status_offline));
                }else{
                    holder.vUserStatus.setBackgroundResource(R.drawable.bg_status_offline);
                }
            }

            if(obj.isMine()){
                holder.tvSender.setVisibility(View.VISIBLE);
            }else{
                holder.tvSender.setVisibility(View.GONE);
            }



            holder.layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, ChatActivity.class);
                    context.startActivity(it);
                }
            });








        }
    }

    @Override
    public int getItemCount() {
        return alListMessage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        View vUserStatus;
        TextView tvName, tvMesssage, tvSender, tvTime;
        ImageView ivMessageStatus;
        LinearLayout layout_item;

        public MyViewHolder(View v) {
            super(v);

            civAvatar = (CircleImageView) v.findViewById(R.id.civAvatar);
            vUserStatus = v.findViewById(R.id.vUserStatus);
            tvName = (TextView) v.findViewById(R.id.tvName);
            tvMesssage = (TextView) v.findViewById(R.id.tvMesssage);
            tvSender = (TextView) v.findViewById(R.id.tvSender);
            tvTime = (TextView) v.findViewById(R.id.tvTime);
            ivMessageStatus = (ImageView) v.findViewById(R.id.ivMessageStatus);
            layout_item = (LinearLayout) v.findViewById(R.id.layout_item);

        }
    }
}
