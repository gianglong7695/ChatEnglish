package gianglong.app.chat.longchat.entity;

/**
 * Created by giang on 5/3/2017.
 */

public class GlobalVars {
    private static BasicUserInfoEntity basicUserInfoEntity;
    private static UserEntity userEntity;

    public static UserEntity getUserEntity() {
        return userEntity;
    }

    public static void setUserEntity(UserEntity userEntity) {
        GlobalVars.userEntity = userEntity;
    }

    public static BasicUserInfoEntity getBasicUserInfoEntity() {
        return basicUserInfoEntity;
    }

    public static void setBasicUserInfoEntity(BasicUserInfoEntity entity) {
        basicUserInfoEntity = entity;
    }
}
