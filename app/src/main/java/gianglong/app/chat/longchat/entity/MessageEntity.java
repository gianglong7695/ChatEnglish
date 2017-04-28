package gianglong.app.chat.longchat.entity;

/**
 * Created by VCCORP on 4/28/2017.
 */

public class MessageEntity {
    private String avatarUrl;
    private String name;
    private String time;
    private String lastMsg;
    private boolean isMine;
    private int msgStatus;
    private boolean isUserActive;


    public MessageEntity(String avatarUrl, String name, String time, String lastMsg, boolean isMine, int msgStatus, boolean isUserActive) {
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.time = time;
        this.lastMsg = lastMsg;
        this.isMine = isMine;
        this.msgStatus = msgStatus;
        this.isUserActive = isUserActive;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public int getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(int msgStatus) {
        this.msgStatus = msgStatus;
    }

    public boolean isUserActive() {
        return isUserActive;
    }

    public void setUserActive(boolean userActive) {
        isUserActive = userActive;
    }
}
