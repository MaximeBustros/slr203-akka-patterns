package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor {

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Empty Constructor
	public SecondActor() {}

	// Static function that creates actor
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof String) {
			log.info("["+getSelf().path().name()+"] received message ["+message+"] from ["+ getSender().path().name() +"]");
		}
	}
	
	
}
