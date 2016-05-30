package pckg;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "chatroom")
public class ChatRoom {
    @XmlElement
    private String name;

    private ChatRoom() {

    }

    public ChatRoom(String name) {
        this.name = name;
    }
}
