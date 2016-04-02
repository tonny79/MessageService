package zhulin.project.dm;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageInfo {
	public int id;
	public String message;
	
	public MessageInfo(){
	}
	
	public MessageInfo(int id,String message){
		this.id=id;
		this.message=message;
	}
}
