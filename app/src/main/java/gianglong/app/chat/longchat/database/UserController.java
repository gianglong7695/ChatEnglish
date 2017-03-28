package gianglong.app.chat.longchat.database;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import gianglong.app.chat.longchat.entity.UserEntity;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Giang Long on 2/22/2017.
 */

public class UserController {
    private static UserController instance;
    private final Realm realm;

    public UserController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static UserController with(Fragment fragment) {

        if (instance == null) {
            instance = new UserController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static UserController with(Activity activity) {

        if (instance == null) {
            instance = new UserController(activity.getApplication());
        }
        return instance;
    }

    public static UserController with(Application application) {

        if (instance == null) {
            instance = new UserController(application);
        }
        return instance;
    }

    public static UserController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.setAutoRefresh(true);
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<UserEntity> getBooks() {

        return realm.where(UserEntity.class).findAll();
    }

    //query a single item with the given id
    public UserEntity getBook(String id) {

        return realm.where(UserEntity.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasUsers() {
        return !realm.isEmpty();
    }

    //query example
    public RealmResults<UserEntity> queryedUser() {

        return realm.where(UserEntity.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
