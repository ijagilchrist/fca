package org.fca.processors;

import org.fca.forms.VideoForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.AudioContent;
import org.fca.microformats.content.VideoContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConvertVideoContent implements Processor {

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

            {

                Ffmpeg.Processor processor = new Ffmpeg(Arrays.asList(new String[]{ "-f","ogg" }))
                        .newProcessor();

                try {

                    InputStream stream = context.getObject(identifiedObject.getObjectName());

                    if (stream != null) {

                        InputStream convertedStream = processor.process(stream);

                        if (convertedStream != null) {

                            String videoUUID = UUID.randomUUID().toString();

                            Map<String,String> headers = new HashMap<>();
                            headers.put("Content-Type","video/ogg");
                            context.putDerivedObject(convertedStream,videoUUID,headers);

                            VideoContent audioContent = VideoContent.builder()
                                    .setMicroFormatUUID(UUID.randomUUID().toString())
                                    .setVideoType("OGG")
                                    .setVideoUUID(videoUUID)
                                    .build();
                            updates.add(audioContent);

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                processor.close();

            }

            {

                Ffmpeg.Processor processor = new Ffmpeg(Arrays.asList(new String[]{ "-ar","16000","-f","wav" }))
                        .newProcessor();

                try {

                    InputStream stream = context.getObject(identifiedObject.getObjectName());

                    if (stream != null) {

                        InputStream convertedStream = processor.process(stream);

                        if (convertedStream != null) {

                            String audioUUID = UUID.randomUUID().toString();

                            Map<String,String> headers = new HashMap<>();
                            headers.put("Content-Type","audio/wav");
                            context.putDerivedObject(convertedStream,audioUUID,headers);

                            AudioContent audioContent = AudioContent.builder()
                                    .setMicroFormatUUID(UUID.randomUUID().toString())
                                    .setAudioType("WAV")
                                    .setAudioUUID(audioUUID)
                                    .build();
                            updates.add(audioContent);

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                processor.close();

            }

        }

        return updates;

    }

}
