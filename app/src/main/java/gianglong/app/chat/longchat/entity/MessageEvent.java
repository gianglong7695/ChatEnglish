package gianglong.app.chat.longchat.entity;

/**
 * Created by VCCORP on 9/7/2017.
 */

public class MessageEvent {
    private String message;
    private UserEntity userEntity;


    public MessageEvent(String message, UserEntity userEntity) {
        this.message = message;
        this.userEntity = userEntity;
    }


    public MessageEvent(String mMessage) {
        this.message = mMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mMessage) {
        this.message = mMessage;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
