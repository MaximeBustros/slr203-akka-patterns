package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;
import java.util.ArrayList;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor and passing his reference to
 *				another actor by message.
 */
public class CommunicationTopologyCreation {

	public static void main(String[] args) {

		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		// Create a matrix with relationships
		int NUMBER_OF_NODES = 4;
		int[][] mat = new int[NUMBER_OF_NODES][NUMBER_OF_NODES];
		mat[0][0] = 0;
		mat[0][1] = 1;
		mat[0][2] = 1;
		mat[0][3] = 0;

		mat[1][0] = 0;
		mat[1][1] = 0;
		mat[1][2] = 0;
		mat[1][3] = 1;

		mat[2][0] = 1;
		mat[2][1] = 0;
		mat[2][2] = 0;
		mat[2][3] = 1;

		mat[3][0] = 1;
		mat[3][1] = 0;
		mat[3][2] = 0;
		mat[3][3] = 1;

		ActorRef[] actors = new ActorRef[NUMBER_OF_NODES];
		// create all actors
		for (int i = 0; i < NUMBER_OF_NODES; i++) {
			actors[i] = system.actorOf(Client.createActor(), "actor"+(i+1));
		}
		
		for (int i = 0; i < NUMBER_OF_NODES; i++) {
			ArrayList<ActorRef> actorReferenceList = new ArrayList<ActorRef>();
			for (int j = 0; j < NUMBER_OF_NODES; j++) {
				if (mat[i][j] == 1) {
					actorReferenceList.add(actors[j]);
				}
			}
			ActorReferences actorReferences  = new ActorReferences(actorReferenceList);
			actors[i].tell(actorReferences, ActorRef.noSender());
		}

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
