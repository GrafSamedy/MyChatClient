package pckg;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class ClientServerCommunicator {
    private static final String loginUrl = "/login";
    private static final String getMessageUrl = "/get";
    private static final String sendMessageUrl = "/send";
    private static final String userListUrl = "/userlist";
    private static final String addRoomUrl = "/addroom";
    private static final String exitUrl = "/exit";

    public static int loginRequest(String baseUrl, User user) throws IOException {
        return generalPostRequest(baseUrl + loginUrl, user, true);
    }

    public static int exitRequest(String baseUrl, User user) throws IOException {
        return generalPostRequest(baseUrl + exitUrl, user, false);
    }
    public static List<Message> getMessageRequest(String baseUrl) throws IOException {
        URL obj = new URL(baseUrl + getMessageUrl);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        setSessionCookie(conn);
        int respCode = conn.getResponseCode();
        if (conn.getHeaderField("Set-Cookie") != null) {
            Session.getInstance().setId(conn.getHeaderField("Set-Cookie").split(";")[0]);
        }
        if (respCode != 200) {
            return null;
        }
//        System.out.println("Messages request: " + conn.getContentLength() + " | " + conn.getInputStream().available());
        MessageList messageList = ClientServerXMLParser.jaxbXMLReader(conn.getInputStream(), MessageList.class);
        if (messageList != null) {
            return messageList.getMessageList();
        } else {
            throw new IOException("Error while parsing message response");
        }
    }

    public static int sendMessageRequest(String baseUrl, Message message) throws IOException {
        return generalPostRequest(baseUrl + sendMessageUrl, message, false);
    }

    private static <T> int generalPostRequest(String urlStr, T obj, boolean updateResponseExpected) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        setSessionCookie(conn);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(ClientServerXMLParser.jaxbXMLWriter(obj).getBytes());
        if (conn.getHeaderField("Set-Cookie") != null) {
            Session.getInstance().setId(conn.getHeaderField("Set-Cookie").split(";")[0]);
        }
        if (conn.getResponseCode() != 200) {
            return conn.getResponseCode();
        }
        if (updateResponseExpected) {
//            System.out.println("Login update: " + conn.getContentLength() + " | " + conn.getInputStream().available());
            T updatedObj = ClientServerXMLParser.jaxbXMLReader(conn.getInputStream(), (Class<T>) obj.getClass());
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    field.set(obj, field.get(updatedObj));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return conn.getResponseCode();
    }

    public static List<User> getUserList(String baseUrl) throws IOException {
        URL obj = new URL(baseUrl + userListUrl);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        setSessionCookie(conn);
        int respCode = conn.getResponseCode();
        if (conn.getHeaderField("Set-Cookie") != null) {
            Session.getInstance().setId(conn.getHeaderField("Set-Cookie").split(";")[0]);
        }
        if (respCode != 200) {
            return null;
        }
//        System.out.println("User List: " + conn.getContentLength() + " | " + conn.getInputStream().available());
        UserList userList = ClientServerXMLParser.jaxbXMLReader(conn.getInputStream(), UserList.class);
        if (userList != null) {
            return userList.getUserList();
        } else {
            throw new IOException("Error while parsing message response");
        }
    }

    public static int addRoomRequest(String baseUrl, ChatRoom room) throws IOException {
        return generalPostRequest(baseUrl + addRoomUrl, room, false);
    }

    private static void setSessionCookie(HttpURLConnection conn) {
        if (!"".equals(Session.getInstance().getId())) {
            conn.setRequestProperty("cookie", Session.getInstance().getId());
        }
    }
}
