package com.eraise.eraisedemo.bean;

public class DemoBean {

	public String title;
	public Class<?> cls;
	
	public DemoBean() {}
	public DemoBean(Class<?> cls, String title) {
		
		this.cls = cls;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Class<?> getCls() {
		return cls;
	}
	public void setCls(Class<?> cls) {
		this.cls = cls;
	}
	
	
}
