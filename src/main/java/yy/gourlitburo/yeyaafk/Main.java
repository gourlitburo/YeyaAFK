package yy.gourlitburo.yeyaafk;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
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

  private static String TEAM_NAME = "yeyaafk_afk";
  private static String TEAM_PREFIX = "[AFK] ";

  List<Player> afkPlayers = new ArrayList<Player>();
  Team team;

  boolean isPlayerAFK(Player player) {
    return afkPlayers.contains(player);
  }

  void setPlayerAFK(Player player, boolean afkStatus) {
    String playerName = player.getName();
    if (afkStatus == true) {
      afkPlayers.add(player);
      team.addEntry(playerName);
    } else {
      afkPlayers.remove(player);
      team.removeEntry(playerName);
    }
    player.setInvulnerable(afkStatus);
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
  }

}
