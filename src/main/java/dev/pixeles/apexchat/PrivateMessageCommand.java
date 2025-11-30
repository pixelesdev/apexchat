package dev.pixeles.apexchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrivateMessageCommand implements CommandExecutor {

    private final ApexChatPlugin plugin;

    public PrivateMessageCommand(ApexChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use private messages.");
            return true;
        }

        Player player = (Player) sender;

        // Block sending PMs if muted and no bypass
        if (plugin.getChatManager().isMuted(player.getUniqueId())
                && !player.hasPermission("apexchat.bypass.mute")) {
            player.sendMessage(ChatColor.RED + "You are muted and cannot send private messages.");
            return true;
        }

        String cmdName = command.getName().toLowerCase();

        if (cmdName.equals("pmsg")) {
            return handlePmsg(player, args);
        } else if (cmdName.equals("reply")) {
            return handleReply(player, args);
        }

        return false;
    }

    private boolean handlePmsg(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /pmsg <player> <message>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player is not online.");
            return true;
        }

        if (target.getUniqueId().equals(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You cannot send messages to yourself.");
            return true;
        }

        String message = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        sendPrivateMessage(sender, target, message);
        return true;
    }

    private boolean handleReply(Player sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /reply <message>");
            return true;
        }

        UUID targetUuid = plugin.getChatManager().getLastPrivatePartner(sender.getUniqueId());
        if (targetUuid == null) {
            sender.sendMessage(ChatColor.RED + "You have nobody to reply to.");
            return true;
        }

        Player target = Bukkit.getPlayer(targetUuid);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "That player is no longer online.");
            return true;
        }

        String message = String.join(" ", args);
        sendPrivateMessage(sender, target, message);
        return true;
    }

    private void sendPrivateMessage(Player sender, Player target, String message) {
        String toTarget = ChatColor.GOLD + "[From " + sender.getName() + "] " + ChatColor.WHITE + message;
        String toSender = ChatColor.GOLD + "[To " + target.getName() + "] " + ChatColor.WHITE + message;

        target.sendMessage(toTarget);
        sender.sendMessage(toSender);

        plugin.getChatManager().setLastPrivatePartner(sender.getUniqueId(), target.getUniqueId());
    }
}
