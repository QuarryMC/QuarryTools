package codes.kooper.quarryTools;

import codes.kooper.quarryTools.commands.ItemCommand;
import codes.kooper.quarryTools.items.ArmorItems;
import codes.kooper.quarryTools.items.BowItems;
import codes.kooper.quarryTools.items.PickaxeItems;
import codes.kooper.quarryTools.items.SwordItems;
import codes.kooper.quarryTools.listeners.*;
import codes.kooper.quarryTools.listeners.skills.AutoRebirthSkill;
import codes.kooper.quarryTools.listeners.skills.HeartSkill;
import codes.kooper.quarryTools.listeners.skills.JackhammerSkill;
import codes.kooper.quarryTools.listeners.skills.RareBlockSkill;
import codes.kooper.quarryTools.managers.ItemManager;
import codes.kooper.quarryTools.managers.MineResetManager;
import codes.kooper.shaded.litecommands.LiteCommands;
import codes.kooper.shaded.litecommands.bukkit.LiteBukkitFactory;
import codes.kooper.shaded.litecommands.bukkit.LiteBukkitMessages;
import codes.kooper.shaded.litecommands.suggestion.SuggestionResult;
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static codes.kooper.koopKore.KoopKore.textUtils;

@Getter
public final class QuarryTools extends JavaPlugin {
    private PickaxeItems pickaxeItems;
    private BowItems bowItems;
    private ArmorItems armorItems;
    private SwordItems swordItems;
    private ItemManager itemManager;
    private MineResetManager mineResetManager;
    private ExecutorService miningThreads;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // PacketEvents & EntityLib
        PacketEvents.getAPI().init();
        SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);
        APIConfig settings = new APIConfig(PacketEvents.getAPI())
                .debugMode()
                .tickTickables()
                .trackPlatformEntities()
                .usePlatformLogger();
        EntityLib.init(platform, settings);

        // Threads
        miningThreads = Executors.newFixedThreadPool(8);

        // Listeners
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new MoonListener(), this);
        getServer().getPluginManager().registerEvents(new PickaxeLevelingListener(), this);
        getServer().getPluginManager().registerEvents(new HeartListener(), this);
        getServer().getPluginManager().registerEvents(new MineResetListener(), this);
        getServer().getPluginManager().registerEvents(new BackpackListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLevelingListener(), this);
        getServer().getPluginManager().registerEvents(new QuarryBlockListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorChangeListener(), this);
        getServer().getPluginManager().registerEvents(new DoubleRebirthListener(), this);

        // Skills
        getServer().getPluginManager().registerEvents(new AutoRebirthSkill(), this);
        getServer().getPluginManager().registerEvents(new HeartSkill(), this);
        getServer().getPluginManager().registerEvents(new JackhammerSkill(), this);
        getServer().getPluginManager().registerEvents(new RareBlockSkill(), this);

        // Managers
        mineResetManager = new MineResetManager();

        // Item Management
        itemManager = new ItemManager();
        pickaxeItems = new PickaxeItems();
        bowItems = new BowItems();
        armorItems = new ArmorItems();
        swordItems = new SwordItems();

        // Commands
        this.liteCommands = LiteBukkitFactory.builder("quarrytools")
                .commands(new ItemCommand())
                .argumentSuggestion(String.class, SuggestionResult.of(itemManager.getItems().keySet()))
                .message(LiteBukkitMessages.INVALID_USAGE, textUtils.error("There's an invalid usage with the /quarrytools command!"))
                .build();
    }

    @Override
    public void onDisable() {
        // Shutdown PacketEvents
        PacketEvents.getAPI().terminate();

        // Shutdown LiteCommands
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        // Shutdown Farming Threads
        miningThreads.shutdown();
        try {
            if (!miningThreads.awaitTermination(60, TimeUnit.SECONDS)) {
                miningThreads.shutdownNow();
            }
        } catch (InterruptedException e) {
            miningThreads.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static QuarryTools getInstance() {
        return QuarryTools.getPlugin(QuarryTools.class);
    }
}
