package org.fca.processors;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;

public class Ffmpeg {

    @Nonnull
    private final List<String> options;

    public Ffmpeg(List<String> options) {
        this.options = options;
    }

    public Ffmpeg.Processor newProcessor() {
        return new Processor();
    }

    public class Processor {

        @Nonnull
        private File inputFile;

        @Nonnull
        private File outputFile;

        public Processor() {
            String mediaUUID = UUID.randomUUID().toString();
            this.inputFile = new File(String.format("%s.in", mediaUUID));
            this.outputFile = new File(String.format("%s.out", mediaUUID));
        }

        public InputStream process(InputStream inputStream) throws IOException, InterruptedException {

            {
                FileOutputStream output = new FileOutputStream(this.inputFile);
                inputStream.transferTo(output);
                output.close();
            }

            String exec = System.getProperty("os.name").toLowerCase().startsWith("win") ? "ffmpeg" : "./ffmpeg";
            List<String> arguments = new ArrayList<String>();
            arguments.add(exec);
            arguments.add("-i");
            arguments.add(this.inputFile.getName());
            arguments.addAll(Ffmpeg.this.options);
            arguments.add(this.outputFile.getName());

            ProcessBuilder pb = new ProcessBuilder(arguments);
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();

            return new FileInputStream(this.outputFile);

        }

        public void close() {

            inputFile.delete();
            outputFile.delete();

        }

    }

}
