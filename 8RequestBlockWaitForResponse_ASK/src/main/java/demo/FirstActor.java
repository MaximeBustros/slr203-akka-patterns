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
	private ActorRef actorRef;
	private boolean isFree;
	private int count;

	public FirstActor() {}

	public FirstActor(ActorRef actorRef) {
		this.actorRef = actorRef;
		this.count = 1;
		isFree = true;
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRef);
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(isFree && count <= 2) {
			if(message instanceof Start ) {
				try {
					sendRequest();
				} catch (Exception e) {e.printStackTrace();}
			} else if(message instanceof Response) {
				// Log response
				try {
					Response resp = (Response) message;
					log.info("["+getSelf().path().name()+"] received response ["+resp.text+"] from ["+ getSender().path().name() +"]");
				} catch (Exception e) {e.printStackTrace();}
				try {
					sendRequest();
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}

	public void sendRequest() {
		// Send request to actorRef
		Request req = new Request("Message " + count++);
		actorRef.tell(req, getSelf());
		log.info("["+getSelf().path().name()+"] sent request ["+req.text+"] to ["+ actorRef.path().name() + "]");
	
	}


	/**
	 * alternative for AbstractActor
	 * @Override
	public Receive createReceive() {
		return receiveBuilder()
				// When receiving a new message containing a reference to an actor,
				// Actor updates his reference (attribute).
				.match(ActorRef.class, ref -> {
					this.actorRef = ref;
					log.info("Actor reference updated ! New reference is: {}", this.actorRef);
				})
				.build();
	}
	 */
}
