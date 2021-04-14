package coral.bedwars.trainer.menus.impl;

import coral.bedwars.trainer.abstraction.inventory.CoreButton;
import coral.bedwars.trainer.abstraction.inventory.CoreMenu;
import coral.bedwars.trainer.abstraction.sessions.bridging.BridgingConfiguration;
import coral.bedwars.trainer.faster.FastLoops;
import coral.bedwars.trainer.faster.ItemBuilder;
import org.bukkit.Material;

public class StraightConfigMenu extends CoreMenu {

    private final BridgingConfiguration config;

    public StraightConfigMenu(BridgingConfiguration config) {
        super("Change bridge config", "straightConfig", 36);
        this.config = config;

        CoreButton
           layer1 = ItemBuilder.createPanel((short) 3).nominated("§b§lCoral§f§lMC").buildPanel(),
           layer2 = ItemBuilder.createPanel((short) 9).nominated("§3§lCoral§f§lMC").buildPanel(),

           description = ItemBuilder.create()
              .asMaterial(Material.WOOL)
              .nominated("§aBridging Config")
              .lores("§7Make some changes to your session",
                 "§7Closing will update.")
              .buildPanel();

        setButton(4, description);

        FastLoops
           .read(0, 2, 6, 8).forEach(slot -> setButton(slot, layer1))
           .then(1, 3, 5, 9).forEach(slot -> setButton(slot, layer2));
    }



}
