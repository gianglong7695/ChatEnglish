package gianglong.app.chat.longchat.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by VCCORP on 11/14/2017.
 */

public class MyUtils {
    public static DisplayMetrics displayMetrics;


    public static LinearLayoutManager getLinearLayoutManager(Context context, int type){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        if(type == 1){
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }else{
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        }

        return layoutManager;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Size getDisplayDpSize(Activity activity){
        try {
            if(displayMetrics == null){

                displayMetrics = activity.getResources().getDisplayMetrics();
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;

                return new Size(width, height);
            }else{
                return new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
            }
        }catch (Exception e){
            Logs.e(e.toString());
        }
        return null;
    }


    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public float getWidthScreenDp(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / displayMetrics.density;
    }


    public static float convertPxToDp(Context context){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1080, r.getDisplayMetrics());
    }


    public static float convertDpToPx(Context context, float dp) {
        Resources res = context.getResources();

        return dp * (res.getDisplayMetrics().densityDpi / 160f);
    }

    public static void backToTop(NestedScrollView nestedScrollView){
        nestedScrollView.scrollTo(0,0);
    }

    public static void callHotLine(Context context){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0987654321"));
        context.startActivity(intent);
    }


    public static void hideShowAnimation(final View v, int type, int duration){
        if(type == -1){
            v.animate().alpha(0.0f).setDuration(duration).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    v.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }else {
            v.animate().alpha(1.0f).setDuration(duration).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    v.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

    }


}
