package me.yourname.pvpflag;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlagCommand implements CommandExecutor {
    private final PvpFlagPlugin plugin;
    private final long cooldownMillis = 86400000; // 24 hours in ms

    public FlagCommand(PvpFlagPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        long lastUsed = plugin.getLastUsed(player);
        long now = System.currentTimeMillis();
        if (now - lastUsed < cooldownMillis) {
            long timeLeft = (cooldownMillis - (now - lastUsed)) / 1000;
            long hours = timeLeft / 3600;
            long minutes = (timeLeft % 3600) / 60;
            player.sendMessage(ChatColor.RED + "You must wait " + hours + "h " + minutes + "m to use /flag again.");
            return true;
        }

        boolean current = plugin.isPvpEnabled(player);
        boolean newState = !current;

        plugin.setPvp(player, newState);
        plugin.setLastUsed(player, now);

        player.sendMessage(ChatColor.GREEN + "PvP is now " + (newState ? ChatColor.RED + "ENABLED" : ChatColor.GRAY + "DISABLED") + ChatColor.GREEN + ".");
        return true;
    }
}
