package yy.gourlitburo.yeyaafk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandHandler implements CommandExecutor {
  
  private Main plugin;

  public CommandHandler(Main instance) {
    plugin = instance;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0 && sender instanceof Player) { // toggle self AFK
      Player player = (Player) sender;
      boolean wasAFK = plugin.isPlayerAFK(player);

      plugin.setPlayerAFK(player, !wasAFK);

      if (!wasAFK) player.sendMessage("You are now AFK.");
      else player.sendMessage("You are no longer AFK.");
      
      return true;
    }
    return false;
  }
  
}
