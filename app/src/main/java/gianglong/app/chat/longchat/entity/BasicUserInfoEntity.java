package gianglong.app.chat.longchat.entity;

import android.net.Uri;

/**
 * Created by giang on 5/3/2017.
 */

public class BasicUserInfoEntity {
    private String providerId;
    private String uid;
    private String email;
    private Uri photoUrl;
    private boolean isEmailVerified ;


    public static BasicUserInfoEntity getInstance() {
        if (GlobalVars.getBasicUserInfoEntity() == null) {

            GlobalVars.setBasicUserInfoEntity(new BasicUserInfoEntity());
        }

        return GlobalVars.getBasicUserInfoEntity();
    }

    public BasicUserInfoEntity() {
        GlobalVars.setBasicUserInfoEntity(this);
    }


    public BasicUserInfoEntity(String providerId, String uid, String email, Uri photoUrl, boolean isEmailVerified) {
        this.providerId = providerId;
        this.uid = uid;
        this.email = email;
        this.photoUrl = photoUrl;
        this.isEmailVerified = isEmailVerified;
    }


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}
