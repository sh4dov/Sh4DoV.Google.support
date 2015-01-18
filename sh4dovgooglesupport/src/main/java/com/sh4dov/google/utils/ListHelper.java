package com.sh4dov.google.utils;

import java.util.List;

public class ListHelper {
    public static <E> E firstOrDefault(List<E> list, Predicate<E> predicate){
        if(list == null || predicate == null){
            return null;
        }

        for(E item : list){
            if(predicate.match(item)){
                return item;
            }
        }

        return null;
    }

    public static <E> boolean any(List<E> list, Predicate<E> predicate){
        return firstOrDefault(list, predicate) != null;
    }

    public interface Predicate<T>{
        boolean match(T item);
    }
}
