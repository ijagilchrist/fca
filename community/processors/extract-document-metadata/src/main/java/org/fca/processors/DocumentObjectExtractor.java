package org.fca.processors;

import org.apache.tika.parser.ParseContext;
import org.fca.microformats.metadata.DocumentMetadata;
import org.fca.processor.Context;

import javax.annotation.Nonnull;
import java.util.Objects;

public class DocumentObjectExtractor  extends TikaObjectExtractor {

    @Nonnull
    private final DocumentMetadata.Builder documentMetadataBuilder;

    private DocumentObjectExtractor(DocumentObjectExtractor.Builder builder) {
        super(Objects.requireNonNull(builder.parseContext, "parseContext"),
                Objects.requireNonNull(builder.processingContext));
        this.documentMetadataBuilder = Objects.requireNonNull(builder.documentMetadataBuilder, "documentMetadataBuilder");
    }

    public static DocumentObjectExtractor.Builder builder() {
        return new DocumentObjectExtractor.Builder();
    }

    @Override
    protected void newChild(String childUUID) {
        this.documentMetadataBuilder.setEmbedded(childUUID);
    }

    public static class Builder implements TikaObjectExtractorBuilder {

        private ParseContext parseContext;
        private Context processingContext;
        private DocumentMetadata.Builder documentMetadataBuilder;

        private Builder() {
        }

        public DocumentObjectExtractor.Builder setParseContext(ParseContext parseContext) {
            this.parseContext = parseContext;
            return this;
        }

        public DocumentObjectExtractor.Builder setProcessingContext(Context processingContext) {
            this.processingContext = processingContext;
            return this;
        }

        public DocumentObjectExtractor.Builder setDocumentMetadataBuilder(DocumentMetadata.Builder documentMetadataBuilder) {
            this.documentMetadataBuilder = documentMetadataBuilder;
            return this;
        }

        public DocumentObjectExtractor build() {
            return new DocumentObjectExtractor(this);
        }

    }

}
