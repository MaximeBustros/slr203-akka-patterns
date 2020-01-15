package demo;
import akka.actor.ActorRef;

class Request {
    public String text;;

    public Request(String text) {
        this.text = text;
    }
}