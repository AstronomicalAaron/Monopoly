package cs414.a5.n;

/*************************************************************************************
 *                                      MONOPOLY									 *
 *************************************************************************************
 *                               CREATED ON: (C)10/28/2016							 *
 *                                   UPDATED ON: 10/28/2016						     *
 *                                   VERSION: 0.0.1									 *
 *                                     WRITTEN BY:									 *
 * 	    								 Joey Bzdek	                                 *
 * 								    Dylan Crescibene 								 *
 * 									 Chris Geohring 								 *
 * 									Aaron Barczewski								 *
 * 																					 *
 *************************************************************************************/

/*************************************************************************************
 * 										JSON										 *
 *************************************************************************************/

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Json {

	private static Gson gson = new Gson();
	
	public static String serialize(Object o) {
		return gson.toJson(o);
	}
	
	public static <T> T deserialize(String s, Class<T> cls) {
		T o = null;
		try {
			o = gson.fromJson(s, cls);
		} catch (JsonSyntaxException | JsonIOException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	public static <T> T deserializeFromFile(String fileName, Class<T> cls) {
		T o = null;
		try {
			FileReader reader = new FileReader(fileName);
			o = gson.fromJson(reader, cls);
			reader.close();
		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
		}
		return o;
	}
	
}
