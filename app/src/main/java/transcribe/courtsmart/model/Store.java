package transcribe.courtsmart.model;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
public class Store {

    @JacksonXmlProperty(localName = "session", namespace = "cs")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Session> sessions;

    @JacksonXmlProperty(localName = "title", namespace = "cs")
    private String title;

    @JacksonXmlProperty(localName = "buttonlabels", namespace = "cs")
    private ButtonLabels buttonLabels;

}