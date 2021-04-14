package coral.bedwars.trainer.abstraction.interfacing.sessions;

import coral.bedwars.trainer.abstraction.interfacing.sidebar.SidebarTemplate;
import org.bukkit.entity.Player;

public interface TrainingSession extends SidebarTemplate {

    long getSessionStart();

    boolean isRunning();

    void stop(Player player);
    void start(Player player);

    int getAttempts();

}
