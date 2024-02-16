package transcribe.courtsmart.model;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
public class Session {
    @JacksonXmlProperty(isAttribute = true)
    private String version;

    @JacksonXmlProperty(isAttribute = true, localName = "sessid")
    private String sessionId;

    @JacksonXmlProperty(isAttribute = true)
    private String server;

    @JacksonXmlProperty(isAttribute = true)
    private String room;

    @JacksonXmlProperty(isAttribute = true, localName = "case")
    private String caseInfo;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String caption;

    @JacksonXmlProperty(isAttribute = true)
    private String judge;

    @JacksonXmlProperty(isAttribute = true)
    private int channels;

    @JacksonXmlProperty(isAttribute = true, localName = "samplerate")
    private int sampleRate;

    @JacksonXmlProperty(isAttribute = true)
    private int recording;

    @JacksonXmlProperty(isAttribute = true, localName = "startuser")
    private String startUser;

    @JacksonXmlProperty(isAttribute = true)
    private int attached;

    @JacksonXmlProperty(isAttribute = true)
    private int flagged;

    @JacksonXmlProperty(isAttribute = true)
    private int sealed;

    @JacksonXmlProperty(isAttribute = true)
    private int video;

    @JacksonXmlProperty(isAttribute = true, localName = "archdate")
    private String archDate;

    @JacksonXmlProperty(isAttribute = true, localName = "doctype")
    private int docType;

    @JacksonXmlProperty(isAttribute = true, localName = "starttime")
    private long startTime;

    @JacksonXmlProperty(isAttribute = true, localName = "stoptime")
    private long stopTime;

    @JacksonXmlProperty(isAttribute = true, localName = "starterid")
    private int starterId;

    @JacksonXmlProperty(isAttribute = true, localName = "bytespersec")
    private int bytesPerSec;

    @JacksonXmlProperty(isAttribute = true, localName = "audiotype")
    private int audioType;

    @JacksonXmlProperty(isAttribute = true, localName = "videotype")
    private int videoType;

    @JacksonXmlProperty(isAttribute = true, localName = "audiocodec")
    private String audioCodec;

    @JacksonXmlProperty(isAttribute = true, localName = "videocodec")
    private String videoCodec;

    @JacksonXmlProperty(isAttribute = true, localName = "selstart")
    private long selStart;

    @JacksonXmlProperty(isAttribute = true, localName = "selend")
    private long selEnd;

    @JacksonXmlProperty(isAttribute = true, localName = "videosync")
    private int videoSync;

    @JacksonXmlProperty(isAttribute = true, localName = "audiogain")
    private String audioGain;

    @JacksonXmlProperty(isAttribute = true, localName = "timeoffset")
    private int timeOffset;

    @JacksonXmlProperty(isAttribute = true, localName = "firstfile")
    private String firstFile;

    @JacksonXmlProperty(localName = "tag", namespace = "cs")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Tag> tags;

    @JacksonXmlProperty(localName = "file", namespace = "cs")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CSFile> files;
}
