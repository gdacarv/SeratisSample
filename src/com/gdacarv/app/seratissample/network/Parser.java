package com.gdacarv.app.seratissample.network;

import java.io.InputStream;

public interface Parser<T> {

	T parse(InputStream inputStream);
}
