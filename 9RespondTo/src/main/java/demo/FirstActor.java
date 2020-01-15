package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class FirstActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Actor reference
	private ActorRef actorRefB;
	private ActorRef actorRefC;

	// Constructor with two Actor References
	public FirstActor(ActorRef actorRefB, ActorRef actorRefC) {
		this.actorRefB = actorRefB;
		this.actorRefC = actorRefC;
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRefB, ActorRef actorRefC) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRefB, actorRefC);
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Start ) {
			try {
				sendRequest();
			} catch (Exception e) {e.printStackTrace();}
		}
	}

	public void sendRequest() {
		// Send request to actorRef
		Request req = new Request("Hello World!", actorRefC);
		actorRefB.tell(req, getSelf());
		log.info("["+getSelf().path().name()+"] sent request ["+req.text+"] to ["+ actorRefB.path().name() + "]");
	}
}
