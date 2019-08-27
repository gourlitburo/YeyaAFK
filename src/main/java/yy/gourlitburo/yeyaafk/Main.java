package yy.gourlitburo.yeyaafk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Main extends JavaPlugin {
  
  Logger logger = getLogger();
  Server server = getServer();
  PluginManager manager = Bukkit.getPluginManager();

  // final long TIMEOUT = 2 * 60 * 1000; // 2 minutes
  // final long CHECK_PERIOD = 200; // 200 ticks ~= 10 seconds
  final long TIMEOUT = 5 * 1000; // 5 seconds
  final long CHECK_PERIOD = 100; // 100 ticks ~= 5 seconds

  private final String TEAM_NAME = "yeyaafk_afk";
  private final String TEAM_PREFIX = "[AFK] ";

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
    player.setInvulnerable(afkStatus);
    server.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  Long getPlayerLastMoveTime(Player player) {
    return lastMoveTimes.get(player.getName());
  }

  void setPlayerLastMoveTime(Player player, Long time) {
    lastMoveTimes.put(player.getName(), time);
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
    team.setPrefix(TEAM_PREFIX);

    // register command
    PluginCommand command = getCommand("yeyaafk");
    command.setExecutor(new CommandHandler(this));

    // register events
    manager.registerEvents(new PlayerEventHandler(this), this);

    // schedule timer
    server.getScheduler().scheduleSyncRepeatingTask(this, new LastMoveTimer(this), 0, CHECK_PERIOD);

    // tell oniichan we are ready~⭐
    logger.info("YeyaAFK ready.");
  }

  @Override
  public void onDisable() {
    for (String name : team.getEntries()) {
      team.removeEntry(name);
    }
  }

}
