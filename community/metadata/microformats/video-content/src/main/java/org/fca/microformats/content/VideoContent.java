package org.fca.microformats.content;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class VideoContent extends MicroFormat {

    @Nonnull
    private final String videoUUID;

    @Nonnull
    private final String videoType;

    public static final String MICROFORMAT_TYPE = "content.video";

    public VideoContent() {
        super(null,null);
        this.videoUUID = null;
        this.videoType = null;
    }

    private VideoContent(Builder builder) {
        super(VideoContent.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.videoUUID = Objects.requireNonNull(builder.videoUUID, "videoUUID");
        this.videoType = Objects.requireNonNull(builder.videoType, "videoType");
    }

    @Nonnull
    public String getMicroFormatUUID() {
        return microFormatUUID;
    }

    @Nonnull
    public String getVideoUUID() {
        return videoUUID;
    }

    @Nonnull
    public String getVideoType() {
        return videoType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String microFormatUUID;
        private String videoUUID;
        private String videoType;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setVideoUUID(String videoUUID) {
            this.videoUUID = videoUUID;
            return this;
        }

        public Builder setVideoType(String videoType) {
            this.videoType = videoType;
            return this;
        }

        public Builder of(VideoContent videoContent) {
            this.microFormatUUID = videoContent.microFormatUUID;
            this.videoUUID = videoContent.videoUUID;
            this.videoType = videoContent.videoType;
            return this;
        }

        public VideoContent build() {
            return new VideoContent(this);
        }

    }

}
