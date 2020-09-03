package org.fca.microformats.metadata;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class DocumentMetadata extends MicroFormat {

    @Nonnull
    private final String documentType;

    @Nonnull
    private final List<String> authors;

    @Nonnull
    private final Date creationDate;

    @Nonnull
    private final Date modificationDate;

    @Nonnull
    private final String title;

    @Nonnull
    private final List<String> embedded;

    public static final String MICROFORMAT_TYPE = "metadata.document";

    public DocumentMetadata() {
        super(null,null);
        this.documentType = null;
        this.authors = null;
        this.creationDate = null;
        this.modificationDate = null;
        this.title = null;
        this.embedded = null;
    }

    private DocumentMetadata(Builder builder) {
        super(DocumentMetadata.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.documentType = Objects.requireNonNull(builder.documentType, "documentType");
        this.authors = Objects.requireNonNull(builder.authors, "authors");
        this.creationDate = Objects.requireNonNull(builder.creationDate, "creationDate");
        this.modificationDate = Objects.requireNonNull(builder.modificationDate, "modificationDate");
        this.title = Objects.requireNonNull(builder.title, "title");
        this.embedded = Objects.requireNonNull(builder.embedded, "embedded");
    }

    @Nonnull
    public String getDocumentType() {
        return documentType;
    }

    @Nonnull
    public List<String> getAuthors() {
        return authors;
    }

    @Nonnull
    public Date getCreationDate() {
        return creationDate;
    }

    @Nonnull
    public Date getModificationDate() {
        return modificationDate;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public List<String> getEmbedded() {
        return embedded;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;
        private String documentType;
        private List<String> authors;
        private Date creationDate;
        private Date modificationDate;
        private String title;
        private List<String> embedded;

        private Builder() {
            this.authors = new ArrayList<>();
            this.embedded = new ArrayList<>();
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setDocumentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder setAuthor(String author) {
            this.authors.add(author);
            return this;
        }

        public Builder setAuthors(Collection<String> authors) {
            this.authors.addAll(authors);
            return this;
        }

        public Builder setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder setModificationDate(Date modificationDate) {
            this.modificationDate = modificationDate;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setEmbedded(String embedded) {
            this.embedded.add(embedded);
            return this;
        }

        public Builder setEmbedded(Collection<String> embedded) {
            this.embedded.addAll(embedded);
            return this;
        }

        public Builder of(DocumentMetadata documentMetadata) {
            this.microFormatUUID = documentMetadata.microFormatUUID;
            this.documentType = documentMetadata.documentType;
            this.authors = documentMetadata.authors;
            this.creationDate = documentMetadata.creationDate;
            this.modificationDate = documentMetadata.modificationDate;
            this.title = documentMetadata.title;
            this.embedded = documentMetadata.embedded;
            return this;
        }

        public DocumentMetadata build() {
            return new DocumentMetadata(this);
        }

    }

}
