package org.fca.processors;

import org.fca.forms.DocumentForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.DocumentMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtractHTMLMetadata extends ExtractTikaMetadata {

    public ExtractHTMLMetadata() {
        super(DocumentForms.FORM_DOCUMENT);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = super.getOutputMicroFormatTypes();
        outputMicroFormatTypes.add(DocumentMetadata.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (this.isHTMLDocument(identifiedObject.getObjectMimetype())) {

            DocumentMetadata.Builder documentMetadata = DocumentMetadata.builder()
                    .setMicroFormatUUID(UUID.randomUUID().toString())
                    .setDocumentType("HTML");

            DocumentObjectExtractor.Builder extractor = DocumentObjectExtractor.builder()
                    .setProcessingContext(context)
                    .setDocumentMetadataBuilder(documentMetadata);
            updates = super.process(context,microFormat,extractor);

            if (updates.size() >= 1 && updates.get(0) instanceof TikaMetadata) {

                this.extractMetadata((TikaMetadata)updates.get(0),documentMetadata);
                updates.add(documentMetadata.build());

            }

        }

        return updates;

    }

    private void extractMetadata(TikaMetadata tikaMetadata, DocumentMetadata.Builder documentMetadataBuilder) {

        Map<String,Object> metadata = tikaMetadata.getTikaMetadata();

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf.parse(this.getFirst(metadata,sdf.format(new Date(0)),"dcterms:created","meta:creation-date"));
            documentMetadataBuilder.setCreationDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date date = sdf.parse(this.getLast(metadata,sdf.format(new Date(0)),"date","dcterms:modified","Last-Modified","Last-Save-Date","meta:save-date"));
            documentMetadataBuilder.setModificationDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        documentMetadataBuilder.setAuthors(this.getAll(metadata,"dc:creator","creator","meta:author","meta:last-author","Last-Author","Author"));
        documentMetadataBuilder.setTitle(this.getLongest(metadata, "","dc:title","title"));

    }

    protected boolean isHTMLDocument(String mimetype) {

        if (mimetype == null) return false;

        switch (mimetype) {

            case "text/html":
                return true;

            default:
                return false;
        }

    }

}
