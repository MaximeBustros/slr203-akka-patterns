package demo;

import java.util.Optional;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorIdentity;
import akka.actor.Identify;
import akka.actor.ActorSelection;
import java.util.NoSuchElementException;

public class SearchService extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private final int identifyId = 0;

	public SearchService() {}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SearchService.class, () -> {
			return new SearchService();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Search) {
			Search search = (Search) message;
			ActorSelection selection = getContext().actorSelection(search.location);
			selection.tell(new Identify(this.identifyId), getSelf());
		} else if (message instanceof ActorIdentity) {
			ActorIdentity ai = (ActorIdentity) message;
			Optional<ActorRef> opt = ai.getActorRef();
			try {
				ActorRef actor = opt.get();
				log.info("Actor path [" + actor.path().toString() + "]");
				log.info("Actor name [" + actor.path().name() + "]");
			} catch (NoSuchElementException e) {
				log.info("No actors at path");
			}
		}
	}
}
