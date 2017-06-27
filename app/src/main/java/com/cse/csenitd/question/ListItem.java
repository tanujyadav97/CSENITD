package com.cse.csenitd.question;

/**
 * Created by 15121 on 6/22/2017.
 */
public class ListItem {

    private String time;
    private String votes;
    private String topic;
    private String ques;
    private String tags;
    private String username;
    private String accepted;


    public String gettime() {
        return time;
    }

    public void settime(String time) {
        this.time = time;
    }

    public String getvotes() {
        return votes;
    }

    public void setvotes(String votes) {
        this.votes = votes;
    }

    public String gettopic() {
        return topic;
    }

    public void settopic(String topic) {
        this.topic = topic;
    }

    public String getques() {
        return ques;
    }

    public void setques(String ques) {
        this.ques = ques;
    }

    public String gettags() {
        return tags;
    }

    public void settags(String tags) {
        this.tags = tags;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getaccepted() {
        return accepted;
    }

    public void setaccepted(String accepted) {
        this.accepted = accepted;
    }

}
