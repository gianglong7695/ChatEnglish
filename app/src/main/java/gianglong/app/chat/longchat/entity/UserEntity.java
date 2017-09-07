package gianglong.app.chat.longchat.entity;

import java.io.Serializable;

/**
 * Created by Giang Long on 2/21/2017.
 */

public class UserEntity implements Serializable {

    private String id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String country;
    private String avatar;
    private String introdution;
    private double rate;
    private int reviewers;





    public static UserEntity getInstance() {
        if (GlobalVars.getBasicUserInfoEntity() == null) {

            GlobalVars.setUserEntity(new UserEntity());
        }

        return GlobalVars.getUserEntity();
    }

    public UserEntity() {
        GlobalVars.setUserEntity(this);
        //Default for DataSnapshot.getValue(User.class)
    }


    public UserEntity(String id, String name, String email, String password, String gender, String country, String avatar, String introdution, double rate, int reviewers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.country = country;
        this.avatar = avatar;
        this.introdution = introdution;
        this.rate = rate;
        this.reviewers = reviewers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntrodution() {
        return introdution;
    }

    public void setIntrodution(String introdution) {
        this.introdution = introdution;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getReviewers() {
        return reviewers;
    }

    public void setReviewers(int reviewers) {
        this.reviewers = reviewers;
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", avatar='" + avatar + '\'' +
                ", introdution='" + introdution + '\'' +
                ", rate=" + rate +
                ", reviewers=" + reviewers +
                '}';
    }

}
