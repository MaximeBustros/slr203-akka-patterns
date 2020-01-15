package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

public class Merger extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private ArrayList<ActorRef> subscribers;

	public Merger() {
		this.subscribers = new ArrayList<ActorRef>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Merger.class, () -> {
			return new Merger();
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
        // Send request to Merger
		subscriber.tell(req, getSelf());
		log.info("["+getSelf().path().name()+"] sent request ["+req.text+"] to ["+ subscriber.path().name() + "]");
	}


	/**
	 * alternative for AbstractActor
	 * @Override
	public Receive createReceive() {
		return receiveBuilder()
				// When receiving a new message containing a reference to an actor,
				// Actor updates his reference (attribute).
				.match(ActorRef.class, ref -> {
					this.actorRef = ref;
					log.info("Actor reference updated ! New reference is: {}", this.actorRef);
				})
				.build();
	}
	 */
}
