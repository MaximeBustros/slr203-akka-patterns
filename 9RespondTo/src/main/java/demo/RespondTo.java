package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class RespondTo {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate all 3 actors
		final ActorRef c = system.actorOf(ThirdActor.createActor(), "c");

		final ActorRef b = system.actorOf(SecondActor.createActor(), "b");
		
		// Pass b and c as reference to a
		final ActorRef a = system.actorOf(FirstActor.createActor(b, c), "a");
		
		// Start the system and force a to send two messages to b in a row
		final Start start = new Start();
		a.tell(start, ActorRef.noSender());

	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
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
