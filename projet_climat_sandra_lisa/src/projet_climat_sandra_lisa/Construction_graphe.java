package projet_climat_sandra_lisa;
import java.util.*;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;

public class Construction_graphe {

	public Construction_graphe() {
		//System.out.println("init graphe");
		Graph graph = new SingleGraph("Tutorial 1");
		graph.addNode("A" );
		graph.addNode("B" );
		graph.addNode("C" );
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		//graph.display();

	}

	//création du graphe en parallèle de la lecture des données
	public Graph affichage_graphe(List<String> ligne, Graph graph_twitter) {


		//nous avons un retweet si la dernière colonne n'est pas vide
		if (ligne.get(4)!=" ") {
			boolean doublon_1=false;
			boolean doublon_4=false;
			boolean doublon_inutile=false;
			
			//verification que les twitters que nous devons ajouter ne sont pas déjà présents parmi les sommets
		    for(Node n:graph_twitter) {
		        if(Objects.equals(n.getId(),ligne.get(1))){
		        	doublon_1=true;
		        } 
		        if(Objects.equals(n.getId(),ligne.get(4))) {
		        	doublon_4=true;
		        }
		        //si l'arete est déjà présente on ne crée pas de nouvelle aretes
		        if((Objects.equals(ligne.get(1),ligne.get(4)))) {
		        	doublon_inutile=true;
		        }
		    }
		    
		    for(Edge e:graph_twitter.getEachEdge()) {
		        if((Objects.equals(e.getId(),ligne.get(1).concat(ligne.get(4)))|| (Objects.equals(e.getId(),ligne.get(4).concat(ligne.get(1))))))  {
		        	doublon_inutile=true;
		        }
		    }
		    
		    
			//System.out.println("sommet i existe deja : " + doublon_1 + " "+ ligne.get(1));
			//System.out.println("sommet j existe deja : " + doublon_4 + " "+ ligne.get(4));
			//System.out.println("arrete i j existe deja : "+ " " + doublon_inutile);
			
			//ajout des sommets et de l arrete si ils n'étaient pas présents auparavant
		    if(doublon_1==false) {
		    	graph_twitter.addNode(ligne.get(1));
		    }
		    //si le sommet n est pas présent parmi les sommets du graphe et est différent de celui qui vient d'être ajouté
			if((doublon_4==false) && !(Objects.equals(ligne.get(1),ligne.get(4)))) {
				graph_twitter.addNode(ligne.get(4));
			}
			if(doublon_inutile==false) {
				graph_twitter.addEdge(ligne.get(1).concat(ligne.get(4)), ligne.get(1), ligne.get(4));
			}
			
		
		}
		return graph_twitter;
		
	}
	
	
	
}

