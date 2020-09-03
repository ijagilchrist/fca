package org.fca.microformats.object;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.MicroFormats;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class UnidentifiedObject extends MicroFormat {

    @Nonnull
    public static final String MICROFORMAT_TYPE = "object.unidentified";

    static {
        MicroFormats.register(UnidentifiedObject.MICROFORMAT_TYPE,UnidentifiedObject.class);
    }

    @Nonnull
    private final String objectUUID;

    @Nonnull
    private final String objectName;

    public UnidentifiedObject() {
        super(null,null);
        this.objectUUID = null;
        this.objectName = null;
    }

    private UnidentifiedObject(Builder builder) {
        super(UnidentifiedObject.MICROFORMAT_TYPE,
              Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.objectUUID = Objects.requireNonNull(builder.objectUUID, "objectUUID");
        this.objectName = Objects.requireNonNull(builder.objectName, "objectName");
    }

    @Nonnull
    public String getObjectUUID() {
        return objectUUID;
    }

    @Nonnull
    public String getObjectName() {
        return objectName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;
        private String objectUUID;
        private String objectName;

        private Builder() {
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setObjectUUID(String objectUUID) {
            this.objectUUID = objectUUID;
            return this;
        }

        public Builder setObjectName(String objectName) {
            this.objectName = objectName;
            return this;
        }

        public Builder of(UnidentifiedObject unidentifiedObject) {
            this.microFormatUUID = unidentifiedObject.microFormatUUID;
            this.objectUUID = unidentifiedObject.objectUUID;
            this.objectName = unidentifiedObject.objectName;
            return this;
        }

        public UnidentifiedObject build() {
            return new UnidentifiedObject(this);
        }

    }

}
