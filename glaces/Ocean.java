package glaces ;
import geometrie.Point ;
import java.util.Random ;

public class Ocean
{
	private int hauteur ;
	private int largeur ;
	private Iceberg2D[] ice ;
	private Pingouin ping ;
	private Poisson[] fish ;
	private int nbFish ; // servira à l'objectif du jeu
	private int nbreIceberg ; // servira à l'objectif du jeu
	final double moveFish = 4. ;

	public Ocean()
	{
		this.largeur = 300 ;
		this.hauteur = 300 ;
		this.ice = new Iceberg2D[2] ;
		this.ice[0] = new Iceberg2D(new Point(0.,0.), new Point(10., 10.)) ;
		this.ice[1] = new Iceberg2D(new Point(20.,20.), new Point(40., 40.)) ;
		this.nbreIceberg = 2 ;

		// On veut que notre pingouin se situe en bas à droite de l'écran
		this.ping = new Pingouin(new Point(300.,0.), 20) ;

		// On va générer des poissons
		this.fish = new Poisson[20] ;
		this.nbFish = 20 ;

		Random g = new Random() ;

		for (int i=0 ; i < this.nbFish ; i++)
		{
			int posX = g.nextInt(250) ;
			int posY = g.nextInt(250) ;
			this.fish[i] = new Poisson(new Point(posX, posY), 10, 5) ;
		}
	}

	public Ocean(int nbreIceberg, int largeur, int hauteur)
	{
		this.largeur = largeur ;
		this.hauteur = hauteur ;
		this.ice = new Iceberg2D[nbreIceberg] ;

		Random g = new Random() ;

		this.nbFish = 20 + g.nextInt( (largeur+hauteur)/50 ) ; // on rajoute 20 au cas où le générateur retourne 0
		this.fish = new Poisson[this.nbFish] ; // valeur arbitraire

		for ( int i = 0 ; i < nbreIceberg ; i++ )
		{
			int firstX = g.nextInt(largeur) ;
			int firstY = g.nextInt(hauteur) ;
			int secondX = g.nextInt(largeur) ;
			int secondY = g.nextInt(hauteur) ;

			Point gauche = new Point((double)firstX, (double)firstY) ;
			Point droit = new Point((double)secondX, (double)secondY) ;

			this.ice[i] = new Iceberg2D(gauche, droit) ;
		}

		for ( int j = 0 ; j < this.nbFish ; j++ )
		{
			int posX = g.nextInt(largeur) ;
			int posY = g.nextInt(hauteur) ;

			this.fish[j] = new Poisson(new Point(posX, posY), 10, 5) ;
		}

		// On veut que le pingouin se situe en bas à droite de l'écran
		this.ping = new Pingouin(new Point(largeur, 0.), 20) ;
	}

	public int getWidth()
	{
		return this.largeur ;
	}

	public int getHeight()
	{
		return this.hauteur ;
	}

	public int getCount()
	{
		return this.ice.length ;
	}

	public String toString()
	{
		return "Je suis un océan de taille " + this.getWidth() + "x" + this.getHeight() + ". Je possède " +
                this.getCount() + " iceberg sur moi." ;
	}

	public void fondre(double fr)
	{
		for ( Iceberg2D i : this.ice )
		{
			i.fondre(fr) ;
		}
	}

