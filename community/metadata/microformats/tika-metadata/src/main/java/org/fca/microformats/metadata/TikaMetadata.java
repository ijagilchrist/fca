package org.fca.microformats.metadata;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class TikaMetadata extends MicroFormat {

    public static final String MICROFORMAT_TYPE = "metadata.tika";

    @Nonnull
    private final Map<String,Object> tikaMetadata;

    public TikaMetadata() {
        super(null,null);
        this.tikaMetadata = null;
    }

    public TikaMetadata(Builder builder) {
        super(TikaMetadata.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.tikaMetadata = Objects.requireNonNull(builder.tikaMetadata, "tikaMetadata");
    }

    @Nonnull
    public String getMicroFormatUUID() {
        return microFormatUUID;
    }

    @Nonnull
    public Map<String, Object> getTikaMetadata() {
        return tikaMetadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String microFormatUUID;
        private Map<String, Object> tikaMetadata;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setTikaMetadata(Map<String, Object> tikaMetadata) {
            this.tikaMetadata = tikaMetadata;
            return this;
        }

        public Builder of(TikaMetadata tikaMetadata) {
            this.microFormatUUID = tikaMetadata.microFormatUUID;
            this.tikaMetadata = tikaMetadata.tikaMetadata;
            return this;
        }

        public TikaMetadata build() {
            return new TikaMetadata(this);
        }

    }

}
