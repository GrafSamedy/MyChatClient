package pckg;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.GregorianCalendar;


@XmlRootElement(name = "message")
public class Message {
    @XmlElement
    private int id;
    @XmlElement
    private String text;
    @XmlElement
    private GregorianCalendar time;
    @XmlElement
    private String from;
    private String to;
    @XmlElement
    private String room;

    private Message() { //Is private enough for JAXB?
    }

    public Message(User user, String text) {
        this.id = -1;
        this.text = text;
        this.time = new GregorianCalendar();
        this.from = user.getName();
        this.to = "";
        this.room = user.getCurrentChatRoom();
    }

    public String getText() {
        return text;
    }

    public GregorianCalendar getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @XmlElement
    public void setTo(String to) {
        this.to = to;
    }
}
