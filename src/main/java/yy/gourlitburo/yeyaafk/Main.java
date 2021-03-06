package yy.gourlitburo.yeyaafk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Main extends JavaPlugin {
  Logger logger = getLogger();
  Server server = getServer();
  PluginManager manager = Bukkit.getPluginManager();

  private final String TEAM_NAME = "yeyaafk_afk";
  final String PERM_MANAGE = "yeyaafk.manage";
  final long CHECK_PERIOD = 200; // 200 ticks ~= 10 seconds

  private List<Player> afkPlayers = new ArrayList<Player>();
  private Map<String, Long> lastMoveTimes = new HashMap<String, Long>();
  private Team team;

  Long getTime() {
    return System.currentTimeMillis();
  }

  boolean isPlayerAFK(Player player) {
    return afkPlayers.contains(player);
  }

  void setPlayerAFK(Player player, boolean afkStatus) {
    setPlayerAFK(player, afkStatus, false);
  }

  void setPlayerAFK(Player player, boolean afkStatus, boolean silent) {
    String playerName = player.getName();
    String message;
    if (isPlayerAFK(player) == afkStatus) return;
    if (afkStatus == true) {
      afkPlayers.add(player);
      team.addEntry(playerName);
      message = String.format("&7%s&7 is now AFK.", playerName);
    } else {
      afkPlayers.remove(player);
      team.removeEntry(playerName);
      message = String.format("&7%s&7 is no longer AFK.", playerName);
    }
    if (getConfig().getBoolean("invulnerable")) player.setInvulnerable(afkStatus);
    if (!silent) server.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  Long getPlayerLastMoveTime(Player player) {
    return lastMoveTimes.get(player.getName());
  }

  void setPlayerLastMoveTime(Player player, Long time) {
    lastMoveTimes.put(player.getName(), time);
  }

  void updatePlayerLastMoveTime(Player player) {
    lastMoveTimes.put(player.getName(), getTime());
  }

  @Override
  public void onEnable() {
    // setup team, creating if necessary
    boolean teamAlreadyExists = false;
    Scoreboard scoreboard = server.getScoreboardManager().getMainScoreboard();
    for (Team _team : scoreboard.getTeams()) {
      if (_team.getName().equals(TEAM_NAME)) {
        teamAlreadyExists = true;
        team = _team;
        break;
      }
    }
    if (!teamAlreadyExists) {
      team = scoreboard.registerNewTeam(TEAM_NAME);
    }
    processConfig();

    getCommand("yeyaafk").setExecutor(new CommandHandler(this));
    manager.registerEvents(new PlayerEventHandler(this), this);
    server.getScheduler().scheduleSyncRepeatingTask(this, new LastMoveTimer(this), 0, CHECK_PERIOD);
    saveDefaultConfig();

    // tell oniichan we are ready~⭐
    logger.info("YeyaAFK ready.");
  }

  @Override
  public void onDisable() {
    for (String name : team.getEntries()) {
      team.removeEntry(name);
    }
    for (Player player : afkPlayers) {
      player.setInvulnerable(false);
    }
  }

  void processConfig() {
    team.setPrefix(getConfig().getString("display.prefix"));
    team.setSuffix(getConfig().getString("display.suffix"));
  }
}
