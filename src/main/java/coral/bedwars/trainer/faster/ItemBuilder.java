package coral.bedwars.trainer.faster;

import com.google.common.collect.Lists;

import coral.bedwars.trainer.abstraction.inventory.CoreButton;
import lombok.*;
import lombok.experimental.Accessors;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

@Getter @Setter
@Accessors(fluent = true, chain = true)
public class ItemBuilder {

    @Setter(value = AccessLevel.NONE) private ItemStack itemStack;
    private Material asMaterial;
    private int withAmount = 1;
    private String nominated;
    private short andSubId;
    private List<String> lore;
    @Setter(value = AccessLevel.NONE) private List<EnchantmentBuilder> enchantments;

    public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
        (enchantments = Nullation.antiNull(enchantments, Lists.newArrayList()))
            .add(new EnchantmentBuilder(enchantment, level));
        return this;
    }

    public ItemBuilder lores(String... strings) {
        Collections.addAll((lore = Nullation.antiNull(lore, Lists.newArrayList())), strings);
        return this;
    }

    public static ItemBuilder create() {
        return new ItemBuilder();
    }

    public CoreButton buildPanel() {
        return new PoorPanel(asMaterial, withAmount, andSubId, nominated, lore);
    }

    public ItemStack buildItemStack() {
        itemStack = new ItemStack(asMaterial, withAmount, andSubId);

        Nullation.nonNull(itemStack.getItemMeta())
                .operable()
                    .operation(lore).run(ItemMeta::setLore)
                    .operation(nominated).run(ItemMeta::setDisplayName)
                    .operation(enchantments).run((itemMeta, enchantments) -> {
                        for (EnchantmentBuilder enchantmentBuilder : enchantments)
                             itemMeta.addEnchant(enchantmentBuilder.enchantment, enchantmentBuilder.level, true);
                    }).finalize(itemStack).run(itemStack::setItemMeta);

        return itemStack;
    }

    public static ItemBuilder createPanel(short subId) {
        return ItemBuilder.create().asMaterial(Material.STAINED_GLASS_PANE).withAmount(1).andSubId(subId);
    }

    @RequiredArgsConstructor
    private static class EnchantmentBuilder {

        private final Enchantment enchantment;
        private final int level;

    }

    public static class PoorPanel extends CoreButton {

        public PoorPanel(Material material, int amount, short subId, String displayName, List<String> lore) {
            super("poorPanel", material);
            setSubId(subId);
            setDisplayName(displayName);
            setLore(lore);
        }

    }
}
