package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorSystem;
import java.util.*; 

public class Server extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Server() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Server.class, () -> {
			return new Server();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Request) {
			Request req = (Request) message;
			log.info("[" + getSelf().path().name() + "] received request [" + req.text + "] from [" + getSender().path().name() + "]");
			try {
				Thread.sleep(500);
			} catch (Exception e) { e.printStackTrace(); }
			EndTask endTask = new EndTask(req);
			getSender().tell(endTask, getSelf());
		}
	}
}
