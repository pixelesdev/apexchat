package dev.pixeles.apexchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.UUID;

public class ApexChatPlugin extends JavaPlugin implements Listener {

    private ChatManager chatManager;
    private boolean luckPermsDetected;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.chatManager = new ChatManager();

        // Detect LuckPerms in a soft way
        this.luckPermsDetected = getServer().getPluginManager().isPluginEnabled("LuckPerms");
        if (luckPermsDetected) {
            getLogger().info("ApexChat: LuckPerms detected, will try to use prefixes via reflection.");
        } else {
            getLogger().info("ApexChat: LuckPerms not found, running without prefix support.");
        }

        // Register events
        getServer().getPluginManager().registerEvents(this, this);

        // Register commands (classic plugin.yml style)
        if (getCommand("clearchat") != null) {
            getCommand("clearchat").setExecutor(new ClearChatCommand(this));
        }
        PrivateMessageCommand pmExecutor = new PrivateMessageCommand(this);
        if (getCommand("pmsg") != null) {
            getCommand("pmsg").setExecutor(pmExecutor);
        }
        if (getCommand("reply") != null) {
            getCommand("reply").setExecutor(pmExecutor);
        }
        MuteCommand muteExecutor = new MuteCommand(this);
        if (getCommand("mute") != null) {
            getCommand("mute").setExecutor(muteExecutor);
        }
        if (getCommand("unmute") != null) {
            getCommand("unmute").setExecutor(muteExecutor);
        }
        if (getCommand("mutechat") != null) {
            getCommand("mutechat").setExecutor(muteExecutor);
        }

        getLogger().info("ApexChat enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ApexChat disabled.");
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    /**
     * Chat handling using AsyncPlayerChatEvent (Spigot/Bukkit API, works across 1.21.x).
     */
    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // Check personal mute
        if (chatManager.isMuted(player.getUniqueId()) && !player.hasPermission("apexchat.bypass.mute")) {
            player.sendMessage(ChatColor.RED + "You are muted and cannot chat.");
            event.setCancelled(true);
            return;
        }

        // Check global mute
        if (chatManager.isGlobalMuted() && !player.hasPermission("apexchat.bypass.globalmute")) {
            player.sendMessage(ChatColor.RED + "Chat is currently muted.");
            event.setCancelled(true);
            return;
        }

        // Get LuckPerms prefix (if available)
        String prefix = null;
        if (luckPermsDetected) {
            prefix = getLuckPermsPrefix(player);
        }
        if (prefix == null) {
            prefix = "";
        }

        // Format: [prefix] Name » message
        // AsyncPlayerChatEvent expects a format string with %1$s = player, %2$s = message
        String format = prefix + ChatColor.RESET + "%1$s" + ChatColor.DARK_GRAY + " \u00BB " + ChatColor.RESET + "%2$s";
        event.setFormat(format);
    }

    /**
     * Try to get a LuckPerms prefix using reflection.
     * If anything fails, returns null and falls back to no prefix.
     */
    private String getLuckPermsPrefix(Player player) {
        try {
            if (!getServer().getPluginManager().isPluginEnabled("LuckPerms")) {
                return null;
            }

            // LuckPermsProvider.get()
            Class<?> providerClass = Class.forName("net.luckperms.api.LuckPermsProvider");
            Method getMethod = providerClass.getMethod("get");
            Object luckPerms = getMethod.invoke(null);

            // luckPerms.getPlayerAdapter(Player.class)
            Class<?> lpClass = luckPerms.getClass();
            Method getPlayerAdapterMethod = lpClass.getMethod("getPlayerAdapter", Class.class);
            Object playerAdapter = getPlayerAdapterMethod.invoke(luckPerms, Player.class);

            // adapter.getUser(player)
            Method getUserMethod = playerAdapter.getClass().getMethod("getUser", Player.class);
            Object user = getUserMethod.invoke(playerAdapter, player);
            if (user == null) {
                return null;
            }

            // user.getCachedData().getMetaData().getPrefix()
            Method getCachedDataMethod = user.getClass().getMethod("getCachedData");
            Object cachedData = getCachedDataMethod.invoke(user);

            Method getMetaDataMethod = cachedData.getClass().getMethod("getMetaData");
            Object metaData = getMetaDataMethod.invoke(cachedData);

            Method getPrefixMethod = metaData.getClass().getMethod("getPrefix");
            Object prefixObj = getPrefixMethod.invoke(metaData);

            if (prefixObj instanceof String) {
                return (String) prefixObj;
            }
        } catch (ClassNotFoundException e) {
            // LuckPerms API classes not present, stop trying.
            if (luckPermsDetected) {
                getLogger().warning("ApexChat: LuckPerms API not found on classpath, disabling prefix support.");
            }
            luckPermsDetected = false;
        } catch (Throwable t) {
            // Any other reflection issue, just log at fine level and continue.
            getLogger().fine("ApexChat: Failed to fetch LuckPerms prefix: " + t.getMessage());
        }
        return null;
    }
}
