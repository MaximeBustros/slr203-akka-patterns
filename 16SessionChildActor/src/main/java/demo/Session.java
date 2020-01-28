package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Session extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Session() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Session.class, () -> {
			return new Session();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Request) {
			// assuming processing request takes 1000ms+
			try { Thread.sleep(2000); } catch (Exception e) { e.printStackTrace(); }
			// parse request
			Request req = (Request) message;
			getSender().tell(req, getSelf());
			log.info("[" + getSelf().path().name() + "] processed request [" + req.text + "]");
		}
	}
}
