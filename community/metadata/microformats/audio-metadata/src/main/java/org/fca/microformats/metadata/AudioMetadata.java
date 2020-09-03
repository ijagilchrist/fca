package org.fca.microformats.metadata;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AudioMetadata extends MicroFormat {

    private final int channels;

    @Nonnull
    private final String compressor;

    private final double duration;

    private final double sampleRate;

    @Nonnull
    private final String audioType;

    public static final String MICROFORMAT_TYPE = "metadata.audio";

    public AudioMetadata() {
        super(null,null);
        this.channels = 0;
        this.compressor = null;
        this.duration = 0.0;
        this.sampleRate = 0.0;
        this.audioType = null;
    }

    private AudioMetadata(Builder builder) {
        super(AudioMetadata.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.channels = Objects.requireNonNull(builder.channels, "channels");
        this.compressor = Objects.requireNonNull(builder.compressor, "compressor");
        this.duration = Objects.requireNonNull(builder.duration, "duration");
        this.sampleRate = Objects.requireNonNull(builder.sampleRate, "sampleRate");
        this.audioType = Objects.requireNonNull(builder.audioType, "audioType");
    }

    @Nonnull
    public String getMicroFormatUUID() {
        return microFormatUUID;
    }

    public int getChannels() {
        return channels;
    }

    @Nonnull
    public String getCompressor() {
        return compressor;
    }

    public double getDuration() {
        return duration;
    }

    public double getSampleRate() {
        return sampleRate;
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
        private Integer channels;
        private String compressor;
        private Double duration;
        private Double sampleRate;
        private String audioType;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setChannels(int channels) {
            this.channels = channels;
            return this;
        }

        public Builder setCompressor(String compressor) {
            this.compressor = compressor;
            return this;
        }

        public Builder setDuration(double duration) {
            this.duration = duration;
            return this;
        }

        public Builder setSampleRate(double sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }

        public Builder setAudioType(String audioType) {
            this.audioType = audioType;
            return this;
        }

        public Builder of(AudioMetadata audioMetadata) {
            this.microFormatUUID = audioMetadata.microFormatUUID;
            this.channels = audioMetadata.channels;
            this.compressor = audioMetadata.compressor;
            this.duration = audioMetadata.duration;
            this.sampleRate = audioMetadata.sampleRate;
            this.audioType = audioMetadata.audioType;
            return this;
        }

        public AudioMetadata build() {
            return new AudioMetadata(this);
        }

    }

}
