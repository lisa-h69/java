package projet_climat_sandra_lisa;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class Interface_import extends JFrame implements ActionListener{
	Graph graph_twitter = new SingleGraph("graphe des twittos");
	
	//affichage de la fenetre
	public static void main(String[] args){       
		Interface_import fenetre = new Interface_import();
	 }       

	//importation des données
	public Interface_import(){
	  	System.out.println("ouverture de la fenetre");
	    this.setTitle("Ma fenêtre Java");
	    this.setSize(400, 200);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    
	    //Instanciation d'un objet JPanel
	    JPanel pan = new JPanel();
	    //On prévient notre JFrame que notre JPanel sera son content pan
	    this.setContentPane(pan);
	    
	    // ----- Pour le label
	    JLabel label = new JLabel("Sélectionnez votre fichier texte : ");
		pan.add(label);
	    
	 // ----- Pour le bouton Chargement
		JButton Chargement = new JButton("Charger ...");
		Chargement.addActionListener(this);
		pan.add(Chargement);

		
	    this.setVisible(true);
	  }
	  
	  public void actionPerformed(ActionEvent e) {

		  
		    /* init du filechooser */
		    JFileChooser fc = new JFileChooser();
		    /* affichage du dialog et test si le bouton ok est pressé */
		    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		    	try {
		    		System.out.println("Chargement en cours...");

		    	    this.setVisible(false);
		    		
		    		//chargement du fichier sélectionné
		    		File fichier_climat = new File(fc.getSelectedFile().getAbsolutePath());
		    		
		    		Scanner sc = new Scanner(fichier_climat);
		    		
		    		// Création des listes des urilisateurs et de leur poids
		    		List<String> users = new ArrayList<>();
		    		List<Integer> poids = new ArrayList<>();
		    		
		    		//initialisation du graphe
		    		Graph graphe_final = null;

					//On parcourt dans chaque ligne du fichier texte
					while (sc.hasNextLine()) {  

		    			int w = 0;
		    			int x = 0;
		    			int y = 0;
		    			
		    			//on passe à la ligne suivante
		    			String line = sc.nextLine();
		    			
		    			//separation des données selon la tabulation
		    			String[] data = line.split("	");
		    			
		    			//reinitialisation de la ligne
		    			List<String> ligne = new ArrayList<>();
		    			
		    			//ajout dans la ligne de chaque parametre contenu dans data
		    			for (String un_element : data) {
		    				ligne.add(un_element);
		    			}
		    			
		    			//si le nombre de param =4, on rajoute pour le retweet un élément vide
		    			if (data.length==4) {
			    			ligne.add(" ");
		    			} 
		    			
		    			// On verifie si le pseudo twitter est deja enregistré ou non
		    			for (int i = 0; i < users.size(); i++) {
		    				if (Objects.equals(users.get(i),ligne.get(4))) {
		    					x = i;
		    				}
		    				if (Objects.equals(users.get(i),ligne.get(1))) {
		    					w = i;
		    				}
		    			}
		    			
		    			// Si le nom n'est pas déjà present, on l'ajoute, sinon on lui met un poids +1
		    			if (x == 0 && ligne.get(4) != " " && w == 0) {
		    				//colonne des utilisateurs qui ont été retweetés
		    				users.add(ligne.get(4));
		    				poids.add(1);
		    				//colonne des utilisateurs qui ont tweetté
		    				users.add(ligne.get(1));
		    				poids.add(1);
		    			}else if (x == 0 && ligne.get(4) != " " && w != 0){
		    				//colonne des utilisateurs qui ont été retweetés
		    				users.add(ligne.get(4));
		    				poids.add(1);
		    				//colonne des utilisateurs qui ont tweetté
		    				y = poids.get(w);
		    				y++;
		    				poids.set(w,y);
		    			}else if(x != 0 && ligne.get(4) != " " && w == 0) {
		    				//colonne des utilisateurs qui ont été retweetés
		    				y = poids.get(x);
		    				y++;
		    				poids.set(x,y); 
		    				//colonne des utilisateurs qui ont tweetté
		    				users.add(ligne.get(1));
		    				poids.add(1);  				
		    			}else if (ligne.get(4) != " ") {
		    				//colonne des utilisateurs qui ont été retweetés
		    				y = poids.get(x);
		    				y++;
		    				poids.set(x,y);
		    				//colonne des utilisateurs qui ont tweetté
		    				y = poids.get(w);
		    				y++;
		    				poids.set(w,y);
		    			}
		    			   			
		    			//passage à la création du graphe
		    			Graphe donnees_ligne = new Graphe();
		    			graphe_final = donnees_ligne.Creation_graphe(ligne,graph_twitter);			    			
		    		}

		    		
		    		System.out.println("chargement terminé");
		    		// attribution des noms à leur sommet
		    	    for (Node node : graphe_final) {
		    	        node.addAttribute("ui.label", node.getId());
		    	    }
		    		
		    	    
		    	    //affichage du graphe
		    		graphe_final.display();
		    		
		    		this.Interface_resultats();
		    	} catch (IOException e1) {
		        e1.printStackTrace();
		      }
	  }
		  
	  
	  public void Interface_resultats() {
		  
			Graphe data_stat = new Graphe();
			ArrayList<Integer> result=data_stat.Statistiques_graphe(graph_twitter);			   
		    this.setSize(250, 150);
		    this.setLocationRelativeTo(null);
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
		    
		    //Instanciation d'un objet JPanel
		    JPanel resu = new JPanel();
		    //On prévient notre JFrame que notre JPanel sera son content pane
		    this.setContentPane(resu);
		    
		    // ----- affichage des différents résultats
		    JLabel label = new JLabel("Voici les différents résultats : ");
		    resu.add(label);

		    JLabel resu1 = new JLabel("Le nombre de sommets est de  : " + result.get(0)+ " \r\n");
		    resu.add(resu1);

		    JLabel resu2 = new JLabel("Le nombre d'aretes est de  : " + result.get(1) + " \r\n");
		    resu.add(resu2);

		    JLabel resu3 = new JLabel("Le degré moyen est de : " + result.get(2));
		    resu.add(resu3);
		    
		    

			
		    this.setVisible(true);
		  
	  }
}
