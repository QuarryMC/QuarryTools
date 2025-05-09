package codes.kooper.quarryTools.listeners;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.quarryEconomy.QuarryEconomy;
import codes.kooper.quarryMoons.QuarryMoons;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLevelingListener implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        int xp = switch (QuarryMoons.getInstance().getMoonManager().getCurrentMoon()) {
            case RED -> 4;
            case LIME -> 6;
            case LIGHT_BLUE -> 7;
            case BLUE -> 8;
            case PURPLE -> 9;
            case YELLOW -> 10;
            default -> 5;
        };
        xp *= 8;

        User user = event.getUser();
        if (user.hasSkill("player_xp_boost") && !user.hasDisabledSkill("player_xp_boost")) {
            xp *= ((int) (user.getSkillLevel("player_xp_boost") / 100.0)) + 1;
        }

        QuarryEconomy.getInstance().getLevelingUtils().addPlayerXP(event.getUser(), xp);

        if (event.getUser().getLevel() >= 50 && event.getUser().isAutoPrestige() && event.getUser().getMined() % 30 == 0) {
            QuarryEconomy.getInstance().getLevelingUtils().prestige(event.getUser());
        }
    }

}
