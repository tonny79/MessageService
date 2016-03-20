package zhulin.project;

import java.text.DateFormat;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ZLMessage")
public class Message implements Comparable<Message>{
	private int id;
	private Date date;
	private String message;
	
	private Message(){
	}
	
	public Message(Date date,String message){
		this.date=date;
		this.message=message;
	}
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment",strategy="increment")
	public int getId(){
		return this.id;
	}
	
	private void setId(int id){
		this.id=id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
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
