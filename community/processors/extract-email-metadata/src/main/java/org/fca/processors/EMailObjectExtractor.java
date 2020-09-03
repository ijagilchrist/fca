package org.fca.processors;

import org.apache.tika.parser.ParseContext;
import org.fca.microformats.metadata.EMailMetadata;
import org.fca.processor.Context;

import javax.annotation.Nonnull;
import java.util.Objects;

public class EMailObjectExtractor  extends TikaObjectExtractor {

    @Nonnull
    private final EMailMetadata.Builder emailMetadataBuilder;

    private EMailObjectExtractor(EMailObjectExtractor.Builder builder) {
        super(Objects.requireNonNull(builder.parseContext, "parseContext"),
                Objects.requireNonNull(builder.processingContext));
        this.emailMetadataBuilder = Objects.requireNonNull(builder.emailMetadataBuilder, "emailMetadataBuilder");
    }

    public static EMailObjectExtractor.Builder builder() {
        return new EMailObjectExtractor.Builder();
    }

    @Override
    protected void newChild(String childUUID) {
        this.emailMetadataBuilder.setAttachment(childUUID);
    }

    public static class Builder implements TikaObjectExtractorBuilder {

        private ParseContext parseContext;
        private Context processingContext;
        private EMailMetadata.Builder emailMetadataBuilder;

        private Builder() {
        }

        public EMailObjectExtractor.Builder setParseContext(ParseContext parseContext) {
            this.parseContext = parseContext;
            return this;
        }

        public EMailObjectExtractor.Builder setProcessingContext(Context processingContext) {
            this.processingContext = processingContext;
            return this;
        }

        public EMailObjectExtractor.Builder setEMailMetadataBuilder(EMailMetadata.Builder emailMetadataBuilder) {
            this.emailMetadataBuilder = emailMetadataBuilder;
            return this;
        }

        public EMailObjectExtractor build() {
            return new EMailObjectExtractor(this);
        }

    }

}
