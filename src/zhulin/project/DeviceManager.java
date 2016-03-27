package zhulin.project;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/devices")
public class DeviceManager {
	private List<Device> devices=null;
	
	private void loadDevices() {
		// Load the devices from DB
		try {
			org.hibernate.Session session = Utils.sessionFactory.getCurrentSession();
			session.beginTransaction();
			devices = session.createCriteria(Device.class).list();
			session.getTransaction().commit();
			System.out.println("Retrieved devices from database:" + devices.size());
		} catch (Throwable ex) {
			System.err.println("Can't retrieve devices!" + ex);
			ex.printStackTrace();
			// Initialize the devices queue anyway
			if (devices == null) {
				devices = new ArrayList<Device>();
			}
		}
	}
	
	public Device[] getDevices() {
		this.loadDevices();

		Device[] result = new Device[devices.size()];
		Iterator<Device> iter = devices.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iter.next();
		}

		Arrays.sort(result);

		return result;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String getDevicesXML() {
		StringBuffer result = new StringBuffer("<?xml version=\"1.0\"?>");
		result.append("<Devices>");
		Device[] devices = getDevices();
		for (int i = 0; i < devices.length; i++) {
			result.append("<Device id=\"" + devices[i].getId() + "\">");
			result.append("</Device>");
		}
		result.append("</Devices>");

		return result.toString();
	}
	
	//@PUT
	//@Path("/adddevice")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void addDevice(String name,Device.DeviceType type,int memory,Location location) {
		if (name != null && !name.trim().equals("")) {
			// Refresh the device list
			this.loadDevices();

			Device device = new Device(name,type,memory,location);
			devices.add(device);

			// Save to database
			try {
				org.hibernate.Session session = Utils.sessionFactory.getCurrentSession();
				session.beginTransaction();
				session.save(device);
				session.getTransaction().commit();
			} catch (Exception e) {
				System.err.println("Can't save the device to database." + e);
				e.printStackTrace();
			}
		}
	}
}
