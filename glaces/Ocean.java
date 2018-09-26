package glaces ;
import geometrie.Point ;
import java.util.Random ;

public class Ocean
{
	private int hauteur ;
	private int largeur ;
	private Iceberg2D[] ice ;
	private Pingouin ping ;

	public Ocean()
	{
		this.largeur = 300 ;
		this.hauteur = 300 ;
		this.ice = new Iceberg2D[2] ;
		this.ice[0] = new Iceberg2D(new Point(0.,0.), new Point(10., 10.)) ;
		this.ice[1] = new Iceberg2D(new Point(20.,20.), new Point(40., 40.)) ;

		// On veut que notre pingouin se situe en bas à droite de l'écran
		this.ping = new Pingouin(new Point(300.,0.), 20) ;
	}

	public Ocean(int nbreIceberg, int largeur, int hauteur)
	{
		this.largeur = largeur ;
		this.hauteur = hauteur ;
		this.ice = new Iceberg2D[nbreIceberg] ;

		Random g = new Random() ;

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

		int absPing = (int)this.ping.getPosition().getAbscisse() - 1 ;
		int ordPing = (int)this.ping.getPosition().getOrdonnee() ;
		int sizePing = this.ping.getSize() ;
		int absMax = absPing - sizePing ;
		int ordMax = ordPing + sizePing ;

		while ( ordPing < ordMax )
		{
			while ( absPing > absMax )
			{
				colors[absPing][ordPing] = 2 ;
				absPing-- ;
			}
			ordPing++ ;
			absPing = (int)this.ping.getPosition().getAbscisse() - 1 ;
		}
		return colors ;

		// 0 : bleu
		// 1 : blanc 
		// 2 : orange / brun (daltonisme)
		// 3 : orange plus foncé
		// 4 : jaune / vert kaki
		// 5 : jaune / vert pâle
		// 6 : vert
		// 7 : rose / violet
	}

	public Pingouin getPing()
	{
		return this.ping ;
	} 

}