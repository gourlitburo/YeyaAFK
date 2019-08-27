package yy.gourlitburo.yeyaafk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

class PlayerEventHandler implements Listener {

  private Main plugin;

  public PlayerEventHandler(Main instance) {
    plugin = instance;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    plugin.setPlayerLastMoveTime(event.getPlayer(), plugin.getTime());
  }

  private void handleEvent(PlayerEvent event) {
    Player player = event.getPlayer();
    plugin.setPlayerLastMoveTime(player, plugin.getTime());
    if (plugin.isPlayerAFK(player)) {
      plugin.setPlayerAFK(player, false);
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (plugin.getConfig().getBoolean("auto_exit.move")) handleEvent(event);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (plugin.getConfig().getBoolean("auto_exit.interact")) handleEvent(event);
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    if (plugin.getConfig().getBoolean("auto_exit.chat")) handleEvent(event);
  }

  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    if (plugin.getConfig().getBoolean("auto_exit.command")) handleEvent(event);
  }

}
