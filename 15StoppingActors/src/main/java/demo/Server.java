package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Server extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Server() {
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Server.class, () -> {
			return new Server();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Request) {
			// assuming processing request takes 200ms+
			try { Thread.sleep(500); } catch (Exception e) { e.printStackTrace(); }
			// parse request
			Request req = (Request) message;
			log.info("[" + getSelf().path().name() + "] processed request [" + req.text + "]");
		}
	}
}
