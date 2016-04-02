package zhulin.project.dm.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import zhulin.project.dm.DeviceInfo;
import zhulin.project.dm.dao.DeviceStatus;
import zhulin.project.dm.dao.Location;
import zhulin.project.dm.dao.Device;

public class DeviceClient {
	private WebTarget base=null;
	
	private int deviceId=-1;
	private String name=null;
	private int memory=1024;
	private Device.DeviceType type=Device.DeviceType.INFLAMER;
	private Location location=new Location(100,100);
	
	public DeviceClient(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public int register(String URL){
		Client client = ClientBuilder.newClient();
		base = client.target(URL);
		
		DeviceInfo device = new DeviceInfo(name, memory, type,location);
		device = base.path("/rest/devices/device").request(MediaType.APPLICATION_XML).post(Entity.xml(device), DeviceInfo.class);
		System.out.println("Sent device and recieved id=" + device.id);
		
		this.deviceId=device.id;
		return deviceId;
	}
	
	public void sendSignal(int temperature){
		DeviceStatus deviceStatus = new DeviceStatus(temperature);
		base.path("/rest/devices/device/" + deviceId + "/status").request(MediaType.APPLICATION_XML).post(Entity.xml(deviceStatus));
		System.out.println(String.format("Send signal \"%d\" for the device \"%s\"!", temperature,this.name));
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("Number of arg:"+args.length);
		if(args.length!=2){
			System.out.println("Usage: DeviceClient <Device Name> <Server URL>");
			System.out.println("For example: DeviceClient Test http://192.168.31.123:8080/devicemanager/");
			System.exit(0);
		}
		String deviceName=args[0];
		String serverURL=args[1];
		
		System.out.println("Create the device \""+deviceName+"\".");
		final DeviceClient client=new DeviceClient(deviceName);
		System.out.println(String.format("Register the device \"%s\" on the server \"%s\".", deviceName,serverURL));
		client.register(serverURL);
		
		System.out.println("Activate the device for sending signal!");
		
		Thread thread=new Thread(){
			@Override
			public void run(){
				while(true){
					int temperature=(int)(Math.random()*100);
					System.out.println(String.format("Send signal \"%d\" for the device \"%s\"!",
							temperature,client.getName()));
					client.sendSignal(temperature);
					
					//Pause 1 s
					try{
						Thread.sleep(3000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		
		thread.join();
	}
}
