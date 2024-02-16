package transcribe.courtsmart.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import lombok.Data;

@Data
public class Tag {
    @JacksonXmlProperty(isAttribute = true, localName = "indextime")
    private long indexTime;

    @JacksonXmlProperty(isAttribute = true, localName = "entertime")
    private long enterTime;

    @JacksonXmlProperty(isAttribute = true, localName = "private")
    private int privateFlag;

    @JacksonXmlProperty(isAttribute = true)
    private String username;

    @JacksonXmlText
    private String content;
}
