package org.fca.processors;

import org.fca.forms.VideoForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.microformats.content.VideoContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.util.*;

public class ConvertVideoContent implements Processor {

    private final ConvertFFmpegContent videoConverter = new ConvertFFmpegContent(VideoForms.FORM_VIDEO,new String[]{ "-f","ogg" },"video/ogg");
    private final ConvertFFmpegContent audioConverter = new ConvertFFmpegContent(VideoForms.FORM_VIDEO,new String[]{ "-ar","16000","-f","wav" },"audio/wav");

    public ConvertVideoContent() { }

    @Override
    public String getInputMicroFormatType() {
        return IdentifiedObject.getQualifiedMicroFormatType(VideoForms.FORM_VIDEO);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(VideoContent.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (identifiedObject.getObjectForm().equals(VideoForms.FORM_VIDEO)) {

            List<MicroFormat> videoConversion = this.videoConverter.process(context,identifiedObject);

            if (videoConversion.size() == 1 && videoConversion.get(0) instanceof ConvertedMedia) {

                VideoContent videoContent = VideoContent.builder()
                        .setMicroFormatUUID(UUID.randomUUID().toString())
                        .setVideoType("OGG")
                        .setVideoUUID(((ConvertedMedia)videoConversion.get(0)).getMediaUUID())
                        .build();
                updates.add(videoContent);
            }

            List<MicroFormat> audioConversion = this.audioConverter.process(context,identifiedObject);

            if (audioConversion.size() == 1 && audioConversion.get(0) instanceof ConvertedMedia) {

                AudioContent audioContent = AudioContent.builder()
                        .setMicroFormatUUID(UUID.randomUUID().toString())
                        .setAudioType("WAV")
                        .setAudioUUID(((ConvertedMedia)audioConversion.get(0)).getMediaUUID())
                        .build();
                updates.add(audioContent);
            }

        }

        return updates;

    }

}
