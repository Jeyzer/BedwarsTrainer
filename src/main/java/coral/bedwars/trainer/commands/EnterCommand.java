package coral.bedwars.trainer.commands;

import coral.bedwars.trainer.abstraction.sessions.BridgingSession;
import coral.bedwars.trainer.data.trainers.bridging.Bridging;

import lombok.RequiredArgsConstructor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class EnterCommand implements CommandExecutor {

    private final Plugin plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return false;

        Player sender = (Player) commandSender;

        if (sender.hasMetadata("bridging")) {
            Bridging.STRAIGHT.endSession(sender);
            sender.removeMetadata("bridging", plugin);
            return false;
        }

        BridgingSession session = Bridging.STRAIGHT.newSession(sender);
        sender.setMetadata("bridging", new FixedMetadataValue(plugin, session.getType().toString()));
        return false;
    }

}
