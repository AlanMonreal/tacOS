package com.disp_mov.tacos.data.model;

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
public class Orders {


    public static final ArrayList<Order> ITEMS = new ArrayList<Order>();
    public static final Map<String, Order> ITEM_MAP = new HashMap<String, Order>();

    private static final int COUNT = 25;

    static {
        /*for (int i = 1; i <= COUNT; i++) {
            //addItem(createDummyItem(i));
        }*/
    }

    public static void addItem(Order item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static Order createDummyItem() {
        return new Order(String.valueOf(ITEMS.size() + 1), "Órden " + (ITEMS.size() + 1), makeDetails(ITEMS.size() + 1));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class Order {
        public final String id;
        public final String content;
        public final String details;

        public Order(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
