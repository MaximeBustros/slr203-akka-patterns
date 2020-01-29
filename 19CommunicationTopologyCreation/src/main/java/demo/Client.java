package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorSystem;
import java.util.ArrayList; 

public class Client extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ArrayList<ActorRef> actorReferences;

	public Client() { 
		actorReferences = new ArrayList<ActorRef>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Client.class, () -> {
			return new Client();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof ActorReferences) {
			ActorReferences a = (ActorReferences) message;
			this.actorReferences  = a.actors;
			for (ActorRef actor : this.actorReferences) {
				log.info("[" + getSelf().path().name() + "] has a reference towards [" +actor.path().name() + "]");
			}
		}
	}
}
