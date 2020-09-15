package org.fca.processors;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.fca.forms.ImageForms;
import org.fca.microformats.MicroFormat;
import org.fca.microformats.content.TextContent;
import org.fca.microformats.object.IdentifiedObject;
import org.fca.processor.Context;
import org.fca.processor.Processor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class OCRImageContent implements Processor {

    public OCRImageContent() { }

    @Override
    public String getInputMicroFormatType() {
        return IdentifiedObject.getQualifiedMicroFormatType(ImageForms.FORM_IMAGE);
    }

    @Override
    public List<String> getOutputMicroFormatTypes() {
        List<String> outputMicroFormatTypes = new ArrayList<>();
        outputMicroFormatTypes.add(TextContent.MICROFORMAT_TYPE);
        return outputMicroFormatTypes;
    }

    @Override
    public List<MicroFormat> process(Context context, MicroFormat microFormat) {

        IdentifiedObject identifiedObject =
                (microFormat instanceof IdentifiedObject) ? (IdentifiedObject)microFormat : null;
        Objects.requireNonNull(identifiedObject);

        List<MicroFormat> updates = new ArrayList<>();

        if (identifiedObject.getObjectForm().equals(ImageForms.FORM_IMAGE)) {

            try {

                InputStream stream = context.getObject(identifiedObject.getObjectName());

                if (stream != null) {

                    BufferedImage image = ImageIO.read(stream);

                    String text;
                    synchronized (this) {

                        ITesseract tesseract = new Tesseract();
                        text = tesseract.doOCR(image);

                    }

                    TextContent textContent = TextContent.builder()
                            .setMicroFormatUUID(UUID.randomUUID().toString())
                            .setValue(text)
                            .build();
                    updates.add(textContent);

                    stream.close();

                }

            } catch (IOException | TesseractException e) {

                e.printStackTrace();

            }

        }

        return updates;

    }

}
