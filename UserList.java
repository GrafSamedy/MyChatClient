package pckg;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "users")
public class UserList {
    @XmlElement(name = "user")
    List<User> users = new ArrayList<>();

    public List<User> getUserList() {
        return users;
    }
}
