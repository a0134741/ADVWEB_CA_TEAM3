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
import iss.sa40.team3.model.CreategameMessage;
import iss.sa40.team3.model.Message;
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
		ChatMessage chatMessage = new ChatMessage();
                CreategameMessage cgm=new CreategameMessage();
		JsonObject obj = Json.createReader(new StringReader(textMessage))
				.readObject();
                if(obj.getString("type").equals("1"))
                {
                    cgm.setType(obj.getString("type"));
                    cgm.setTitle(obj.getString("title"));
                    cgm.setDuration(obj.getString("duration"));
                    cgm.setMaxPlayers(obj.getInt("maxplayers"));
                    return cgm;
                }
		return null;
	}
 
	@Override
	public boolean willDecode(final String s) {
		return true;
	}
}
