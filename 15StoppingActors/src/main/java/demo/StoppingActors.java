package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.time.Duration;
import static akka.pattern.Patterns.gracefulStop;
import akka.pattern.AskTimeoutException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class StoppingActors {

	public static void main(String[] args) {

		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		// create 4 different servers that will respond differently due to different ways of killing the service
		final ActorRef serverPoisonPill = system.actorOf(Server.createActor(), "ServerPoisonPill");
		final ActorRef serverStop = system.actorOf(Server.createActor(), "ServerStop");
		final ActorRef serverKill = system.actorOf(Server.createActor(), "ServerKill");
		final ActorRef serverStopGraceful = system.actorOf(Server.createActor(), "ServerStopGraceful");

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		
		// create two different requests
		Request requestHello = new Request("Hello");
		Request requestWorld = new Request("World!");

		system.log().info("*** POISON PILL ***");
		// PoisonPill Test
		serverPoisonPill.tell(requestHello, ActorRef.noSender());
		system.log().info("Sent request hello to [" + serverPoisonPill.path().name() + "]");

		// PoisonPill code
		serverPoisonPill.tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
		system.log().info("Sent poison pill to [" + serverPoisonPill.path().name() + "]");
		serverPoisonPill.tell(requestWorld, ActorRef.noSender());
		system.log().info("Sent request world to [" + serverPoisonPill.path().name() + "]");


		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}

		system.log().info("*** STOP TEST ***");
		// Stop Test
		serverStop.tell(requestHello, ActorRef.noSender());
		system.log().info("Sent request hello to [" + serverStop.path().name() + "]");
		// StopCode
		system.stop(serverStop);
		system.log().info("system stop [" + serverStop.path().name() + "]");
		serverStop.tell(requestWorld, ActorRef.noSender());
		system.log().info("Sent request world to [" + serverStop.path().name() + "]");

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		
		system.log().info("*** KILL TEST ***");
		// Kill Test
		serverKill.tell(requestHello, ActorRef.noSender());
		system.log().info("Sent request hello to [" + serverKill.path().name() + "]");
		
		// Kill Code
		serverKill.tell(akka.actor.Kill.getInstance(), ActorRef.noSender());
		system.log().info("Sent Kill request to [" + serverKill.path().name() + "]");
		// expecting the actor to indeed terminate:
		serverKill.tell(requestWorld, ActorRef.noSender());
		system.log().info("Sent request world to [" + serverPoisonPill.path().name() + "]");

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		
		system.log().info("*** GRACEFUL STOP TEST ***");
		// Graceful Stop Test
		serverStopGraceful.tell(requestHello, ActorRef.noSender());
		system.log().info("Sent request to [" + serverStopGraceful.path().name() + "]");
		// Graceful Stop Test
		try {
			CompletionStage<Boolean> stopped =
				gracefulStop(serverStopGraceful, Duration.ofSeconds(5), akka.actor.PoisonPill.getInstance());
			stopped.toCompletableFuture().get(6, TimeUnit.SECONDS);
			// the actor has been stopped
		} catch (Exception e) {
			// the actor wasn't stopped within 5 seconds
		}
		serverStopGraceful.tell(requestWorld, ActorRef.noSender());
		system.log().info("Sent request to [" + serverStopGraceful.path().name() + "]");


	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
			system.log().info("All methods acted quite similarily for our use case: \nThey all processed the first request but never the 2nd");
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
