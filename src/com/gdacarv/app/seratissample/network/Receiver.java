package com.gdacarv.app.seratissample.network;

public interface Receiver<T> {
	
	void onReceive(T t);

}
