package demo;
import akka.actor.ActorRef;
import java.util.ArrayList;

public class MulticastGroup {
    String name;
    ArrayList<ActorRef> actors;

    public MulticastGroup(String name, ArrayList<ActorRef> actors) {
        this.name = name;
        this.actors = actors; 
    }

    public MulticastGroup(CreateMulticastGroup createMulticastGroup) {
        this.name = createMulticastGroup.name;
        this.actors = createMulticastGroup.actors;
    }
}