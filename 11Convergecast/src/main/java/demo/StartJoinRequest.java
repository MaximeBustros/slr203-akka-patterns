package demo;

import akka.actor.ActorRef;

public class StartJoinRequest {
    ActorRef actorRef;

    public StartJoinRequest(ActorRef actorRef) {
        this.actorRef = actorRef;
    }
}