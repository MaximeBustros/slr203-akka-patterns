package demo;
import akka.actor.ActorRef;
import java.util.ArrayList;

public class CreateMulticastGroup {
    String name;
    ArrayList<ActorRef> actors;

    public CreateMulticastGroup(String name, ArrayList<ActorRef> actors) {
        this.name = name;
        this.actors = actors; 
    }
}