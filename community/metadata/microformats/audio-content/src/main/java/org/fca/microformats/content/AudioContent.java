package org.fca.microformats.content;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AudioContent extends MicroFormat {

    @Nonnull
    private final String audioUUID;

    @Nonnull
    private final String audioType;

    public static final String MICROFORMAT_TYPE = "content.audio";

    public AudioContent() {
        super(null,null);
        this.audioUUID = null;
        this.audioType = null;
    }

    private AudioContent(Builder builder) {
        super(AudioContent.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.audioUUID = Objects.requireNonNull(builder.audioUUID, "audioUUID");
        this.audioType = Objects.requireNonNull(builder.audioType, "audioType");
    }

    @Nonnull
    public String getMicroFormatUUID() {
        return microFormatUUID;
    }

    @Nonnull
    public String getAudioUUID() {
        return audioUUID;
    }

    @Nonnull
    public String getAudioType() {
        return audioType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String microFormatUUID;
        private String audioUUID;
        private String audioType;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setAudioUUID(String audioUUID) {
            this.audioUUID = audioUUID;
            return this;
        }

        public Builder setAudioType(String audioType) {
            this.audioType = audioType;
            return this;
        }

        public Builder of(AudioContent audioContent) {
            this.microFormatUUID = audioContent.microFormatUUID;
            this.audioUUID = audioContent.audioUUID;
            this.audioType = audioContent.audioType;
            return this;
        }

        public AudioContent build() {
            return new AudioContent(this);
        }

    }

}
