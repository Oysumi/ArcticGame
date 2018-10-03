package glaces ;
import geometrie.Point ;

/**
 * Représentation d'un pingouin dans un océan
 * @author Faucher Aurélien
 * @version Octobre 2018
 */

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

    /**
     * Construction selon un point indiquant une position de spawn et un entier représentant la taille du pingouin
     * @param pos point représentant la position du pingouin
     * @param size taille du pingouin (représenté par un carré)
     */
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

    /**
     * Méthode de déplacement sur les abscisses
     * @param x déplacement à effectuer sur les abscisses
     */
    public void deplacerAbscisse(double x)
    {
        this.position.deplacer(x, 0.) ;
        this.nbMovement += 1 ;
        this.isTired() ;
        this.isDead() ;
    }
    
    /**
     * Méthode de déplacement sur les ordonnées
     * @param y déplacement à effectuer sur les ordonnées
     */
    public void deplacerOrdonnee(double y)
    {
        this.position.deplacer(0., y) ;
        this.nbMovement += 1 ;
        this.isTired() ;
        this.isDead() ;
    }
    
    /*********************************************************************************************/
    /*********************************************************************************************/

    /**
     * Réel de l'abscisse du pingouin
     * @return l'abscisse du pingouin
     */
    public double getAbscisse()
    {
        return this.position.getAbscisse() ;
    }   

    /**
     * Réel de l'ordonnée du pingouin
     * @return l'ordonnée du pingouin
     */
    public double getOrdonnee()
    {
        return this.position.getOrdonnee() ;
    }
    
    /**
     * Entier représentant la taille du pingouin
     * @return taille du pingouin
     */
    public int getSize()
    {
        return this.size ;
    }
    
    /**
     * Entier correspondant à la couleur du pingouin
     * @return couleur du pingouin
     */
    public int getItsColor()
    {
        return this.color ;
    }

    /**
     * Booléen qui décrit l'état de vie du pingouin
     * @return si le pingouin est vivant/mort
     */
    public boolean isAlive()
    {
        return this.alive ;
    }

    /**
     * Combien de mouvements le pingouin a-t-il effectué ?
     * @return nombre de mouvements effectués depuis le début du jeu
     */
    public int howManyMovements()
    {
        return this.nbMovement ;
    }

    /**
     * Vitesse de déplacement du pingouin
     * @return vitesse du pingouin
     */
    public double getSpeed()
    {
        return this.speed ;
    }

    /*********************************************************************************************/
    /*********************************************************************************************/

    /**
     * Méthode qui change la couleur du pingouin s'il est fatigué
     */
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

    /**
     * Méthode qui change la couleur/l'état de vie du pingouin s'il est mort de faim
     */
    private void isDead()
    {
        if (this.nbMovement == this.thresholdDeath)
        {
            this.color = 7 ;
            this.alive = false ;
        }
    }

    /**
     * Méthode qui modifie la taille/vitesse du pingouin s'il mange un poisson
     */
    public void ateFish()
    {
        this.nbMovement = 0 ;
        this.nbFishEaten += 1 ;
        if ( this.nbFishEaten % 10 == 0 )
        {
            // On fait grossir notre pingouin et on réduit sa vitesse
            this.size += 5 ;
            this.speed -= 1. ;
        }
    }

    /**
     * Méthode qui vérifie si le pingouin monte sur un iceberg
     * @param ice l'iceberg sur lequel le pingouin monte
     */
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

    /**
     * Est-ce que le pingouin a assez mangé pour avoir gagné le jeu ?
     * @param sea océan sur lequel le pingouin se déplace
     * @return un booléen indiquant si le pingouin a mangé à sa faim
     */
    public boolean ateEnough(Ocean sea)
    {
        return ( this.nbFishEaten >= sea.getNbFish() / 2 ) ;
    }
    
    /*********************************************************************************************/
    /*********************************************************************************************/

    /**
     * Vérifie si le pingouin monte sur un iceberg par la partie du bas
     * @param ice iceberg concerné par le pingouin
     */
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

    /**
     * Vérifie si le pingouin monte sur un iceberg par la partie droite
     * @param ice iceberg concerné par le pingouin
     */
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

    /**
     * Vérifie si le pingouin monte sur un iceberg par la partie du haut
     * @param ice iceberg concerné par le pingouin
     */
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

    /**
     * Vérifie si le pingouin monte sur un iceberg par la partie gauche
     * @param ice iceberg concerné par le pingouin
     */
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