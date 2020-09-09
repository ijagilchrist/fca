package org.fca.microformats.content;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class TextContent extends MicroFormat {

    @Nonnull
    private final String value;

    private final String language;

    private final String translation;

    public static final String MICROFORMAT_TYPE = "content.text";

    public TextContent() {
        super(null,null);
        this.value = null;
        this.language = null;
        this.translation = null;
    }

    private TextContent(Builder builder) {
      super(TextContent.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.value = Objects.requireNonNull(builder.value, "value");
        this.language = builder.language;
        this.translation = builder.translation;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Nonnull
    public String getLanguage() {
        return language;
    }

    @Nonnull
    public String getTranslation() {
        return translation;
    }

    @Override
    public String getQualifiedMicroFormatType() {
        return (this.language != null) ?
                String.format("%s.%s",super.getMicroFormatType(),this.language) :
                super.getMicroFormatType();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;
        private String value;
        private String language;
        private String translation;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public Builder setTranslation(String translation) {
            this.translation = translation;
            return this;
        }

        public Builder of(TextContent textContent) {
            this.value = textContent.value;
            this.language = textContent.language;
            this.translation = textContent.translation;
            return this;
        }

        public TextContent build() {
            return new TextContent(this);
        }

    }


//    @Nonnull
//    private final String audioUUID;
//
//    @Nonnull
//    private final String audioType;
//
//    public static final String MICROFORMAT_TYPE = "content.audio";
//
//    public AudioContent() {
//        super(null,null);
//        this.audioUUID = null;
//        this.audioType = null;
//    }
//
//    private AudioContent(Builder builder) {
//        super(AudioContent.MICROFORMAT_TYPE,
//                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
//        this.audioUUID = Objects.requireNonNull(builder.audioUUID, "audioUUID");
//        this.audioType = Objects.requireNonNull(builder.audioType, "audioType");
//    }
//
//    @Nonnull
//    public String getMicroFormatUUID() {
//        return microFormatUUID;
//    }
//
//    @Nonnull
//    public String getAudioUUID() {
//        return audioUUID;
//    }
//
//    @Nonnull
//    public String getAudioType() {
//        return audioType;
//    }
//
//    public static Builder builder() {
//        return new Builder();
//    }
//
//    public static class Builder {
//        private String microFormatUUID;
//        private String audioUUID;
//        private String audioType;
//
//        private Builder() {
//        }
//
//        public Builder setMicroFormatUUID(String microFormatUUID) {
//            this.microFormatUUID = microFormatUUID;
//            return this;
//        }
//
//        public Builder setAudioUUID(String audioUUID) {
//            this.audioUUID = audioUUID;
//            return this;
//        }
//
//        public Builder setAudioType(String audioType) {
//            this.audioType = audioType;
//            return this;
//        }
//
//        public Builder of(AudioContent audioContent) {
//            this.microFormatUUID = audioContent.microFormatUUID;
//            this.audioUUID = audioContent.audioUUID;
//            this.audioType = audioContent.audioType;
//            return this;
//        }
//
//        public AudioContent build() {
//            return new AudioContent(this);
//        }
//
//    }

}
