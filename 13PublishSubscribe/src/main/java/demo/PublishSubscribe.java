package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.ArrayList;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class PublishSubscribe {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(Subscriber.createActor(), "a");
		final ActorRef b = system.actorOf(Subscriber.createActor(), "b");
		final ActorRef c = system.actorOf(Subscriber.createActor(), "c");

		final ActorRef topic1 = system.actorOf(Topic.createActor(), "topic1");
		final ActorRef topic2 = system.actorOf(Topic.createActor(), "topic2");

		final ActorRef publisher1 = system.actorOf(Publisher.createActor(topic1), "publisher1");
		final ActorRef publisher2 = system.actorOf(Publisher.createActor(topic2), "publisher2");

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		
		SubscribeTo subTo1 = new SubscribeTo(topic1);
		SubscribeTo subTo2 = new SubscribeTo(topic2);
		// program start
		a.tell(subTo1, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		b.tell(subTo1, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		b.tell(subTo2, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		c.tell(subTo2, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		SendMessage hello = new SendMessage("Hello");
		SendMessage world = new SendMessage("World");

		publisher1.tell(hello, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		publisher2.tell(world, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		Unsubscribe unsub = new Unsubscribe();
		a.tell(unsub, ActorRef.noSender());
		try {Thread.sleep(50);} catch(Exception e) {e.printStackTrace();}

		SendMessage hello2 = new SendMessage("Hello2");
		publisher1.tell(hello2, ActorRef.noSender());

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
