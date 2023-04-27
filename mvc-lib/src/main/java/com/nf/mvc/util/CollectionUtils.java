package com.nf.mvc.util;

import java.util.Collection;

public abstract class CollectionUtils {
    public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
        Object[] arr = ObjectUtils.toObjectArray(array);
        for (Object elem : arr) {
            collection.add((E) elem);
        }
    }
}
