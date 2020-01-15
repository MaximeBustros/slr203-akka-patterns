package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
import java.util.HashMap; 

public class Merger extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	private ArrayList<ActorRef> subscribers;
	private ActorRef actorRef;
	private HashMap<String, Integer> map;
	private MergedRequest mergedRequest;

	public Merger(ActorRef actorRef) {
		this.actorRef = actorRef;
		this.subscribers = new ArrayList<ActorRef>();
		this.map = new HashMap<>(); 
		this.mergedRequest = new MergedRequest();
	}

	// Static function creating actor
	public static Props createActor(ActorRef actorRef) {
		return Props.create(Merger.class, () -> {
			return new Merger(actorRef);
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Request) {
			Request req = (Request) message;
			if (map.containsKey(req)) {
				// do nothing
			} else {
				// if does not contain add to hashmap
				map.put(getSender().path().name(), 1);
				mergedRequest.add(req);

				if(checkIfAllSubscribers()) {
					sendRequest();
					clear();
				}
			}
		} else if (message instanceof JoinRequest) {
			this.subscribers.add(getSender());
        } else if (message instanceof UnjoinRequest) {
			subscribers.remove(getSender());
			log.info("[" + getSender().path().name() + "] unjoined from [" + getSelf().path().name() + "]");
		}
	}

	private boolean checkIfAllSubscribers() {
		for (ActorRef subscriber : subscribers) {
			if (!map.containsKey(subscriber.path().name())) {
				return false;
			}
		}
		return true;
	}

	private void clear() {
		mergedRequest.clear();
		map.clear();
	}

	private void sendRequest() {
        // Send request to Merger
		actorRef.tell(mergedRequest, getSelf());
		log.info("["+getSelf().path().name()+"] sent merged request to ["+ actorRef.path().name() + "]");
	}
}
