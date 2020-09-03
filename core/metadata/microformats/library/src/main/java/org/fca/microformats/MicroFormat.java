package org.fca.microformats;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.util.Map;

@javax.annotation.ParametersAreNonnullByDefault
public abstract class MicroFormat {

    @javax.annotation.Nonnull
    protected final String microFormatType;
    @javax.annotation.Nonnull
    protected final String microFormatUUID;

    public MicroFormat(String microFormatType, String microFormatUUID) {
        this.microFormatType = microFormatType;
        this.microFormatUUID = microFormatUUID;
    }

    @Nonnull
    public String getMicroFormatType() {
        return microFormatType;
    }

    @Nonnull
    public String getMicroFormatUUID() {
        return microFormatUUID;
    }

    @JsonIgnore
    public String getQualifiedMicroFormatType() { return microFormatType; }

    public MicroFormat merge(MicroFormat other) { return this; }

}
