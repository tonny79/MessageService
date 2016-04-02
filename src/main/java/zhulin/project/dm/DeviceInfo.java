package zhulin.project.dm;

import javax.xml.bind.annotation.XmlRootElement;

import zhulin.project.dm.dao.Device;
import zhulin.project.dm.dao.Location;

@XmlRootElement
public class DeviceInfo {
	public int id;
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
}
