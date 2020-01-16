package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Sender extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private ActorRef multicaster;

	public Sender(ActorRef multicaster) {
		this.multicaster = multicaster;
	}

	// Static function creating actor
	public static Props createActor(ActorRef multicaster) {
		return Props.create(Sender.class, () -> {
			return new Sender(multicaster);
		});
	}


	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof CreateMulticastGroup) {
			// Cast message
			CreateMulticastGroup createMulticastGroup = (CreateMulticastGroup) message;
			createMulticastGroup(createMulticastGroup);
		} else if (message instanceof SendMulticastMessage) {
			// Cast message
			SendMulticastMessage sendMulticastMessage = (SendMulticastMessage) message;
			sendGroupMessage(sendMulticastMessage);
		}
	}

	private void createMulticastGroup(CreateMulticastGroup createMulticastGroup) {
		MulticastGroup multicastGroup = new MulticastGroup(createMulticastGroup);
		multicaster.tell(multicastGroup, getSelf());
	}

	public void sendGroupMessage(SendMulticastMessage sendMulticastMessage) {
		MulticastMessage multicastMessage = new MulticastMessage(sendMulticastMessage);
		multicaster.tell(multicastMessage, getSelf());
	}
}
