package coral.bedwars.trainer.abstraction.inventory;

import coral.bedwars.trainer.abstraction.interfacing.inventory.Button;
import coral.bedwars.trainer.abstraction.interfacing.inventory.Menu;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@Data
public abstract class CoreMenu implements Menu {

    protected @NonNull String title, name;
    protected final int size;
    protected final Button[] buttons;

    @Setter(value = AccessLevel.NONE)
    protected Inventory built;

    private InventoryType inventoryType;

    public CoreMenu(String title, String name, InventoryType inventoryType) {
        this.title = title;
        this.name = name;
        this.size = inventoryType.getDefaultSize();
        this.inventoryType = inventoryType;
        buttons = new CoreButton[inventoryType.getDefaultSize()];
    }

    public CoreMenu(String title, String name, int size) {
        this.title = title;
        this.name = name;
        this.size = size;
        buttons = new CoreButton[size * 9];
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void build(boolean scratch) {
        if (scratch) {
            built = inventoryType == null
                    ? Bukkit.createInventory(null, size * 9, title)
                    : Bukkit.createInventory(null, inventoryType, title);
        }

        for (int i = 0; i < buttons.length; i++) {
            CoreButton button = (CoreButton) buttons[i];
            if (button == null) continue;

            button.build(scratch);
            if (!scratch && button.canRefresh()) button.refresh();

            built.setItem(i, button.getBuilt());
        }
    }

    public void setButton(int i, Button button) {
        buttons[i] = button;

        if (isBuilt()) {
            if (!button.isBuilt()) {
                button.build(true);
            }

            built.setItem(i, button.getBuilt());
        }
    }

    @Override
    public void setFirstEmpty(Button button) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] != null) continue;
            setButton(i, button);
            return;
        }
    }

    public void setEmptyRange(int start, int end, Button button) {
        for (int i = start; i < end + 1; i++) {
            if (getButton(i) != null) continue;
            setButton(i, button);
            break;
        }
    }

    @Override
    public Button getButton(int i) {
        return buttons[i];
    }

    @Override
    public Inventory getBuilt() {
        return built;
    }

    public boolean isBuilt() {
        return built != null;
    }

}
