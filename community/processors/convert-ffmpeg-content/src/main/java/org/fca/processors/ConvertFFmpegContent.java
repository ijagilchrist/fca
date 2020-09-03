package org.fca.processors;

import org.fca.microformats.MicroFormat;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;

public class ConvertFFmpegContent implements Processor {

    @Nonnull
    private final String form;

    @Nonnull
    private final String[] options;

    @Nonnull
    private final String contentType;

    public ConvertFFmpegContent(String form, String[] options, String contentType) {
        this.form = form;
        this.options = options;
        this.contentType = contentType;
    }

    @Override
    public String getInputMicroFormatType() {
        return IdentifiedObject.getQualifiedMicroFormatType(this.form);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(ConvertedMedia.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (identifiedObject.getObjectForm().equals(this.form)) {

            try {

                InputStream stream = context.getObject(identifiedObject.getObjectName());

                if (stream != null) {

                    String mediaUUID = UUID.randomUUID().toString();

                    File inputFile = new File(String.format("%s.in",mediaUUID));
                    File outputFile = new File(String.format("%s.out",mediaUUID));

                    {
                        FileOutputStream output = new FileOutputStream(inputFile);
                        stream.transferTo(output);
                        output.close();
                        stream.close();
                    }

                    String exec = System.getProperty("os.name").toLowerCase().startsWith("win") ? "ffmpeg" : "./ffmpeg";
                    String[] arguments = new String[options.length+4];
                    arguments[0] = exec;
                    arguments[1] = "-i";
                    arguments[2] = inputFile.getName();
                    System.arraycopy(options,0,arguments,3,options.length);
                    arguments[arguments.length-1] = outputFile.getName();
                    ProcessBuilder pb = new ProcessBuilder(arguments);
                    pb.inheritIO();
                    Process p = pb.start();
                    p.waitFor();

                    {
                        FileInputStream input = new FileInputStream(outputFile);
                        Map<String,String> headers = new HashMap<String,String>();
                        headers.put("Content-Type",this.contentType);
                        context.putDerivedObject(input,mediaUUID,headers);
                        input.close();
                        ConvertedMedia convertedMedia = ConvertedMedia.builder()
                                .setMicroFormatUUID(UUID.randomUUID().toString())
                                .setMediaUUID(mediaUUID)
                                .build();
                        updates.add(convertedMedia);
                    }

                    inputFile.delete();
                    outputFile.delete();

                    stream.close();

                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        return updates;

    }

}
