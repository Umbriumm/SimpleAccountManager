package org.example.view;

import javafx.collections.ObservableList;
import org.example.model.Item;

public class Sort {

    public static void sortItemsBySite(ObservableList<Item> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).getSite().compareToIgnoreCase(list.get(j + 1).getSite()) > 0) {
                    Item temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}
