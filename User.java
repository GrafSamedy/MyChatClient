package pckg;

import javax.net.ssl.SSLEngineResult;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.GregorianCalendar;

@XmlRootElement(name = "user")
public class User {
    @XmlElement
    private String name;
    @XmlElement
    private String pass;
    private String currentChatRoom;
    private String currentStatus;
    @XmlElement
    private GregorianCalendar lastRequestTime;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        currentChatRoom = "";
        currentStatus = "";
        lastRequestTime = new GregorianCalendar();
        lastRequestTime.add(GregorianCalendar.MINUTE, -10);
    }

    public User() {
        this("", "");
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public String getCurrentChatRoom() {
        return currentChatRoom;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    @XmlElement
    public void setCurrentChatRoom(String currentChatRoom) {
        this.currentChatRoom = currentChatRoom;
    }

    @XmlElement
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
