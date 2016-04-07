package zhulin.project.dm.test;

import static org.junit.Assert.*;

import org.junit.Test;

import zhulin.project.dm.dao.Device.DeviceType;
import zhulin.project.dm.dao.*;
import zhulin.project.dm.*;

public class DeviceInfoTest {
	@Test
	public void test() {
		DeviceInfo deviceInfo=new DeviceInfo("Test",1024,DeviceType.INFLAMER,new Location(100,100));
		
		assertEquals(deviceInfo.name,"Test");
		assertEquals(deviceInfo.memory,1024);
		assertEquals(deviceInfo.type,DeviceType.INFLAMER);
		assertEquals(deviceInfo.location,new Location(100,100));
	}

}
