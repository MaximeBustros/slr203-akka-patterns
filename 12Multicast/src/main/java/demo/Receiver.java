package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Receiver extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Receiver() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Receiver.class, () -> {
			return new Receiver();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message) {
            // Cast message
            Message m = (Message) message;
			log.info("[" + getSelf().path().name() + "] received message [" + m.text +"] from [" + getSender().path().name() + "]");
        }
	}
}
