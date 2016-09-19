package com.it.common.component.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.it.common.component.log.LogPrint;

/**
 * @author liu
 * */
public class StreamUtil {
	
	/**
	 * input steam to object
	 * */
	public static Object in2Object(InputStream in) {   
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(in);
			Object object = ois.readObject();
			return object;
		} catch (Exception e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e2) {
					e2.printStackTrace();   
				}
			}   
		}
		return null;
	}   
 
	/**
	 * object to output stream
	 * */
	public static void object2Out(Object obj, OutputStream out) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(out);
			oos.writeObject(obj);
		} catch (Exception e) {
			LogPrint.error(e.getMessage());
			e.printStackTrace();   
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e1) {
					e1.printStackTrace();   
				}   
			}
		}
	}   

}
