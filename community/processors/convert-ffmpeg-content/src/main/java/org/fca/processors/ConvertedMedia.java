package org.fca.processors;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class ConvertedMedia extends MicroFormat {

    public static final String MICROFORMAT_TYPE = "content.converted.media";

    @Nonnull
    private final String mediaUUID;

    public ConvertedMedia() {
        super(null,null);
        this.mediaUUID = null;
    }

    private ConvertedMedia(Builder builder) {
        super(ConvertedMedia.MICROFORMAT_TYPE,
              Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.mediaUUID = Objects.requireNonNull(builder.mediaUUID, "mediaUUID");
    }

    @Nonnull
    public String getMediaUUID() {
        return mediaUUID;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;
        private String mediaUUID;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setMediaUUID(String mediaUUID) {
            this.mediaUUID = mediaUUID;
            return this;
        }

        public Builder of(ConvertedMedia convertedMedia) {
            this.microFormatUUID = convertedMedia.microFormatUUID;
            this.mediaUUID = convertedMedia.mediaUUID;
            return this;
        }

        public ConvertedMedia build() {
            return new ConvertedMedia(this);
        }
    }
}
