package codes.kooper.quarryTools.items;

import codes.kooper.koopKore.item.ItemBuilder;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.enums.RARITIES;
import codes.kooper.quarryTools.enums.TOOL_TYPES;
import codes.kooper.shaded.nbtapi.NBT;
import dev.lone.itemsadder.api.CustomStack;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static codes.kooper.koopKore.KoopKore.numberUtils;
import static codes.kooper.koopKore.KoopKore.textUtils;

public class PickaxeItems {
    private final Map<RARITIES, Map<String, Pickaxe>> pickaxes;

    public PickaxeItems() {
        pickaxes = new HashMap<>();
        int pickaxeIndex = 1;
        for (RARITIES rarity : RARITIES.values()) {
            pickaxes.put(rarity, new HashMap<>());
            ConfigurationSection pickaxeConfig = QuarryTools.getInstance().getConfig().getConfigurationSection("pickaxes." + rarity.name().toLowerCase());
            if (pickaxeConfig == null) {
                QuarryTools.getInstance().getLogger().severe("[Pickaxes] Could not load pickaxes in QuarryTools from config.yml!");
                return;
            }
            if (rarity == RARITIES.COSMIC) {
                for (String pickName : pickaxeConfig.getKeys(false)) {
                    ConfigurationSection section = pickaxeConfig.getConfigurationSection(pickName);
                    if (section == null) continue;
                    int fortune = section.getInt("fortune");
                    int model = section.getInt("model");
                    ItemStack item = getPickaxeItem(model, rarity, pickName, fortune);
                    pickaxes.get(rarity).put(pickName, new Pickaxe(item, rarity, pickName, fortune));
                    QuarryTools.getInstance().getItemManager().addItem(pickName + "_pickaxe", item);
                }
            } else {
                for (String pickName : pickaxeConfig.getKeys(false)) {
                    int fortune = pickaxeConfig.getInt(pickName);
                    ItemStack item = getPickaxeItem(pickaxeIndex, rarity, pickName, fortune);
                    pickaxes.get(rarity).put(pickName, new Pickaxe(item, rarity, pickName, fortune));
                    QuarryTools.getInstance().getItemManager().addItem(pickName + "_pickaxe", item);
                    pickaxeIndex++;
                }
            }
        }
    }

    public Pickaxe getPickaxe(RARITIES rarity, String name) {
        return pickaxes.get(rarity).get(name);
    }

    public Set<Pickaxe> getPickaxes(RARITIES rarity) {
        return new HashSet<>(pickaxes.get(rarity).values());
    }

