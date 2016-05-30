package pckg;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "messages")
public class MessageList {
    @XmlElement(name = "message")
    private List<Message> messageList = new ArrayList<>();

    public List<Message> getMessageList() {
        return messageList;
    }
}
