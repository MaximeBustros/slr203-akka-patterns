package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SecondActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	// Actor reference
	private ActorRef actorRef;

	public SecondActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Request) {
			try {
				Thread.sleep(50);
				Request req = (Request) message;
				sendResponse(req);
			} catch (Exception e) {e.printStackTrace();}
		}
	}

	public void sendResponse(Request req) {
		// Send request to broadcaster
		Response resp = new Response(req.text);
		actorRef = req.respondTo;
		actorRef.tell(resp, getSelf());
		log.info("["+getSelf().path().name()+"] sent response ["+resp.text+"] to ["+ actorRef.path().name() + "]");
	}
}
