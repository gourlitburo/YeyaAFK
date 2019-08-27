package yy.gourlitburo.yeyaafk;

import org.bukkit.entity.Player;

class LastMoveTimer implements Runnable {

  private Main plugin;

  public LastMoveTimer(Main instance) {
    plugin = instance;
  }

  @Override
  public void run() {
    for (Player player : plugin.server.getOnlinePlayers()) {
      Long lastMoveTime = plugin.getPlayerLastMoveTime(player);
      if (lastMoveTime != null && plugin.getTime() - lastMoveTime > plugin.TIMEOUT) {
        plugin.setPlayerAFK(player, true);
      }
    }
  }
}
