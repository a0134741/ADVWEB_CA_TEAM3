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
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {
	@Override
	public void init(final EndpointConfig config) {
	}
 
	@Override
	public void destroy() {
	}
 
	@Override
	public String encode(final ChatMessage chatMessage) throws EncodeException {
		return Json.createObjectBuilder()
                                .add("type",chatMessage.getType())
				.add("message", chatMessage.getChatmessage())
				.add("sender", chatMessage.getSender())
				.add("received", chatMessage.getReceivedtime().toString())
                                .build()
				.toString();
	}
}
