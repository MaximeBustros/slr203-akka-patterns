package demo;

import java.util.ArrayList;
import akka.actor.ActorRef;

class ActorReferences {
    public ArrayList<ActorRef> actors;

    public ActorReferences(ArrayList<ActorRef> actors) {
        this.actors = actors;
    }
}