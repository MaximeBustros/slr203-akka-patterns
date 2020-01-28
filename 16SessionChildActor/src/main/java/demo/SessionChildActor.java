package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class SessionChildActor {

	public static void main(String[] args) {

		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef sessionManager = system.actorOf(SessionManager.createActor(), "SessionManager");
		final ActorRef client1 = system.actorOf(Client.createActor(sessionManager), "Client1");

		CreateSession createSession = new CreateSession();
		client1.tell(createSession, ActorRef.noSender());

		try {
			Thread.sleep(1000);
		} catch (Exception e) { e.printStackTrace(); }

		SendRequest sendRequest = new SendRequest();
		client1.tell(sendRequest, ActorRef.noSender());

		EndSession endSession = new EndSession();
		client1.tell(endSession, ActorRef.noSender());


		try {
			Thread.sleep(1500);
		} catch (Exception e) { e.printStackTrace(); }
		client1.tell(sendRequest, ActorRef.noSender());
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
