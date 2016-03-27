package zhulin.project.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import zhulin.project.*;
import zhulin.project.Device.DeviceType;

public class TestDevice {
	public static void main(String[] args){
		//testHibernate();
		testRest();
		
		System.out.println("Test Device Done!");
	}
	
	private static void testHibernate(){
		DeviceManager deviceManager=new DeviceManager();
		Device device=new Device("Test Device",Device.DeviceType.INFLAMER,1024,
				new Location(200,100));
		device.getStatuses().add(new DeviceStatus(100));
		device.getStatuses().add(new DeviceStatus(200));
		deviceManager.addDevice(device);
		device.getStatuses().add(new DeviceStatus(300));
		deviceManager.updateDevice(device.getId());
		
		device=new Device("Test Device2",Device.DeviceType.BOILER,1024,
				new Location(20,50));
		device.getStatuses().add(new DeviceStatus(50));
		deviceManager.addDevice(device);
		
	}
	
	private static void testRest(){
		Client client=ClientBuilder.newClient();
		WebTarget base=client.target("http://192.168.172.141/messageservice/rest/devices");
		
		//Create a device via XML
		DeviceInfo device=new DeviceInfo("Test2",1024,Device.DeviceType.BOILER,new Location(100,100));
		device=base.path("device").request(MediaType.APPLICATION_XML).post(Entity.xml(device),DeviceInfo.class);
		System.out.println("Sent device and recieved id="+device.id);
		
		//Send a device status
		DeviceStatus deviceStatus=new DeviceStatus(150);
		base.path("device/"+1+"/status").request(MediaType.APPLICATION_XML).post(Entity.xml(deviceStatus));
		System.out.println("Sent the status with the return code:");
	}
}
