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
	private ActorRef broadcaster;

	public FirstActor(ActorRef broadcaster) {
		this.broadcaster = broadcaster;
	}

	// Static function creating actor
	public static Props createActor(ActorRef broadcaster) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(broadcaster);
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Start) {
			try {
				sendRequest();
			} catch (Exception e) {e.printStackTrace();}
		}
	}

	public void sendRequest() {
		// Send request to broadcaster
		Request req = new Request("Hello World!");
		broadcaster.tell(req, getSelf());
		log.info("["+getSelf().path().name()+"] sent request ["+req.text+"] to ["+ broadcaster.path().name() + "]");
	}
}
