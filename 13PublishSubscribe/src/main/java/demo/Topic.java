package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;

public class Topic extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ArrayList<ActorRef> subscribers;;

	public Topic() {
		subscribers = new ArrayList<ActorRef>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Topic.class, () -> {
			return new Topic();
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Message) {
            Message m = (Message) message;
			log.info("[" + getSelf().path().name() + "] received message [" + m.text +"] from [" + getSender().path().name() + "]");
			sendMulticastMessage(m);
		} else if(message instanceof Subscribe) {
			log.info("[" + getSender().path().name() + "] subscribed to topic [" + getSelf().path().name() + "]");
			subscribers.add(getSender());
		} else if (message instanceof Unsubscribe) {
			for (ActorRef subscriber : this.subscribers) {
				if (getSender() == subscriber) {
					log.info("[" + subscriber.path().name() + "] unsubscribed from [" + getSelf().path().name() + "]");
					subscribers.remove(subscriber);
					break;
				}
			}
		}
	}

	private void sendMulticastMessage(Message m) {
		for (ActorRef subscriber : subscribers) {
			subscriber.tell(m , getSender());
		}
	}
}
