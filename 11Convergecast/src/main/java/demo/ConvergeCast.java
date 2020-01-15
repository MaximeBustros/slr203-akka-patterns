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

		final ActorRef d = system.actorOf(SecondActor.createActor(), "d");
		final ActorRef merger = system.actorOf(Merger.createActor(d), "broadcaster");
		final ActorRef a = system.actorOf(FirstActor.createActor(), "a");
		final ActorRef b = system.actorOf(FirstActor.createActor(), "b");
		final ActorRef c = system.actorOf(FirstActor.createActor(), "c");

		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		// Start the system and force a to send two messages to b in a row
		final Start start = new Start();

		final StartJoinRequest startJoinRequest = new StartJoinRequest(merger);
		// Make all three actors join the merger
		a.tell(startJoinRequest, ActorRef.noSender());
		b.tell(startJoinRequest, ActorRef.noSender());
		c.tell(startJoinRequest, ActorRef.noSender());
		try {
			wait50();
		} catch(Exception e) {e.printStackTrace();}

		// Force all actors to send messages to merger
		a.tell(start, ActorRef.noSender());
		b.tell(start, ActorRef.noSender());
		c.tell(start, ActorRef.noSender());
		try {
			wait50();
		} catch(Exception e) {e.printStackTrace();}

		final StartUnjoinRequest startUnjoinRequest = new StartUnjoinRequest();
		// Actor c unjoins merger
		c.tell(startUnjoinRequest, ActorRef.noSender());
		try {
			wait50();
		} catch(Exception e) {e.printStackTrace();}

		// // Force actor a and b to send a message to merger
		a.tell(start, ActorRef.noSender());
		b.tell(start, ActorRef.noSender());
		// c.tell(start, ActorRef.noSender());
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

	public static void wait50() throws InterruptedException {
		Thread.sleep(50);
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
