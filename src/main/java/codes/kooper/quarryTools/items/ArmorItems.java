package codes.kooper.quarryTools.items;

import codes.kooper.koopKore.item.ItemBuilder;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.enums.ARMOR_TYPES;
import codes.kooper.quarryTools.utils.ColorUtils;
import codes.kooper.shaded.nbtapi.NBT;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class ArmorItems {
    private final Map<String, ArmorSet> armorSets;
    private final Map<UUID, ArmorSet> fullSetCache;

    public ArmorItems() {
        armorSets = new HashMap<>();
        fullSetCache = new HashMap<>();
        ConfigurationSection armorConfig = QuarryTools.getInstance().getConfig().getConfigurationSection("armor");
        if (armorConfig == null) {
            QuarryTools.getInstance().getLogger().severe("[Armor] Could not load armor sets from config.yml!");
            return;
        }

        for (String setName : armorConfig.getKeys(false)) {
            Map<ARMOR_TYPES, Armor> armorPieces = new HashMap<>();
            ConfigurationSection setConfig = armorConfig.getConfigurationSection(setName);
            if (setConfig == null) {
                QuarryTools.getInstance().getLogger().severe("[Armor] Could not load armor items for set: " + setName);
                continue;
            }

            String displayName = setConfig.getString("display_name", setName);
            String color = setConfig.getString("color", "white");

            ConfigurationSection setBonusConfig = setConfig.getConfigurationSection("set_bonus");
            SetBonus setBonus = null;
            if (setBonusConfig != null) {
                setBonus = new SetBonus(
                        setBonusConfig.getInt("extra_pickaxe_xp", 0),
                        setBonusConfig.getInt("double_rebirth_chance", 0),
                        setBonusConfig.getDouble("sell_bonus", 0.0)
                );
            }

            ConfigurationSection itemsConfig = setConfig.getConfigurationSection("items");
            if (itemsConfig == null) {
                QuarryTools.getInstance().getLogger().severe("[Armor] Missing items section for set: " + setName);
                continue;
            }

            for (ARMOR_TYPES armorType : ARMOR_TYPES.values()) {
                ConfigurationSection pieceConfig = itemsConfig.getConfigurationSection(armorType.name().toLowerCase());
                if (pieceConfig == null) {
                    QuarryTools.getInstance().getLogger().warning("[Armor] Missing " + armorType.name().toLowerCase() + " for " + setName);
                    continue;
                }

                ItemStack item = getArmorItem(
                        setName,
                        pieceConfig.getString("name", setName + " " + armorType.getTitleCaseName()),
                        pieceConfig.getString("material", "LEATHER_CHESTPLATE"),
                        pieceConfig.getString("skull", null),
                        color,
                        setBonus
                );

                armorPieces.put(armorType, new Armor(item, setName, armorType, pieceConfig.getInt("protection")));
                QuarryTools.getInstance().getItemManager().addItem(setName + "_" + armorType.name().toLowerCase(), item);
            }

            armorSets.put(setName, new ArmorSet(armorPieces, setName, displayName, color, setBonus));
        }
    }

    public ArmorSet getArmorSet(String setName) {
        return armorSets.get(setName);
    }

    public void addFullSet(UUID uuid, ArmorSet armorSet) {
        fullSetCache.put(uuid, armorSet);
    }

    public static boolean hasFullArmorSet(Player player) {
        ArmorItems.ArmorSet lastArmorSet = null;
        for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
            if (armorPiece == null || armorPiece.isEmpty() || !ArmorItems.isArmor(armorPiece)) return false;
            ArmorItems.ArmorSet set = QuarryTools.getInstance().getArmorItems().getArmorSet(armorPiece);
            if (lastArmorSet != null && set != lastArmorSet) return false;
            lastArmorSet = set;
        }
        return true;
    }

    public void removeFullSet(UUID uuid) {
        fullSetCache.remove(uuid);
    }

    public boolean inFullSetCache(UUID uuid) {
        return fullSetCache.containsKey(uuid);
    }

    public ArmorSet getFullSet(UUID uuid) {
        return fullSetCache.get(uuid);
    }

    public Armor getArmorPiece(String setName, ARMOR_TYPES type) {
        ArmorSet set = getArmorSet(setName);
        if (set != null) {
            return set.getArmorPiece(type);
        }
        return null;
    }

    public Set<ArmorSet> getAllArmorSets() {
        return new HashSet<>(armorSets.values());
    }

    private ItemStack getArmorItem(String key, String name, String material, String skull, String color, SetBonus setBonus) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(material.toUpperCase()));
        itemBuilder.setName(name);
        itemBuilder.setLore(getLore(color, setBonus));

        itemBuilder.setUnbreakable(true);
        itemBuilder.hideFlags(true);

        if (skull != null && !skull.isEmpty()) {
            itemBuilder.setTexture(skull);
        }

        itemBuilder.hideFlags(true);
        int[] rgb = ColorUtils.getRGB(color.replaceAll("<", "").replaceAll(">", ""));
        itemBuilder.setColor(rgb[0], rgb[1], rgb[2]);

        ItemStack item = itemBuilder.build();
        NBT.modify(item, (nbt) -> {
            nbt.setString("armor", key);
        });
        return item;
    }

    public static boolean isArmor(ItemStack item) {
        return NBT.get(item, (nbt) -> {
            return nbt.hasTag("armor");
        });
    }

    public ArmorSet getArmorSet(ItemStack item) {
        String name = NBT.get(item, (nbt) -> {
            return nbt.getString("armor");
        });
        return armorSets.get(name);
    }

    public static List<String> getLore(String color, SetBonus setBonus) {
        List<String> lore = new ArrayList<>();

        // Divider to separate base stats from the set bonuses
        lore.add("");
        lore.add(String.format("%s<dark_gray>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</dark_gray>", color));

        // Full Set Bonus Title
        lore.add(String.format("%s<bold><gradient:#FFFF00:#FFA500>FULL SET BONUS</gradient></bold>", color));
        lore.add(String.format("%s<gray>These effects activate when the full set is equipped:</gray>", color));

        // Full Set Bonus Details
        if (setBonus != null) {
            if (setBonus.extraPickaxeXp() > 0) lore.add(String.format("<#B59410>➜ <gold>+%d%% Extra Pickaxe XP</gold>", setBonus.extraPickaxeXp()));
            if (setBonus.doubleRebirthChance() > 0) lore.add(String.format("<#B59410>➜ <gold>%d%% Chance for Double Rebirth</gold>", setBonus.doubleRebirthChance()));
            if (setBonus.sellBonus() > 0) lore.add(String.format("<#B59410>➜ <gold>+%.1fx Sell Multiplier</gold>", setBonus.sellBonus()));
        }

        // Divider to end the lore
        lore.add(String.format("%s<dark_gray>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</dark_gray>", color));

        return lore;
    }

    public record Armor(ItemStack itemStack, String setName, ARMOR_TYPES armorType, int protection) {}

    public record ArmorSet(Map<ARMOR_TYPES, Armor> armorPieces, String setName, String displayName, String color, SetBonus setBonus) {
        public Armor getArmorPiece(ARMOR_TYPES type) {
            return armorPieces.get(type);
        }
    }

    public record SetBonus(int extraPickaxeXp, int doubleRebirthChance, double sellBonus) {}
}
