package ru.DmN.bpl.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionsHelper {
    public static <T> List<T> combine(List<T> list0, List<T> list1) {
        List<T> list = new ArrayList<>();
        if (list0 != null)
            list.addAll(list0);
        if (list1 != null)
            list.addAll(list1);
        return list;
    }
}