    private ItemStack getPickaxeItem(int id, RARITIES rarity, String name, int fortune) {
        TOOL_TYPES pickaxe = TOOL_TYPES.PICKAXE;
        ItemStack itemStack;
        if (rarity != RARITIES.COSMIC) {
            CustomStack stack = CustomStack.getInstance("pickaxe_fantasy" + id);
            if (stack != null) {
                itemStack = stack.getItemStack();
            } else {
                QuarryTools.getInstance().getLogger().severe("Could not load pickaxe model for: " + pickaxe);
                return null;
            }
        } else {
            itemStack = new ItemStack(Material.NETHERITE_PICKAXE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(id);
            itemStack.setItemMeta(itemMeta);
        }
        ItemStack item = new ItemBuilder(itemStack)
                .setName(rarity.getToolName(pickaxe, name))
                .setLore(pickaxe.getLore(fortune, rarity))
                .addEnchant(Enchantment.EFFICIENCY, 500, true)
                .setUnbreakable(true)
                .build();
        NBT.modify(item, nbt -> {
            nbt.setString("pickaxe", name);
            nbt.setInteger("level", 1);
            nbt.setInteger("fortune", fortune);
            nbt.setDouble("fortune-boost", 0.1);
        });
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addAttributeModifier(Attribute.SPAWN_REINFORCEMENTS, new AttributeModifier(NamespacedKey.fromString("attribute"), 0, AttributeModifier.Operation.ADD_NUMBER));
        itemMeta.removeItemFlags(ItemFlag.values());
        item.setItemMeta(itemMeta);
        return item;
    }

    public static double getFortuneBoost(ItemStack tool) {
        return NBT.get(tool, (nbt) -> {
            return nbt.getDouble("fortune-boost");
        });
    }

    public static int getFortune(ItemStack tool) {
        return NBT.get(tool, (nbt) -> {
            return nbt.getInteger("fortune");
        });
    }

    public Pickaxe getPickaxe(String name) {
        for (Map<String, Pickaxe> pick : pickaxes.values()) {
            if (pick.containsKey(name)) {
                return pick.get(name);
            }
        }
        return null;
    }

    public Pickaxe getPickaxe(ItemStack tool) {
        return getPickaxe(getPickaxeName(tool));
    }

    public static String getPickaxeName(ItemStack tool) {
        return NBT.get(tool, nbt -> {
            return nbt.getString("pickaxe");
        });
    }

    public static boolean isPickaxe(ItemStack tool) {
        if (tool == null || tool.isEmpty()) return false;
        return NBT.get(tool, nbt -> {
            return nbt.hasTag("pickaxe");
        });
    }

    public static int getXPCost(int level) {
        if (level >= 100) return -1;
        return (int) (1000 * level + Math.pow(level, 2) * 50) * 50;
    }

    public static void addPickaxeXP(Player player, Pickaxe pickaxe, double amount) {
        ItemStack tool = player.getInventory().getItemInMainHand();

        int level = NBT.get(tool, (nbt) -> {
            return nbt.getInteger("level");
        });
        int xpCost = getXPCost(level);
        if (xpCost == -1) return;
        int currentXP = player.getTotalExperience();
        player.giveExp((int) amount);
        int newXP = player.getTotalExperience();

        // Check if player has enough XP to level up the pickaxe
        if (newXP >= xpCost) {
            levelPickaxe(player, pickaxe);
            return;
        }

        // Calculate percentage progress towards next XP cost
        double progressPercentage = ((double) newXP / xpCost) * 100;
        double previousProgressPercentage = ((double) currentXP / xpCost) * 100;

        // Call updatePickaxeLore only if progress crosses a 10% threshold
        int currentProgressMarker = (int) (progressPercentage / 10);
        int previousProgressMarker = (int) (previousProgressPercentage / 10);

        if (currentProgressMarker > previousProgressMarker) {
            updatePickaxeLore(player, pickaxe);
        }
    }


    public static void levelPickaxe(Player player, Pickaxe pickaxe) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        int level = NBT.get(tool, nbt -> {
            return nbt.getInteger("level");
        });
        double fortuneBoost = NBT.get(tool, nbt -> {
            return nbt.getDouble("fortune-boost");
        });
        int xpCost = getXPCost(level);
        if (xpCost == -1) return;
        NBT.modify(tool, nbt -> {
            nbt.setInteger("level", level + 1);
            nbt.setDouble("fortune-boost", fortuneBoost + 0.5);
        });
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);
        player.giveExp(-xpCost);
        player.sendMessage(textUtils.colorize(
        "<white><newline><#4d9be6>You have reached <white><bold>LEVEL " + (level + 1) + " <reset><#4d9be6>on your <white><bold>" + textUtils.capitalize(pickaxe.name) + " Pickaxe<reset><#4d9be6>!<reset><newline>" +
            "<gray><italic>( Your Pickaxe got +0.5% FORTUNE BOOST for this LEVEL UP! )<reset><newline><white>"
        ));
        updatePickaxeLore(player, pickaxe);
    }

    public static void updatePickaxeLore(Player player, Pickaxe pickaxe) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        RARITIES rarity = pickaxe.rarity;
        int level = NBT.get(tool, nbt -> {
            return nbt.getInteger("level");
        });
        double fortuneBoost = NBT.get(tool, nbt -> {
            return nbt.getDouble("fortune-boost");
        });
        int cost = getXPCost(level);
        String progressBar;
        if (cost == -1) {
            progressBar = "<#FFD700>MAXED";
        } else {
            progressBar = textUtils.progressBar(player.getTotalExperience(), cost, 10, "┃", rarity.getColor(), "<color:#b2ba90>");
        }
        List<Component> newLore = List.of(
                Component.empty(),
                textUtils.colorize(rarity.getColor() + "<bold>|</bold> <color:#1ebc73>Fortune " + numberUtils.commaFormat(pickaxe.fortune) + "</color>"),
                textUtils.colorize(rarity.getColor() + "<bold>|</bold> <color:#9babb2>Level " + level + "</color> " + rarity.getColor() + "(" + progressBar + rarity.getColor() + ")"),
                Component.empty(),
                textUtils.colorize(rarity.getColor() + "<bold>|</bold> <color:#92a984>Fortune boost</color> <color:#1ebc73>+" + fortuneBoost + "%</color>"),
                Component.empty(),
                textUtils.colorize(rarity.getLoreLine())
        );
        tool.lore(newLore);
    }

    public record Pickaxe(ItemStack itemStack, RARITIES rarity, String name, int fortune) {}
}