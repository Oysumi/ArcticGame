package glaces ;
import geometrie.Point ;
import java.util.Random ;

/**
 * Représentation d'un poisson dans un océan
 * @author Faucher Aurélien
 * @version Octobre 2018
 */

public class Poisson
{
	private Point position ;
	private int hauteur ;
	private int largeur ;
	private String movement ;

	// Déplacement en abscisse ou ordonnée commun à tous les poissons
	private final static double moveXOrY = 2. ;

	// On genère un seuil de déplacement auquel au delà de ce dernier, le poisson est considéré comme fatigué / mort
	private Random g = new Random() ;
	final private int thresholdTired = 90 + g.nextInt(21) ;
	final private int thresholdDeath = 180 + g.nextInt(21) ;

	// Ces champs indiqueront la jauge de fatigue du poisson (sa couleur dépend du nombre de mouvement)
	// Couleur : [4 : normal] [5 : fatigué]
	private int nbMovement ;
	private int color ;

	// Ce champ explicite indique si le poisson est vivant ou mort (ainsi facilite son affichage ou non)
	private boolean alive ;

	/**
	 * Construction par une position, une largeur et une hauteur
	 * @param pos point définissant la position du poisson
	 * @param height hauteur du poisson
	 * @param width largeur du poisson
	 * la largeur et la hauteur doivent être positives
	 * @exception AssertionError si largeur ou hauteur négative ou nulle
	 */
	public Poisson(Point pos, int height, int width)
	{
		assert(height>0):"Erreur : hauteur négative ou nulle" ;
		assert(width>0):"Erreur : largeur négative ou nulle" ;
		
		this.position = new Point(pos) ;
		this.nbMovement = 0 ;
		this.color = 4 ;
		this.alive = true ;

		// On produit un nombre compris entre 0 et 3 et selon ce dernier, on définit arbitrairement son sens de déplacement
		int alea = this.g.nextInt(4) ;
		switch (alea)
		{
			/* Petit plus, si un poisson bouge horizontalement, on le représente "couché" et réciproquement
			 * s'il se déplace verticalement, on le représente debout. Ainsi, selon son sens de déplacement
			 * et partant du fait qu'on appelle ce constructeur avec height > width, on inverse quand nécessaire */

			case 0:
				this.movement = "pa" ; // déplacement positif en abscisse

				// On inverse hauteur et largeur pour donner une cohérence dans la forme du poisson par rapport à son déplacement
				this.hauteur = width ;
				this.largeur = height ;
				break ;
			case 1:
				this.movement = "na" ; // déplacement négatif en abscisse

				// On inverse hauteur et largeur pour donner une cohérence dans la forme du poisson par rapport à son déplacement
				this.hauteur = width ;
				this.largeur = height ;
				break ;
			case 2:
				this.movement = "po" ; // déplacement positif en ordonnée
				this.hauteur = height ;
				this.largeur = width ;
				break ;
			case 3:
				this.movement = "no" ; // déplacement négatif en ordonnée
				this.hauteur = height ;
				this.largeur = width ;
				break ;
			default:
				break ;
		}
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

    /**
     * Réel de l'abscisse du poisson
     * @return l'abscisse du poisson
     */
	public double getAbscisse()
	{
		return this.position.getAbscisse() ;
	}

	/**
	 * Réel de l'ordonnée du poisson
	 * @return l'ordonnée du poisson
	 */
	public double getOrdonnee()
	{
		return this.position.getOrdonnee() ;
	}

	/**
	 * Entier représentant la largeur du poisson
	 * @return largeur du poisson
	 */
	public int getWidth()
	{
		return this.largeur ;
	}

	/**
	 * Entier représentant la hauteur du poisson
	 * @return hauteur du poisson
	 */
	public int getHeight()
	{
		return this.hauteur ;
	}

	/**
	 * Chaîne de caractères qui décrit le mouvement du poisson
	 * @return le type de mouvement du poisson
	 */
	public String getMov()
	{
		return this.movement ;
	}

	/**
	 * Entier représentant la couleur de poisson
	 * @return couleur du poisson
	 */
	public int getItsColor()
	{
		return this.color ;
	}

	/**
	 * Booléen indiquant si le poisson est vivant ou mort
	 * @return si le poisson est vivant ou non
	 */
	public boolean isAlive()
	{
		return this.alive ;
	}

	/*************************************************************************************************************/
	/*************************************************************************************************************/

	/**
	 * Méthode de déplacement du poisson selon son sens de mouvement
	 */
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

    /**
     * Méthode qui change ou non la couleur du poisson selon son état de fatigue
     */
	private void isTired()
	{
		if (this.nbMovement == this.thresholdTired)
		{
			this.color = 5 ;
		}
	}

	/**
	 * Méthode qui fixe l'état de vie du poisson à mort si besoin
	 */
	private void isDead()
	{
		if (this.nbMovement == this.thresholdDeath)
		{
			this.alive = false ;
		}
	}

	/*********************************************************************************************/
    /*********************************************************************************************/

    /**
     * Cette méthode permet d'anticiper un tour à l'avance si le poisson risque de percuter un iceberg
     * ce qui explique la présence d'une addition de son abscisse avec sa largeur et respectivement de
     * son ordonnée avec sa hauteur
     * @param ice iceberg sur lequel on test la collision
     * @return un booléen qui dit si oui ou non le poisson percute l'iceberg
     */
	public boolean collidesIceberg(Iceberg2D ice)
	{
		// Pour dessiner un poisson, on part du point situé en bas à gauche et on dessine un rectangle vers la droite
		// ainsi on se ramène en quelque sorte à la méthode collision d'un iceberg avec le point en bas à gauche et en haut à droite
		
		double sens = ( this.movement == "po" || this.movement == "pa" ) ? 1. : -1. ;

		// On test si le point bas gauche du poisson est inférieur en abscisse au coint haut droit de l'iceberg
    	boolean bas_gauche_abs = (this.getAbscisse() + sens * this.largeur <= ice.coinEnHautADroite().getAbscisse()) ;

    	// On test si le point bas gauche du poisson est inférieur en ordonné au coint haut droit de l'iceberg
    	boolean bas_gauche_ord = (this.getOrdonnee() + sens * this.hauteur <= ice.coinEnHautADroite().getOrdonnee()) ;

    	// On test si le point droit haut du poisson est supérieur en ordonnée au coin bas gauche de l'iceberg
    	boolean haut_droit_ord = (this.getOrdonnee() + 2 * sens * this.hauteur >= ice.coinEnBasAGauche().getOrdonnee()) ;

    	// On test si le point droit haut du poisson est supérieur en abscisse au coin bas gauche de l'iceberg
    	boolean haut_droit_abs = (this.getAbscisse() + 2 * sens * this.largeur >= ice.coinEnBasAGauche().getAbscisse()) ;

    	return ( bas_gauche_abs && bas_gauche_ord && haut_droit_ord && haut_droit_abs ) ;
	}

	/**
	 * Méthode qui permet de changer le mouvement du poisson s'il rencontre un iceberg
	 * et qui redéfinit ses dimensions selon son mouvement
	 */
	public void changeDirection()
	{
		int alea = this.g.nextInt(10) ;

		if ( this.movement == "po" || this.movement == "no" )
		{
			this.movement = (alea % 2 == 0) ? "pa" : "na" ;
		}

		else if ( this.movement == "pa" || this.movement == "na" )
		{
			this.movement = (alea % 2 == 0) ? "po" : "no" ;
		}

		// On oublie pas d'inverser la largeur et la hauteur pour la représentation du sens de déplacement
		int temp = this.largeur ;
		this.largeur = this.hauteur ;
		this.hauteur = temp ;
	}

	/**
	 * Méthode qui vérifie si un poisson sort de l'écran ou non et le remet dans le droit chemin
	 * @param sea océan dans lequel le poisson se situe
	 */
	public void outOfSea(Ocean sea)
	{
		// On vérifie si il n'y a pas de problèmes quand on va accéder aux cases du tableau
		// Dans le cas où l'on a un depassement, on ramène le poisson en absicsse/ordonné 0
		boolean depassAbsPos = ( this.getAbscisse() + this.largeur >= sea.getWidth() ) ;

		// Ici, nous n'ajoutons pas widthFish étant donné qu'on se situe à l'emplacement du point exact
		// en effet, nous avons décidé de partir du point et de dessiner le rectangle vers la droite, en rajoutons
		// widthFish on risque d'avoir une coordonnée négative
		boolean depassAbsNeg = ( this.getAbscisse() < 0 ) ;

		boolean depassOrdPos = ( this.getOrdonnee() + this.hauteur >= sea.getHeight() ) ;
		boolean depassOrdNeg = ( this.getOrdonnee() < 0 ) ;

		// Si un poisson sort de l'écran par la droite, on le ramène à gauche de l'écran
		if (depassAbsPos)
		{
			this.position.deplacer(-this.getAbscisse(), 0.) ;
		}

		// Si un poisson sort de l'écran par la gauche, on le ramène à droite de l'écran
		// donc à la largeur de l'écran - la valeur négative de l'abscisse - 
		// la largeur du poisson à dessiner vers la droite, - 1 à cause du tableau (éviter un dépassement mémoire)
		if (depassAbsNeg)
		{
			this.position.deplacer((double)sea.getWidth() - this.getAbscisse() - this.largeur - 1, 0.) ;
		}

		if (depassOrdPos)
		{
			this.position.deplacer(0., -this.getOrdonnee()) ;
		}

		if (depassOrdNeg)
		{
			this.position.deplacer(0., (double)sea.getHeight() - this.getOrdonnee() - this.hauteur - 1) ;
		}
	}

	/**
	 * Méthode qui vérifie si le poisson a été mangé par le pingouin et dans ce cas, on le passe à mort
	 * et on appelle une méthode du pingouin qui incrémente son compteur de poisson mangé
	 * @param ping pingouin qui risque d'avoir mangé le poisson
	 */
	public void getEaten(Pingouin ping)
	{
		// On test si le poisson est encore vivant, car une fois mort il est toujours présent dans l'océan mais n'est juste
		// plus affiché, il est probable qu'un poisson mort rentre en collision avec le pingouin même s'il n'est plus affiché
		if (this.alive)
		{	// Même principe que la collision avec un iceberg, seul les valeurs dans les tests changent
			// car nous avons décidés de partir d'un point représentant le pingouin et de dessiner le carré vers la gauche

			// On test si le point bas gauche du poisson est inférieur en abscisse au coint haut droit du pingouin
    		boolean bas_gauche_abs = (this.getAbscisse() <= ping.getAbscisse()) ;

    		// On test si le point bas gauche du poisson est inférieur en ordonné au coint haut droit du pingouin
    		boolean bas_gauche_ord = (this.getOrdonnee() <= ping.getOrdonnee() + ping.getSize()) ;

    		// On test si le point droit haut du poisson est supérieur en ordonnée au coin bas gauche du pingouin
    		boolean haut_droit_ord = (this.getOrdonnee() + this.hauteur >= ping.getOrdonnee()) ;

    		// On test si le point droit haut du poisson est supérieur en abscisse au coin bas gauche du pingouin
    		boolean haut_droit_abs = (this.getAbscisse() + this.largeur >= ping.getAbscisse() - ping.getSize()) ;

    		if ( bas_gauche_abs && bas_gauche_ord && haut_droit_ord && haut_droit_abs )
    		{
    			this.alive = false ;
    			ping.ateFish() ;
    		}
    	}
	}
}