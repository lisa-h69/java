package projet_climat_sandra_lisa;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class Fenetre extends JFrame implements ActionListener{
		public Fenetre(){
		  	System.out.println("ouverture de la fenetre");
		    this.setTitle("Ma première fenêtre Java");
		    this.setSize(400, 200);
		    this.setLocationRelativeTo(null);
		    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
		    
		    //Instanciation d'un objet JPanel
		    JPanel pan = new JPanel();
		    //On prévient notre JFrame que notre JPanel sera son content pane
		    this.setContentPane(pan);
		    
		    // ----- Pour le label
		    JLabel label = new JLabel("Sélectionnez votre fichier texte : ");
			pan.add(label);
		    
		    // ----- Pour la zone de texte
			//JTextField textField = new JTextField();
			//textField.setColumns(20); //On lui donne un nombre de colonnes à afficher
		    //pan.add(textField);
		    
		 // ----- Pour le bouton
			JButton bouton = new JButton("Charger ...");
			bouton.addActionListener(this);
			pan.add(bouton);

			
		    this.setVisible(true);
		  }
		  
		  public void actionPerformed(ActionEvent e) {

			  
			    /* init du filechooser */
			    JFileChooser fc = new JFileChooser();
			    /* affichage du dialog et test si le bouton ok est pressé */
			    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			    	try {
			    		System.out.println("Chargement en cours...");
			    		
			    		//enleve la boîte de dialogue
			    		this.setVisible(false);
			    		
			    		//chargement du fichier sélectionné
			    		File fichier_climat = new File(fc.getSelectedFile().getAbsolutePath());
			    		ArrayList<List<String>> donnees_climat = new ArrayList<>();
			    		Scanner sc = new Scanner(fichier_climat);
			    		
			    		// Création des listes des urilisateurs et de leur poids
			    		List<String> users = new ArrayList<>();
			    		List<Integer> poids = new ArrayList<>();
			    		
			    		//initialisation du graphe
			    		Graph graphe_final = null;
						Graph graph_twitter = new SingleGraph("graphe des twittos");

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
			    			//donnees_climat.add(ligne);
			    			
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
			    			Construction_graphe donnees_ligne = new Construction_graphe();
			    			graphe_final = donnees_ligne.affichage_graphe(ligne,graph_twitter);			    			
			    		}

			    		
			    		System.out.println("chargement terminé");
			    		int nb_sommet=0;
			    		int nb_arete=0;
			    		// attribution des noms à leur sommet
			    	    for (Node node : graphe_final) {
			    	        node.addAttribute("ui.label", node.getId());
			    	        nb_sommet++;
			    	    }
			    		
			    	    //comptage du nombre d'aretes
			    	    for(Edge arete:graph_twitter.getEachEdge()) {
			    	    	nb_arete++;
					    }
			    	    
			    	    //affichage du graphe
			    		graphe_final.display();
			    		System.out.println("nombre de lignes : "+ donnees_climat.size());
			    		System.out.println("nombre de sommets : "+ nb_sommet);
			    		System.out.println("nombre d'arètes : "+ nb_arete);
			    		//double deg_moy= new Double((nb_arete*2)/nb_sommet);
			    		float deg_moyen= (float) (nb_arete*2)/nb_sommet;
			    		String deg_moy = String.format("%.2f", deg_moyen);
			    		System.out.println("degré moyen : "+ deg_moy);

			    		//System.out.println("nom des sommets : \t" +users);
			    		//System.out.println("poids des sommets : \t" + poids);
			    		
			    	} catch (IOException e1) {
			        e1.printStackTrace();
			      }
		  }
}
