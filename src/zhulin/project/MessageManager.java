package zhulin.project;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.w3c.dom.*;
import javax.xml.parsers.*;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


@Path("/messages")
public class MessageManager {
	private static List<Message> messages = null;

	private void loadMessages() {
		// Load the messages from DB
		try {
			org.hibernate.Session session = Utils.sessionFactory.getCurrentSession();
			session.beginTransaction();
			messages = session.createCriteria(Message.class).list();
			session.getTransaction().commit();
			System.out.println("Retrieved messages from database:" + messages.size());
		} catch (Throwable ex) {
			System.err.println("Can't retrieve messages!" + ex);
			ex.printStackTrace();
			// Initialize the message queue anyway
			if (messages == null) {
				messages = new ArrayList<Message>();
			}
		}
	}

	public Message[] getMessages() {
		this.loadMessages();

		Message[] result = new Message[messages.size()];
		Iterator<Message> iter = messages.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iter.next();
		}

		Arrays.sort(result);

		return result;
	}
	
	public int sendMessage(String text) {
		if (text != null && !text.trim().equals("")) {
			// Refresh the message list
			this.loadMessages();

			Message message = new Message(new Date(), text);
			MessageManager.messages.add(message);

			// Save to database
			try {
				org.hibernate.Session session = Utils.sessionFactory.getCurrentSession();
				session.beginTransaction();
				session.save(message);
				session.getTransaction().commit();
			} catch (Exception e) {
				System.err.println("Can't save the message to database." + e);
				e.printStackTrace();
			}
			
			return message.getId();
		}
		
		return 0;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<MessageInfo> getMessagesXML(){
		Message[] messages=getMessages();
		List<MessageInfo> result=new ArrayList<MessageInfo>(messages.length);
		
		for(int i=0;i<messages.length;i++){
			result.add(new MessageInfo(messages[i].getId(),messages[i].getMessage()));
		}
		
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public List<MessageInfo> addMessagesXML(List<MessageInfo> msgInfoes){
		for(MessageInfo msgInfo:msgInfoes){
			//Get the message id
			msgInfo.id=this.sendMessage(msgInfo.message);
		}
		
		return msgInfoes;
	}
	
	@GET
	@Path("/message")
	@Produces(MediaType.APPLICATION_XML)
	public MessageInfo getMessageText(@QueryParam("id") int id){
		System.out.println("Recieve the XML request for message: "+id);
		Message[] messages=getMessages();
		for(int i=0;i<messages.length;i++){
			if(messages[i].getId()==id){
				return new MessageInfo(messages[i].getId(),messages[i].getMessage());
			}
		}
		
		//Return the first message by default
		return new MessageInfo(messages[0].getId(),messages[0].getMessage());
	}
	
	@POST
	@Path("/message")
	@Consumes(MediaType.APPLICATION_XML)
	public MessageInfo addMessageXML(MessageInfo msgInfo){
		System.out.println("Recieve message:"+msgInfo.message);
		msgInfo.id=this.sendMessage(msgInfo.message);
		return msgInfo;
		
	}
	
	/*public JsonObject getMessagesJSON() {
	Message[] messages = getMessages();
	
	JsonObjectBuilder resultBuilder=Json.createObjectBuilder()
			.add("size", messages.length);
	
	JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
	for (int i = 0; i < messages.length; i++) {
		arrayBuilder.add(Json.createObjectBuilder()
				.add("id", messages[i].getId())
				.add("message", messages[i].getMessage()));
	}
	resultBuilder.add("messages", arrayBuilder);
	
	return resultBuilder.build();
    }*/
}
