package zhulin.project;

import java.text.DateFormat;
import java.util.*;

public class Message {
	private String id;
	private Date date;
	private String message;
	
	private Message(){
	}
	
	public Message(String id,Date date,String message){
		this.id=id;
		this.date=date;
		this.message=message;
	}
	
	// Properties
	private void setId(String id){
		this.id=id;
	}
		
	public String getId(){
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
}
