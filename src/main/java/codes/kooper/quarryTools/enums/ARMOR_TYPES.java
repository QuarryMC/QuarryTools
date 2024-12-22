package codes.kooper.quarryTools.enums;

import lombok.Getter;

@Getter
public enum ARMOR_TYPES {
    HELMET("Helmet"),
    CHESTPLATE("Chestplate"),
    LEGGINGS("Leggings"),
    BOOTS("Boots");

    private final String displayName;

    ARMOR_TYPES(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the lowercase version of the type name (for config keys).
     */
    public String getLowercaseName() {
        return this.name().toLowerCase();
    }

    /**
     * Get the title-cased version of the type name.
     */
    public String getTitleCaseName() {
        return displayName;
    }

    /**
     * Convert a string to its matching enum (ignoring case differences).
     */
    public static ARMOR_TYPES fromString(String name) {
        for (ARMOR_TYPES type : ARMOR_TYPES.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown armor type: " + name);
    }
}