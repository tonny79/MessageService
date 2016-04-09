package zhulin.project.dm;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;

import zhulin.project.dm.dao.Device;
import zhulin.project.dm.dao.DeviceStatus;

@Path("/devices")
public class DeviceManager {
	private List<Device> devices = null;

	public DeviceManager(){
		this.loadDevices();
	}
	
	private void loadDevices() {
		// Load the devices from DB
		try {
			org.hibernate.Session session = Utils.sessionFactory
					.getCurrentSession();
			session.beginTransaction();
			Criteria c = session.createCriteria(Device.class);
			c.setFetchMode("statuses", FetchMode.JOIN);
			c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			devices=c.list();
			session.getTransaction().commit();
			System.out.println("Retrieved devices from database:"
					+ devices.size());
		} catch (Throwable ex) {
			System.err.println("Can't retrieve devices!" + ex);
			ex.printStackTrace();
		}
	}

	private Device getDevice(int id) {
		if (devices != null) {
			for (Device device : devices) {
				if (device.getId() == id) {
					return device;
				}
			}
		}

		return null;
	}

	private Device[] getDevices() {
		this.loadDevices();

		Device[] result = new Device[devices.size()];
		Iterator<Device> iter = devices.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iter.next();
		}

		Arrays.sort(result);

		return result;
	}

	private void addDevice(Device device) {
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
		
		// Refresh the device list
		this.loadDevices();
	}

	private int updateDevice(int id) {
		Device device = getDevice(id);
		if (device != null) {
			try {
				org.hibernate.Session session = Utils.sessionFactory
						.getCurrentSession();
				session.beginTransaction();
				session.update(device);
				session.flush();
				session.getTransaction().commit();

				return device.getId();
			} catch (Exception e) {
				System.err.println("Can't update the device to database." + e);
				e.printStackTrace();
			}
		}

		return -1;
	}
	
	/**
	 * Retrieve the device list
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<DeviceInfo> getDevicesInfo() {
		this.loadDevices();
		
		List<DeviceInfo> result = new ArrayList<DeviceInfo>(devices.size());
		for (Device device : devices) {
			System.out.println("Find the device \""+device.getName()+"\"!");
			DeviceInfo deviceInfo = new DeviceInfo(device.getName(),
					device.getMemory(), device.getType(), device.getLocation());
			deviceInfo.id = device.getId();
			result.add(deviceInfo);
		}

		return result;
	}
	
	/**
	 * Retrieve the detail info of a particular device
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/device/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public DeviceInfo getDeviceInfo(@PathParam("id") int id){
		Device device=this.getDevice(id);
		if(device!=null){
			DeviceInfo result=new DeviceInfo(device.getName(),device.getMemory(),
					device.getType(),device.getLocation());
			result.id=device.getId();
			
			return result;
		}
		
		return null;
	}

	/**
	 * Create a new device
	 * 
	 * @param deviceInfo
	 * @return
	 */
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
	
	/**
	 * Update a particular device
	 * 
	 * @param id
	 * @param deviceInfo
	 * @return
	 */
	@POST
	@Path("/device/{id}")
	@Consumes(MediaType.APPLICATION_XML)
	public DeviceInfo updateDeviceInfo(@PathParam("id") int id,DeviceInfo deviceInfo){
		//Find the device
		Device device=this.getDevice(id);
		if(device!=null&&deviceInfo!=null){
			device.setMemory(deviceInfo.memory);
			device.setLocation(deviceInfo.location);
			
			this.updateDevice(id);
		}
		
		return deviceInfo;
	}
	
	/**
	 * Retrieve the statuses of a particular device
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/device/{id}/status")
	@Produces(MediaType.APPLICATION_XML)
	public List<DeviceStatus> getDeviceStatus(@PathParam("id") int id) {
		Device device=this.getDevice(id);
		
		return device==null?null:device.getStatuses();
	}
	
	/**
	 * Create a new status for a particular device 
	 * 
	 * @param id
	 * @param deviceStatus
	 */
	@POST
	@Path("/device/{id}/status")
	@Consumes(MediaType.APPLICATION_XML)
	public void addDeviceStatus(@PathParam("id") int id,DeviceStatus deviceStatus){
		System.out.println("Recieved the status for "+id+"!");
		Device device=this.getDevice(id);
		if(device!=null){
			System.out.println("Find the device:"+device.getId()+" add new temperature:"+deviceStatus.getTemperature());
			device.getStatuses().add(deviceStatus);
			System.out.println(String.format("Now the device \"%d\" has %d statuses!", 
					device.getId(),device.getStatuses().size()));
			
			System.out.println("Update the device \""+device.getName()+"\"");
			this.updateDevice(id);
		}else{
			System.out.println("Can't find the device: id="+id+"! Discard the status:"+deviceStatus);
		}
	}
	
}
