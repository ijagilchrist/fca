package org.fca.processor;

import org.fca.microformats.MicroFormat;

import java.util.List;

public interface Processor {

    public String getInputMicroFormatType();

    public List<String> getOutputMicroFormatTypes();

    public List<MicroFormat> process(Context context, MicroFormat in);

}
