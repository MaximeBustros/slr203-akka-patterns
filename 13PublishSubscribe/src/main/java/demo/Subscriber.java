package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Subscriber extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef topic = null;

	public Subscriber() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Subscriber.class, () -> {
			return new Subscriber();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message) {
			// Cast Message
			Message m = (Message) message;
			log.info("[" + getSelf().path().name() + "] Received message [" + m.text + "]");

		} else if (message instanceof Unsubscribe) {
			// Cast message
			Unsubscribe unsub = (Unsubscribe) message;
			try {
				topic.tell(unsub, getSelf());
			} catch (NullPointerException e) {e.printStackTrace();}
		} else if (message instanceof SubscribeTo) {
			SubscribeTo subTo = (SubscribeTo) message;
			Subscribe sub = new Subscribe();
			this.topic  = subTo.actorRef;
			topic.tell(sub, getSelf());
		}
	}
}
