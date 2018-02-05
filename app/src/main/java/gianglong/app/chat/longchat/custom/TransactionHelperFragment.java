package gianglong.app.chat.longchat.custom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import gianglong.app.chat.longchat.R;

/**
 * Created by giang on 2/4/2018.
 */

public class TransactionHelperFragment {
    public static final int SLIDE_RIGHT = 1;
    public static final int SLIDE_BOTTOM = 2;
    private FragmentActivity activity;
    private FragmentManager fragmentManager;
    private int layoutId;

    public TransactionHelperFragment(FragmentActivity activity, int layoutId) {
        this.activity = activity;
        this.layoutId = layoutId;
        fragmentManager = activity.getSupportFragmentManager();
    }

    /**
     * @param fragment       to add fragment
     * @param addToBackStack using for add to back stack
     */
    public void addFragment(Fragment fragment, boolean addToBackStack, int styleAnimation) {
        if (activity.getSupportFragmentManager() != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (styleAnimation == SLIDE_RIGHT) {
                transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.stack_pop, R.anim.stack_push, R.anim.slide_out_to_right);
            } else if (styleAnimation == SLIDE_BOTTOM) {
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.stack_push, R.anim.stack_pop, R.anim.slide_out_bottom);
            }else if (styleAnimation == 0){
                transaction.setCustomAnimations(0,R.anim.stack_pop, 0, R.anim.slide_out_to_right);
            }
            if (addToBackStack) {
//                try {
//                    transaction.add(layoutId, fragment).addToBackStack("1").commit();
//                }catch (Exception e){
//                    transaction.add(layoutId, fragment).addToBackStack("1").commitAllowingStateLoss();
//                }
                transaction.add(layoutId, fragment).addToBackStack("1").commitAllowingStateLoss();
            } else {
//                try {
//                    transaction.add(layoutId, fragment).commit();
//                }catch (Exception e){
//                    transaction.add(layoutId, fragment).commitAllowingStateLoss();
//                }
                transaction.add(layoutId, fragment).commitAllowingStateLoss();
            }
        }
    }

    /**
     * @param fragment       to add fragment
     * @param addToBackStack using for add to back stack
     */
    public void addFragmentWithTag(Fragment fragment, boolean addToBackStack, int styleAnimation, String tag) {
        if (activity.getSupportFragmentManager() != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (styleAnimation == SLIDE_RIGHT) {
                transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.stack_pop, R.anim.stack_push, R.anim.slide_out_to_right);
            } else if (styleAnimation == SLIDE_BOTTOM) {
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.stack_push, R.anim.stack_pop, R.anim.slide_out_bottom);
            }
            if (addToBackStack) {
                transaction.add(layoutId, fragment, tag).addToBackStack("1").commit();
            } else {
                transaction.add(layoutId, fragment, tag).commit();
            }
        }
    }

    /**
     * @param fragmentNew
     * @param addToBackStack
     * @param tag
     */
    public void addFragmentWithTag(Fragment fragmentOld, Fragment fragmentNew, boolean addToBackStack, int styleAnimation, String tag) {
        if (null != activity.getSupportFragmentManager()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (styleAnimation == SLIDE_RIGHT) {
                transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.stack_pop, R.anim.stack_push, R.anim.slide_out_to_right);
            } else if (styleAnimation == SLIDE_BOTTOM) {
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.stack_push, R.anim.stack_pop, R.anim.slide_out_bottom);
            }
//            transaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left, R.anim.left_to_right, R.anim.right_to_left);
            if (addToBackStack) {
                transaction.remove(fragmentOld).add(layoutId, fragmentNew, tag).addToBackStack("1").commit();
            } else {
                transaction.remove(fragmentOld).add(layoutId, fragmentNew, tag).commit();
            }
        }
    }

    /**
     * @param fragment
     * @param addToBackStack
     * @param tag
     */
    public void replaceFragmentWithTag(Fragment fragment, boolean addToBackStack, String tag) {
        if (null != activity.getSupportFragmentManager()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left, R.anim.left_to_right, R.anim.right_to_left);
            if (addToBackStack) {
                transaction.replace(layoutId, fragment, tag).addToBackStack("2").commit();
            } else {
                transaction.replace(layoutId, fragment, tag).commit();
            }
        }
    }

    /**
     * @param fragment
     * @param addToBackStack
     */
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        if (null != activity.getSupportFragmentManager()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
//            transaction.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left, R.anim.left_to_right, R.anim.right_to_left);
            if (addToBackStack) {
                transaction.replace(layoutId, fragment).addToBackStack("3").commit();
            } else {
                transaction.replace(layoutId, fragment).commit();
            }
        }
    }

    /**
     * @param fragment
     * @param addToBackStack
     * @param tag
     */
    public void replaceFragment(Fragment fragment, boolean addToBackStack, String tag) {
        if (null != activity.getSupportFragmentManager()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.stack_push, R.anim.stack_pop, R.anim.slide_out_to_right);
            if (addToBackStack) {
                transaction.replace(layoutId, fragment, tag).addToBackStack("4").commit();
            } else {
                transaction.replace(layoutId, fragment, tag).commit();
            }
        }
    }


    /**
     * Pop the top fragment in BackStack
     */
    public void popTopFragment() {
        if (null != activity.getSupportFragmentManager()) {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        }
    }


    /**
     * @return count
     */
    public int getBackStackEntryCount() {
        return fragmentManager.getBackStackEntryCount();
    }
}
