package coral.bedwars.trainer.abstraction.interfacing.inventory;

import coral.bedwars.trainer.abstraction.interfacing.Buildable;
import coral.bedwars.trainer.abstraction.interfacing.Nominable;
import org.bukkit.inventory.Inventory;

public interface Menu extends Nominable, Buildable<Inventory> {

    String getTitle();

    default int getSize() { return 1; }

    Button[] getButtons();

    Button getButton(int i);

    void setFirstEmpty(Button button);

    void setEmptyRange(int start, int end, Button button);

}
