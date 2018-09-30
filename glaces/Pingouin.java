package glaces ;
import geometrie.Point ;

public class Pingouin
{
    private Point position ;
    private int size ;

    // On impose un seuil de fatigue
    final private int thresholdHungry = 20 ;
    final private int thresholdDeath = 50 ;

    // Ce champ permettra d'indiquer la jauge de faim en fonction du nombre de déplacement 
    private int nbMovement ;

    // Ce champ indique si notre poisson est vivant ou non, et permet alors de signer la fin de la partie
    private boolean alive ;

    // Couleur : [2: normal] [3: fatigué]
    private int color ;

    public Pingouin()
    {
        this.position = new Point(0.,0.) ;
        this.setSize(0) ;
        this.nbMovement = 0 ;
        this.alive = true ;
        this.color = 2 ;
    }
    
    public Pingouin(Point pos, int size)
    {
        this.position = new Point(pos) ;
        this.setSize(size) ;
        this.nbMovement = 0 ;
        this.alive = true ;
        this.color = 2 ;
    }
    
    private void setSize(int size)
    {
        this.size = size ;
    }
    
    public void deplacerAbscisse(double x)
    {
        this.position.deplacer(x, 0.) ;
        this.nbMovement += 1 ;
    }
    
    public void deplacerOrdonnee(double y)
    {
        this.position.deplacer(0., y) ;
        this.nbMovement += 1 ;
        this.isTired() ;
    }
    
    public Point getPosition()
    {
        return this.position ;
    }   
    
    public int getSize()
    {
        return this.size ;
    }
    
    public boolean isAlive()
    {
        return this.alive ;
    }

    public int getItsColor()
    {
        return this.color ;
    }

    private void isTired()
    {
        if (this.nbMovement >= this.thresholdHungry)
        {
            this.color = 3 ;
        }
        else
        {
            this.color = 2 ;
        }
    }

    public void ateFish()
    {
        this.nbMovement = 0 ;
    }

}