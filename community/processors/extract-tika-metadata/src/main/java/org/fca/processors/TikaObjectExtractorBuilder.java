package org.fca.processors;

import org.apache.tika.extractor.EmbeddedDocumentExtractor;
import org.apache.tika.parser.ParseContext;

public interface TikaObjectExtractorBuilder {

    public TikaObjectExtractorBuilder setParseContext(ParseContext parseContext);

    public EmbeddedDocumentExtractor build();

}
