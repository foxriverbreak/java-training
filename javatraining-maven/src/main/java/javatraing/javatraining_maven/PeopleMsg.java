package javatraing.javatraining_maven;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeopleMsg {

	private static Logger logger = LoggerFactory.getLogger(CmdLine.class
			.getName());
	private String name;
	private String id;
	private String phoneNumber;
	private String address;

	public static Map<String, PeopleMsg> PeopleMap = new ConcurrentHashMap<String, PeopleMsg>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNum(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "PeopleMsg [name=" + name + ", id=" + id + ", phoneNumber="
				+ phoneNumber + ", address=" + address + "]";
	}

}
