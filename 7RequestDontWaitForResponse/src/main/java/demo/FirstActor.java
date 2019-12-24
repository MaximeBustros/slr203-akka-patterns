package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

public class FirstActor extends UntypedAbstractActor{

	// Actor reference
	private ActorRef actorRef;
	private ActorRef transmitter;

	public FirstActor() {}

	public FirstActor(ActorRef actorRef) {
		this.transmitter = transmitter;
		this.actorRef = actorRef;
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(actorRef);
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Start){
			MessageWithReference messageWithReference = new MessageWithReference(actorRef, "Hello World!");
			transmitter.tell(messageWithReference, getSelf());
		}
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
