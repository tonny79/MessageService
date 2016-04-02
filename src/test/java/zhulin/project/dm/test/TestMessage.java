package zhulin.project.dm.test;

import java.util.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import zhulin.project.dm.MessageInfo;

public class TestMessage {

	public static void main(String[] args) {
		Client client=ClientBuilder.newClient();
		WebTarget base=client.target("http://192.168.50.128/messageservice/rest/messages");
		
		//Query one message via xml
		MessageInfo message=base.path("message").request(MediaType.APPLICATION_XML).get(MessageInfo.class);
		System.out.println(message.message);
		
		//Send one message via xml
		message.id=0;
		message.message="A message from TestMessage client!";
		message=base.path("message").request(MediaType.APPLICATION_XML).post(Entity.xml(message),MessageInfo.class);
		System.out.println("Send message and recieved id="+message.id);
		
		//Query multiple messages via XML
		List<MessageInfo> messages=base.request(MediaType.APPLICATION_XML).get(new GenericType<List<MessageInfo>>(){});
		System.out.println("Recieved "+messages.size()+" messages from server!");
		
		//Send multiple messages via XML
		messages=new ArrayList<MessageInfo>(2);
		messages.add(new MessageInfo(0,"Test 1 from Client!"));
		messages.add(new MessageInfo(0,"Test 2 from Client!"));
		base.request(MediaType.APPLICATION_XML).post(Entity.xml(messages));
		System.out.println("Recieved ID="+messages.get(0).id+","+messages.get(1).id);
		
	}

}
