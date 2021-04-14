package coral.bedwars.trainer.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocationDebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        Player sender = (Player) commandSender;
        Location location = sender.getLocation();

        sender.sendMessage("§6Debug §7» §c" + location.getX() + "§7,§c " + location.getY() + "§7,§c " + location.getZ() + "§7,§c " + location.getYaw() + "§7,§c " + location.getPitch());
        return false;
    }
}
