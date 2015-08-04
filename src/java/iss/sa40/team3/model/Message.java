/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iss.sa40.team3.model;

/**
 *
 * @author jiandong
 */
public class Message {
    private int type;
    private String Messages;

    public String getMessages() {
        return Messages;
    }

    public void setMessages(String Messages) {
        this.Messages = Messages;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
