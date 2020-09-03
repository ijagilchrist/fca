package org.fca.microformats.metadata;

import org.fca.microformats.MicroFormat;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class EMailMetadata extends MicroFormat {

    @Nonnull
    private final String emailType;

    @Nonnull
    private final Date sent;

    @Nonnull
    private final String from;

    @Nonnull
    private final String to;

    @Nonnull
    private final String cc;

    @Nonnull
    private final String subject;

    @Nonnull
    private final List<String> attachments;

    public static final String MICROFORMAT_TYPE = "metadata.email";

    public EMailMetadata() {
        super(null,null);
        this.emailType = null;
        this.sent = null;
        this.from = null;
        this.to = null;
        this.cc = null;
        this.subject = null;
        this.attachments = null;
    }

    private EMailMetadata(Builder builder) {
        super(EMailMetadata.MICROFORMAT_TYPE,
                Objects.requireNonNull(builder.microFormatUUID, "microFormatUUID"));
        this.emailType = Objects.requireNonNull(builder.emailType, "emailType");
        this.sent = Objects.requireNonNull(builder.sent, "sent");
        this.from = Objects.requireNonNull(builder.from, "from");
        this.to = Objects.requireNonNull(builder.to, "to");
        this.cc = Objects.requireNonNull(builder.cc, "cc");
        this.subject = Objects.requireNonNull(builder.subject, "subject");
        this.attachments = Objects.requireNonNull(builder.attachments, "attachments");
    }

    @Nonnull
    public String getEMailType() {
        return emailType;
    }

    @Nonnull
    public Date getSent() {
        return sent;
    }

    @Nonnull
    public String getFrom() {
        return from;
    }

    @Nonnull
    public String getTo() {
        return to;
    }

    @Nonnull
    public String getCC() {
        return cc;
    }

    @Nonnull
    public String getSubject() {
        return subject;
    }

    @Nonnull
    public List<String> getAttachments() {
        return attachments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String microFormatUUID;
        private String emailType;
        private Date sent;
        private String from;
        private String to;
        private String cc;
        private String subject;
        private List<String> attachments;

        private Builder() {
            this.attachments = new ArrayList<>();
        }

        public Builder setMicroFormatUUID(String microFormatUUID) {
            this.microFormatUUID = microFormatUUID;
            return this;
        }

        public Builder setEMailType(String emailType) {
            this.emailType = emailType;
            return this;
        }

        public Builder setSent(Date sent) {
            this.sent = sent;
            return this;
        }

        public Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public Builder setCC(String cc) {
            this.cc = cc;
            return this;
        }

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setAttachment(String attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public Builder setAttachments(Collection<String> attachments) {
            this.attachments.addAll(attachments);
            return this;
        }

        public Builder of(EMailMetadata emailMetadata) {
            this.microFormatUUID = emailMetadata.microFormatUUID;
            this.emailType = emailMetadata.emailType;
            this.sent = emailMetadata.sent;
            this.from = emailMetadata.from;
            this.to = emailMetadata.to;
            this.cc = emailMetadata.cc;
            this.subject = emailMetadata.subject;
            this.attachments = emailMetadata.attachments;
            return this;
        }

        public EMailMetadata build() {
            return new EMailMetadata(this);
        }

    }

}
