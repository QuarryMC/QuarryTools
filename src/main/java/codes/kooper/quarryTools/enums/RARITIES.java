package codes.kooper.quarryTools.enums;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Getter
public enum RARITIES {
    COMMON("<#625565>"),
    UNCOMMON("<#92a984>"),
    RARE("<#4d65b4>"),
    EPIC("<#905ea9>"),
    LEGENDARY("<#f79617>"),
    MYTHIC("<#e83b3b>"),
    COSMIC("<#3C4D85>");

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
            case COSMIC -> this.getColor() + "<bold><obfuscated>O</obfuscated> " + this.getColor() + "<bold>COSMIC</bold> <obfuscated><bold>O</bold></obfuscated>";

        };
    }
}
