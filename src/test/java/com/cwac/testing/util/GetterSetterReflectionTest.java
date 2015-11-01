package com.cwac.testing.util;

import com.cwac.mongoDocs.User;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 11/1/2015.
 * Utility class to automatically test a class' getters and setters reflectively.
 *
 * By default will read all fields of a class and dynamically call get<FieldInCamelCase> and Set<FieldInCamelCase>.
 * You can also pass in an access method renaming map if an access method does not follow the standard getField format.
 * This is very common for boolean getters and setters which may instead use prefixes like is or has instead of get.
 * Additionally you can map a field access method to null indicating that it should not be auto-tested.
 */
public class GetterSetterReflectionTest {
    private static final String GET = "get",
                                SET = "set";

    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        GetterSetterReflectionTest test = new GetterSetterReflectionTest();
        User user = new User("abc", "123");
        Map<String, String> accessMethodRenaming = new HashMap<>();
        accessMethodRenaming.put("getIsActive", "isActive");
        accessMethodRenaming.put("getFoundMeeting", "hasFoundMeeting");
        test.run(user, accessMethodRenaming);
    }

    public static void run(Object object) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        run(object, new HashMap<String, String>());
    }

    public static void run(Object object, Map<String, String> accessMethodRenaming) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        List<Field> fields = Arrays.asList(object.getClass().getDeclaredFields());
        testGetters(object, fields, accessMethodRenaming);
        testSetters(object, fields, accessMethodRenaming);
    }

    public static void testSetters(Object object, List<Field> fields, Map<String, String> accessMethodRenaming)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for(Field field : fields){
            Object expectedValue = newInstanceOfField(field);
            Method setter = getFieldAccessMethod(object.getClass(), SET, field, accessMethodRenaming);
            setter.invoke(object, expectedValue);
            Object actualValue = getfield(object, field);
            assertEquals(actualValue, expectedValue);
        }
    }

    private static Object newInstanceOfField(Field field) throws IllegalAccessException, InstantiationException {
        Object newInstance = null;
        Class<?> fieldType = field.getType();
        try{
            newInstance = fieldType.newInstance();
        } catch (InstantiationException e) {
            switch (fieldType.getTypeName()){
                case "java.util.List":
                    newInstance = new ArrayList<>();
                    break;
                case "boolean":
                    newInstance = true;
                    break;
                default:
                    throw e;
            }
        }

        return newInstance;
    }

    public static void testGetters(Object object, List<Field> fields, Map<String, String> accessMethodRenaming) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for(Field field : fields){
            Object ExpectedValue = getfield(object, field);
            Method getter = getFieldAccessMethod(object.getClass(), GET, field, accessMethodRenaming);
            if(getter == null) {
                continue;
            }
            Object actualValue = getter.invoke(object);
            assertEquals(ExpectedValue, actualValue);
        }
    }

    private static Object getfield(Object object, Field field) throws IllegalAccessException {
        Object returnVal = null;
        if(field.isAccessible()){
            returnVal = field.get(object);
        }
        else {
            field.setAccessible(true);
            returnVal = field.get(object);
            field.setAccessible(false);
        }
        return returnVal;
    }

    private static Method getFieldAccessMethod(Class clazz, String getOrSet, Field field, Map<String, String> accessMethodRenaming) throws NoSuchMethodException {
        Method method = null;
        String methodName = getOrSet + StringUtils.capitalize(field.getName());
        if(accessMethodRenaming.containsKey(methodName)){
            methodName = accessMethodRenaming.get(methodName);
            if(methodName == null){
                return null;
            }
        }
        switch (getOrSet){
            case GET:
                method = clazz.getMethod(methodName);
                break;
            case SET:
                method = clazz.getMethod(methodName, field.getType());
                break;
            default:
                throw new IllegalArgumentException("Second parameter 'getOrSet' must have value of 'get' or 'set'");
        }
        return method;
    }
}
