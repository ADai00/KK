package edu.hnie.kk.domain;

public class GroupChatGroup {
    private int id;
    private String name;

    public GroupChatGroup() {
    }

    public GroupChatGroup(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
