package com.study.util;

import java.io.*;

/**
 * @author xiquee.com
 */
public final class SerializeUtil {

	public static byte[] serialize(Object object) throws Exception {
		ObjectOutputStream oos;
		ByteArrayOutputStream baos;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static Object unserialize(byte[] bytes) throws Exception {
		if (bytes == null) {
			return null;
		}
		ByteArrayInputStream bais;
		try {
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static String logExceptionStack(Throwable e) {
		StringWriter errorsWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(errorsWriter));
		return errorsWriter.toString();
	}

}
