package zhulin.project;

import java.text.DateFormat;
import java.util.*;

public class Message implements Comparable<Message>{
	private int id;
	private Date date;
	private String message;
	
	private Message(){
	}
	
	public Message(int id,Date date,String message){
		this.id=id;
		this.date=date;
		this.message=message;
	}
	
	// Properties
	private void setId(int id){
		this.id=id;
	}
		
	public int getId(){
		return this.id;
	}
	
	public Date getDate(){
		return date;
	}
	
	private void setDate(Date date){
		this.date=date;
	}
	
	public String getMessage(){
		return message;
	}
	
	private void setMessage(String message){
		this.message=message;
	}
	
	@Override
	public String toString(){
		DateFormat df=DateFormat.getDateInstance(DateFormat.LONG,Locale.US);
		return df.format(date)+" : "+message;
	}

	@Override
	public int compareTo(Message m) {
		return this.id-m.id;
	}
}
