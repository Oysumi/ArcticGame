package glaces ;
import geometrie.Point ;

public class Pingouin
{
    private Point position ;
    private int size ;

    // On impose un seuil de fatigue
    final private int thresholdHungry = 30 ;
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
        this.size = 0 ;
        this.nbMovement = 0 ;
        this.alive = true ;
        this.color = 2 ;
    }
    
    public Pingouin(Point pos, int size)
    {
        this.position = new Point(pos) ;
        this.size = size ;
        this.nbMovement = 0 ;
        this.alive = true ;
        this.color = 2 ;
    }
    
    /*********************************************************************************************/
    /*********************************************************************************************/

    public void deplacerAbscisse(double x)
    {
        this.position.deplacer(x, 0.) ;
        this.nbMovement += 1 ;
        this.isTired() ;
        this.isDead() ;
    }
    
    public void deplacerOrdonnee(double y)
    {
        this.position.deplacer(0., y) ;
        this.nbMovement += 1 ;
        this.isTired() ;
        this.isDead() ;
    }
    
    /*********************************************************************************************/
    /*********************************************************************************************/

    public double getAbscisse()
    {
        return this.position.getAbscisse() ;
    }   

    public double getOrdonnee()
    {
        return this.position.getOrdonnee() ;
    }
    
    public int getSize()
    {
        return this.size ;
    }
    

    public int getItsColor()
    {
        return this.color ;
    }

    public boolean isAlive()
    {
        return this.alive ;
    }

    public int howManyMovements()
    {
        return this.nbMovement ;
    }

    /*********************************************************************************************/
    /*********************************************************************************************/

    private void isTired()
    {
        if (this.nbMovement >= this.thresholdHungry)
        {
            this.color = 3 ;
            System.out.println("[!] ATTENTION : VOTRE PINGOUIN SE FATIGUE [!]") ;
        }
        else
        {
            this.color = 2 ;
        }
    }

    private void isDead()
    {
        if (this.nbMovement == this.thresholdDeath)
        {
            this.color = 7 ;
            this.alive = false ;
        }
    }

    public void ateFish()
    {
        this.nbMovement = 0 ;
    }

    public void jumpOnIceberg(Iceberg2D ice)
    {
        double facteurDeCasse = 1./10. ;

        double[] tabPos = {ice.coinEnBasAGauche().getAbscisse() , ice.coinEnBasAGauche().getOrdonnee() ,      // 0 - 1
                           ice.coinEnHautADroite().getAbscisse() , ice.coinEnHautADroite().getOrdonnee() ,    // 2 - 3
                           this.getAbscisse() - (double)this.getSize() , this.getOrdonnee() ,                 // 4 - 5
                           this.getAbscisse() , this.getOrdonnee() + (double)this.getSize()               } ; // 6 - 7

        // Ces booléens vont être utiles pour déterminer quelle partie de l'iceberg casser
        boolean gaucheAbs ;
        boolean hautGaucheOrd ;
        boolean basGaucheOrd ;

        boolean droiteAbs ;
        boolean hautDroiteOrd ;
        boolean basDroiteOrd ;

        // On test si le pingouin monte sur l'iceberg par le bas
        hautGaucheOrd = ( tabPos[7] > tabPos[1] ) ; // le test pour le coin droit est le même
        basGaucheOrd = ( tabPos[5] < tabPos[1] ) ;
        gaucheAbs = ( tabPos[4] > tabPos[0] ) ;
        droiteAbs = ( tabPos[4] < tabPos[2] ) ;

        if ( hautGaucheOrd && basGaucheOrd && gaucheAbs && droiteAbs )
        {
            ice.casserBas(facteurDeCasse) ;
            System.out.println("CASSER BAS") ;
        }
    }
    
}