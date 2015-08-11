/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iss.sa40.team3.websocket;

/**
 *
 * @author jiandong
 */
import iss.sa40.team3.model.ChatMessage;
import iss.sa40.team3.model.Message;
import iss.sa40.team3.model.VerifyChosenSetMessage;
import java.io.StringReader;
import java.util.Date;
 
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
 
public class MessageDecoder implements Decoder.Text<Message> {
	@Override
	public void init(final EndpointConfig config) {
	}
 
	@Override
	public void destroy() {
	}
 
	@Override
	public Message decode(final String textMessage) throws DecodeException {
            
            Message remessage;    
            JsonObject obj = Json.createReader(new StringReader(textMessage))
				.readObject();
            switch(obj.getInt("type"))
            {
                case 0:remessage=decodechatMessage(obj);
                    break;
                case 1:remessage=decodeVerifychosensetmessage(obj);
                    break;
                default:
                    remessage=null;
                    break;
            }
            return remessage;
	}
 
	@Override
	public boolean willDecode(final String s) {
		return true;
	}
        public ChatMessage decodechatMessage(JsonObject obj){
            ChatMessage cm = new ChatMessage();
            cm.setType(obj.getInt("type"));
            cm.setChatmessage(obj.getString("message"));
            cm.setSender(obj.getString("sender"));
            cm.setReceivedtime(new Date());
            return cm;
        }
        public VerifyChosenSetMessage decodeVerifychosensetmessage(JsonObject obj){
            VerifyChosenSetMessage vcsm=new VerifyChosenSetMessage();
            vcsm.setType(obj.getInt("type"));
            vcsm.setEmail(obj.getString("email"));
            vcsm.setGameId(obj.getInt("gameId"));
            vcsm.setPosition1(obj.getInt("position1"));
            vcsm.setPosition2(obj.getInt("position2"));
            vcsm.setPosition3(obj.getInt("position3"));
            return vcsm;
        }
        
        
}
