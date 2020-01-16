package demo;

public class SendMulticastMessage {
    public String groupName;
    public Message message;

    public SendMulticastMessage(String groupName, Message message) {
        this.groupName = groupName;
        this.message = message;
    }
}