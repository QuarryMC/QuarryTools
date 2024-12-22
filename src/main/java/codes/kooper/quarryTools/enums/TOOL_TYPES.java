package codes.kooper.quarryTools.enums;

import java.util.List;

import static codes.kooper.koopKore.KoopKore.numberUtils;

public enum TOOL_TYPES {
    BOW,
    PICKAXE,
    SWORD;

    public List<String> getLore(int value, RARITIES rarity) {
        return switch(this) {
            case BOW -> List.of(
                    "",
                    rarity.getColor() + "<bold>|</bold> <color:#b2ba90>Power " + numberUtils.commaFormat(value) + "</color>",
                    "",
                    rarity.getLoreLine()
            );
            case PICKAXE -> List.of(
                    "",
                    rarity.getColor() + "<bold>|</bold> <color:#1ebc73>Fortune " + numberUtils.commaFormat(value) + "</color>",
                    rarity.getColor() + "<bold>|</bold> <color:#9babb2>Level 1</color> " + rarity.getColor() + "(<color:#b2ba90>┃┃┃┃┃┃┃┃┃┃</color>)",
                    "",
                    rarity.getColor() + "<bold>|</bold> <color:#92a984>Fortune boost</color> <color:#1ebc73>+0.1%</color>",
                    "",
                    rarity.getLoreLine()
            );
            case SWORD -> List.of(
                    "",
                    rarity.getColor() + "<bold>|</bold> <color:#b2ba90>Sharpness " + numberUtils.commaFormat(value) + "</color>",
                    "",
                    rarity.getLoreLine()
            );
        };
    }
}