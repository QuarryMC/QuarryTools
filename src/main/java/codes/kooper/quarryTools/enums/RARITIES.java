package codes.kooper.quarryTools.enums;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Getter
public enum RARITIES {
    COMMON("<#625565>", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE2MjcyNTU0MzE2NjVmZTFkYmJiNDljODIxNjVkNjk5ZjMwNjYyMDg0OTVlYTM1NjliZGZlMGQwYjU3MDA3ZCJ9fX0="),
    UNCOMMON("<#92a984>", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE4NjVkZTFhNzdkYTg2Zjg0MjNhZjRiYTQ0MTAxMzk3NDI5ZDIwMGJlNDIwNDU2NTEyMTJhNjE1Y2Y3NTRmMiJ9fX0="),
    RARE("<#4d65b4>", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGViZjZiZTgyZTgyM2U1ODJmZjhmNDVlMDFjZjUxNzUxNzM0ZDlhNzc2Y2ZmNTE0ZTg4ZWEyNmQ0OTczYmJmMCJ9fX0="),
    EPIC("<#905ea9>", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNjY2Y5ZjZmZWE4ZDg4YmI1YTA5NzA0ZDlmMzY1N2NhYWM0MzZlZmMwMDI0MWE1ZmYwNDFjOGI1NjJkYjU0NCJ9fX0="),
    LEGENDARY("<#f79617>", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDVlMTVkMTVkNjA4YTg2MzcyYmI1YzNkMTBiMDZlNjI4Yzg3ZWQzMTAwOWFkMzg5MWM1OTIyYjNmYmQ4Njc3MSJ9fX0="),
    MYTHIC("<#e83b3b>", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU0NGIwOWQ1NTljZWJiNDA2YjcyMjcwMGYzYmQzN2NiMTZkMjhkMmY2ODE0YjU4M2M2NDI3MDA4NGJjNjhiMyJ9fX0="),
    COSMIC("<#3C4D85>", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmY1NjhkZDgwODIwNjU3NTU0ZjliNDVkYjAzMWQyMmNkOWI3ZDE2MWMxOTgyNzczYjFiMzU4NTRjOTkzOTgyNCJ9fX0=");

    private final String color;
    private final String texture;

    RARITIES(String color, String texture) {
        this.color = color;
        this.texture = texture;
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
