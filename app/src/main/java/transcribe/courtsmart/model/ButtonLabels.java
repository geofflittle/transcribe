package transcribe.courtsmart.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ButtonLabels {
    @JacksonXmlProperty(isAttribute = true, localName = "roomlabel")
    private String roomLabel;

    @JacksonXmlProperty(isAttribute = true, localName = "caselabel")
    private String caseLabel;

    @JacksonXmlProperty(isAttribute = true, localName = "typelabel")
    private String typeLabel;

    @JacksonXmlProperty(isAttribute = true, localName = "captionlabel")
    private String captionLabel;

    @JacksonXmlProperty(isAttribute = true, localName = "judgelabel")
    private String judgeLabel;
}
