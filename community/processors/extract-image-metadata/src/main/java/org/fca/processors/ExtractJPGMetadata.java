package org.fca.processors;

import org.fca.forms.ImageForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.ImageMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;

import java.util.*;

public class ExtractJPGMetadata extends ExtractTikaMetadata {

    public ExtractJPGMetadata() {
        super(ImageForms.FORM_IMAGE);
    }
    
    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = super.getOutputMicroFormatTypes();
        outputMicroFormatTypes.add(ImageMetadata.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (this.isJPGImage(identifiedObject.getObjectMimetype())) {

            updates = super.process(context,microFormat);

            if (updates.size() == 1 && updates.get(0) instanceof TikaMetadata) {

                ImageMetadata.Builder imageMetadata = ImageMetadata.builder()
                        .setMicroFormatUUID(UUID.randomUUID().toString());
                this.extractMetadata((TikaMetadata)updates.get(0),imageMetadata);
                updates.add(imageMetadata.build());


            }

        }

        return updates;

    }

    private void extractMetadata(TikaMetadata tikaMetadata, ImageMetadata.Builder imageMetadataBuilder) {

        Map<String,Object> metadata = tikaMetadata.getTikaMetadata();

        imageMetadataBuilder.setHeight(Integer.parseInt(this.getFirst(metadata,"0","height")));
        imageMetadataBuilder.setWidth(Integer.parseInt(this.getFirst(metadata,"0","width")));
        imageMetadataBuilder.setImageType("JPG");

    }

    protected boolean isJPGImage(String mimetype) {

        if (mimetype == null) return false;

        switch (mimetype) {

            case "image/jpg":
            case "image/jpeg":
                return true;

            default:
                return false;
        }

    }

}
