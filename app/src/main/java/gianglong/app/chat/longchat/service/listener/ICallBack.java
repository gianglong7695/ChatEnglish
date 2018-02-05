package gianglong.app.chat.longchat.service.listener;

import android.support.v4.app.Fragment;

/**
 *
 * styleAnimation: 1 = from right, 2 = from bottom, 3 = no animation
 */

public interface ICallBack {
    void onAddFragment(Fragment fragment, int... styleAnimation);

}
