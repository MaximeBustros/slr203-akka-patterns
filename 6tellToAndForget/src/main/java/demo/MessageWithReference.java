package demo;
import akka.actor.ActorRef;

public class MessageWithReference {
    public final ActorRef actorRef;
    public final String text;

    public MessageWithReference(ActorRef actorRef, String text) {
        this.actorRef = actorRef;
        this.text = text;

    }
  }