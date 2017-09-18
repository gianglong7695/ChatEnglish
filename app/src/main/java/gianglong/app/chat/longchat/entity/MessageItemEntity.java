package gianglong.app.chat.longchat.entity;

/**
 * Created by giang on 4/30/2017.
 */

public class MessageItemEntity {
    private String message;
    private String time;
    private int statusType;
    private boolean isHideAvatar;
    private boolean isHideTime;
    private String senderID;
    private String receiverID;

    public MessageItemEntity() {
    }

    public MessageItemEntity(String message, String time, int statusType, boolean isHideAvatar, boolean isHideTime, String senderID, String receiverID) {
        this.message = message;
        this.time = time;
        this.statusType = statusType;
        this.isHideAvatar = isHideAvatar;
        this.isHideTime = isHideTime;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }

    public boolean isHideAvatar() {
        return isHideAvatar;
    }

    public void setHideAvatar(boolean hideAvatar) {
        isHideAvatar = hideAvatar;
    }

    public boolean isHideTime() {
        return isHideTime;
    }

    public void setHideTime(boolean hideTime) {
        isHideTime = hideTime;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    @Override
    public String toString() {
        return "MessageItemEntity{" +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", statusType=" + statusType +
                ", typeView=" + isHideAvatar +
                ", isHideTime=" + isHideTime +
                ", senderID='" + senderID + '\'' +
                '}';
    }
}
