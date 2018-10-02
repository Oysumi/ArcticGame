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
	private int nbFish ;
	private int nbreIceberg ;
	final private double moveFish = 4. ;
	private Random g = new Random() ; // on lui attribue un random pour éviter la multiplication des objets morts

	public Ocean()
	{
		this.largeur = 300 ;
		this.hauteur = 300 ;
		this.ice = new Iceberg2D[2] ;
		this.ice[0] = new Iceberg2D(new Point(0.,0.), new Point(10., 10.)) ;
		this.ice[1] = new Iceberg2D(new Point(20.,20.), new Point(40., 40.)) ;
		this.nbreIceberg = 2 ;

		// On veut que notre pingouin se situe en bas à droite de l'écran
		this.ping = new Pingouin(new Point(this.largeur - 1.,0.), 5) ; // on retire 1 pour éviter un dépassement mémoire lors du dessin

		// On va générer des poissons
		this.fish = new Poisson[20] ;
		this.nbFish = 20 ;

		for (int i=0 ; i < this.nbFish ; i++)
		{
			int posX = this.g.nextInt(250) ;
			int posY = this.g.nextInt(250) ;
			this.fish[i] = new Poisson(new Point(posX, posY), 10, 10) ;
		}
	}

	public Ocean(int nbreIceberg, int largeur, int hauteur)
	{
		this.largeur = largeur ;
		this.hauteur = hauteur ;
		this.ice = new Iceberg2D[nbreIceberg] ;

		this.nbFish = 30 + this.g.nextInt( (largeur+hauteur)/50 ) ; // on rajoute 30 au cas où le générateur retourne 0
		this.fish = new Poisson[this.nbFish] ; // valeur arbitraire

		for ( int i = 0 ; i < nbreIceberg ; i++ )
		{
			int firstX = this.g.nextInt(largeur) ;
			int firstY = this.g.nextInt(hauteur) ;
			int secondX = this.g.nextInt(largeur) ;
			int secondY = this.g.nextInt(hauteur) ;

			Point gauche = new Point((double)firstX, (double)firstY) ;
			Point droit = new Point((double)secondX, (double)secondY) ;

			this.ice[i] = new Iceberg2D(gauche, droit) ;
		}

		for ( int j = 0 ; j < this.nbFish ; j++ )
		{
			int posX = this.g.nextInt(largeur) ;
			int posY = this.g.nextInt(hauteur) ;

			this.fish[j] = new Poisson(new Point(posX, posY), 10, 5) ;
		}

		// On veut que le pingouin se situe en bas à droite de l'écran, mais on retire 1 pour faciliter le dessin
		this.ping = new Pingouin(new Point((double)largeur - 1., 0.), 10) ;
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

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

	// Nous sommes obligés ici de fournir un accès au pingouin de l'océan pour le déplacer
	public Pingouin getPing()
	{
		return this.ping ;
	} 

	public int getNbFish()
	{
		return this.nbFish ;
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

	public String toString()
	{
		return "Je suis un océan de taille " + this.getWidth() + "x" + this.getHeight() + ". Je possède " +
                this.getCount() + " iceberg sur moi." ;
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

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

		/* MEMO
		 * 0 : bleu
		 * 1 : blanc 
		 * 2 : orange / brun (daltonisme)
		 * 3 : orange plus foncé
		 * 4 : jaune / vert kaki
		 * 5 : jaune / vert pâle
		 * 6 : vert
		 * 7 : rose / violet
		*/

		// Dessinons les poissons VIVANTS
		for ( Poisson p : this.fish )
		{
			// On vérifie si le poisson a été mangé par le pingouin
			p.getEaten(this.ping) ;

			if ( p.isAlive() )
			{
				// On vérifie si le poisson ne sort pas du bord gauche ou droit et dans ce cas on le ramène sur un des bords
				// de l'écran
				p.outOfSea(this) ;

				// On s'occupe maintenant de l'affichage
				int absFish = (int)p.getAbscisse() ;
				int ordFish = (int)p.getOrdonnee() ;
				int maxAbs = absFish + p.getWidth() ;
				int maxOrd = ordFish + p.getHeight() ;

				while ( ordFish < maxOrd ) // Parcours de l'ordonnée
				{
					while ( absFish < maxAbs ) // Parcours de l'abscisse
					{
						colors[absFish][ordFish] = p.getItsColor() ;
						absFish++ ;
					}
					ordFish++ ;
					absFish = (int)p.getAbscisse() ;
				}
			}
		}

		for ( Iceberg2D i : this.ice )
		{
			this.ping.jumpOnIceberg(i) ;
			
			int basAbs = (int)i.coinEnBasAGauche().getAbscisse() ;
			int basOrd = (int)i.coinEnBasAGauche().getOrdonnee() ;
			int hautAbs = (int)i.coinEnHautADroite().getAbscisse() ;
			int hautOrd = (int)i.coinEnHautADroite().getOrdonnee() ;

			while ( basOrd < hautOrd ) // Parcours de l'ordonnée
			{
				while ( basAbs < hautAbs ) // Parcours de l'abscisse
				{
					colors[basAbs][basOrd] = 1 ;
					basAbs++ ;
				}
				basOrd++ ;
				basAbs = (int)i.coinEnBasAGauche().getAbscisse() ;
			}
		}

		// On dessine maintenant le pingouin 
		int absPing = (int)this.ping.getAbscisse() ;
		int ordPing = (int)this.ping.getOrdonnee() ;
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
			absPing = (int)this.ping.getAbscisse() ; // Même raison
		}

		return colors ;
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

	public void moveFish()
	{
		for ( Poisson p : this.fish )
		{
			for ( Iceberg2D i : this.ice )
			{
				// On vérifie d'abord avant le déplacement si on percute un iceberg (donc on anticipe un tour)
				if ( p.collidesIceberg(i) )
				{
					p.changeDirection() ;
				}
			}

			// Après changement de direction si nécessaire, on effectue le déplacement
			p.deplacement() ;
		}
	}

	public boolean areFishsAllDead()
	{
		boolean dead = false ;

		for ( Poisson p : this.fish )
		{
			dead = dead || p.isAlive() ;
		}

		return !dead ;
	}

}