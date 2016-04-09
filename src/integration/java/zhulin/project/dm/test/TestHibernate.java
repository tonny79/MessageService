package zhulin.project.dm.test;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import zhulin.project.dm.DeviceManager;
import zhulin.project.dm.Utils;
import zhulin.project.dm.DeviceInfo;
import zhulin.project.dm.dao.DeviceStatus;
import zhulin.project.dm.dao.Location;
import zhulin.project.dm.dao.Device.DeviceType;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;

public class TestHibernate {
	@Test
	public void testDeviceManager() {
		DeviceManager deviceManager=new DeviceManager();
		Assert.assertEquals(0, deviceManager.getDevicesInfo().size());

		DeviceInfo device1 = new DeviceInfo("Test Device", 1024,DeviceType.INFLAMER, new Location(200, 100));
		device1=deviceManager.addDevice(device1);
		Assert.assertTrue(device1.id>0);
		Assert.assertEquals(1, deviceManager.getDevicesInfo().size());
		
		deviceManager.addDeviceStatus(device1.id, new DeviceStatus(100));
		deviceManager.addDeviceStatus(device1.id, new DeviceStatus(200));
		Assert.assertEquals(2, deviceManager.getDeviceStatus(device1.id).size());

		// Test cascade update
		device1.memory=2048;
		deviceManager.updateDeviceInfo(device1.id, device1);
		deviceManager.addDeviceStatus(device1.id, new DeviceStatus(300));
		// TODO Why the following code can't save the device status?
		// device1.getStatuses().add(new DeviceStatus(300));
		// deviceManager.updateDevice(device1.getId();
		Assert.assertEquals(3, deviceManager.getDeviceStatus(device1.id).size());
		Assert.assertEquals(1, deviceManager.getDevicesInfo().size());

		DeviceInfo device2 = new DeviceInfo("Test Device2", 1024,DeviceType.BOILER, new Location(20, 50));
		device2=deviceManager.addDevice(device2);
		Assert.assertTrue(device2.id>0);
		deviceManager.addDeviceStatus(device2.id, new DeviceStatus(50));
		Assert.assertEquals(2, deviceManager.getDevicesInfo().size());
		Assert.assertEquals(1, deviceManager.getDeviceStatus(device2.id).size());
	}

	@Test
	public void test() throws Exception{
		this.addNewField("test1", "string");
		this.createEmp("1");
		this.createEmp("2");
		//this.readEmpUsingObject("1");
	}
	
	public void addNewField(String name,String type) throws Exception{
		//TODO Change the file path to class resource path
		String inFile="src/main/java/zhulin/project/dm/dao/Employee.hbm.xml";
		String outFile="bin/zhulin/project/dm/dao/Employee.hbm.xml";
		DocumentBuilder docBuilder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc=docBuilder.parse(inFile);
		System.out.println("Get XML:"+doc);
		
		// Get the element "hibernate-mapping"
		Element rootElm=doc.getDocumentElement();
		System.out.println("Found element:"+rootElm.getNodeName());
		// Get the element "class"
		Element classElm=null;
		NodeList nodes=rootElm.getChildNodes();
		for (int i=0;i<nodes.getLength();i++){
			Node temp=nodes.item(i);
			if(temp instanceof Element){
				classElm=(Element)temp;
				break;
			}
		}
		System.out.println("Found element:"+classElm.getNodeName());
		
		// Create the element for new field
		Element propertyElm=doc.createElement("property");
		propertyElm.setAttribute("name", name);
		propertyElm.setAttribute("type", type);
		classElm.appendChild(propertyElm);
		doc.createElement("test");
		
		System.out.println("Write xml:"+doc);
		Transformer transformer=TransformerFactory.newInstance().newTransformer();
		DOMSource source=new DOMSource(doc);
		transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result=new StreamResult(new PrintWriter(new FileOutputStream(outFile)));
		transformer.transform(source, result);
	}
	
	public void createEmp(String id){
		Map emp=new HashMap();
		emp.put("id", id);
		emp.put("name", "emp_"+id);
		emp.put("emailId", "emailId_"+id);
		emp.put("test1", "test1_"+id);
		
		Session session=Utils.sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.save("zhulin.project.dm.dao.Employee",emp);
		session.getTransaction().commit();
	}
	
	public void readEmpUsingObject(String id){
		HashMap emp=new HashMap();
		emp.put("id", id);
		emp.put("name", "emp_"+id);
		
		Session session=Utils.sessionFactory.getCurrentSession();
		session.beginTransaction();
		Map empRead=(Map)session.load("zhulin.project.dm.dao.Employee",emp);
		session.getTransaction().commit();
		System.out.println(empRead);
	}
}
