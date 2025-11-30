package dev.pixeles.apexchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    private final ApexChatPlugin plugin;
    private static final int CLEAR_LINES = 100;

    public ClearChatCommand(ApexChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
            // /clearchat all
            if (!sender.hasPermission("apexchat.clearchat.all")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to clear global chat.");
                return true;
            }
            clearGlobal(sender);
            return true;
        }

        // Self clear
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can clear their own chat.");
            return true;
        }

        Player player = (Player) sender;
        clearSelf(player);
        return true;
    }

    private void clearSelf(Player player) {
        for (int i = 0; i < CLEAR_LINES; i++) {
            player.sendMessage(" ");
        }
        player.sendMessage(ChatColor.GRAY + "Your chat has been cleared.");
    }

    private void clearGlobal(CommandSender sender) {
        String msg;
        if (sender instanceof Player) {
            msg = ChatColor.GRAY + ((Player) sender).getName() + " cleared the chat.";
        } else {
            msg = ChatColor.GRAY + "Chat has been cleared by staff.";
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < CLEAR_LINES; i++) {
                p.sendMessage(" ");
            }
            p.sendMessage(msg);
        }

        sender.sendMessage(ChatColor.GREEN + "You cleared the global chat.");
    }
}
