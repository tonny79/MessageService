package zhulin.project;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Utils {
	public static SessionFactory sessionFactory;
	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			ex.printStackTrace();
		}
	}
	
	private Utils(){
	}
}
