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

    // Ce champ décide si on gagné la partie, si on a mangé au moins la moitié de l'océan alors c'est dans la poche !
    private int nbFishEaten ;

    // Ce champ indique de combien de pixels le pingouin se déplace en sachant qu'en grossissant, il ralentit
    private double speed ;

    public Pingouin()
    {
        this.position = new Point(0.,0.) ;
        this.size = 0 ;
        this.nbMovement = 0 ;
        this.alive = true ;
        this.color = 2 ;
        this.nbFishEaten = 0 ;
        this.speed = 0 ;
    }
    
    public Pingouin(Point pos, int size)
    {
        this.position = new Point(pos) ;
        this.size = size ;
        this.nbMovement = 0 ;
        this.alive = true ;
        this.color = 2 ;
        this.nbFishEaten = 0 ;
        this.speed = 10 ;
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

    public double getSpeed()
    {
        return this.speed ;
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
        this.nbFishEaten += 1 ;
        if ( this.nbFishEaten % 7 == 0 )
        {
            this.size += 5 ;
            this.speed -= 1. ;
        }
        System.out.println("A MANGE") ;
    }

    public void jumpOnIceberg(Iceberg2D ice)
    {
        double facteurDeCasse = 1./4. ;

        if ( this.checkBottom(ice) )
            ice.casserBas(facteurDeCasse) ;

        if ( this.checkRight(ice) )
            ice.casserDroite(facteurDeCasse) ;

        if ( this.checkTop(ice) )
            ice.casserHaut(facteurDeCasse) ;

        if ( this.checkLeft(ice) )
            ice.casserGauche(facteurDeCasse) ;
    }

    public boolean ateEnough(Ocean sea)
    {
        return ( this.nbFishEaten >= sea.getNbFish() / 2 ) ;
    }
    
    /*********************************************************************************************/
    /*********************************************************************************************/

    private boolean checkBottom(Iceberg2D ice)
    {
        boolean basGaucheAbs ;
        boolean basDroitAbs ;
        boolean basGaucheOrd ;
        boolean hautDroitOrd ;

        double pingGaucheAbs = this.getAbscisse() - (double)this.getSize() ;
        double pingDroitAbs = this.getAbscisse() ;
        double pingOrdBas = this.getOrdonnee() ;
        double pingOrdHaut = pingOrdBas + (double)this.getSize() ;

        basGaucheAbs = ( pingGaucheAbs >= ice.coinEnBasAGauche().getAbscisse() ) ;
        basDroitAbs = ( pingDroitAbs <= ice.coinEnHautADroite().getAbscisse() ) ;
        basGaucheOrd = ( pingOrdBas <= ice.coinEnBasAGauche().getOrdonnee() ) ;
        hautDroitOrd = ( pingOrdHaut >= ice.coinEnBasAGauche().getOrdonnee() ) ;

        return ( basGaucheAbs && basDroitAbs && basGaucheOrd && hautDroitOrd ) ;
    }

    private boolean checkRight(Iceberg2D ice)
    {
        boolean basGaucheAbs ;
        boolean basDroitAbs ;
        boolean basGaucheOrd ;
        boolean hautDroitOrd ;

        double pingGaucheAbs = this.getAbscisse() - (double)this.getSize() ;
        double pingDroitAbs = this.getAbscisse() ;
        double pingOrdBas = this.getOrdonnee() ;
        double pingOrdHaut = pingOrdBas + (double)this.getSize() ;

        basGaucheAbs = ( pingGaucheAbs <= ice.coinEnHautADroite().getAbscisse() ) ;
        basDroitAbs = ( pingDroitAbs >= ice.coinEnHautADroite().getAbscisse() ) ;
        basGaucheOrd = ( pingOrdBas >= ice.coinEnBasAGauche().getOrdonnee() ) ;
        hautDroitOrd = ( pingOrdHaut <= ice.coinEnHautADroite().getOrdonnee() ) ;

        return ( basGaucheAbs && basDroitAbs && basGaucheOrd && hautDroitOrd ) ;
    }

    private boolean checkTop(Iceberg2D ice)
    {
        boolean basGaucheAbs ;
        boolean basDroitAbs ;
        boolean basGaucheOrd ;
        boolean hautDroitOrd ;

        double pingGaucheAbs = this.getAbscisse() - (double)this.getSize() ;
        double pingDroitAbs = this.getAbscisse() ;
        double pingOrdBas = this.getOrdonnee() ;
        double pingOrdHaut = pingOrdBas + (double)this.getSize() ;

        basGaucheAbs = ( pingGaucheAbs >= ice.coinEnBasAGauche().getAbscisse() ) ;
        basDroitAbs = ( pingDroitAbs <= ice.coinEnHautADroite().getAbscisse() ) ;
        basGaucheOrd = ( pingOrdBas <= ice.coinEnHautADroite().getOrdonnee() ) ;
        hautDroitOrd = ( pingOrdHaut >= ice.coinEnHautADroite().getOrdonnee() ) ;

        return ( basGaucheAbs && basDroitAbs && basGaucheOrd && hautDroitOrd ) ;
    }

    private boolean checkLeft(Iceberg2D ice)
    {
        boolean basGaucheAbs ;
        boolean basDroitAbs ;
        boolean basGaucheOrd ;
        boolean hautDroitOrd ;

        double pingGaucheAbs = this.getAbscisse() - (double)this.getSize() ;
        double pingDroitAbs = this.getAbscisse() ;
        double pingOrdBas = this.getOrdonnee() ;
        double pingOrdHaut = pingOrdBas + (double)this.getSize() ;

        basGaucheAbs = ( pingGaucheAbs <= ice.coinEnBasAGauche().getAbscisse() ) ;
        basDroitAbs = ( pingDroitAbs >= ice.coinEnBasAGauche().getAbscisse() ) ;
        basGaucheOrd = ( pingOrdBas >= ice.coinEnBasAGauche().getOrdonnee() ) ;
        hautDroitOrd = ( pingOrdHaut <= ice.coinEnHautADroite().getOrdonnee() ) ;

        return ( basGaucheAbs && basDroitAbs && basGaucheOrd && hautDroitOrd ) ;
    }
    
}