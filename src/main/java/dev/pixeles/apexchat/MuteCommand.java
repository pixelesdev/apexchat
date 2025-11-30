package dev.pixeles.apexchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

    private final ApexChatPlugin plugin;

    public MuteCommand(ApexChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String cmdName = command.getName().toLowerCase();

        switch (cmdName) {
            case "mute":
                return handleMute(sender, args);
            case "unmute":
                return handleUnmute(sender, args);
            case "mutechat":
                return handleMuteChat(sender);
            default:
                return false;
        }
    }

    private boolean handleMute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("apexchat.mute")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to mute players.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /mute <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return true;
        }

        plugin.getChatManager().mute(target.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + "Muted " + target.getName() + ".");
        target.sendMessage(ChatColor.RED + "You have been muted by staff.");
        return true;
    }

    private boolean handleUnmute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("apexchat.mute")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to unmute players.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /unmute <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return true;
        }

        plugin.getChatManager().unmute(target.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + "Unmuted " + target.getName() + ".");
        target.sendMessage(ChatColor.GREEN + "You have been unmuted.");
        return true;
    }

    private boolean handleMuteChat(CommandSender sender) {
        if (!sender.hasPermission("apexchat.mutechat")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to mute the chat.");
            return true;
        }

        boolean newState = !plugin.getChatManager().isGlobalMuted();
        plugin.getChatManager().setGlobalMuted(newState);

        String status = newState ? "muted" : "unmuted";

        String broadcast = ChatColor.RED + "Global chat has been " + status + " by staff.";
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(broadcast);
        }

        sender.sendMessage(ChatColor.GREEN + "You " + status + " chat.");
        return true;
    }
}
