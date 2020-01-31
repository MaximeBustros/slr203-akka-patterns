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
public class UncontrolledFlooding {

	public static void main(String[] args) {

		// Instantiate an actor system
		final ActorSystem system = ActorSystem.create("system");

		// Create a matrix of img.png
		int NUMBER_OF_NODES = 5;
		int[][] mat = new int[NUMBER_OF_NODES][NUMBER_OF_NODES];
		mat[0][0] = 0;
		mat[0][1] = 1;
		mat[0][2] = 1;
		mat[0][3] = 0;
		mat[0][3] = 0;

		mat[1][0] = 0;
		mat[1][1] = 0;
		mat[1][2] = 0;
		mat[1][3] = 1;
		mat[1][4] = 0;

		mat[2][0] = 0;
		mat[2][1] = 0;
		mat[2][2] = 0;
		mat[2][3] = 1;
		mat[2][4] = 0;

		mat[3][0] = 0;
		mat[3][1] = 0;
		mat[3][2] = 0;
		mat[3][3] = 0;
		mat[3][4] = 1;

		mat[4][0] = 0;
		mat[4][1] = 0;
		mat[4][2] = 0;
		mat[4][3] = 0;
		mat[4][4] = 1;

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

		system.log().info("There is an infinite loop when following the topology because actor3 actor4 and actor5 form a connected subgraph");
		try {
			Thread.sleep(10000);
		} catch (Exception e) { e.printStackTrace(); }

		// start flood from actor1
		FloodingMessage fm = new FloodingMessage("");
		actors[0].tell(fm, ActorRef.noSender());

		try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(1500);
	}

}
