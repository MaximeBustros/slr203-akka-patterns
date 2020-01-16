package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.ArrayList;
import java.util.HashMap; 

public class Multicaster extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private HashMap<String, ArrayList<ActorRef>> groups;

	public Multicaster() {
		groups = new HashMap<String, ArrayList<ActorRef>>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Multicaster.class, () -> {
			return new Multicaster();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MulticastGroup) {
			MulticastGroup multicastGroup = (MulticastGroup) message;
			createMulticastGroup(multicastGroup);
		} else if (message instanceof MulticastMessage) {
			MulticastMessage multicastMessage = (MulticastMessage) message;
			sendMulticastMessage(multicastMessage);
		}
	}

	private void createMulticastGroup(MulticastGroup multicastGroup) {
		if (!checkIfMulticastGroupExists(multicastGroup.name)) {
			groups.put(multicastGroup.name, multicastGroup.actors);
			log.info("Create multicast group [" + multicastGroup.name + "]");
		} else {
			log.info("Multicast group ["+ multicastGroup.name +"] exists already");
		}
	}

	private void sendMulticastMessage(MulticastMessage multicastMessage) {
		if (checkIfMulticastGroupExists(multicastMessage.groupName)) {
			sendMessage(multicastMessage);
		} else {
			log.info("This multicast group doesn't exist");
		}
	}

	private boolean checkIfMulticastGroupExists(String multicastGroupName) {
		return groups.containsKey(multicastGroupName);
	}

	private void sendMessage(MulticastMessage multicastMessage) {
		// get actors of said group
		ArrayList<ActorRef> actors = groups.get(multicastMessage.groupName);
		for (ActorRef actor : actors) {
			actor.tell(multicastMessage.message, getSelf());
		}
	}
}
