package pckg;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class ConsoleChatClient {
    private static final String baseUrl = "http://localhost:8280";

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter name:");
            String name = sc.nextLine();
            System.out.println("Enter password:");
            String pass = sc.nextLine();
            User user = new User(name, pass);
            int respCode = ClientServerCommunicator.loginRequest(baseUrl, user);
            if (respCode != 200) {
                System.out.println("HTTP Error: " + respCode);
                return;
            }

            startMessageReceiver(baseUrl);
            System.out.println("You have joined the chat. Print \"help\" for the list of commands.");
            while (true) {
                String strIn = sc.nextLine();
                Message message = performCommand(user, strIn);
                if (message != null) {
                    ClientServerCommunicator.sendMessageRequest(baseUrl, message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startMessageReceiver(String baseUrl) {
        ConsoleChatClientMessageReceiver messageReceiver = new ConsoleChatClientMessageReceiver(baseUrl);
        messageReceiver.setDaemon(true);
        messageReceiver.start();
    }

    private static Message performCommand(User user, String strIn) {
        String firstCommand = strIn.split("\\W+")[0];
        switch (firstCommand) {
            case "exit":
                try {
                    ClientServerCommunicator.exitRequest(baseUrl, user);
                } catch (IOException e){
                    e.printStackTrace();
                }
                System.exit(0);
            case "help":
                showHelp();
                break;
            case "to":
                String[] res = strIn.split("\\W+", 3);
                Message message = new Message(user, res[2]);
                message.setTo(res[1]);
                return message;
            case "userinfo":
                showUserInfo(user);
                break;
            case "userlist":
                try {
                    List<User> roomUsers = ClientServerCommunicator.getUserList(baseUrl);
                    if (roomUsers.size() > 0) {
                        System.out.println("Users in room " + user.getCurrentChatRoom() + ":");
                    }
                    for (User roomUser : roomUsers) {
                        System.out.println(roomUser.getName() + " - " + roomUser.getCurrentStatus());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "addroom":
                try {
                    ClientServerCommunicator.addRoomRequest(baseUrl, new ChatRoom(strIn.split("\\W+")[1]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "move":
            case "status":
                if ("move".equals(firstCommand)) {
                    user.setCurrentChatRoom(strIn.split("\\W+")[1]);
                } else{
                    user.setCurrentStatus(strIn.split("\\W+")[1]);
                }
                try {
                    int respCode = ClientServerCommunicator.loginRequest(baseUrl, user);
                    if (respCode != 200) {
                        System.out.println("HTTP Error: " + respCode);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return new Message(user, strIn);
        }
        return null;
    }

    private static void showHelp() {
        System.out.println("help - shows this help.");
        System.out.println("to:username \"Message\" - sends private \"Message\" to username.");
        System.out.println("userinfo - prints information about current user.");
        System.out.println("userlist - prints list of users in current chat room.");
        System.out.println("addroom:roomname - adds the chatroom with specified name");
        System.out.println("move:roomname - move to room with specified name");
        System.out.println("move:roomname - move to room with specified name");
        System.out.println("status:statusname - change status to specified status.");
        System.out.println("exit - exits chat client.");
    }

    private static void showUserInfo(User user) {
        System.out.println("User info:");
        System.out.println("Name: " + user.getName());
        System.out.println("Status: " + user.getCurrentStatus());
        System.out.println("Room: " + user.getCurrentChatRoom());
    }
}
