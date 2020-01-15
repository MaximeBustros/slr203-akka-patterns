package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public SecondActor() {}

	public void join(ActorRef broadcaster) {
		JoinRequest joinRequest = new JoinRequest();
		broadcaster.tell(joinRequest, getSelf());
		log.info("["+getSelf().path().name()+"] joined broadcaster");
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof StartJoinRequest ) {
			try {
				// get broadcaster reference from system
				StartJoinRequest startJoinRequest = (StartJoinRequest) message;
				ActorRef broadcaster = startJoinRequest.actorRef;

				// create join request
				JoinRequest joinRequest = new JoinRequest();

				// send join request and log
				broadcaster.tell(joinRequest, getSelf());
				log.info("[" + getSelf().path().name() + "] joined [" + broadcaster.path().name() + "]");
			} catch(Exception e) { e.printStackTrace(); }
		}
	}
}
