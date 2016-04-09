package zhulin.project.dm.dao;

import java.util.*;

import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import zhulin.project.dm.DeviceInfo;

@Entity
public class Device implements Comparable<Device> {
	public static enum DeviceType{
		INFLAMER,
		BOILER
	}
	
	private int id;
	private String name;
	@Enumerated
	private Device.DeviceType type;
	private int memory;
	@Embedded
	private Location location;
	private List<DeviceStatus> statuses=new ArrayList<>();
	
	
	Device(){
	}
	
	public Device(DeviceInfo deviceInfo){
		this(deviceInfo.name,deviceInfo.type,deviceInfo.memory,deviceInfo.location);
	}
	
	public Device(String name,Device.DeviceType type,int memory, Location location){
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
	
	public Device.DeviceType getType(){
		return this.type;
	}
	
	private void setType(Device.DeviceType type){
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
	
	//@ElementCollection(fetch=FetchType.EAGER)
	@OneToMany(cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
	public List<DeviceStatus> getStatuses(){
		return this.statuses;
	}
	
	private void setStatuses(List<DeviceStatus> statuses){
		this.statuses=statuses;
	}

	@Override
	public int compareTo(Device d) {
		// TODO Auto-generated method stub
		return this.id-d.id;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Device)){
			return false;
		}
		
		Device temp=(Device)obj;
		
		if (this.id==temp.id&&this.name==temp.name&&this.memory==temp.memory&&this.type==temp.type
				&&this.location.equals(location)&&this.statuses.size()==temp.statuses.size()){
			return true;
		}
		
		return false;
	}

}
