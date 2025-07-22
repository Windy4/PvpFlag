package me.yourname.pvpflag;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class PvpFlagPlugin extends JavaPlugin implements Listener {
    private File dataFile;
    private FileConfiguration dataConfig;

    @Override
    public void onEnable() {
        createDataFile();
        getCommand("flag").setExecutor(new FlagCommand(this));
        getServer().getPluginManager().registerEvents(this, this);
    }

    public FileConfiguration getData() {
        return dataConfig;
    }

    public void saveData() {
        try {
            dataConfig.save(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
            if (!isPvpEnabled(victim) || !isPvpEnabled(attacker)) {
                event.setCancelled(true);
            }
        }
    }
    
    private void createDataFile() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public boolean isPvpEnabled(Player player) {
        return getData().getBoolean("players." + player.getUniqueId() + ".pvp", false);
    }

    public void setPvp(Player player, boolean enabled) {
        getData().set("players." + player.getUniqueId() + ".pvp", enabled);
        saveData();
    }

    public long getLastUsed(Player player) {
        return getData().getLong("players." + player.getUniqueId() + ".lastUsed", 0);
    }

    public void setLastUsed(Player player, long time) {
        getData().set("players." + player.getUniqueId() + ".lastUsed", time);
        saveData();
    }
}
