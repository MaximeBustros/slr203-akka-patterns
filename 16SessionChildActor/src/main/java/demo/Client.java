package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Client extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef session;
	private ActorRef sessionManager;
	private int count = 1;

	public Client(ActorRef sessionManager) {
		this.sessionManager = sessionManager; 
	}

	// Static function creating actor
	public static Props createActor(ActorRef sessionManager) {
		return Props.create(Client.class, () -> {
			return new Client(sessionManager);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof SendRequest) {
			Request req = new Request("m" + (count++));
			try {
				session.tell(req, getSelf());
				log.info("Sent request [" + req.text + "] to [" + session.path().name() + "]");
			} catch (NullPointerException e) { e.printStackTrace(); }
		} else if (message instanceof ActorRef) {
			try {
				this.session = (ActorRef) message;
			} catch (Exception e) {e.printStackTrace();}
		} else if (message instanceof Request) {
			Request req = (Request) message;
			log.info("[" + getSelf().path().name() + "] received response ["+ req.text + "] from [" + getSender().path().name() + "]");
		} else if (message instanceof EndSession) {
			try {
				EndSession endSession = (EndSession) message;
				this.sessionManager.tell(endSession, getSelf());
				log.info("Send endsession to SessionManager");
			} catch (Exception e) { e.printStackTrace(); }
		} else if (message instanceof CreateSession) {
			CreateSession createSession = (CreateSession) message;
			this.sessionManager.tell(createSession, getSelf());
		}
	}
}
