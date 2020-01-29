package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorSystem;
import java.util.*; 

public class LoadBalancer extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Instantiate an actor system
	private ActorSystem system;

	private int maxNumberOfServers = 0;
	private int currentNumberOfServers = 0;

	// maps an actorRef to the number of tasks currently on that server
	private HashMap<ActorRef, Integer> map;

	// we need to be able to iterate over servers
	private ArrayList<ActorRef> servers;
	private int serverNext = 0;

	public LoadBalancer() {
		map = new HashMap<ActorRef, Integer>();
		servers = new ArrayList<ActorRef>();
		system = getContext().getSystem();		
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof MaxNoServers) {
			MaxNoServers no = (MaxNoServers) message;
			this.maxNumberOfServers = no.number;
		} else if (message instanceof Request){
			if (currentNumberOfServers < maxNumberOfServers) {
				ActorRef server = system.actorOf(Server.createActor(), "server"+currentNumberOfServers);
				log.info("[" + getSelf().path().name() + "] created [" + server.path().name() + "]");
				servers.add(server);
				map.put(server, 0);
				currentNumberOfServers++;
			}
			// send request to server
			Request req = (Request) message;
			if (serverNext >= maxNumberOfServers) {
				serverNext = 0;
			}
			ActorRef actor = servers.get(serverNext);
			serverNext++;
			actor.tell(req, getSelf());
			log.info("[" + getSelf().path().name() + "] sent request [" + req.text + "] to [" + actor.path().name() + "]");
			int newVal = map.get(actor) + 1;
			map.replace(actor, newVal);
		} else if (message instanceof EndTask){
			ActorRef sender = getSender();
			EndTask endTask = (EndTask) message;
			log.info("received endtask [" + endTask.text + "] from [" + sender.path().name() + "]");
			int newVal = map.get(sender) - 1;
			map.replace(sender, newVal);
			if(newVal == 0) {
				// if a server has no more tasks then remove it from map, servers and stop it
				currentNumberOfServers--;
				map.remove(sender);
				servers.remove(sender);
				log.info("send stop to [" + sender.path().name() + "]");
				system.stop(sender);
				// stopped system
			}
		}
	}
}