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

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SecondActor.class, () -> {
			return new SecondActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MergedRequest) {
			MergedRequest mergedRequest = (MergedRequest) message;
			log.info("["+ getSelf().path().name() + "]" + " received merged request from ["+ getSender().path().name()+ "]");
			for (Request request : mergedRequest.requests) {
				log.info(request.text);
			}
		}
	}
}
