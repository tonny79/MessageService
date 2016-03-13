package zhulin.project;

import java.util.*;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MessageManager {
	private static SessionFactory sessionFactory;
	static{
		try{
			sessionFactory=new Configuration().configure().buildSessionFactory();
		}catch(Throwable ex){
			System.err.println("Initial SessionFactory creation failed."+ex);
			ex.printStackTrace();
			//throw new ExceptionInInitializerError(ex);
		}
	}
	private static List<Message> messages=null;
	
	private void loadMessages() {
		// Load the messages from DB
		try {
			org.hibernate.Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			messages = session.createCriteria(Message.class).list();
			session.getTransaction().commit();
			System.out.println("Retrieved messages from database:"
					+ messages.size());
		} catch (Throwable ex) {
			System.err.println("Can't retrieve messages!" + ex);
			ex.printStackTrace();
			// Initialize the message queue anyway
			if (messages == null) {
				messages = new ArrayList<Message>();
			}
		}
	}
	
	public Message[] getMessages(){
		this.loadMessages();
		
		Message[] result=new Message[messages.size()];
		Iterator<Message> iter=messages.iterator();
		for(int i=0;i<result.length;i++){
			result[i]=iter.next();
		}
		
		Arrays.sort(result);
		
		return result;
	}
	
	public void sendMessage(String text){
		//Refresh the message list
		this.loadMessages();
		
		Message message=new Message(messages.size(),new Date(),text);
		MessageManager.messages.add(message);
		
		//Save to database
		try{
		    org.hibernate.Session session=sessionFactory.getCurrentSession();
		    session.beginTransaction();
		    session.save(message);
		    session.getTransaction().commit();
		}catch(Exception e){
			System.err.println("Can't save the message to database."+e);
			e.printStackTrace();
		}
	}
}
