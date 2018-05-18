package edu.hnie.kk.domain;

public class User {
    private int id;
    private String nickname;//昵称
    private int icon;//头像图标
    private String sign;//个性签名
    private String phone;//手机号
    private int age;//年龄
    private String sex;//性别
    private String address;//地址

    public User() {
    }

    public User(String nickname, int icon) {
        this.nickname = nickname;
        this.icon = icon;
    }

    public User(String nickname, int icon, String sign) {
        this.nickname = nickname;
        this.icon = icon;
        this.sign = sign;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
