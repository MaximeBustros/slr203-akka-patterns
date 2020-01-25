package demo;

public class Message {
    public String text;

    public Message(String text) {
        this.text = text;
    }

    public Message(SendMessage sm) {
        this.text = sm.text;
    }
}