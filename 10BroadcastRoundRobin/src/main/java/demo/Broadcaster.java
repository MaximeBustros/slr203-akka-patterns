package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

public class Broadcaster extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private ArrayList<ActorRef> subscribers;

	public Broadcaster() {
		this.subscribers = new ArrayList<ActorRef>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Broadcaster.class, () -> {
			return new Broadcaster();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Request) {
            Request req = (Request) message;
            for (ActorRef subscriber : this.subscribers) {
                try {
                    sendRequest(req, subscriber);
                } catch (Exception e) {e.printStackTrace();}
            }
		} else if (message instanceof JoinRequest) {
            this.subscribers.add(getSender());
        }
	}

	public void sendRequest(Request req, ActorRef subscriber) {
        // Send request to broadcaster
		subscriber.tell(req, getSelf());
		log.info("["+getSelf().path().name()+"] forwarded request ["+req.text+"] to ["+ subscriber.path().name() + "]");
	}
}
