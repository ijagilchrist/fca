package org.fca.processors;

import org.apache.tika.extractor.ParsingEmbeddedDocumentExtractor;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.apache.tika.parser.ParseContext;
import org.fca.processor.Context;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@ParametersAreNonnullByDefault
public abstract class TikaObjectExtractor extends ParsingEmbeddedDocumentExtractor {

    @Nonnull
    private final Context processingContext;

    protected TikaObjectExtractor(ParseContext parseContext, Context processingContext) {
        super(Objects.requireNonNull(parseContext, "parseContext"));
        this.processingContext = Objects.requireNonNull(processingContext, "processingContext");
    }

    @Override
    public boolean shouldParseEmbedded(Metadata metadata) {
        return true;
    }

    @Override
    public void parseEmbedded(InputStream stream, ContentHandler handler, Metadata metadata, boolean outputHtml)
            throws SAXException, IOException {

        String resourceName = metadata.get(TikaMetadataKeys.RESOURCE_NAME_KEY);
        String name = (resourceName != null) ? resourceName : UUID.randomUUID().toString();
        String mimetype = metadata.get("Content-Type");
        String modificationDate = metadata.get("Last-Modified");
        String lastModified = this.getLastModified(metadata,modificationDate);

        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Parent-UUID",processingContext.getObjectUUID());
        if (lastModified != null) headers.put("Last-Modified",lastModified);
        if (mimetype != null) headers.put("Content-Type",mimetype);

        String childUUID = processingContext.putObject(stream,name,headers);
        this.newChild(childUUID);

    }

    protected abstract void newChild(String childUUID);

    private String getLastModified(Metadata metadata, String modificationDate) {
        String lastModified = modificationDate;
        for (String name: metadata.names()) {
            switch (name) {

                case "dcterms:created":
                case "meta:creation-date":
                case "date":
                case "dcterms:modified":
                case "Last-Modified":
                case "Last-Save-Date":
                case "meta:save-date":
                    lastModified = this.latest(modificationDate,metadata.get(name));
                    break;

                default:
                    break;
            }
        }
        return lastModified;
    }

    private String latest(String date1, String date2) {
        if (date1 == null) return date2;
        return (date1.compareTo(date2) > 0) ? date1 : date2;
    }

}
