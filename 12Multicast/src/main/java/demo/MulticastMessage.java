package demo;

public class MulticastMessage {
    public String groupName;
    public Message message;

    public MulticastMessage(String groupName, Message message) {
        this.groupName = groupName;
        this.message = message;
    }

    public MulticastMessage(SendMulticastMessage sendMulticastMessage) {
        this.groupName = sendMulticastMessage.groupName;
        this.message = sendMulticastMessage.message;
    }
}