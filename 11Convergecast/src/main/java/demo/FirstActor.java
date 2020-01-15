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
	private ActorRef merger;

	public FirstActor() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Start) {
			try {
				sendRequest();
			} catch (Exception e) {e.printStackTrace();}
		} else if (message instanceof StartJoinRequest) {
			StartJoinRequest startJoinRequest = (StartJoinRequest) message;
			merger = startJoinRequest.actorRef;
			join();
		} else if (message instanceof StartUnjoinRequest) {
			unjoin();
		}
	}

	public void sendRequest() throws NullPointerException {
		// Send request to merger
		Request req = new Request("Hello World!");
		log.info("["+getSelf().path().name()+"] sent request ["+req.text+"] to ["+ merger.path().name() + "]");
		merger.tell(req, getSelf());
	}

	public void join() {
		JoinRequest joinRequest = new JoinRequest();
		this.merger.tell(joinRequest, getSelf());
		log.info("["+getSelf().path().name()+"] joined merger ["+this.merger.path().name()+"]");
	}

	private void unjoin() {
		sendUnjoinRequest();
		merger = null;
	}

	private void sendUnjoinRequest() {
		UnjoinRequest unjoinRequest = new UnjoinRequest();
		merger.tell(unjoinRequest, getSelf());
	}
}
