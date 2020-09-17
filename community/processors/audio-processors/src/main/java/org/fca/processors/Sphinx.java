package org.fca.processors;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;

public class Sphinx {

    @Nonnull
    private final List<String> options;

    @Nonnull
    private final String acousticModel;

    @Nonnull
    private final String dictionary;

    @Nonnull
    private final String languageModel;

    public Sphinx(List<String> options, String acousticModel, String dictionary, String languageModel) {
        this.options = options;
        this.acousticModel = acousticModel;
        this.dictionary = dictionary;
        this.languageModel = languageModel;
    }

    public Sphinx.Processor newProcessor() {
        return new Sphinx.Processor();
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

            String exec = System.getProperty("os.name").toLowerCase().startsWith("win") ? "pocketsphinx_continuous.exe " : "pocketsphinx_continuous";
            List<String> arguments = new ArrayList<String>();
            arguments.add(exec);
            arguments.add("-samprate");
            arguments.add("16000");
            arguments.add("-lm");
            arguments.add(Sphinx.this.languageModel);
            arguments.add("-dict");
            arguments.add(Sphinx.this.dictionary);
            arguments.add("-hmm");
            arguments.add(Sphinx.this.acousticModel);
            arguments.add("-remove_noise");
            arguments.add("no");
            arguments.addAll(Sphinx.this.options);
            arguments.add("-infile");
            arguments.add(this.inputFile.getName());

            ProcessBuilder pb = new ProcessBuilder(arguments);
            pb.inheritIO();
            pb.redirectOutput(this.outputFile);
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
