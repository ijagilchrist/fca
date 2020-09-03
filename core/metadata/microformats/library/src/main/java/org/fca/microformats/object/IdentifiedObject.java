package org.fca.microformats.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.MicroFormats;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class IdentifiedObject extends MicroFormat {

    @Nonnull
    public static final String MICROFORMAT_TYPE = "object.identified";

    static {
        MicroFormats.register(IdentifiedObject.MICROFORMAT_TYPE,IdentifiedObject.class);
    }

    public static String qualifiedMicroFormatType(String form) {
        return String.format("%s.%s",MICROFORMAT_TYPE,form);
    }

    @Nonnull
    private final String objectUUID;

    @Nonnull
    private final String objectName;

    @Nonnull
    private final String objectForm;

    @Nonnull
    private final String objectMimetype;

    public IdentifiedObject() {
        super(null,null);
        this.objectUUID = null;
        this.objectName = null;
        this.objectForm = null;
        this.objectMimetype = null;
    }

    private IdentifiedObject(Builder builder) {
        super(IdentifiedObject.MICROFORMAT_TYPE,
              Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.objectUUID = Objects.requireNonNull(builder.objectUUID, "objectUUID");
        this.objectName = Objects.requireNonNull(builder.objectName, "objectName");
        this.objectForm = Objects.requireNonNull(builder.objectForm, "objectForm");
        this.objectMimetype = Objects.requireNonNull(builder.objectMimetype, "objectMimetype");
    }

    public static String getQualifiedMicroFormatType(String objectForm) {
        return String.format("%s.%s",IdentifiedObject.MICROFORMAT_TYPE,objectForm);
    }

    @Nonnull
    public String getObjectUUID() {
        return objectUUID;
    }

    @Nonnull
    public String getObjectName() {
        return objectName;
    }

    @Nonnull
    public String getObjectForm() {
        return objectForm;
    }

    @Nonnull
    public String getObjectMimetype() {
        return objectMimetype;
    }

    @Override
    @JsonIgnore
    public String getQualifiedMicroFormatType() {
        return String.format("%s.%s",this.getMicroFormatType(),this.getObjectForm());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;
        private String objectUUID;
        private String objectName;
        private String objectForm;
        private String objectMimetype;

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

        public Builder setObjectForm(String objectForm) {
            this.objectForm = objectForm;
            return this;
        }

        public Builder setObjectMimetype(String objectMimetype) {
            this.objectMimetype = objectMimetype;
            return this;
        }

        public Builder of(IdentifiedObject identifiedObject) {
            this.microFormatUUID = identifiedObject.microFormatUUID;
            this.objectUUID = identifiedObject.objectUUID;
            this.objectName = identifiedObject.objectName;
            this.objectForm = identifiedObject.objectForm;
            this.objectMimetype = identifiedObject.objectMimetype;
            return this;
        }

        public Builder of(UnidentifiedObject unidentifiedObject) {
            this.microFormatUUID = unidentifiedObject.getMicroFormatUUID();
            this.objectUUID = unidentifiedObject.getObjectUUID();
            this.objectName = unidentifiedObject.getObjectName();
            return this;
        }

        public IdentifiedObject build() {
            return new IdentifiedObject(this);
        }

    }

}
