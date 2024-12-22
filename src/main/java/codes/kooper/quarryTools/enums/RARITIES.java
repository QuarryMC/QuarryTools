package codes.kooper.quarryTools.enums;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Getter
public enum RARITIES {
    COMMON("<#625565>"),
    UNCOMMON("<#92a984>"),
    RARE("<#4d65b4>"),
    EPIC("<#905ea9>"),
    LEGENDARY("<#f79617>"),
    MYTHIC("<#e83b3b>");

    private final String color;

    RARITIES(String color) {
        this.color = color;
    }

    public Component getToolName(TOOL_TYPES type, String name) {
        String formattedType = textUtils.capitalize(type.name());
        String formattedName = textUtils.capitalize(name);
        return textUtils.colorize(this.getColor() + "<bold>" + formattedName + " " + formattedType);
    }

    public String getLoreLine() {
        return switch (this) {
            case COMMON -> this.getColor() + "<bold>COMMON</bold>";
            case UNCOMMON -> this.getColor() + "<bold>UNCOMMON</bold>";
            case RARE -> this.getColor() + "<bold>RARE</bold>";
            case EPIC -> this.getColor() + "<bold>EPIC</bold>";
            case LEGENDARY -> this.getColor() + "<bold><obfuscated>O</obfuscated> " + this.getColor() + "<bold>LEGENDARY</bold> <obfuscated><bold>O</bold></obfuscated>";
            case MYTHIC -> this.getColor() + "<bold><obfuscated>O</obfuscated> " + this.getColor() + "<bold>MYTHIC</bold> <obfuscated><bold>O</bold></obfuscated>";
        };
    }

    public Material getPickaxeType() {
        return switch (this) {
            case COMMON -> Material.WOODEN_PICKAXE;
            case UNCOMMON -> Material.STONE_PICKAXE;
            case RARE -> Material.IRON_PICKAXE;
            case EPIC -> Material.GOLDEN_PICKAXE;
            case LEGENDARY -> Material.DIAMOND_PICKAXE;
            case MYTHIC -> Material.NETHERITE_PICKAXE;
        };
    }

    public Material getSwordType() {
        return switch (this) {
            case COMMON -> Material.WOODEN_SWORD;
            case UNCOMMON -> Material.STONE_SWORD;
            case RARE -> Material.IRON_SWORD;
            case EPIC -> Material.GOLDEN_SWORD;
            case LEGENDARY -> Material.DIAMOND_SWORD;
            case MYTHIC -> Material.NETHERITE_SWORD;
        };
    }
}
