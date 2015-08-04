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
public class VerifyChosenSetMessage extends Message {

    public int getType() {
        return type;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPosition1() {
        return position1;
    }

    public void setPosition1(int position1) {
        this.position1 = position1;
    }

    public int getCardId1() {
        return cardId1;
    }

    public void setCardId1(int cardId1) {
        this.cardId1 = cardId1;
    }

    public int getPosition2() {
        return position2;
    }

    public void setPosition2(int position2) {
        this.position2 = position2;
    }

    public int getCardId2() {
        return cardId2;
    }

    public void setCardId2(int cardId2) {
        this.cardId2 = cardId2;
    }

    public int getPosition3() {
        return position3;
    }

    public void setPosition3(int position3) {
        this.position3 = position3;
    }

    public int getCardId3() {
        return cardId3;
    }

    public void setCardId3(int cardId3) {
        this.cardId3 = cardId3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    private int type=3;
    private int gameId;
    private int position1;
    private int cardId1;
    private int position2;
    private int cardId2;
    private int position3;
    private int cardId3;
    private String email;
        
}
