package zhulin.project;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/devices")
public class DeviceManager {
	private List<Device> devices = null;

	private void loadDevices() {
		// Load the devices from DB
		try {
			org.hibernate.Session session = Utils.sessionFactory
					.getCurrentSession();
			session.beginTransaction();
			devices = session.createCriteria(Device.class).list();
			session.getTransaction().commit();
			System.out.println("Retrieved devices from database:"
					+ devices.size());
		} catch (Throwable ex) {
			System.err.println("Can't retrieve devices!" + ex);
			ex.printStackTrace();
			// Initialize the devices queue anyway
			if (devices == null) {
				devices = new ArrayList<Device>();
			}
		}
	}

	private Device findDevice(int id) {
		//Load devices from database
		this.loadDevices();
		
		if (devices != null) {
			for (Device device : devices) {
				if (device.getId() == id) {
					return device;
				}
			}
		}

		return null;
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

	public void addDevice(Device device) {
		// Refresh the device list
		this.loadDevices();

		devices.add(device);

		// Save to database
		try {
			org.hibernate.Session session = Utils.sessionFactory
					.getCurrentSession();
			session.beginTransaction();
			session.save(device);
			session.getTransaction().commit();
		} catch (Exception e) {
			System.err.println("Can't save the device to database." + e);
			e.printStackTrace();
		}
	}

	public int updateDevice(int id) {
		Device device = findDevice(id);
		if (device != null) {
			try {
				org.hibernate.Session session = Utils.sessionFactory
						.getCurrentSession();
				session.beginTransaction();
				session.update(device);
				session.getTransaction().commit();

				return device.getId();
			} catch (Exception e) {
				System.err.println("Can't update the device to database." + e);
				e.printStackTrace();
			}
		}

		return -1;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<DeviceInfo> getDevicesXML() {
		this.loadDevices();
		List<DeviceInfo> result = new ArrayList<DeviceInfo>(devices.size());
		for (Device device : devices) {
			DeviceInfo deviceInfo = new DeviceInfo(device.getName(),
					device.getMemory(), device.getType(), device.getLocation());
			deviceInfo.id = device.getId();
			result.add(deviceInfo);
		}

		return result;
	}

	@POST
	@Path("/device")
	@Consumes(MediaType.APPLICATION_XML)
	public DeviceInfo addDevice(DeviceInfo deviceInfo) {
		if (deviceInfo.name != null && !deviceInfo.name.trim().equals("")) {
			Device device = new Device(deviceInfo);
			this.addDevice(device);
			// Get device id
			deviceInfo.id = device.getId();

			return deviceInfo;
		}

		return null;
	}

	@GET
	@Path("/device/{id}/status")
	@Produces(MediaType.APPLICATION_XML)
	public List<DeviceStatus> getDeviceStatus(@PathParam("id") int id) {
		Device device=this.findDevice(id);
		
		return device==null?null:device.getStatuses();
	}
	
	@POST
	@Path("/device/{id}/status")
	@Consumes(MediaType.APPLICATION_XML)
	public void addDeviceStatus(@PathParam("id") int id,DeviceStatus deviceStatus){
		System.out.println("Recieved the status for "+id+"!");
		Device device=this.findDevice(id);
		if(device!=null){
			System.out.println("Find the device:"+device.getId()+" add the status:"+deviceStatus);
			//TODO: Why program stops here?
			//device.getStatuses().add(deviceStatus);
			
			System.out.println("Update the device \""+device.getName()+"\"");
			this.updateDevice(id);
			
			return;
		}
		System.out.println("Can't find the device: id="+id+"! Discard the status:"+deviceStatus);
		
		return;
	}
	
}
