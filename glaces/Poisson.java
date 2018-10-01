package glaces ;
import geometrie.Point ;
import java.util.Random ;

public class Poisson
{
	private Point position ;
	private int hauteur ;
	private int largeur ;
	private String movement ;

	// Déplacement en abscisse ou ordonnée commun à tous les poissons
	private static double moveXOrY = 4. ;

	// On genère un seuil de déplacement auquel au delà de ce dernier, le poisson est considéré comme fatigué / mort
	private Random g = new Random() ;
	final private int thresholdTired = 40 + g.nextInt(21) ;
	final private int thresholdDeath = 80 + g.nextInt(21) ;

	// Ces champs indiqueront la jauge de fin du poisson (sa couleur dépend du nombre de mouvement)
	// Couleur : [4 : normal] [5 : fatigué]
	private int nbMovement ;
	private int color ;

	// Ce champ explicite indique si le poisson est vivant ou mort (ainsi facilite son affichage ou non)
	private boolean alive ;

	public Poisson()
	{
		this.position = new Point(0.,0.) ;
		this.hauteur = 0 ;
		this.largeur = 0 ;
		this.movement = "none" ;
		this.nbMovement = 0 ;
		this.color = 4 ;
		this.alive = true ;
	}

	// Pour plus de pseudo aléatoire, on génère son mouvement dans le constructeur
	public Poisson(Point pos, int height, int width)
	{
		this.position = new Point(pos) ;
		this.hauteur = height ;
		this.largeur = width ;
		this.nbMovement = 0 ;
		this.color = 4 ;
		this.alive = true ;

		// On produit un nombre compris entre 0 et 3 et selon ce dernier, on définit arbitrairement son sens de déplacement
		int alea = this.g.nextInt(4) ;
		switch (alea)
		{
			case 0:
				this.movement = "pa" ; // déplacement positif en abscisse
				break ;
			case 1:
				this.movement = "na" ; // déplacement négatif en abscisse
				break ;
			case 2:
				this.movement = "po" ; // déplacement positif en ordonnée
				break ;
			case 3:
				this.movement = "no" ; // déplacement négatif en ordonnée
				break ;
			default:
				break ;
		}
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

	public Point getPos()
	{
		return this.position ;
	}

	public int getWidth()
	{
		return this.largeur ;
	}

	public int getHeight()
	{
		return this.hauteur ;
	}

	public String getMov()
	{
		return this.movement ;
	}

	public int getItsColor()
	{
		return this.color ;
	}

	public boolean isAlive()
	{
		return this.alive ;
	}

	/*************************************************************************************************************/
	/*************************************************************************************************************/

	public void deplacement()
	{
		switch (this.movement)
		{
			case "pa":
				this.position.deplacer(this.moveXOrY, 0.) ;
				break ;
			case "na":
				this.position.deplacer(-this.moveXOrY, 0.) ;
				break ;
			case "po":
				this.position.deplacer(0., this.moveXOrY) ;
				break ;
			case "no":
				this.position.deplacer(0., -this.moveXOrY) ;
				break ;
			default:
				break ;
		}
		this.nbMovement += 1 ;
		this.isTired() ;
		this.isDead() ;
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

	private void isTired()
	{
		if (this.nbMovement == this.thresholdTired)
		{
			this.color = 5 ;
		}
	}

	private void isDead()
	{
		if (this.nbMovement == this.thresholdDeath)
		{
			this.alive = false ;
		}
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

    // Cette méthode permet d'anticiper un tour à l'avance si le poisson risque de percuter un iceberg
    // ce qui explique la présence d'une addition avec sa largeur
	public boolean collidesIceberg(Iceberg2D ice)
	{
		// Pour dessiner un poisson, on part du point situé en bas à gauche et on dessine un rectangle vers la droite
		// ainsi on se ramène en quelque sorte à la méthode collision d'un iceberg avec le point en bas à gauche et en haut à droite
		
		// On test si le point bas gauche du poisson est inférieur en abscisse au coint haut droit de l'iceberg
    	boolean bas_gauche_abs = (this.position.getAbscisse() + this.largeur <= ice.coinEnHautADroite().getAbscisse()) ;

    	// On test si le point bas gauche du poisson est inférieur en ordonné au coint haut droit de l'iceberg
    	boolean bas_gauche_ord = (this.position.getOrdonnee() + this.hauteur <= ice.coinEnHautADroite().getOrdonnee()) ;

    	// On test si le point droit haut du poisson est supérieur en ordonnée au coin bas gauche de l'iceberg
    	boolean haut_droit_ord = (this.position.getOrdonnee() + 2 * this.hauteur >= ice.coinEnBasAGauche().getOrdonnee()) ;

    	// On test si le point droit haut du poisson est supérieur en abscisse au coin bas gauche de l'iceberg
    	boolean haut_droit_abs = (this.position.getAbscisse() + 2 * this.largeur >= ice.coinEnBasAGauche().getAbscisse()) ;

    	return ( bas_gauche_abs && bas_gauche_ord && haut_droit_ord && haut_droit_abs ) ;
	}

	public void changeDirection()
	{
		int alea = this.g.nextInt(10) ;

		if ( this.movement == "po" || this.movement == "no" )
		{
			this.movement = (alea % 2 == 0) ? "pa" : "na" ;
		}

		if ( this.movement == "pa" || this.movement == "na" )
		{
			this.movement = (alea % 2 == 0) ? "po" : "no" ;
		}
	}

	public void outOfSea(Ocean sea)
	{
		// On vérifie si il n'y a pas de problèmes quand on va accéder aux cases du tableau
		// Dans le cas où l'on a un depassement, on ramène le poisson en absicsse/ordonné 0
		boolean depassAbsPos = ( this.position.getAbscisse() + this.largeur >= sea.getWidth() ) ;

		// Ici, nous n'ajoutons pas widthFish étant donné qu'on se situe à l'emplacement du point exact
		// en effet, nous avons décidé de partir du point et de dessiner le rectangle vers la droite, en rajoutons
		// widthFish on risque d'avoir une coordonnée négative
		boolean depassAbsNeg = ( this.position.getAbscisse() < 0 ) ;

		boolean depassOrdPos = ( this.position.getAbscisse() + this.hauteur >= sea.getHeight() ) ;
		boolean depassOrdNeg = ( this.position.getOrdonnee() < 0 ) ;

		// Si un poisson sort de l'écran par la droite, on le ramène à gauche de l'écran
		if (depassAbsPos)
		{
			this.position.deplacer(-this.position.getAbscisse(), 0.) ;
		}

		// Si un poisson sort de l'écran par la gauche, on le ramène à droite de l'écran
		// donc à la largeur de l'écran - la valeur négative de l'abscisse - 
		// la largeur du poisson à dessiner vers la droite, - 1 à cause du tableau (éviter un dépassement mémoire)
		if (depassAbsNeg)
		{
			this.position.deplacer((double)sea.getWidth() - this.position.getAbscisse() - this.largeur - 1, 0.) ;
		}

		if (depassOrdPos)
		{
			this.position.deplacer(0., -this.position.getOrdonnee()) ;
		}

		if (depassOrdNeg)
		{
			this.position.deplacer(0., (double)sea.getHeight() - this.position.getOrdonnee() - this.hauteur - 1) ;
		}
	}

	public void getEaten(Pingouin ping)
	{
		// Même principe que la collision avec un iceberg, seul les valeurs dans les tests changent
		// car nous avons décidés de partir d'un point représentant le pingouin et de dessiner le carré vers la gauche

		// On test si le point bas gauche du poisson est inférieur en abscisse au coint haut droit du pingouin
    	boolean bas_gauche_abs = (this.position.getAbscisse() <= ping.getPosition().getAbscisse()) ;

    	// On test si le point bas gauche du poisson est inférieur en ordonné au coint haut droit du pingouin
    	boolean bas_gauche_ord = (this.position.getOrdonnee() <= ping.getPosition().getOrdonnee() + ping.getSize()) ;

    	// On test si le point droit haut du poisson est supérieur en ordonnée au coin bas gauche du pingouin
    	boolean haut_droit_ord = (this.position.getOrdonnee() + this.hauteur >= ping.getPosition().getOrdonnee()) ;

    	// On test si le point droit haut du poisson est supérieur en abscisse au coin bas gauche du pingouin
    	boolean haut_droit_abs = (this.position.getAbscisse() + this.largeur >= ping.getPosition().getAbscisse() - ping.getSize()) ;

    	if ( bas_gauche_abs && bas_gauche_ord && haut_droit_ord && haut_droit_abs )
    	{
    		this.alive = false ;
    		ping.ateFish() ;
    	}
	}
}