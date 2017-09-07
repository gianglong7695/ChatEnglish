package gianglong.app.chat.longchat.entity;

/**
 * Created by VCCORP on 9/7/2017.
 */

public class MessageEvent {
    private String mMessage;

    public MessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "mMessage='" + mMessage + '\'' +
                '}';
    }
}
