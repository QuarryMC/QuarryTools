package codes.kooper.quarryTools.database.models;

import codes.kooper.koopKore.item.ItemBuilder;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.enums.RARITIES;
import codes.kooper.quarryTools.enums.TOOL_TYPES;
import codes.kooper.quarryTools.items.PickaxeItems;
import codes.kooper.shaded.nbtapi.NBT;
import dev.lone.itemsadder.api.CustomStack;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static codes.kooper.koopKore.KoopKore.numberUtils;
import static codes.kooper.koopKore.KoopKore.textUtils;

@Getter
@Setter
@ToString
public class Pickaxe {
    private String name;
    private double fortuneBoost;
    private int fortune;
    private int level;

    public Pickaxe() {}

    public Pickaxe(PickaxeItems.Pickaxe pickaxe) {
        name = pickaxe.name();
        fortune = pickaxe.fortune();
        level = 1;
        fortuneBoost = 0.1;
    }

    @BsonIgnore
    public ItemStack toItem(Player player) {
        TOOL_TYPES pickaxe = TOOL_TYPES.PICKAXE;
        int cost = PickaxeItems.getXPCost(level);
        PickaxeItems.Pickaxe pick = QuarryTools.getInstance().getPickaxeItems().getPickaxe(name);
        String progressBar = textUtils.progressBar(player.getTotalExperience(), cost, 10, "┃", pick.rarity().getColor(), "<color:#b2ba90>");
        ItemStack item = new ItemBuilder(pick.itemStack())
                .setName(pick.rarity().getToolName(pickaxe, name))
                .setLore(List.of(
                    "",
                    pick.rarity().getColor() + "<bold>|</bold> <color:#1ebc73>Fortune " + numberUtils.commaFormat(fortune) + "</color>",
                    pick.rarity().getColor() + "<bold>|</bold> <color:#9babb2>Level " + level + "</color> " + pick.rarity().getColor() + "(" + progressBar + pick.rarity().getColor() + ")",
                    "",
                    pick.rarity().getColor() + "<bold>|</bold> <color:#92a984>Fortune boost</color> <color:#1ebc73>+" + fortuneBoost + "%</color>",
                    "",
                    pick.rarity().getLoreLine()
                ))
                .addEnchant(Enchantment.EFFICIENCY, 500, true)
                .setUnbreakable(true)
                .hideFlags(true)
                .build();
        NBT.modify(item, nbt -> {
            nbt.setString("pickaxe", name);
            nbt.setInteger("level", level);
            nbt.setInteger("fortune", fortune);
            nbt.setDouble("fortune-boost", fortuneBoost);
        });
        return item;
    }
}
