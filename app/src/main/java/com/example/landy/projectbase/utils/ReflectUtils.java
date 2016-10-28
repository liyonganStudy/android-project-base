package com.example.landy.projectbase.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {
	public static Object getField(Class<?> clazz, Object obj, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setField(Class<?> clazz, String fieldName, Object obj, Object value) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object invokeMethod(Class<?> clazz, String methodName, Class<?>[] signature, Object receiver, Object... args) {
		try {
			Method method = clazz.getDeclaredMethod(methodName, signature);
			method.setAccessible(true);
			return method.invoke(receiver, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
