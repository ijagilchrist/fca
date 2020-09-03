package org.fca.microformats.metadata;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class VideoMetadata extends MicroFormat {

    private final double duration;

    @Nonnull
    private final String videoType;

    public static final String MICROFORMAT_TYPE = "metadata.video";

    public VideoMetadata() {
        super(null,null);
        this.duration = 0.0;
        this.videoType = null;
    }

    private VideoMetadata(Builder builder) {
        super(VideoMetadata.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.duration = Objects.requireNonNull(builder.duration, "duration");
        this.videoType = Objects.requireNonNull(builder.videoType, "videoType");
    }

    @Nonnull
    public String getMicroFormatUUID() {
        return microFormatUUID;
    }

    public double getDuration() {
        return duration;
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
        private Double duration;
        private String videoType;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setDuration(double duration) {
            this.duration = duration;
            return this;
        }

        public Builder setVideoType(String videoType) {
            this.videoType = videoType;
            return this;
        }

        public Builder of(VideoMetadata videoMetadata) {
            this.microFormatUUID = videoMetadata.microFormatUUID;
            this.duration = videoMetadata.duration;
            this.videoType = videoMetadata.videoType;
            return this;
        }

        public VideoMetadata build() {
            return new VideoMetadata(this);
        }

    }

}
