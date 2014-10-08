package com.gdacarv.app.seratissample.network;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonFactory;

public abstract class JsonParser<T> implements Parser<T> {

	@Override
	public T parse(InputStream inputStream) {
		com.fasterxml.jackson.core.JsonParser jsonParser = null;
        try {
            jsonParser = new JsonFactory().createParser(inputStream);
            return handleJson(jsonParser);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(jsonParser != null)
                try {
                    jsonParser.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
	}

	protected abstract T handleJson(com.fasterxml.jackson.core.JsonParser jsonParser);

}
