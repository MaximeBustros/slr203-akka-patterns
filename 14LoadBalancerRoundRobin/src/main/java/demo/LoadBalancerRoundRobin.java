package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.ArrayList;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class LoadBalancerRoundRobin {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef loadBalancer = system.actorOf(LoadBalancer.createActor(), "loadbalancer");
		final ActorRef a = system.actorOf(Client.createActor(loadBalancer), "a");
		final ActorRef b = system.actorOf(Server.createActor(), "b");
		final ActorRef c = system.actorOf(Server.createActor(), "c");

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		
		// make b and c join the loadbalancer
		Join join = new Join(loadBalancer);
		b.tell(join, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}
		c.tell(join, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		// send 3 requests in a row to see how the loadbalancer reacts
		SendRequest sendRequest = new SendRequest();
		a.tell(sendRequest, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		a.tell(sendRequest, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		a.tell(sendRequest, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		// unjoin
		Unjoin unjoin = new Unjoin();
		c.tell(unjoin, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		// send new request

		a.tell(sendRequest, ActorRef.noSender());

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
