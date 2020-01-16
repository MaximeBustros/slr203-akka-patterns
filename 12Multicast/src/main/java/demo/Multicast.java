package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.ArrayList;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class Multicast {

	public static void main(String[] args) {
		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef multicaster = system.actorOf(Multicaster.createActor(), "multicaster");
		final ActorRef sender = system.actorOf(Sender.createActor(multicaster), "sender");
		final ActorRef receiver1 = system.actorOf(Receiver.createActor(), "receiver1");
		final ActorRef receiver2 = system.actorOf(Receiver.createActor(), "receiver2");
		final ActorRef receiver3 = system.actorOf(Receiver.createActor(), "receiver3");
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}
		
		// program start
		final ArrayList<ActorRef> group1 = new ArrayList<ActorRef>();
		group1.add(receiver1);
		group1.add(receiver2);

		final ArrayList<ActorRef> group2 = new ArrayList<ActorRef>();
		group2.add(receiver2);
		group2.add(receiver3);

		final CreateMulticastGroup createMulticastGroup1 = new CreateMulticastGroup("group1", group1);
		sender.tell(createMulticastGroup1, ActorRef.noSender());

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}

		final CreateMulticastGroup createMulticastGroup2 = new CreateMulticastGroup("group2", group2);
		sender.tell(createMulticastGroup2, ActorRef.noSender());

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}

		final Message message1 = new Message("Hello");
		final Message message2 = new Message("World");

		final SendMulticastMessage sendMulticast1 = new SendMulticastMessage("group1", message1);
		final SendMulticastMessage sendMulticast2 = new SendMulticastMessage("group2", message2);

		sender.tell(sendMulticast1, ActorRef.noSender());

		try {
			Thread.sleep(1000);
		} catch(Exception e) {e.printStackTrace();}

		sender.tell(sendMulticast2, ActorRef.noSender());
		// // Force actor a and b to send a message to merger
		// a.tell(start, ActorRef.noSender());
		// b.tell(start, ActorRef.noSender());
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

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
