package com.tutorial;

import java.util.List;
import java.util.Map;

public class BeanList {
	List addressList;
	Map addressMap;
	List beanList;
		
	public List getAddressList() {
		System.out.println("List eements: " + addressList);
		return addressList;
	}
	
	public void setAddressList(List addressList) {
		this.addressList = addressList;
	}
	
	public Map getAddressMap() {
		System.out.println("Map elements: " + addressMap);
		return addressMap;
	}
	
	public void setAddressMap(Map addressMap) {
		this.addressMap = addressMap;
	}
	
	public List getBeanList() {
		System.out.println("beans elements: " + beanList);
		return beanList;
	}
	
	public void setBeanList(List beanList) {
		this.beanList = beanList;
	}
}
