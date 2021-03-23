package com.dataox.linkedinscraper.utils;

import lombok.experimental.UtilityClass;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
@UtilityClass
public class ObjectUtils {

    public static boolean equalsAny(Object obj, Object... objects) {
        for (Object object : objects) {
            if (obj.equals(object)) {
                return true;
            }
        }
        return false;
    }
}
