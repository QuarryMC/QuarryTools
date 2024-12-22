package codes.kooper.quarryTools.items;

import codes.kooper.koopKore.item.ItemBuilder;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.enums.RARITIES;
import codes.kooper.quarryTools.enums.TOOL_TYPES;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BowItems {
    private final Map<RARITIES, Map<String, Bow>> bows;

    public BowItems() {
        bows = new HashMap<>();
        for (RARITIES rarity : RARITIES.values()) {
            bows.put(rarity, new HashMap<>());
            ConfigurationSection bowsConfig = QuarryTools.getInstance().getConfig().getConfigurationSection("bows." + rarity.name().toLowerCase());
            if (bowsConfig == null) {
                QuarryTools.getInstance().getLogger().severe("[Bows] Could not load bows in QuarryTools from config.yml!");
                return;
            }
            for (String bowName : bowsConfig.getKeys(false)) {
                int power = bowsConfig.getInt(bowName);
                ItemStack item = getBowItem(rarity, bowName, power);
                bows.get(rarity).put(bowName, new Bow(item, rarity, bowName, power));
                QuarryTools.getInstance().getItemManager().addItem(bowName + "_bow", item);
            }
        }
    }

    public Bow getBow(RARITIES rarity, String name) {
        return bows.get(rarity).get(name);
    }

    public Set<Bow> getBows(RARITIES rarity) {
        return new HashSet<>(bows.get(rarity).values());
    }

    private ItemStack getBowItem(RARITIES rarity, String name, int power) {
        TOOL_TYPES bow = TOOL_TYPES.BOW;
        return new ItemBuilder(Material.BOW)
                .setName(rarity.getToolName(bow, name))
                .setLore(bow.getLore(power, rarity))
                .setUnbreakable(true)
                .addEnchant(Enchantment.INFINITY, 1, false)
                .addEnchant(Enchantment.POWER, power, true)
                .hideFlags(true)
                .build();
    }

    public record Bow(ItemStack itemStack, RARITIES rarity, String name, int power) {}
}
