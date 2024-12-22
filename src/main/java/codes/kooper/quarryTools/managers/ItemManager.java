package codes.kooper.quarryTools.managers;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {
    private final Map<String, ItemStack> items;

    public ItemManager() {
        items = new HashMap<>();
    }

    public void addItem(String name, ItemStack item) {
        items.put(name, item);
    }

    public void addItems(Map<String, ItemStack> items) {
        this.items.putAll(items);
    }

    public void removeItem(String name) {
        items.remove(name);
    }

    public void removeItems(List<String> names) {
        names.forEach(items::remove);
    }

    public ItemStack getItem(String name) {
        return items.get(name);
    }

    public List<ItemStack> getItems(List<String> names) {
        List<ItemStack> itemsFetched = new ArrayList<>();
        for (String name : names) {
            itemsFetched.add(items.get(name));
        }
        return itemsFetched;
    }

    public Map<String, ItemStack> getItems() {
        return new HashMap<>(items);
    }
}
