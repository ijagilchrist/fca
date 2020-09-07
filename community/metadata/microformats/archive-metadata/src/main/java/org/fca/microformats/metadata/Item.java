package org.fca.microformats.metadata;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class Item implements Comparable<Item> {

    @Nonnull
    private final String name;

    @Nonnull
    private final String itemUUID;

    public Item(String name, String itemUUID) {
        this.name = Objects.requireNonNull(name, "name");
        this.itemUUID = Objects.requireNonNull(itemUUID, "itemUUID");
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getItemUUID() {
        return itemUUID;
    }

    @Override
    public int compareTo(Item other) {
        return this.name.compareTo(other.name);
    }
}
