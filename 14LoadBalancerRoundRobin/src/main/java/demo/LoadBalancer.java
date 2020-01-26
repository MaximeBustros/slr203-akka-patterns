package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

public class LoadBalancer extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ArrayList<ActorRef> subscribers;
	private int count;

	public LoadBalancer() {
		subscribers = new ArrayList<ActorRef>();
		count = 0;
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Join) {
			// Cast Message
			subscribers.add(getSender());
			log.info("[" + getSender().path().name() + "] joined the load balancer");
		} else if (message instanceof Request) {
			Request request = (Request) message;
			// log.info("count = " + this.count);
			// log.info("subscribers.size = " + subscribers.size());
			if (this.count > subscribers.size() - 1) {
				this.count = 0;
			}
			subscribers.get(this.count).tell(request, getSelf());
			this.count++;
		} else if (message instanceof Unjoin) {
			log.info("[" + getSender().path().name() + "] unjoined the load balancer");
			subscribers.remove(getSender());
		}
	}
}
