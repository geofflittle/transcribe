package transcribe.courtsmart.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
public class CSFile {
    @JacksonXmlProperty(isAttribute = true, localName = "fileid")
    private String fileId;

    @JacksonXmlProperty(isAttribute = true, localName = "starttime")
    private long startTime;

    @JacksonXmlProperty(isAttribute = true, localName = "stoptime")
    private long stopTime;

    @JacksonXmlProperty(isAttribute = true)
    private int sequence;

    @JacksonXmlProperty(isAttribute = true)
    private int type;

    @JacksonXmlProperty(isAttribute = true)
    private String filename;

    @JacksonXmlProperty(isAttribute = true)
    private int stream;

    @JacksonXmlProperty(isAttribute = true)
    private String hash;

    @JacksonXmlProperty(isAttribute = true)
    private String path;
}
