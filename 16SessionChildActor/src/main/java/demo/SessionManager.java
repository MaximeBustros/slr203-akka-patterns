package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorSystem;
import java.util.*; 

public class SessionManager extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private final HashMap<ActorRef, ActorRef> map;
	private int count = 1;

	// Instantiate an actor system
	private ActorSystem system;

	public SessionManager() {
		map = new HashMap<ActorRef, ActorRef>();
		system = getContext().getSystem();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SessionManager.class, () -> {
			return new SessionManager();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Request) {
			// assuming processing request takes 200ms+
			try { Thread.sleep(500); } catch (Exception e) { e.printStackTrace(); }
			// parse request
			Request req = (Request) message;
			log.info("[" + getSelf().path().name() + "] processed request [" + req.text + "]");
		} else if (message instanceof CreateSession) {
			ActorRef session = system.actorOf(Session.createActor(), "session"+count);
			map.put(getSender(), session);
			getSender().tell(session, getSelf());
			log.info("Session was created for client [" + getSender().path().name() + "]");
		} else if (message instanceof EndSession) {
			// stop session associated with sender using kill
			ActorRef sessionStop = map.get(getSender());
			log.info("Sent stop to [" + sessionStop.path().name() + "]");
			system.stop(sessionStop);

			// remove from map
			map.remove(getSender());
			// } catch (Exception e) { e.printStackTrace(); } 
		}
	}
}
