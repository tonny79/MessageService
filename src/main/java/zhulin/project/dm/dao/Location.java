package zhulin.project.dm.dao;

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
	
	@Override
	public String toString(){
		return "("+X+","+Y+")";
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Location)){
			return false;
		}
		
		Location temp=(Location)obj;
		return X==temp.X&&Y==temp.Y;
	}
}
