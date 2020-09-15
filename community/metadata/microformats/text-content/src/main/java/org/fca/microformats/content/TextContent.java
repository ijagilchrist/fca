package org.fca.microformats.content;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.MicroFormats;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class TextContent extends MicroFormat {

    static {
        MicroFormats.register(TextContent.MICROFORMAT_TYPE,TextContent.class);
    }

    public static String qualifiedMicroFormatType(String language) {
        return String.format("%s.%s",MICROFORMAT_TYPE,language);
    }

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
            this.microFormatUUID = textContent.microFormatUUID;
            this.value = textContent.value;
            this.language = textContent.language;
            this.translation = textContent.translation;
            return this;
        }

        public TextContent build() {
            return new TextContent(this);
        }

    }

}
