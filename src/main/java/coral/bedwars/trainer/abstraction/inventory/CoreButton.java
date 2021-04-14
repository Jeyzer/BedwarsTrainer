package coral.bedwars.trainer.abstraction.inventory;

import coral.bedwars.trainer.abstraction.interfacing.inventory.Button;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;

import lombok.Setter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Data
public abstract class CoreButton implements Button {

    @Setter(value = AccessLevel.NONE)
    protected ItemStack built;

    protected @NonNull String name;
    protected @NonNull Material material;
    protected int amount = 1;
    protected short subId;
    protected String displayName;
    protected List<String> lore;

    public void build(boolean scratch) {
        if (scratch) {
            built = new ItemStack(getMaterial(), getAmount(), getSubId());
        }

        ItemMeta meta = built.getItemMeta();
        meta.setDisplayName(getDisplayName());
        meta.setLore(getLore());
        built.setItemMeta(meta);
    }

    public boolean isBuilt() {
        return built != null;
    }

    public ItemStack getBuilt() {
        return built;
    }

    public CoreButton clone() throws CloneNotSupportedException {
        return (CoreButton) super.clone();
    }

}
