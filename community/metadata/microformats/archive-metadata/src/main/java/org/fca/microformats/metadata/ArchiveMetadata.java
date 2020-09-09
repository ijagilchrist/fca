package org.fca.microformats.metadata;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class ArchiveMetadata extends MicroFormat {

    @Nonnull
    private final Folder root;

    @Nonnull
    private final String archiveType;

    public static final String MICROFORMAT_TYPE = "metadata.archive";

    public ArchiveMetadata() {
        super(null,null);
        this.root = null;
        this.archiveType = null;
    }

    private ArchiveMetadata(Builder builder) {
        super(ArchiveMetadata.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.root = Objects.requireNonNull(builder.root, "root");
        this.archiveType = Objects.requireNonNull(builder.archiveType, "archiveType");
    }

    @Nonnull
    public Folder getRoot() {
        return root;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;

        private Folder root;

        private String archiveType;

        private Builder() {
            this.root = new Folder("<root>");
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setArchiveType(String archiveType) {
            this.archiveType = archiveType;
            return this;
        }

        public Builder addFolder(String path) {
            this.root.addFolder(path);
            return this;
        }

        public Builder addItem(String path, String itemUUID) {
            this.root.addItem(path,itemUUID);
            return this;
        }

        public Builder of(ArchiveMetadata archiveMetadata) {
            this.microFormatUUID = archiveMetadata.microFormatUUID;
            this.root = archiveMetadata.root;
            this.archiveType = archiveMetadata.archiveType;
            return this;
        }

        public ArchiveMetadata build() {
            return new ArchiveMetadata(this);
        }

    }

}
