package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

public class Server extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef loadBalancer;

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
		if (message instanceof Join) {
			//cast
			Join join = (Join) message;
			ActorRef loadBalancer = join.actorRef;
			loadBalancer.tell(join, getSelf());
			this.loadBalancer = loadBalancer;
		} else if (message instanceof Request) {
			Request request = (Request) message;
			log.info("[" + getSelf().path().name() + "] received message [" + request.text + "]");
		} else if (message instanceof Unjoin) {
			Unjoin unjoin = (Unjoin) message;
			loadBalancer.tell(unjoin, getSelf());
			loadBalancer = null;
		}
	}
}
