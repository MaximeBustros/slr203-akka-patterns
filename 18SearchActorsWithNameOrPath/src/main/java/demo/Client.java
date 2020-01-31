package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorSystem;

public class Client extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorSystem system;
	private int count = 1;

	public Client() { 
		system = getContext().getSystem();	
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Client.class, () -> {
			return new Client();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof CreateSession) {
			ActorRef actor = system.actorOf(Client.createActor(), "actor"+count);
			log.info("created [" + actor + "]");
			count++;
		}
	}
}
