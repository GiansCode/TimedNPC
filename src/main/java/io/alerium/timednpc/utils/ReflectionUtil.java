package io.alerium.timednpc.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * This method sets a field in an Object
     * @param object The Object
     * @param fieldName The Field name
     * @param value The value
     */
    public static void setField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets a value from a static Field
     * @param clazz The class
     * @param fieldName The Field name
     * @param <T> The type of the Field
     * @return The Field value, null if not found
     */
    public static <T> T getStaticField(Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * This method creates a new instance of a specified Class
     * @param className The Class name
     * @param i The number of Constructor to use
     * @param parameters The list of all the parameters of the Constructor
     * @param <T> The class
     * @return The instance, null if there is a problem
     */
    public static <T> T getInstance(String className, int i, Object... parameters) {
        try {
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getConstructors()[i];
            return (T) constructor.newInstance(parameters);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
