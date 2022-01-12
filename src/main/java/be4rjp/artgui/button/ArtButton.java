package be4rjp.artgui.button;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ArtButton {

    private final ItemStack itemStack;

    private Consumer<InventoryClickEvent> eventConsumer;

    public ArtButton(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {return itemStack;}

    public Consumer<InventoryClickEvent> getEventConsumer() {return eventConsumer;}

    public ArtButton listener(Consumer<InventoryClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }
}
