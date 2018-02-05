package gianglong.app.chat.longchat.utils;

import android.app.Application;

import io.realm.Realm;


/**
 * Created by Giang Long on 2/23/2017.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
