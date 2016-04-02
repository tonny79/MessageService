package zhulin.project.dm.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import zhulin.project.dm.*;
import zhulin.project.dm.dao.Device.DeviceType;
import zhulin.project.dm.client.DeviceClient;
import zhulin.project.dm.dao.Device;
import zhulin.project.dm.dao.DeviceStatus;
import zhulin.project.dm.dao.Location;

import org.junit.Test;
import org.junit.Assert;

public class TestDevice {

	public void testHibernate() {
		DeviceManager deviceManager = new DeviceManager();
		Assert.assertEquals(0, deviceManager.getDevices().length);

		Device device1 = new Device("Test Device", DeviceType.INFLAMER, 1024, new Location(200, 100));
		device1.getStatuses().add(new DeviceStatus(100));
		device1.getStatuses().add(new DeviceStatus(200));
		deviceManager.addDevice(device1);
		Assert.assertEquals(1, deviceManager.getDevices().length);
		Assert.assertEquals(1, deviceManager.getDevicesXML().size());
		Assert.assertEquals(2, deviceManager.getDeviceStatus(device1.getId()).size());

		// Test cascade update
		device1.setMemory(2048);
		deviceManager.addDeviceStatus(device1.getId(), new DeviceStatus(300));
		// TODO Why the following code can't save the device status?
		// device1.getStatuses().add(new DeviceStatus(300));
		// deviceManager.updateDevice(device1.getId();
		Assert.assertEquals(3, deviceManager.getDeviceStatus(device1.getId()).size());
		Assert.assertEquals(1, deviceManager.getDevicesXML().size());

		Device device2 = new Device("Test Device2", DeviceType.BOILER, 1024, new Location(20, 50));
		device2.getStatuses().add(new DeviceStatus(50));
		deviceManager.addDevice(device2);
		Assert.assertEquals(2, deviceManager.getDevicesXML().size());
		Assert.assertEquals(1, deviceManager.getDeviceStatus(device2.getId()).size());
	}

	@Test
	public void testRest() {
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target("http://192.168.31.123:8080/devicemanager/");

		// Create a device via XML
		DeviceInfo device = new DeviceInfo("Test-from-RestClient", 1024, DeviceType.BOILER,
				new Location(100, 100));
		device = base.path("rest/devices/device").request(MediaType.APPLICATION_XML).post(Entity.xml(device), DeviceInfo.class);
		Assert.assertTrue(device.id > 0);
		System.out.println("Sent device and recieved id=" + device.id);

		// Send 10 device statuses
		for (int i = 0; i < 10; i++) {
			DeviceStatus deviceStatus = new DeviceStatus(i*10);
			base.path("rest/devices/device/" + device.id + "/status").request(MediaType.APPLICATION_XML).post(Entity.xml(deviceStatus));
			System.out.println(String.format("Send signal \"%d\" for the device \"%s\"!", 
					deviceStatus.temperature,device.name));
		}
	}
	
	@Test
	public void testDeviceClient(){
		DeviceClient client=new DeviceClient("Test by JUnit");
		int deviceId=client.register("http://192.168.31.123:8080/devicemanager");
		Assert.assertTrue(deviceId>0);
		
		client.sendSignal(100);
		client.sendSignal(50);
	}
}
