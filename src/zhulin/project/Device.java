package zhulin.project;

import java.awt.Point;

import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ZLDevice")
public class Device implements Comparable<Device> {
	public static enum DeviceType{
		INFLAMER,
		BOILER
	}
	
	private int id;
	private String name;
	@Enumerated
	private DeviceType type;
	private int memory;
	@Embedded
	private Location location;
	
	
	private Device(){
	}
	
	public Device(String name,DeviceType type,int memory, Location location){
		this.name=name;
		this.type=type;
		this.memory=memory;
		this.location=location;
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
	
	@Basic(optional=false)
	public String getName(){
		return name;
	}
	
	private void setName(String name){
		this.name=name;
	}
	
	public DeviceType getType(){
		return this.type;
	}
	
	private void setType(DeviceType type){
		this.type=type;
	}
	
	public int getMemory(){
		return this.memory;
	}
	
	public void setMemory(int memory){
		this.memory=memory;
	}
	
	public Location getLocation(){
		return this.location;
	}
	
	public void setLocation(Location location){
		this.location=location;
	}

	@Override
	public int compareTo(Device d) {
		// TODO Auto-generated method stub
		return this.id-d.id;
	}

}
