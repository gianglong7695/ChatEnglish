package gianglong.app.chat.longchat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import gianglong.app.chat.longchat.entity.UserEntity;


/**
 * Created by AzteC on 03/01/2017.
 */
public class PreferenceUtil {
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public PreferenceUtil(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        gson = new Gson();
    }


    public void saveUser(UserEntity userEntity) {
        // Saving basic user entity to preference
        pref = context.getSharedPreferences(Constants.KEY_USER, Context.MODE_PRIVATE);
        editor = pref.edit();
        String json = gson.toJson(userEntity);
        editor.putString(Constants.KEY_USER_ENTITY, json);
        editor.commit();
    }


    public UserEntity getUser() {
        String json = pref.getString(Constants.KEY_USER_ENTITY, DataNotify.NO_DATA);
        return gson.fromJson(json, UserEntity.class);
    }

}
