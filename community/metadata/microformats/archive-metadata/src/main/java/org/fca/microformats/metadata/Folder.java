package org.fca.microformats.metadata;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class Folder implements Comparable<Folder> {

    @Nonnull
    private final String name;

    @Nonnull
    private final Map<String,Folder> folders;

    @Nonnull
    private final Set<Item> items;

    public Folder(String name) {
        this.name = Objects.requireNonNull(name, "name");
        this.folders = new TreeMap<>();
        this.items = new TreeSet<>();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public Map<String, Folder> getFolders() {
        return folders;
    }

    @Nonnull
    public Set<Item> getItems() {
        return items;
    }

    public void addFolder(String path) {
        String[] pathElements = path.split("/");
        String folderName = pathElements[0];
        if (!this.folders.containsKey(folderName)) this.folders.put(folderName,new Folder(folderName));
        if (pathElements.length > 1) {
            Folder folder = this.folders.get(folderName);
            String subPath = path.substring(folderName.length()+1);
            folder.addFolder(subPath);
        }
    }

    public void addItem(String path, String itemUUID) {
        String[] pathElements = path.split("/");
        if (pathElements.length > 1) {
            String folderName = pathElements[0];
            if (!this.folders.containsKey(folderName)) this.folders.put(folderName,new Folder(folderName));
            Folder folder = this.folders.get(folderName);
            String subPath = path.substring(folderName.length()+1);
            folder.addItem(subPath,itemUUID);
        } else {
            String itemName = pathElements[0];
            this.items.add(new Item(itemName,itemUUID));
        }
    }

    @Override
    public int compareTo(Folder other) {
        return this.name.compareTo(other.name);
    }

}
