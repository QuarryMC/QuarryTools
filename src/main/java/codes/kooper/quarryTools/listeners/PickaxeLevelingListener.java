package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryTools.QuarryTools;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import codes.kooper.quarryTools.items.PickaxeItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PickaxeLevelingListener implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        int amount = 2;

        double multi;
        if (event.getUser().hasSkill("pickaxe_xp") && !event.getUser().hasDisabledSkill("pickaxe_xp")) {
            long level = event.getUser().getSkillLevel("pickaxe_xp");
            multi = (level / 5.0 * 0.1) + 1;
        } else {
            multi = 1;
        }

        Player player = event.getPlayer();
        double armorMulti;
        if (QuarryTools.getInstance().getArmorItems().inFullSetCache(player.getUniqueId())) {
            armorMulti = switch (QuarryTools.getInstance().getArmorItems().getFullSet(player.getUniqueId()).setName()) {
                case "phantom" -> 2.0;
                case "iron_golem" -> 1.7;
                case "polar_bear" -> 1.6;
                case "zoglin" -> 1.4;
                case "skeleton_horse" -> 1.2;
                default -> 1.0;
            };
        } else {
            armorMulti = 1.0;
        }

        Tasks.runSync(() -> PickaxeItems.addPickaxeXP(event.getPlayer(), event.getPickaxe(), amount * multi * armorMulti));
    }

}
