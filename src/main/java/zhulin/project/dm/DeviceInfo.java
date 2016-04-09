package zhulin.project.dm;

import javax.xml.bind.annotation.XmlRootElement;

import zhulin.project.dm.dao.Device;
import zhulin.project.dm.dao.Location;

@XmlRootElement
public class DeviceInfo {
	public int id=-1;
	public String name;
	public int memory;
	public Device.DeviceType type;
	public Location location;
	
	public DeviceInfo(){
	}
	
	public DeviceInfo(String name,int memory,Device.DeviceType type, Location location){
		this.name=name;
		this.memory=memory;
		this.type=type;
		this.location=location;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof DeviceInfo)){
			return false;
		}
		
		DeviceInfo temp=(DeviceInfo)obj;
		
		if (this.id==temp.id&&this.name==temp.name&&this.memory==temp.memory&&this.type==temp.type
				&&this.location.equals(temp.location)){
			return true;
		}
		
		return false;
	}
}
