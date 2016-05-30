package pckg;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConsoleChatClientMessageReceiver extends Thread {
    private String baseUrl;

    public ConsoleChatClientMessageReceiver(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                sleep(5000); //For debug purposes mostly
                List<Message> messageList = ClientServerCommunicator.getMessageRequest(baseUrl);
                if (messageList == null) {
//                    interrupt();
                    continue;
                }
                for (Message message : messageList) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(message.getTime().getTime()) + "] ");
                    sb.append(message.getFrom() + ": ");
                    if (message.getTo() != null && !"".equals(message.getTo())) { //Private message
                        sb.append("(private) ");
                    }
                    sb.append(message.getText());
                    System.out.println(sb);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
