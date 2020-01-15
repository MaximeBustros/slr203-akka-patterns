package demo;
import akka.actor.ActorRef;

class Request {
    public String text;
    public ActorRef respondTo;

    public Request(String text, ActorRef respondTo) {
        this.text = text;
        this.respondTo = respondTo;
    }
}