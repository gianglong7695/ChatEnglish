package gianglong.app.chat.longchat.database;


import io.realm.Realm;

/**
 * Created by apple on 1/13/18.
 */

public class RealmHandle {
    public static Realm realm = Realm.getDefaultInstance();

//    public static void addNews(News news){
//        realm.beginTransaction();
//        realm.copyToRealm(news);
//        realm.commitTransaction();
//    }
//
//    public static List<News> getListNews(){
//        return realm.where(News.class).findAll();
//    }
}
