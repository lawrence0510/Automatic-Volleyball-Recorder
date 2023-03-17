import java.util.*;
public class Players {
	private int number;
	private String position, sex, name;
	
	public Players(String name, int number, String position, String sex) {
		this.number = number;
		this.position = position;
		this.name = name;
		this.sex = sex;
	}
	public String getname() {
		return this.name;
	}
	
	public String getposition() {
		return this.position;
	}
	
	public int getnumber() {
		return this.number;
	}
	
	public String getsex() {
		return this.sex;
	}
}