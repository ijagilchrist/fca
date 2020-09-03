package org.fca.microformats.metadata;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class ImageMetadata extends MicroFormat {

    @Nonnull
    private final String imageType;

    private final int height;

    private final int width;

    public static final String MICROFORMAT_TYPE = "metadata.image";

    public ImageMetadata() {
        super(null,null);
        this.imageType = null;
        this.height = 0;
        this.width = 0;
    }

    private ImageMetadata(Builder builder) {
        super(ImageMetadata.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.imageType = Objects.requireNonNull(builder.imageType, "imageType");
        this.height = builder.height;
        this.width = builder.width;
    }

    @Nonnull
    public int getHeight() {
        return height;
    }

    @Nonnull
    public int getWidth() {
        return width;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;
        private String imageType;
        private int height;
        private int width;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setImageType(String imageType) {
            this.imageType = imageType;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder of(ImageMetadata imageMetadata) {
            this.microFormatUUID = imageMetadata.microFormatUUID;
            this.imageType = imageMetadata.imageType;
            this.height = imageMetadata.height;
            this.width = imageMetadata.width;
            return this;
        }

        public ImageMetadata build() {
            return new ImageMetadata(this);
        }

    }

}
