package zhulin.project;

import java.util.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@XmlRootElement
@Embeddable
public class DeviceStatus {
	private int temperature;
	private Date createdDate;
	
	private DeviceStatus(){
	}
	
	public DeviceStatus(int temperature){
		this.temperature=temperature;
		this.createdDate=new Date();
	}
	
	/*@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment",strategy="increment")
	public int getId(){
		return this.id;
	}
	
	private void setId(int id){
		this.id=id;
	}*/
	
	public int getTemperature(){
		return this.temperature;
	}
	
	private void setTemperature(int temperature){
		this.temperature=temperature;
	}
	
	public Date getCreatedDate(){
		return this.createdDate;
	}
	
	private void setCreatedDate(Date createdDate){
		this.createdDate=createdDate;
	}
}
