package nebulous.embassy;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Embassy embassy;
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        EmbassySaveData.Load();
        embassy = Embassy.createDefaultEmbassy();
        // Plugin startup logic
        this.getCommand("embassy").setExecutor(new Executors());
        this.getCommand("embassy").setTabCompleter(new Completer());
    }

    @Override
    public void onDisable() {
        EmbassySaveData.Save();
    }
}
