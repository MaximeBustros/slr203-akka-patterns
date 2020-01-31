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
	private ArrayList<Integer> seqNumbersSeen;

	public Client() { 
		this.actorReferences = new ArrayList<ActorRef>();
		this.seqNumbersSeen = new ArrayList<Integer>();
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
		} else if (message instanceof FloodingMessage) {
			FloodingMessage fm = (FloodingMessage) message;
			
			if (!this.seqNumbersSeen.contains(fm.seqNo)) {
				seqNumbersSeen.add(fm.seqNo);
				log.info("[" + getSelf().path().name() + "] received sequence no [" + fm.seqNo + "] from [" + getSender().path().name() + "]");
				for (ActorRef actor : this.actorReferences) {
					actor.tell(fm, getSelf());
				}
			}
		}
	}
}
