package glaces ;
import geometrie.Point ;
import java.util.Random ;

public class Poisson
{
	private Point position ;
	private int hauteur ;
	private int largeur ;
	private String movement ;

	// On genère un seuil de déplacement auquel au delà de ce dernier, le poisson est considéré comme fatigué / mort
	Random g = new Random() ;
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

		Random g = new Random() ;

		// On produit un nombre compris entre 0 et 3 et selon ce dernier, on définit arbitrairement son sens de déplacement
		int alea = g.nextInt(4) ;
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

	public void deplacement(double pos)
	{
		switch (this.movement)
		{
			case "pa":
				this.position.deplacer(pos, 0.) ;
				break ;
			case "na":
				this.position.deplacer(-pos, 0.) ;
				break ;
			case "po":
				this.position.deplacer(0., pos) ;
				break ;
			case "no":
				this.position.deplacer(0., -pos) ;
				break ;
			default:
				break ;
		}
		this.nbMovement += 1 ;
		this.isTired() ;
		this.isDead() ;
	}

	public boolean isAlive()
	{
		return this.alive ;
	}

	public int getItsColor()
	{
		return this.color ;
	}

	public void getEaten()
	{
		this.alive = false ;
	}

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

}