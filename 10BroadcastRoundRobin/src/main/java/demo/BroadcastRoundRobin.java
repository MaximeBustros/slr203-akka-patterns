package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class BroadcastRoundRobin {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef broadcaster = system.actorOf(Broadcaster.createActor(), "broadcaster");

		final ActorRef a = system.actorOf(FirstActor.createActor(broadcaster), "a");

		final ActorRef b = system.actorOf(SecondActor.createActor(), "b");

		final ActorRef c = system.actorOf(SecondActor.createActor(), "c");
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		// Start the system and force a to send two messages to b in a row
		final Start start = new Start();
		final StartJoinRequest join = new StartJoinRequest(broadcaster);

		// force b and c to join 
		b.tell(join, ActorRef.noSender());
		c.tell(join, ActorRef.noSender());

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}

		// Force a to send a message to broadcaster
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
