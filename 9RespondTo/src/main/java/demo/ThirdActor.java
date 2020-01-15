package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ThirdActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public ThirdActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(ThirdActor.class, () -> {
			return new ThirdActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Response) {
			try {
				Thread.sleep(50);
				Response resp = (Response) message;
				sendRequest(resp);
			} catch (Exception e) {e.printStackTrace();}
		}
	}

	public void sendRequest(Response resp) {
		// Send request to broadcaster
		log.info("["+getSelf().path().name()+"] Received response ["+resp.text+"] from ["+ getSender().path().name() + "]");
	}
}
