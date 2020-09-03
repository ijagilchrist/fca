package org.fca.metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.MicroFormats;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class MetadataCollection {

    @Nonnull
    private final List<Metadata> metadata;

    private MetadataCollection(Builder builder) {
        this.metadata = Objects.requireNonNull(builder.metadata, "metadata");
    }

    @Nonnull
    public List<Metadata> getMetadata() {
        return metadata;
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Metadata> metadata;

        private Builder() {
        }

        public Builder setMetadata(List<Metadata> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder fromJSON(String json) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                MetadataCollection.Reader reader = mapper.readValue(json, MetadataCollection.Reader.class);
                Objects.requireNonNull(reader,"reader");
                Objects.requireNonNull(reader.metadata);
                this.metadata = new ArrayList<Metadata>();
                for (Metadata.Reader reader1: reader.metadata) {
                    Metadata metadata = Metadata.builder()
                            .fromJSONReader(reader1)
                            .build();
                    this.metadata.add(metadata);
                }
                return this;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Builder of(MetadataCollection metadataCollection) {
            this.metadata = metadataCollection.metadata;
            return this;
        }

        public MetadataCollection build() {
            return new MetadataCollection(this);
        }
    }

    public static class Reader {

        public List<Metadata.Reader> metadata;

        public Reader() {}

    }

}
