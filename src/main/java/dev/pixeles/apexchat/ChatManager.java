package dev.pixeles.apexchat;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager {

    private final Set<UUID> mutedPlayers = ConcurrentHashMap.newKeySet();
    private volatile boolean globalMuted = false;
    private final Map<UUID, UUID> lastPrivatePartner = new ConcurrentHashMap<>();

    public void mute(UUID uuid) {
        mutedPlayers.add(uuid);
    }

    public void unmute(UUID uuid) {
        mutedPlayers.remove(uuid);
    }

    public boolean isMuted(UUID uuid) {
        return mutedPlayers.contains(uuid);
    }

    public boolean isGlobalMuted() {
        return globalMuted;
    }

    public void setGlobalMuted(boolean globalMuted) {
        this.globalMuted = globalMuted;
    }

    /**
     * Remember last private messaging partner (two-way).
     */
    public void setLastPrivatePartner(UUID a, UUID b) {
        lastPrivatePartner.put(a, b);
        lastPrivatePartner.put(b, a);
    }

    public UUID getLastPrivatePartner(UUID uuid) {
        return lastPrivatePartner.get(uuid);
    }
}
