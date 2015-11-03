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
 * You can also provide a mapping of overrides for fields that are non-instantiable (ie List -> ArrayList).
 */
public class FieldAccessTester {
    private static final String GET = "get",
                                SET = "set";
    private final Object objectToTest;
    private Map<String, String> accessMethodRenaming = new HashMap<>();
    private Map<String, Object> nonInstantiableFieldsDefaultValues = new HashMap<>();
    private boolean testGetters = true,
                    testSetters = true;

    public FieldAccessTester(Object objectToTest) {
        this.objectToTest = objectToTest;
    }

    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        User user = new User("abc", "123");
        Map<String, String> accessMethodRenaming = new HashMap<>();
        accessMethodRenaming.put("getIsActive", "isActive");
        accessMethodRenaming.put("getFoundMeeting", "hasFoundMeeting");
        accessMethodRenaming.put("setUsername", null);
        accessMethodRenaming.put("setHistory", null);
        accessMethodRenaming.put("setVersion", null);
        Map<String, Object> nonInstantiableFieldsDefaultValues = new HashMap<>();
        nonInstantiableFieldsDefaultValues.put(List.class.getTypeName(), new ArrayList<>());
        FieldAccessTester fieldAccessTester = new FieldAccessTester(user).setAccessMethodRenaming(accessMethodRenaming)
                .setNonInstantiableFieldsDefaultValues(nonInstantiableFieldsDefaultValues);
        fieldAccessTester.run();
    }

    public static void testSetters(Object object, List<Field> fields, Map<String, String> accessMethodRenaming,
                                   Map<String, Object> nonInstantiableFieldsDefaultValues)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for (Field field : fields) {
            Method setter = getFieldAccessMethod(object.getClass(), SET, field, accessMethodRenaming);
            if (setter == null) {
                continue;
            }
            Object expectedValue = newInstanceOfField(field, nonInstantiableFieldsDefaultValues);
            setter.invoke(object, expectedValue);
            Object actualValue = getField(object, field);
            assertEquals(actualValue, expectedValue);
        }
    }

    public static void testGetters(Object object, List<Field> fields, Map<String, String> accessMethodRenaming)
            throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Field field : fields) {
            Object ExpectedValue = getField(object, field);
            Method getter = getFieldAccessMethod(object.getClass(), GET, field, accessMethodRenaming);
            if (getter == null) {
                continue;
            }
            Object actualValue = getter.invoke(object);
            assertEquals(ExpectedValue, actualValue);
        }
    }

    private static Object newInstanceOfField(Field field, Map<String, Object> nonInstantiableFieldsDefaultValues)
            throws IllegalAccessException, InstantiationException {
        Object newInstance;
        Class<?> fieldType = field.getType();
        try {
            newInstance = fieldType.newInstance();
        } catch (InstantiationException e) {
            newInstance = attemptDefaultFieldValue(fieldType.getTypeName(), nonInstantiableFieldsDefaultValues);
            if (newInstance == null) {
                throw e;
            }
        }

        return newInstance;
    }

    private static Object attemptDefaultFieldValue(String typeName, Map<String, Object> nonInstantiableFieldsDefaultValues) {
        Object newInstance;
        switch (typeName) {
            case "boolean":
                newInstance = false;
                break;
            case "byte":
                newInstance = 0;
                break;
            case "char":
                newInstance = '\u0000';
                break;
            case "double":
                newInstance = 0.0d;
                break;
            case "float":
                newInstance = 0.0f;
                break;
            case "int":
                newInstance = 0;
                break;
            case "long":
                newInstance = 0L;
                break;
            case "short":
                newInstance = 0;
                break;
            default:
                newInstance = nonInstantiableFieldsDefaultValues.get(typeName);
        }
        return newInstance;
    }

    private static Object getField(Object object, Field field) throws IllegalAccessException {
        Object returnVal;
        if (field.isAccessible()) {
            returnVal = field.get(object);
        } else {
            field.setAccessible(true);
            returnVal = field.get(object);
            field.setAccessible(false);
        }
        return returnVal;
    }

    private static Method getFieldAccessMethod(Class clazz, String getOrSet, Field field, Map<String, String> accessMethodRenaming) throws NoSuchMethodException {
        Method method;
        String methodName = getOrSet + StringUtils.capitalize(field.getName());
        if (accessMethodRenaming.containsKey(methodName)) {
            methodName = accessMethodRenaming.get(methodName);
            if (methodName == null) {
                return null;
            }
        }
        switch (getOrSet) {
            case GET:
                method = clazz.getMethod(methodName);
                break;
            case SET:
                method = clazz.getMethod(methodName, field.getType());
                break;
            default:
                throw new IllegalArgumentException("Parameter 'getOrSet' must have value of 'get' or 'set'");
        }
        return method;
    }

    public Object getObjectToTest() {
        return objectToTest;
    }

    public Map<String, String> getAccessMethodRenaming() {
        return accessMethodRenaming;
    }

    public FieldAccessTester setAccessMethodRenaming(Map<String, String> accessMethodRenaming) {
        this.accessMethodRenaming = accessMethodRenaming;
        return this;
    }

    public Map<String, Object> getNonInstantiableFieldsDefaultValues() {
        return nonInstantiableFieldsDefaultValues;
    }

    public FieldAccessTester setNonInstantiableFieldsDefaultValues(Map<String, Object> nonInstantiableFieldsDefaultValues) {
        this.nonInstantiableFieldsDefaultValues = nonInstantiableFieldsDefaultValues;
        return this;
    }

    public boolean isTestGetters() {
        return testGetters;
    }

    public FieldAccessTester setTestGetters(boolean testGetters) {
        this.testGetters = testGetters;
        return this;
    }

    public boolean isTestSetters() {
        return testSetters;
    }

    public FieldAccessTester setTestSetters(boolean testSetters) {
        this.testSetters = testSetters;
        return this;
    }

    public FieldAccessTester run() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        List<Field> fields = Arrays.asList(objectToTest.getClass().getDeclaredFields());
        if (testGetters) {
            testGetters(objectToTest, fields, accessMethodRenaming);
        }
        if (testSetters) {
            testSetters(objectToTest, fields, accessMethodRenaming, nonInstantiableFieldsDefaultValues);
        }
        return this;
    }
}
