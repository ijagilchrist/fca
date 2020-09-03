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
public class CompletedObject extends MicroFormat {

    @Nonnull
    public static final String MICROFORMAT_TYPE = "object.completed";

    @Nonnull
    public static final String FORM_UNKNOWN = "unknown";

    static {
        MicroFormats.register(CompletedObject.MICROFORMAT_TYPE,CompletedObject.class);
    }

    public static String qualifiedMicroFormatType(String form) {
        return String.format("%s.%s",MICROFORMAT_TYPE,form);
    }

    @Nonnull
    private final String objectUUID;

    @Nonnull
    private final String objectForm;

    public CompletedObject() {
        super(null,null);
        this.objectUUID = null;
        this.objectForm = null;
    }

    private CompletedObject(Builder builder) {
        super(CompletedObject.MICROFORMAT_TYPE,
              Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.objectUUID = Objects.requireNonNull(builder.objectUUID, "objectUUID");
        this.objectForm = Objects.requireNonNull(builder.objectForm, "objectForm");
    }

    public static String getQualifiedMicroFormatType(String objectForm) {
        return String.format("%s.%s",CompletedObject.MICROFORMAT_TYPE,objectForm);
    }

    @Nonnull
    public String getObjectUUID() {
        return objectUUID;
    }

    @Nonnull
    public String getObjectForm() {
        return objectForm;
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
        private String objectForm;

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

        public Builder setObjectForm(String objectForm) {
            this.objectForm = objectForm;
            return this;
        }

        public Builder of(CompletedObject completedObject) {
            this.microFormatUUID = completedObject.microFormatUUID;
            this.objectUUID = completedObject.objectUUID;
            this.objectForm = completedObject.objectForm;
            return this;
        }

        public Builder of(IdentifiedObject identifiedObject) {
            this.microFormatUUID = identifiedObject.getMicroFormatUUID();
            this.objectUUID = identifiedObject.getObjectUUID();
            this.objectForm = identifiedObject.getObjectForm();
            return this;
        }

        public Builder of(UnidentifiedObject unidentifiedObject) {
            this.microFormatUUID = unidentifiedObject.getMicroFormatUUID();
            this.objectUUID = unidentifiedObject.getObjectUUID();
            this.objectForm = FORM_UNKNOWN;
            return this;
        }

        public CompletedObject build() {
            return new CompletedObject(this);
        }

    }

}
