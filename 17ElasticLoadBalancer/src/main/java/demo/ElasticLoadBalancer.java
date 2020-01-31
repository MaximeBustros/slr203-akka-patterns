package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class ElasticLoadBalancer {

	public static void main(String[] args) {

		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef loadBalancer = system.actorOf(LoadBalancer.createActor(), "LoadBalancer");
		final ActorRef a = system.actorOf(Client.createActor(loadBalancer), "a");

		MaxNoServers max = new MaxNoServers(2);
		loadBalancer.tell(max, ActorRef.noSender());

		Request request1 = new Request("req1");
		Request request2 = new Request("req2");
		Request request3 = new Request("req3");

		a.tell(request1, ActorRef.noSender());
		a.tell(request2, ActorRef.noSender());
		a.tell(request3, ActorRef.noSender());
		// Also try with more requests to check that it works as intended :)

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
