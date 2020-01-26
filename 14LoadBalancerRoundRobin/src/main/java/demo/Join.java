package demo;

import akka.actor.ActorRef;

public class Join {
    public ActorRef actorRef;
    
    public Join(ActorRef actorRef) {
        this.actorRef = actorRef;
    }
}