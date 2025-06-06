package codes.kooper.quarryTools.database.models;

import codes.kooper.koopKore.database.models.BaseEntity;
import codes.kooper.shaded.nbtapi.NBT;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class PickaxeStorage extends BaseEntity {
    private UUID owner;
    private Map<String, Pickaxe> pickaxes;
    private Pickaxe selected;

    public PickaxeStorage() {
        pickaxes = new HashMap<>();
    }

    public PickaxeStorage(UUID owner, Pickaxe selected) {
        this.owner = owner;
        pickaxes = new HashMap<>();
        pickaxes.put(selected.getName(), selected);
        this.selected = selected;
    }

    @Override
    public UUID getId() {
        return owner;
    }

    @Override
    public void setId(UUID uuid) {
        owner = uuid;
    }

    @BsonIgnore
    public boolean hasPickaxe(String name) {
        return pickaxes.containsKey(name);
    }

    @BsonIgnore
    public Pickaxe getPickaxe(String name) {
        return pickaxes.get(name);
    }

    @BsonIgnore
    public void updateSelected(ItemStack tool) {
        NBT.get(tool, nbt -> {
            selected.setFortune(nbt.getInteger("fortune"));
            selected.setLevel(nbt.getInteger("level"));
            selected.setFortuneBoost(nbt.getDouble("fortune-boost"));
        });
    }
}
