package zhulin.project;

import javax.persistence.Embeddable;

@Embeddable
public class Location{
	private int X;
	private int Y;
	
	private Location(){
	}
	
	public Location(int X,int Y){
		this.X=X;
		this.Y=Y;
	}
	
	public int getX(){
		return X;
	}
	
	public void setX(int X){
		this.X=X;
	}
	
	public int getY(){
		return Y;
	}
	
	public void setY(int Y){
		this.Y=Y;
	}
}
