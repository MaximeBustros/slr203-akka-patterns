package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Publisher extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef actorRef = null;

	public Publisher(ActorRef actorRef) {
		this.actorRef = actorRef;
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(Publisher.class, () -> {
			return new Publisher(actorRef);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof SendMessage) {
			// cast and create message to send
			SendMessage sendMessage = (SendMessage) message;
			Message m = new Message(sendMessage);

			// Send message to Topic
			this.actorRef.tell(m, getSelf());
		}
	}
}
