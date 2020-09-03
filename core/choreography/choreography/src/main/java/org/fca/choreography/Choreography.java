package org.fca.choreography;

import org.fca.microformats.MicroFormat;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class Choreography implements Processor {

    @Nonnull
    private final String inputMicroFormat;

    @Nonnull
    private final List<String> outputMicroFormats;

    @Nonnull
    private final Map<String,List<Processor>> registry;

    private Choreography(Builder builder) {
        this.inputMicroFormat = Objects.requireNonNull(builder.inputMicroFormat,"inputMicroFormat");
        this.outputMicroFormats = new ArrayList<>(Objects.requireNonNull(builder.outputMicroFormats,"outputMicroFormats"));
        this.registry = Objects.requireNonNull(builder.registry, "registry");
    }

    @Override
    public String getInputMicroFormatType() {
        return this.inputMicroFormat;
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        return this.outputMicroFormats;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat in) {

        List<MicroFormat> stack = new ArrayList<>();
        stack.add(in);

        int p = 0;
        while( p < stack.size()) {

            MicroFormat microFormat = stack.get(p++);
            String microFormatType = microFormat.getQualifiedMicroFormatType();
            if (!this.registry.containsKey(microFormatType)) continue;

            for (Processor processor: this.registry.get(microFormatType)) {
                stack.addAll(processor.process(context,microFormat));
            }

        }

        return stack.stream()
                .skip(1)  // Ignore input microformat
                .map(s -> s.getMicroFormatUUID())  // Get UUIDs
                .distinct()  // Remove duplicates
                .map(s -> stack.stream()
                        .filter(t -> t.getMicroFormatUUID().equals(s))
                        .reduce((u,v) -> v.merge(u)))  // Merge microformats with same UUID
                .map(s -> s.get()) // Convert Optional<MicroFormat> to MicroFormat
                .collect(Collectors.toList());

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String inputMicroFormat;
        private Set<String> outputMicroFormats;
        private Map<String,List<Processor>> registry;

        private Builder() {
            this.outputMicroFormats = new HashSet<String>();
            this.registry = new HashMap<>();
        }

        public Builder setInitialProcessor(Processor initialProcessor) {
            this.inputMicroFormat = initialProcessor.getInputMicroFormatType();
            this.setProcessor(initialProcessor);
            return this;
        }

        public Builder setProcessor(Processor processor) {
            String inputMicroFormat = processor.getInputMicroFormatType();
            if (!this.registry.containsKey(inputMicroFormat)) this.registry.put(inputMicroFormat,new ArrayList<>());
            this.registry.get(inputMicroFormat).add(processor);
            this.outputMicroFormats.addAll(processor.getOutputMicroFormatTypes());
            return this;
        }

        public Builder of(Choreography choreography) {
            this.inputMicroFormat = choreography.inputMicroFormat;
            this.outputMicroFormats = new HashSet<>(choreography.outputMicroFormats);
            this.registry = choreography.registry;
            return this;
        }

        public Choreography build() {
            return new Choreography(this);
        }

    }

}
