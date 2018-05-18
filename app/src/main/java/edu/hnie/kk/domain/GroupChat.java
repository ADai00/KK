package edu.hnie.kk.domain;

public class GroupChat {

    private int id;
    private String name;
    private int icon;//群头像
    private String description;//描述

    public GroupChat(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public GroupChat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
