package zhulin.project.test;

import zhulin.project.*;
import zhulin.project.Device.DeviceType;

public class TestDevice {
	public static void main(String[] args){
		DeviceManager deviceManager=new DeviceManager();
		deviceManager.addDevice("Test!", Device.DeviceType.INFLAMER, 1024, new Location(100, 100));
		deviceManager.addDevice("Test2!", DeviceType.BOILER, 2048, new Location(100,152));
		
		System.out.println("Test Device Done!");
	}
}
