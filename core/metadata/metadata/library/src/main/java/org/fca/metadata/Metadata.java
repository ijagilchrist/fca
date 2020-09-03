package org.fca.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Metadata {

    @Nonnull
    private final String objectUUID;

    @Nonnull
    private final String source;

    @Nonnull
    private final String parentUUID;

    @Nonnull
    private final String metadataUUID;

    @Nonnull
    private final Long timestamp;

    @Nonnull
    private final List<MicroFormat> microFormats;

    private Metadata(Builder builder) {
        this.objectUUID = Objects.requireNonNull(builder.objectUUID,"objectUUID");
        this.source = Objects.requireNonNull(builder.source, "source");
        this.parentUUID = Objects.requireNonNull(builder.parentUUID,"parentUUID");
        this.metadataUUID = Objects.requireNonNull(builder.metadataUUID,"metadataUUID");
        this.timestamp = Objects.requireNonNull(builder.timestamp,"timestamp");
        this.microFormats = Objects.requireNonNull(builder.microFormats, "microFormats");
    }

    @Nonnull
    public String getObjectUUID() { return objectUUID; }

    @Nonnull
    public String getSource() {
        return source;
    }

    @Nonnull
    public String getParentUUID() { return parentUUID; }

    @Nonnull
    public String getMetadataUUID() { return metadataUUID; }

    @Nonnull
    public Long getTimestamp() { return timestamp; }

    @Nonnull
    public List<MicroFormat> getMicroFormats() {
        return microFormats;
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

        private String objectUUID;
        private String source;
        private String parentUUID;
        private String metadataUUID;
        private Long timestamp;
        private List<MicroFormat> microFormats;

        private Builder() {
        }

        public Builder setObjectUUID(String objectUUID) {
            this.objectUUID = objectUUID;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setParentUUID(String parentUUID) {
            this.parentUUID = parentUUID;
            return this;
        }

        public Builder setMetadataUUID(String metadataUUID) {
            this.metadataUUID = metadataUUID;
            return this;
        }

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setMicroFormats(List<MicroFormat> microFormats) {
            this.microFormats = microFormats;
            return this;
        }

        public Builder fromJSON(String json) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Reader reader = mapper.readValue(json, Reader.class);
                return this.fromJSONReader(reader);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Builder fromJSONReader(Metadata.Reader reader) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Objects.requireNonNull(reader,"reader");
                this.objectUUID = Objects.requireNonNull(reader.objectUUID,"reader.objectUUID");
                this.source = Objects.requireNonNull(reader.source,"reader.source");
                this.parentUUID = Objects.requireNonNull(reader.parentUUID,"reader.parentUUID");
                this.metadataUUID = Objects.requireNonNull(reader.metadataUUID,"reader.metadataUUID");
                this.timestamp = Objects.requireNonNull(reader.timestamp,"reader.timestamp");
                Objects.requireNonNull(reader.microFormats);
                this.microFormats = new ArrayList<MicroFormat>();
                for (int i=0; i<reader.microFormats.size(); i++) {
                    JsonNode node = Objects.requireNonNull(reader.microFormats.get(i),"microFormatAsNode");
                    String microFormatType = Objects.requireNonNull(node.get("microFormatType").asText(),"microformatName");
                    Class<? extends MicroFormat> pojoClass = Objects.requireNonNull(MicroFormats.find(microFormatType),"pojoClass");
                    MicroFormat microFormat = Objects.requireNonNull(mapper.treeToValue(node,pojoClass),"microFormat");
                    this.microFormats.add(microFormat);
                }
                return this;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }

        public Builder of(Metadata metadata) {
            this.objectUUID = metadata.objectUUID;
            this.source = metadata.source;
            this.parentUUID = metadata.parentUUID;
            this.metadataUUID = metadata.metadataUUID;
            this.timestamp = metadata.timestamp;
            this.microFormats = metadata.microFormats;
            return this;
        }

        public Metadata build() {
            return new Metadata(this);
        }

    }

    public static class Reader {

        public String objectUUID;
        public String source;
        public String parentUUID;
        public String metadataUUID;
        public Long timestamp;
        public JsonNode microFormats;

        public Reader() {}

    }

}
