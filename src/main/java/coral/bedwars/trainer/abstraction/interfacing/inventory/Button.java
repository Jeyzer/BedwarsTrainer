package coral.bedwars.trainer.abstraction.interfacing.inventory;

import coral.bedwars.trainer.abstraction.interfacing.Buildable;
import coral.bedwars.trainer.abstraction.interfacing.Nominable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiFunction;

public interface Button extends Nominable, Buildable<ItemStack>, BiFunction<Player, ClickType, Menu>, Cloneable {

    Material getMaterial();

    default int getAmount() { return 1; }

    default short getSubId() { return 0; }

    String getDisplayName();

    List<String> getLore();

    default boolean canRefresh() { return false; }

    default void refresh() {}

    default Menu apply(Player player, ClickType clickType) { return null; }

}
