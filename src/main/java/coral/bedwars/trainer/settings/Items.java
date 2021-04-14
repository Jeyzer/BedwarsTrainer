package coral.bedwars.trainer.settings;

import coral.bedwars.trainer.faster.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {

    private Items() {
        throw new AssertionError("nope");
    }

    public static class SmartMode {

        private SmartMode() {
            throw new AssertionError("nope");
        }

        public static ItemStack
           ENABLED = ItemBuilder.create()
                        .asMaterial(Material.INK_SACK)
                        .andSubId((short) 10)
                        .nominated("§aModalità Intelligente Attiva")
                        .buildItemStack(),

           DISABLED = ItemBuilder.create()
                        .asMaterial(Material.INK_SACK)
                        .andSubId((short) 8)
                        .nominated("§7Modalità Intelligente Inattiva")
                        .buildItemStack();

    }

}
