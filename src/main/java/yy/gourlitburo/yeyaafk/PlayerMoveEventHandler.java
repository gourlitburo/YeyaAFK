package yy.gourlitburo.yeyaafk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

class PlayerEventHandler implements Listener {

  private Main plugin;

  public PlayerEventHandler(Main instance) {
    plugin = instance;
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
    handleEvent(event);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    handleEvent(event);
  }

}
