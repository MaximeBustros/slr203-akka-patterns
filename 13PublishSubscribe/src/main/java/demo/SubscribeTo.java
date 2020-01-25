package demo;

import akka.actor.ActorRef;

public class SubscribeTo {
    public ActorRef actorRef;

    public SubscribeTo(ActorRef actorRef) {
        this.actorRef = actorRef;
    }
}