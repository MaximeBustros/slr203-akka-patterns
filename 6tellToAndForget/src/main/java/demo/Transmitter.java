package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Transmitter extends UntypedAbstractActor {

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Empty Constructor
	public Transmitter() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(Transmitter.class, () -> {
			return new Transmitter();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
        if (message instanceof MessageWithReference) {
			MessageWithReference messageWithReference = (MessageWithReference) message;
			messageWithReference.actorRef.tell(messageWithReference.text, getSender());
			log.info("["+getSelf().path().name()+"] received message ["+message+"] from ["+ getSender().path().name() +"] and sending to ["+ messageWithReference.actorRef.path().name()+"]");
        }
	}
	
	
}
