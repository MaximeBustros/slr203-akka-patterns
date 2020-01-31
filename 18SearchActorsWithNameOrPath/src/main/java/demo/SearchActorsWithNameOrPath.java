package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;
import java.time.Duration;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class SearchActorsWithNameOrPath {

	public static void main(String[] args) {

		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef searchService = system.actorOf(SearchService.createActor(), "SearchService");

		final ActorRef a = system.actorOf(Client.createActor(), "a");

		CreateSession createSession = new CreateSession();
		a.tell(createSession, ActorRef.noSender());
		a.tell(createSession, ActorRef.noSender());

		try {
			Thread.sleep(500);
		} catch (Exception e) { e.printStackTrace(); }

		// find actors' references with path. To search by name we can only use relative Path.
		Search searchA = new Search("/user/a");
		Search searchActor1 = new Search("/user/actor1");
		Search searchActor2 = new Search("/user/actor2");

		system.log().info("***Search by Path \nNOTE: we have access to the actorRef in the SearchService [Can use any actor in other examples]");
		searchService.tell(searchA, ActorRef.noSender());
		try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }
		searchService.tell(searchActor1, ActorRef.noSender());
		try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }
		searchService.tell(searchActor2, ActorRef.noSender());


		system.log().info("*** LOG ALL under /USER");
		searchService.tell(new Search("/user/*"), ActorRef.noSender());
		try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }

		system.log().info("*** LOG ALL UNDER /system");
		searchService.tell(new Search("/system/*"), ActorRef.noSender());
		try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }

		system.log().info("*** LOG ALL UNDER /deadLetters");
		searchService.tell(new Search("/deadLetters/*"), ActorRef.noSender());
		try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }

		system.log().info("*** LOG ALL UNDER /temp");
		searchService.tell(new Search("/temp/*"), ActorRef.noSender());
		try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }

		system.log().info("*** LOG ALL UNDER /remote");
		searchService.tell(new Search("/remote/*"), ActorRef.noSender());

		system.log().info("In Conclusion, we have no deadLetters or stopped actors, no short-lived system-created actors and no actor with a remote actor reference");
		try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
