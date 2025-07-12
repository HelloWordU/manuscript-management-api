package com.rz.manuscript.common;

import com.rz.manuscript.exception.RZException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

public class AssertUtil {
    public AssertUtil() {
    }

    public static void isTrue(boolean expression, String message) throws RZException {
        if (!expression) {
            throw new RZException(message);
        }
    }

    public static void isTrue(boolean expression) throws RZException {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isNull(Object object, String message) throws RZException {
        if (object != null) {
            throw new RZException(message);
        }
    }

    public static void isNull(Object object) throws RZException {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) throws RZException {
        if (object == null) {
            throw new RZException(message);
        }
    }

    public static void notNull(Object object) throws RZException {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    public static void hasLength(String text, String message) throws RZException {
        if (!StringUtils.hasLength(text)) {
            throw new RZException(message);
        }
    }

    public static void hasLength(String text) throws RZException {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static void hasText(String text, String message) throws RZException {
        if (!StringUtils.hasText(text)) {
            throw new RZException(message);
        }
    }

    public static void hasText(String text) throws RZException {
        hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    public static void doesNotContain(String textToSearch, String substring, String message) throws RZException {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new RZException(message);
        }
    }

    public static void doesNotContain(String textToSearch, String substring) throws RZException {
        doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
    }

    public static void notEmpty(Object[] array, String message) throws RZException {
        if (ObjectUtils.isEmpty(array)) {
            throw new RZException(message);
        }
    }

    public static void notEmpty(Object[] array) throws RZException {
        notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
    }

    public static void noNullElements(Object[] array, String message) throws RZException {
        if (array != null) {
            Object[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object element = var2[var4];
                if (element == null) {
                    throw new RZException(message);
                }
            }
        }

    }

    public static void noNullElements(Object[] array) throws RZException {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    public static void notEmpty(Collection<?> collection, String message) throws RZException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new RZException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) throws RZException {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Map<?, ?> map, String message) throws RZException {
        if (CollectionUtils.isEmpty(map)) {
            throw new RZException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map) throws RZException {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    public static void isInstanceOf(Class<?> clazz, Object obj) throws RZException {
        isInstanceOf(clazz, obj, "");
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) throws RZException {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new RZException((StringUtils.hasLength(message) ? message + " " : "") + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType) throws RZException {
        isAssignable(superType, subType, "");
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) throws RZException {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new RZException((StringUtils.hasLength(message) ? message + " " : "") + subType + " is not assignable to " + superType);
        }
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression) {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }
}
