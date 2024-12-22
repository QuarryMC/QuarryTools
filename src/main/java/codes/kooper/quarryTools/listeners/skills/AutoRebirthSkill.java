package codes.kooper.quarryTools.listeners.skills;

import codes.kooper.koopKore.database.models.User;
import codes.kooper.koopKore.utils.Tasks;
import codes.kooper.quarryEconomy.QuarryEconomy;
import codes.kooper.quarryTools.events.QuarryMineEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AutoRebirthSkill implements Listener {

    @EventHandler
    public void onBreak(QuarryMineEvent event) {
        User user = event.getUser();
        if (!user.hasSkill("auto_rebirth") || user.getMined() % 30 != 0 || user.hasDisabledSkill("auto_rebirth")) return;
        Tasks.runSync(() -> QuarryEconomy.getInstance().getLevelingUtils().rebirth(user));
    }

}
