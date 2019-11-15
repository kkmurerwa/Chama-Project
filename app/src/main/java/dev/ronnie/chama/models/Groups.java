package dev.ronnie.chama.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Groups implements Parcelable {

    private String group_name;
    private String creator_id;
    private String group_id;
    private Activities activities;
    private Projects projects;
    private Financials financials;
    private List<String> users;
    private List<ChatMessage> chatroom_messages;

    public Groups() {
    }

    public Groups(String group_name, String creator_id, String group_id, Activities activities, Projects projects, Financials financials, List<String> users, List<ChatMessage> chatroom_messages) {
        this.group_name = group_name;
        this.creator_id = creator_id;
        this.group_id = group_id;
        this.activities = activities;
        this.projects = projects;
        this.financials = financials;
        this.users = users;
        this.chatroom_messages = chatroom_messages;
    }


    protected Groups(Parcel in) {
        group_name = in.readString();
        creator_id = in.readString();
        group_id = in.readString();
        users = in.createStringArrayList();
    }

    public static final Creator<Groups> CREATOR = new Creator<Groups>() {
        @Override
        public Groups createFromParcel(Parcel in) {
            return new Groups(in);
        }

        @Override
        public Groups[] newArray(int size) {
            return new Groups[size];
        }
    };

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public Activities getActivities() {
        return activities;
    }

    public void setActivities(Activities activities) {
        this.activities = activities;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public Financials getFinancials() {
        return financials;
    }

    public void setFinancials(Financials financials) {
        this.financials = financials;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<ChatMessage> getChatroom_messages() {
        return chatroom_messages;
    }

    public void setChatroom_messages(List<ChatMessage> chatroom_messages) {
        this.chatroom_messages = chatroom_messages;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "group_name='" + group_name + '\'' +
                ", creator_id='" + creator_id + '\'' +
                ", group_id='" + group_id + '\'' +
                ", activities=" + activities +
                ", projects=" + projects +
                ", financials=" + financials +
                ", users=" + users +
                ", chatroom_messages=" + chatroom_messages +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group_name);
        dest.writeString(creator_id);
        dest.writeString(group_id);
        dest.writeStringList(users);
    }
}
