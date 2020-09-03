package org.fca.processors;

import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.EmbeddedDocumentExtractor;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.DefaultParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExtractTikaMetadata implements Processor {

    private final String form;

    public ExtractTikaMetadata(String form) {
        this.form = form;
    }

    @Override
    public String getInputMicroFormatType() {
        return IdentifiedObject.getQualifiedMicroFormatType(this.form);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(TikaMetadata.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {
        return this.process(context,microFormat,null);
    }

    public List<MicroFormat> process(Context context, MicroFormat microFormat, TikaObjectExtractorBuilder extractor) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (identifiedObject.getObjectForm().equals(this.form)) {

            try {

                InputStream stream = context.getObject(identifiedObject.getObjectName());

                if (stream != null) {

                    DefaultParser parser = new DefaultParser();

                    BodyContentHandler handler = new BodyContentHandler();
                    Metadata metadata = new Metadata();
                    metadata.add(Metadata.CONTENT_TYPE,identifiedObject.getObjectMimetype());
                    ParseContext parseContext = new ParseContext();
                    if (extractor != null) {
                        parseContext.set(Parser.class, parser);
                        extractor.setParseContext(parseContext);
                        parseContext.set(EmbeddedDocumentExtractor.class, extractor.build());
                    }

                    parser.parse(stream,handler,metadata,parseContext);

                    TikaMetadata.Builder tikaMetadata = TikaMetadata.builder()
                            .setMicroFormatUUID(UUID.randomUUID().toString());
                    this.extractMetadata(metadata,tikaMetadata);

                    updates.add(tikaMetadata.build());

                    stream.close();

                }

            } catch (TikaException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

        }

        return updates;

    }

    protected String getFirst(Map<String,Object> metadata, String defaultValue, String... names) {
        String result = defaultValue;
        for (String name: names) {
            if (!(metadata.get(name) instanceof String)) continue;
            String value = (String)metadata.get(name);
            if ((result==null) || (value.compareTo(result) < 0)) result = value;
        }
        return result;
    }

    protected String getLast(Map<String,Object> metadata, String defaultValue, String... names) {
        String result = defaultValue;
        for (String name: names) {
            if (!(metadata.get(name) instanceof String)) continue;
            String value = (String)metadata.get(name);
            if ((result==null) || (value.compareTo(result) > 0)) result = value;
        }
        return result;
    }

    protected String getLongest(Map<String,Object> metadata, String defaultValue, String... names) {
        String result = defaultValue;
        for (String name: names) {
            if (!(metadata.get(name) instanceof String)) continue;
            String value = (String)metadata.get(name);
            if ((result==null) || (value.length() > result.length())) result = value;
        }
        return result;
    }

    protected Collection<String> getAll(Map<String,Object> metadata, String... names) {
        Set<String> values = new HashSet<>();
        for (String name: names) {
            if (!(metadata.get(name) instanceof String)) continue;
            String value = (String)metadata.get(name);
            values.add(value);
        }
        return values;
    }

    private void extractMetadata(Metadata metadata, TikaMetadata.Builder tikaMetadataBuilder) {

        Map<String,Object> tikaMetadata = new HashMap<>();
        for (String name: metadata.names()) {

            if (metadata.isMultiValued(name)) {
                tikaMetadata.put(name,metadata.getValues(name));
            } else {
                tikaMetadata.put(name,metadata.get(name));
            }

        }
        tikaMetadataBuilder.setTikaMetadata(tikaMetadata);

    }

}
