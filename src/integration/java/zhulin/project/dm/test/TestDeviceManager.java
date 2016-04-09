package zhulin.project.dm.test;

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import zhulin.project.dm.*;
import zhulin.project.dm.dao.Device.DeviceType;
import zhulin.project.dm.client.DeviceClient;
import zhulin.project.dm.dao.Device;
import zhulin.project.dm.dao.DeviceStatus;
import zhulin.project.dm.dao.Location;

import org.junit.Test;
import org.junit.Assert;

public class TestDeviceManager {
	public static final String SERVER_URL="http://192.168.50.128/devicemanager-1.0/";

	@Test
	public void testDeviceCreation() {
		Client client = ClientBuilder.newClient();
		WebTarget base = client.target(SERVER_URL);

		// Create a device via XML
		DeviceInfo device = new DeviceInfo("Test-from-RestClient", 1024, DeviceType.BOILER,
				new Location(100, 100));
		device = base.path("dmrest/devices/device").request(MediaType.APPLICATION_XML).post(Entity.xml(device), DeviceInfo.class);
		Assert.assertTrue(device.id > 0);
		System.out.println("Sent device and recieved id=" + device.id);

		// Send 10 device statuses
		for (int i = 0; i < 10; i++) {
			DeviceStatus deviceStatus = new DeviceStatus(i*10);
			base.path("dmrest/devices/device/" + device.id + "/status").request(MediaType.APPLICATION_XML).post(Entity.xml(deviceStatus));
			System.out.println(String.format("Send signal \"%d\" for the device \"%s\"!", 
					deviceStatus.temperature,device.name));
		}
		
		// Retrieve the detail info of the device
		DeviceInfo result=base.path("dmrest/devices/device/"+device.id).request(MediaType.APPLICATION_XML).get(DeviceInfo.class);
		Assert.assertEquals("Test-from-RestClient",result.name);
		Assert.assertEquals(1024, result.memory);
		
		// Update the memory of the device
		result.memory=2048;
		result=base.path("dmrest/devices/device/"+device.id).request(MediaType.APPLICATION_XML).post(Entity.xml(result),DeviceInfo.class);
		device=base.path("dmrest/devices/device/"+device.id).request(MediaType.APPLICATION_XML).get(DeviceInfo.class);
		Assert.assertEquals(2048, device.memory);
	}
	
	@Test
	public void testRetrieveDevices(){
		Client client=ClientBuilder.newClient();
		WebTarget base=client.target(SERVER_URL);
		List<DeviceInfo> devices=base.path("dmrest/devices").request(MediaType.APPLICATION_XML).get(new GenericType<List<DeviceInfo>>(){});
		System.out.println(String.format("Retrieved %d devices!",devices.size()));
		for(DeviceInfo device:devices){
			System.out.println(device.name);
		}
	}
	
	@Test
	public void testDeviceClient(){
		DeviceClient client=new DeviceClient("Test by JUnit");
		int deviceId=client.register(SERVER_URL);
		Assert.assertTrue(deviceId>0);
		
		client.sendSignal(100);
		client.sendSignal(50);
	}
}
