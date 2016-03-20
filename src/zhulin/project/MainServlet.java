package zhulin.project;

import java.io.*;
import java.util.*;
import java.text.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class MainServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	private static List<Message> chatHistory=null;
	
	private static void initChatHistory(){
		//Load the chat history from DB
			try{
			    org.hibernate.Session session=sessionFactory.getCurrentSession();
			    session.beginTransaction();
			    chatHistory=session.createCriteria(Message.class).list();
			    session.getTransaction().commit();
			    System.out.println("Initialize the chat history done:"+chatHistory.size());
			}catch(Throwable ex){
				System.err.println("Can't retrieve the chat history."+ex);
				ex.printStackTrace();
				//Initialize the chat history anyway
				chatHistory=new ArrayList<Message>();
			}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		try {
			out.println("<html><head><title>Cool Chat System</title>");
			out.println("</head>");
			out.println("<body>" + "<h1>Welcome!</h1>");
			out.println("<form action=\"./\" method=\"post\" name=\"mainForm\">");
			out.println("Chat: <input type=\"text\" name=\"chat\" size=\"100\" /> <input type=\"submit\" value=\"Submit\" /> <br />");
			out.println("</form>");
			if(chatHistory==null){
				MainServlet.initChatHistory();
			}
			for (Iterator<Message> iter=chatHistory.iterator();iter.hasNext();){
				out.println(iter.next().toString()+"<br />");
			}	
			out.println("</body></html>");
		} catch (Exception e) {
			out.println("Error:" + e.getMessage());
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String chat=req.getParameter("chat");
		if (!chat.trim().equals("")){
			//Date currentDate=new Date();
			Message message=new Message(new Date(),chat);
			chatHistory.add(message);
			
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
		res.sendRedirect("./");
	}
}