	public int[][] getColors()
	{
		int[][] colors = new int[this.getHeight()][this.getWidth()] ;

		// 0 : bleu
		// 1 : blanc 
		// 2 : orange / brun (daltonisme)
		// 3 : orange plus foncé
		// 4 : jaune / vert kaki
		// 5 : jaune / vert pâle
		// 6 : vert
		// 7 : rose / violet

		/* Etablissons un ordre logique de dessin :
		   - les poissons seront dessinés en premier
		   - vient le tour des icebergs ensuite
		   - et finalement le pingouin

		   Ainsi, les poissons passeront sous les icebergs (ce qui est plus cohérent),
		   et le pingouin pourra monter sur les iceberg afin de les casser */

		// Dessinons les poissons VIVANTS
		for ( Poisson p : this.fish )
		{
			if ( p.isAlive() )
			{
				int widthFish = p.getWidth() ;
				int heightFish = p.getHeight() ;

				// On vérifie si il n'y a pas de problèmes quand on va accéder aux cases du tableau
				// Dans le cas où l'on a un depassement, on ramène le poisson en absicsse/ordonné 0
				boolean depassAbsPos = ( (int)p.getPos().getAbscisse() + widthFish >= this.largeur ) ;

				// Ici, nous n'ajoutons pas widthFish étant donné qu'on se situe à l'emplacement du point exact
				// en effet, nous avons décidé de partir du point et de dessiner le rectangle vers la droite, en rajoutons
				// widthFish on risque d'avoir une coordonnée négative
				boolean depassAbsNeg = ( (int)p.getPos().getAbscisse() < 0 ) ;

				boolean depassOrdPos = ( (int)p.getPos().getOrdonnee() + heightFish >= this.hauteur ) ;
				boolean depassOrdNeg = ( (int)p.getPos().getOrdonnee() < 0 ) ;

				// On gère le cas d'un dépassement
				double moveAbs = p.getPos().getAbscisse() ;
				double moveOrd = p.getPos().getOrdonnee() ;

				// Si un poisson sort de l'écran par la droite, on le ramène à gauche de l'écran
				if (depassAbsPos)
				{
					p.getPos().deplacer(-moveAbs, 0.) ;
				}

				// Si un poisson sort de l'écran par la gauche, on le ramène à droite de l'écran
				// donc à la largeur de l'écran - la valeur négative de l'abscisse - 
				// la largeur du poisson à dessiner vers la droite, - 1 à cause du tableau (éviter un dépassement mémoire)
				if (depassAbsNeg)
				{
					p.getPos().deplacer((double)this.largeur - moveAbs - widthFish - 1, 0.) ;
				}

				if (depassOrdPos)
				{
					p.getPos().deplacer(0., -moveOrd) ;
				}

				if (depassOrdNeg)
				{
					p.getPos().deplacer(0., (double)this.hauteur - moveOrd - heightFish - 1) ;
				}

				// On s'occupe maintenant de l'affichage
				int absFish = (int)p.getPos().getAbscisse() ;
				int ordFish = (int)p.getPos().getOrdonnee() ;
				int maxAbs = absFish + p.getWidth() ;
				int maxOrd = ordFish + p.getHeight() ;

				/* Pendant le parcours, on vérifie si le poisson n'a pas été mangé par le pingouin
				 * ce qui correspond à vérifier la position du poisson par rapport au pingouin.
				 * Si le poisson se fait manger, on continue de l'afficher sur "une frame"
				 * avant de le faire disparaître, le joueur pourra alors utiliser son imagination
				 * pour se représenter le pingouin en train d'avaler le poisson :) */

				int absPing = (int)ping.getPosition().getAbscisse() - ping.getSize() ;
				int ordPing = (int)ping.getPosition().getOrdonnee() + ping.getSize() ;
				boolean betweenAbs = false ;
				boolean betweenOrd = false ;

				while ( ordFish < maxOrd ) // Parcours de l'ordonnée
				{
					while ( absFish < maxAbs ) // Parcours de l'abscisse
					{
						// On vérifie si le poisson n'est pas dans le cube représentant le pingouin
						betweenAbs = ( absFish > absPing && absFish <= absPing + ping.getSize() ) ;
						betweenOrd = ( ordFish > ordPing - ping.getSize() && ordFish <= ordPing ) ;

						// S'il l'est, alors il est considéré comme mort / mangé
						if ( betweenAbs && betweenOrd )
						{
							p.getEaten() ;
							// On réinitialise la jauge de faim (de déplacement en réalité) du pingouin
							ping.ateFish() ;
						}
						colors[absFish][ordFish] = p.getItsColor() ;
						absFish++ ;
					}
					ordFish++ ;
					absFish = (int)p.getPos().getAbscisse() ;
				}
			}
		}

		for ( Iceberg2D i : this.ice )
		{
			int basAbs = (int)i.coinEnBasAGauche().getAbscisse() ;
			int basOrd = (int)i.coinEnBasAGauche().getOrdonnee() ;
			int hautAbs = (int)i.coinEnHautADroite().getAbscisse() ;
			int hautOrd = (int)i.coinEnHautADroite().getOrdonnee() ;

			while ( basOrd < hautOrd ) // Parcours de l'ordonnée
			{
				while ( basAbs < hautAbs ) // Parcours de l'abscisse
				{
					colors[basOrd][basAbs] = 1 ;
					basAbs++ ;
				}
				basOrd++ ;
				basAbs = (int)i.coinEnBasAGauche().getAbscisse() ;
			}
		}

		// On retire 1 car les indices du tableau vont de 0 à 299 
		int absPing = (int)this.ping.getPosition().getAbscisse() - 1 ;
		int ordPing = (int)this.ping.getPosition().getOrdonnee() ;
		int sizePing = this.ping.getSize() ;
		int absMax = absPing - sizePing ;
		int ordMax = ordPing + sizePing ;

		while ( ordPing < ordMax )
		{
			while ( absPing > absMax )
			{
				colors[absPing][ordPing] = ping.getItsColor() ;
				absPing-- ;
			}
			ordPing++ ;
			absPing = (int)this.ping.getPosition().getAbscisse() - 1 ; // Même raison
		}

		return colors ;

	}

	public Pingouin getPing()
	{
		return this.ping ;
	} 

	public void moveFish()
	{
		for ( Poisson p : this.fish )
		{
			p.deplacement(this.moveFish) ;
		}
	}

}