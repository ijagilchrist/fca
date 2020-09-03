package org.fca.forms;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@javax.annotation.ParametersAreNonnullByDefault
public abstract class Forms {

    public static final String FORM_UNKNOWN = "unknown";

    @javax.annotation.Nonnull
    private final Map<String,String> mimetypeToForm;

    @javax.annotation.Nonnull
    private final Map<String,String> mimetypeToSubform;

    protected Forms(Builder builder) {
        this.mimetypeToForm = Objects.requireNonNull(builder.mimetypeToForm, "mimetypeToForm");
        this.mimetypeToSubform = Objects.requireNonNull(builder.mimetypeToSubform, "mimetypeToSubform");
    }

    public static Builder builder() {
        return new Builder();
    }

    @javax.annotation.Nonnull
    public String getForm(String mimetype) {
        return mimetypeToForm.containsKey(mimetype) ? mimetypeToForm.get(mimetype) : FORM_UNKNOWN;
    }

    @javax.annotation.Nonnull
    public String getSubform(String mimetype) {
        return mimetypeToSubform.containsKey(mimetype) ? mimetypeToSubform.get(mimetype) : FORM_UNKNOWN;
    }

    public boolean isKnown(String mimetype) {
        return mimetypeToForm.containsKey(mimetype);
    }

    public static class Builder {

        private Map<String,String> mimetypeToForm = new HashMap<>();
        private Map<String,String> mimetypeToSubform = new HashMap<>();

        private Builder() {
        }

        public Builder setMimetypeToForm(String mimetype, String form, String subform) {
            this.mimetypeToForm.put(mimetype,form);
            this.mimetypeToSubform.put(mimetype,subform);
            return this;
        }

        public Builder of(Forms forms) {
            this.mimetypeToForm = forms.mimetypeToForm;
            this.mimetypeToSubform = forms.mimetypeToSubform;
            return this;
        }

    }
}
