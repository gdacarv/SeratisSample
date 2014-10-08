package com.gdacarv.app.seratissample.model;

public class Person {
	
	private long mId;
	private String mName;
	
	public Person(long id, String name) {
		this.mId = id;
		this.mName = name;
	}

	public long getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	@Override
	public String toString() {
		return getName();
	}
}
