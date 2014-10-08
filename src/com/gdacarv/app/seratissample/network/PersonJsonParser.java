package com.gdacarv.app.seratissample.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.gdacarv.app.seratissample.model.Patient;
import com.gdacarv.app.seratissample.model.Person;
import com.gdacarv.app.seratissample.model.Provider;

public class PersonJsonParser extends JsonParser<List<Person>> {
	
	private static final String TAG = "PersonJsonParser";
	
	private Class<? extends Person> mClass;

	public PersonJsonParser(Class<? extends Person> cls) { 
		this.mClass = cls;
	}

	public PersonJsonParser() {
	}

	@Override
	protected List<Person> handleJson(com.fasterxml.jackson.core.JsonParser jsonParser) {
		List<Person> persons = new ArrayList<Person>();
		try {
			while (jsonParser.nextToken()!= JsonToken.START_ARRAY);
			JsonToken token;
			long id = 0;
			String name = null;
			while ((token = jsonParser.nextToken()) != JsonToken.END_ARRAY){
				if(token == JsonToken.END_OBJECT){
					if(mClass == Patient.class)
						persons.add(new Patient(id, name));
					else if(mClass == Provider.class)
						persons.add(new Provider(id, name));
					else
						persons.add(new Person(id, name));
				} else if(token == JsonToken.FIELD_NAME) {
					String namefield = jsonParser.getCurrentName();
					jsonParser.nextToken(); // move to value
					if ("id".equals(namefield)) {
						id = jsonParser.getLongValue();
					} else if ("name".equals(namefield)) {
						name = jsonParser.getText();
					} else {
						Log.e(TAG, "Unrecognized field '"+namefield+"'!");
					}
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return persons;
	}

}
