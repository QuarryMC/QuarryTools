package codes.kooper.quarryTools.items;

import codes.kooper.koopKore.item.ItemBuilder;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.enums.RARITIES;
import codes.kooper.quarryTools.enums.TOOL_TYPES;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SwordItems {
    private final Map<RARITIES, Map<String, Sword>> swords;

    public SwordItems() {
        swords = new HashMap<>();
        for (RARITIES rarity : RARITIES.values()) {
            swords.put(rarity, new HashMap<>());
            ConfigurationSection swordsConfig = QuarryTools.getInstance().getConfig().getConfigurationSection("swords." + rarity.name().toLowerCase());
            if (swordsConfig == null) {
                QuarryTools.getInstance().getLogger().severe("[Swords] Could not load swords in QuarryTools from config.yml!");
                return;
            }
            for (String swordName : swordsConfig.getKeys(false)) {
                int sharpness = swordsConfig.getInt(swordName);
                ItemStack item = getSwordItem(rarity, swordName, sharpness);
                swords.get(rarity).put(swordName, new Sword(item, rarity, swordName, sharpness));
                QuarryTools.getInstance().getItemManager().addItem(swordName + "_sword", item);
            }
        }
    }

    public Sword getSword(RARITIES rarity, String name) {
        return swords.get(rarity).get(name);
    }

    public Set<Sword> getSwords(RARITIES rarity) {
        return new HashSet<>(swords.get(rarity).values());
    }

    private ItemStack getSwordItem(RARITIES rarity, String name, int sharpness) {
        TOOL_TYPES sword = TOOL_TYPES.SWORD;
        return new ItemBuilder(rarity.getSwordType())
                .setName(rarity.getToolName(sword, name))
                .setLore(sword.getLore(sharpness, rarity))
                .setUnbreakable(true)
                .addEnchant(Enchantment.SHARPNESS, sharpness, true)
                .hideFlags(true)
                .build();
    }

    public record Sword(ItemStack itemStack, RARITIES rarity, String name, int sharpness) {}
}
