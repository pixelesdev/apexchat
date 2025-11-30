# ApexChat

ApexChat is a lightweight and modern chat management plugin for **Minecraft 1.21.x**.  
It provides private messaging, chat clearing, mute tools, and optional LuckPerms prefix support — all in a simple, fast, and reliable package.

---

# ✨ Features

## For Players
- Private messages: `/pmsg`, `/pm`, `/msg`, `/tell`, `/w`, `/reply`
- Quick reply to last PM: `/reply`
- Clear your own chat: `/clearchat`
- Clean message formatting
- Optional LuckPerms prefix support (no dependency required)

## For Staff
- Clear global chat: `/clearchat all`
- Mute players: `/mute <player>`
- Unmute players: `/unmute <player>`
- Toggle global chat mute: `/mutechat`
- Bypass permissions for trusted staff

---

# 🔧 Commands

| Command | Description |
|--------|-------------|
| `/clearchat` | Clears your own chat |
| `/clearchat all` | Clears chat for everyone |
| `/pmsg <player> <message>` | Sends a private message |
| `/reply <message>` | Replies to the last private message |
| `/mute <player>` | Mutes a player |
| `/unmute <player>` | Unmutes a player |
| `/mutechat` | Toggles global chat mute |

---

# 🔐 Permissions

| Permission | Description | Default |
|-----------|-------------|---------|
| `apexchat.clearchat.self` | Allow clearing your own chat | true |
| `apexchat.clearchat.all` | Clear global chat | op |
| `apexchat.msg` | Use private messages | true |
| `apexchat.mute` | Mute/unmute players | op |
| `apexchat.mutechat` | Toggle global chat mute | op |
| `apexchat.bypass.mute` | Speak while muted | op |
| `apexchat.bypass.globalmute` | Talk while global chat is muted | op |

---

# 📦 Installation

1. Download the latest release from **Modrinth** or **GitHub**.
2. Place the jar into your server’s `/plugins/` directory.
3. Restart your server.
4. The plugin works out of the box — no configuration needed.

**Optional:**  
Install **LuckPerms** to enable prefix support.

---

# 🔌 Compatibility

ApexChat is compatible with:

- Minecraft **1.21.x**
- Bukkit
- Spigot
- Paper
- Purpur
- Any other server software based on the Bukkit API

LuckPerms support works automatically when installed.

# 📁 Source Structure

```
src/main/java/dev/pixeles/apexchat/
 ├── ApexChatPlugin.java
 ├── ChatManager.java
 ├── ClearChatCommand.java
 ├── PrivateMessageCommand.java
 └── MuteCommand.java

src/main/resources/
 └── plugin.yml
```

# 🛡 License

MIT

---

# 🧑‍💻 Author

**Ralph Turner (pixelesdev)**  
Minecraft plugin developer & creator of ApexChat.

---

# ⭐ Contributing

Pull requests, improvements, and suggestions are always welcome!  
If you encounter bugs or want new features, feel free to open an issue.
