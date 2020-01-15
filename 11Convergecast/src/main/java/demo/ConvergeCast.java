package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class ConvergeCast {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef merger = system.actorOf(Merger.createActor(), "broadcaster");
		final ActorRef a = system.actorOf(FirstActor.createActor(), "a");
		final ActorRef b = system.actorOf(FirstActor.createActor(), "b");
		final ActorRef c = system.actorOf(FirstActor.createActor(), "c");
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		// Start the system and force a to send two messages to b in a row
		final Start start = new Start();
		// Make all three actors join the merger
		a.tell(joinRequest, ActorRef.noSender());
		b.tell(joinRequest, ActorRef.noSender());
		c.tell(joinRequest, ActorRef.noSender());

		// Force all actors to send messages to merger
		a.tell(start, ActorRef.noSender());
		b.tell(start, ActorRef.noSender());
		c.tell(start, ActorRef.noSender());

		// Actor c unjoins merger
		c.tell(unjoinRequest, ActorRef.noSender());

		// Force actor a and b to send a message to merger
		a.tell(start, ActorRef.noSender());
		b.tell(start, ActorRef.noSender());

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
