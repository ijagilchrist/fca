package org.fca.processors;

import org.fca.forms.ArchiveForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.metadata.ArchiveMetadata;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExtractZipMetadata implements Processor {

    public ExtractZipMetadata() { }

    @Override
    public String getInputMicroFormatType() {
        return IdentifiedObject.getQualifiedMicroFormatType(ArchiveForms.FORM_ARCHIVE);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(ArchiveMetadata.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (this.isZipFile(identifiedObject.getObjectMimetype())) {

            try {

                InputStream stream = context.getObject(identifiedObject.getObjectName());

                if (stream != null) {

                    ArchiveMetadata.Builder archiveMetadata = ArchiveMetadata.builder()
                            .setMicroFormatUUID(UUID.randomUUID().toString())
                            .setArchiveType("ZIP");

                    ZipInputStream zipStream = new ZipInputStream(stream);

                    ZipEntry entry = zipStream.getNextEntry();
                    while (entry != null) {

                        if (entry.isDirectory()) {
//                    this.addFile(entry,entry.getName(),null,contents,null,null);
                        } else {
                            String creationDate = (entry.getCreationTime() != null) ? entry.getCreationTime().toString() : null;
                            String modificationDate = (entry.getLastModifiedTime() != null) ? entry.getLastModifiedTime().toString() : creationDate;
                            Map<String,String> headers = new HashMap<String,String>();
                            headers.put("Parent-UUID",context.getObjectUUID());
                            if (modificationDate != null) headers.put("Last-Modified",modificationDate);
                            String itemUUID = context.putObject(zipStream,entry.getName(),headers);
                            archiveMetadata.addItem(entry.getName(),itemUUID);
                        }
                        zipStream.closeEntry();

                        entry = zipStream.getNextEntry();
                    }

                    updates.add(archiveMetadata.build());

                    stream.close();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return updates;

    }

    protected boolean isZipFile(String mimetype) {

        if (mimetype == null) return false;

        switch (mimetype) {

            case "application/zip":
                return true;

            default:
                return false;
        }

    }

}
