package gianglong.app.chat.longchat.entity;

/**
 * Created by giang on 4/30/2017.
 */

public class MessageItemEntity {
    private String avatarUrl;
    private String message;
    private String time;
    private int statusType;
    private int typeView;
    private boolean isHideTime;
    private String senderID;

    public MessageItemEntity() {
    }

    public MessageItemEntity(String avatarUrl, String message, String time, int statusType, int typeView, boolean isHideTime, String senderID) {
        this.avatarUrl = avatarUrl;
        this.message = message;
        this.time = time;
        this.statusType = statusType;
        this.typeView = typeView;
        this.isHideTime = isHideTime;
        this.senderID = senderID;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
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

    @Override
    public String toString() {
        return "MessageItemEntity{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", statusType=" + statusType +
                ", typeView=" + typeView +
                ", isHideTime=" + isHideTime +
                ", senderID='" + senderID + '\'' +
                '}';
    }
}
