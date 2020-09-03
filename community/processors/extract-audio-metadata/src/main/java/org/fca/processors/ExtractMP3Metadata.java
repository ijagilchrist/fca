package org.fca.processors;

import org.fca.forms.AudioForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.AudioMetadata;
import org.fca.microformats.metadata.TikaMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;

import java.util.*;

public class ExtractMP3Metadata extends ExtractTikaMetadata {

    public ExtractMP3Metadata() {
        super(AudioForms.FORM_AUDIO);
    }
    
    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = super.getOutputMicroFormatTypes();
        outputMicroFormatTypes.add(AudioMetadata.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (identifiedObject.getObjectMimetype().equals("audio/mpeg")) {

            updates = super.process(context,microFormat);

            if (updates.size() == 1 && updates.get(0) instanceof TikaMetadata) {

                AudioMetadata.Builder audioMetadata = AudioMetadata.builder()
                        .setMicroFormatUUID(UUID.randomUUID().toString());
                this.extractMetadata((TikaMetadata)updates.get(0),audioMetadata);
                updates.add(audioMetadata.build());


            }

        }

        return updates;

    }

    private void extractMetadata(TikaMetadata tikaMetadata, AudioMetadata.Builder audioMetadataBuilder) {

        Map<String,Object> metadata = tikaMetadata.getTikaMetadata();

        audioMetadataBuilder.setSampleRate(Double.parseDouble(this.getFirst(metadata,"0","xmpDM:audioSampleRate","samplerate")));
        audioMetadataBuilder.setChannels(Integer.parseInt(this.getFirst(metadata,"0","channels")));
        audioMetadataBuilder.setCompressor(this.getFirst(metadata,"-","xmpDM:audioCompressor"));
        audioMetadataBuilder.setDuration(Double.parseDouble(this.getFirst(metadata,"0.0","xmpDM:duration")));
        audioMetadataBuilder.setAudioType("MP3");
    }

    protected boolean isMP3Audio(String mimetype) {

        if (mimetype == null) return false;

        switch (mimetype) {

            case "audio/mpeg":
                return true;

            default:
                return false;
        }

    }

}
