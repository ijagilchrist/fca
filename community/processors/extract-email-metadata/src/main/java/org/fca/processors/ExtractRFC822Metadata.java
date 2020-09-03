package org.fca.processors;

import org.fca.forms.EMailForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.EMailMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtractRFC822Metadata extends ExtractTikaMetadata {

    public ExtractRFC822Metadata() {
        super(EMailForms.FORM_EMAIL);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = super.getOutputMicroFormatTypes();
        outputMicroFormatTypes.add(EMailMetadata.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (this.isOutlookDocument(identifiedObject.getObjectMimetype())) {

            EMailMetadata.Builder emailMetadata = EMailMetadata.builder()
                    .setMicroFormatUUID(UUID.randomUUID().toString())
                    .setEMailType("RFC822");

            EMailObjectExtractor.Builder extractor = EMailObjectExtractor.builder()
                    .setProcessingContext(context)
                    .setEMailMetadataBuilder(emailMetadata);
            updates = super.process(context,microFormat,extractor);

            if (updates.size() == 1 && updates.get(0) instanceof TikaMetadata) {

                this.extractMetadata((TikaMetadata)updates.get(0),emailMetadata);
                updates.add(emailMetadata.build());

            }

        }

        return updates;

    }

    private void extractMetadata(TikaMetadata tikaMetadata, EMailMetadata.Builder documentMetadataBuilder) {

        Map<String,Object> metadata = tikaMetadata.getTikaMetadata();

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf.parse(this.getFirst(metadata,sdf.format(new Date(0)),"date"));
            documentMetadataBuilder.setSent(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        documentMetadataBuilder.setFrom(this.getFirst(metadata, "","Message:From-Email"));
        documentMetadataBuilder.setTo(this.getFirst(metadata, "","Message-Recipient-Address"));
        documentMetadataBuilder.setCC(this.getFirst(metadata, "","Message:CC"));
        documentMetadataBuilder.setSubject(this.getLongest(metadata, "","subject"));

    }

    protected boolean isOutlookDocument(String mimetype) {

        if (mimetype == null) return false;

        switch (mimetype) {

            case "message/rfc822":
                return true;

            default:
                return false;
        }

    }

}
