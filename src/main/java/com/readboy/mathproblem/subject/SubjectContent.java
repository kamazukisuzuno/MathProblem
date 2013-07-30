package com.readboy.mathproblem.subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SubjectContent {

    /**
     * An array of sample (subject) items.
     */
    public static List<SubjectItem> ITEMS = new ArrayList<SubjectItem>();

    /**
     * A map of sample (subject) items, by ID.
     */
    public static Map<String, SubjectItem> ITEM_MAP = new HashMap<String, SubjectItem>();

    static {
        // Add 3 sample items.
        addItem(new SubjectItem("1", "Item 1"));
        addItem(new SubjectItem("2", "Item 2"));
        addItem(new SubjectItem("3", "Item 3"));
    }

    private static void addItem(SubjectItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

}
