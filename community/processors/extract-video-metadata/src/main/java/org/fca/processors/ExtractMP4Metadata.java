package org.fca.processors;

import org.fca.forms.VideoForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.VideoMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;

import java.util.*;

public class ExtractMP4Metadata extends ExtractTikaMetadata {

    public ExtractMP4Metadata() {
        super(VideoForms.FORM_VIDEO);
    }
    
    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = super.getOutputMicroFormatTypes();
        outputMicroFormatTypes.add(VideoMetadata.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (this.isMP4Video(identifiedObject.getObjectMimetype())) {

            updates = super.process(context,microFormat);

            if (updates.size() >= 1 && updates.get(0) instanceof TikaMetadata) {

                VideoMetadata.Builder videoMetadata = VideoMetadata.builder()
                        .setMicroFormatUUID(UUID.randomUUID().toString());
                this.extractMetadata((TikaMetadata)updates.get(0),videoMetadata);
                updates.add(videoMetadata.build());


            }

        }

        return updates;

    }

    private void extractMetadata(TikaMetadata tikaMetadata, VideoMetadata.Builder videoMetadataBuilder) {

        Map<String,Object> metadata = tikaMetadata.getTikaMetadata();

        videoMetadataBuilder.setDuration(Double.parseDouble(this.getFirst(metadata,"0.0","length")));
        videoMetadataBuilder.setVideoType("MP4");

    }

    protected boolean isMP4Video(String mimetype) {

        if (mimetype == null) return false;

        switch (mimetype) {

            case "video/mp4":
            case "video/quicktime":
                return true;

            default:
                return false;
        }

    }

}
