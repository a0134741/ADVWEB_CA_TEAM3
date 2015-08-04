/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iss.sa40.team3.model;

import java.util.Date;

/**
 *
 * @author jiandong
 */
public class ChatMessage extends Message {
    private String type="0";
    private String chatmessage;
    private String sender;
    private Date receivedtime;

    public String getType() {
        return type;
    }

    public String getChatmessage() {
        return chatmessage;
    }

    public void setChatmessage(String chatmessage) {
        this.chatmessage = chatmessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getReceivedtime() {
        return receivedtime;
    }

    public void setReceivedtime(Date receivedtime) {
        this.receivedtime = receivedtime;
    }

}