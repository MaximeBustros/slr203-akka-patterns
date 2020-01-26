package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Client extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef loadBalancer;
	private int count;

	public Client(ActorRef loadBalancer) {
		this.loadBalancer = loadBalancer;
		this.count = 1;
	}

	// Static function creating actor
	public static Props createActor(ActorRef loadBalancer) {
		return Props.create(Client.class, () -> {
			return new Client(loadBalancer);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof SendRequest) {
			Request request = new Request("m" + this.count);
			this.count++;
			loadBalancer.tell(request, getSelf());
		}
	}
}
