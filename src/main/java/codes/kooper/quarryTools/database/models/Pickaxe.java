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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    public ItemStack toItem() {
        TOOL_TYPES pickaxe = TOOL_TYPES.PICKAXE;
        PickaxeItems.Pickaxe pick = QuarryTools.getInstance().getPickaxeItems().getPickaxe(name);
        ItemStack item = new ItemBuilder(pick.itemStack())
                .setName(pick.rarity().getToolName(pickaxe, name))
                .setLore(pickaxe.getLore(fortune, pick.rarity()))
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
