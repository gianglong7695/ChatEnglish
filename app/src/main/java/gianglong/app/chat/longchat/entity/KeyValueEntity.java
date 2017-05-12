package gianglong.app.chat.longchat.entity;

/**
 * Created by VCCORP on 5/12/2017.
 */

public class KeyValueEntity {
    private String key;
    private String value;


    public KeyValueEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "KeyValueEntity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
