package glaces;
import geometrie.Point ;

/**
 * Un iceberg rectangulaire
 * @author Martine Gautier, Université de Lorraine
 * @version Février 2014
 */
public class Iceberg2D {

    private Point enBasAGauche ;
    private Point enHautADroite ;
    
    /**
     * Construction par deux points g et d
     * @param g le coin en bas à gauche
     * @param d le coin en haut à droite
     * uniquement en coordonnées positives
     */
    public Iceberg2D(Point g, Point d) {

    	// Première étape : s'assurer des coordonnées positives
    	double g_absc = absol(g.getAbscisse()) ;
    	double g_ord = absol(g.getOrdonnee()) ;
    	double d_absc = absol(d.getAbscisse()) ;
    	double d_ord = absol(d.getOrdonnee()) ;

    	// On instancie deux points avec les coordonnées positives cette fois
    	Point g_prime = new Point (g_absc, g_ord) ;
    	Point d_prime = new Point (d_absc, d_ord) ;

    	// On vérifie aussi que le point en bas à gauche est bien inférieur au point en bas à droite sinon on inverse
    	Point g_final = new Point(inferieur(g_prime, d_prime)) ;
    	Point d_final = new Point(superieur(g_prime, d_prime)) ;

    	this.enBasAGauche = g_final ;
    	this.enHautADroite = d_final ;
    }
    
    /** Construction par fusion de deux icebergs qui se touchent
     * @param i1 l'iceberg venant de la gauche
     * @param i2 l'iceberg venant de la droite
     */
    public Iceberg2D(Iceberg2D i1, Iceberg2D i2) {

    	// Choix : retourner un iceberg de taille 1 situé au centre du plan cartésien si il n'y a pas de collision
        Point basGauche = new Point(0., 0.) ;
        Point hautDroit = new Point(1., 1.) ;

        if ( i1.collision(i2) )
        {   
            // Testons un certain nombre de choses :
            // 1) Quel sera le point en bas à gauche du nouvel iceberg ?
        	if (i1.coinBasGaucheInf(i2))
        	{
        		basGauche = i1.coinEnBasAGauche() ;
        	}
        	else
        	{
        		basGauche = i2.coinEnBasAGauche() ;
        	}

        	// 2) Quel sera le point en haut à droite du nouvel iceberg ?
        	if (i1.coinHautDroitSup(i2))
        	{
        		hautDroit = i1.coinEnHautADroite() ;
        	}
        	else
        	{
        		hautDroit = i2.coinEnHautADroite() ;
        	}
        }

        this.enBasAGauche = basGauche ;
        this.enHautADroite = hautDroit ;
    }
    
    /**
     * Point définissant le coin bas gauche
     * @return le coin en bas à gauche
     */
    public Point coinEnBasAGauche() {
        return this.enBasAGauche ;
    }
    
    /**
     * Point définissant le coin haut droite
     * @return le coin en haut à droite
     */
    public Point coinEnHautADroite() {
        return this.enHautADroite ;
    }
    
    
    /**
     * Hauteur de l'iceberg
     * @return hauteur
     */
    public double hauteur() {
        return (this.coinEnHautADroite().getOrdonnee() - this.coinEnBasAGauche().getOrdonnee());
    }
    
    /**
     * Largeur de l'iceberg
     * @return largeur
     */
    public double largeur() {
        return (this.coinEnHautADroite().getAbscisse() - this.coinEnBasAGauche().getAbscisse());
    }
    
    /**
     * Surface totale de l'iceberg
     * @return surface totale
     */
    public double surface() {
        return (this.largeur() * this.hauteur()) ;
    }
    
    /**
     * Permet de savoir si deux iceberg vont subir une collision
     * @param i l'iceberg avec qui il y a un risque de collision
     * @return vrai si collision entre les deux icebergs
     */
    public boolean collision(Iceberg2D i) {
    	// Pour plus de clarté, on décompose les différents tests booléens

    	// On test si le point bas gauche de l'iceberg i est inférieur en abscisse au coint haut droit du this
    	boolean bas_gauche_abs = (i.coinEnBasAGauche().getAbscisse() <= this.coinEnHautADroite().getAbscisse()) ;

    	// On test si le point bas gauche de l'icebeg i est inférieur en ordonné au coint haut droit du this
    	boolean bas_gauche_ord = (i.coinEnBasAGauche().getOrdonnee() <= this.coinEnHautADroite().getOrdonnee()) ;

    	// On test si le point droit haut de l'iceberg i est supérieur en ordonnée au coin bas gauche du this
    	boolean haut_droit_ord = (i.coinEnHautADroite().getOrdonnee() >= this.coinEnBasAGauche().getOrdonnee()) ;

    	// On test si le point droit haut de l'iceberg i est supérieur en abscisse au coin bas gauche du this
    	boolean haut_droit_abs = (i.coinEnHautADroite().getAbscisse() >= this.coinEnBasAGauche().getAbscisse()) ;

    	return ( bas_gauche_abs && bas_gauche_ord && haut_droit_ord && haut_droit_abs ) ;
    }

    /**
     * Compare le volume de deux iceberg
     * @param i l'iceberg avec qui on cherche a comparer le volume
     * @return vrai si this est plus volumineux que i
     */
    public boolean estPlusGrosQue(Iceberg2D i) {
        return (this.surface() > i.surface())  ;
    }
    
    public String toString() {
        return "Je suis un iceberg de hauteur " + this.hauteur() + " et de largeur " + this.largeur() + ". Je fais donc " +
                this.surface() + "cm carre en surface. Mon centre se trouve en " + this.centre() + ". Mon point en bas a gauche est " +
                "de coordonnees " + this.coinEnBasAGauche() + " et mon point en haut a droite " + this.coinEnHautADroite() ;
    }
    
    /**
     * Donne le centre de l'iceberg
     * @return le point au centre de l'iceberg
     */
    public Point centre() {
        return new Point( (this.coinEnHautADroite().getAbscisse() + this.coinEnBasAGauche().getAbscisse())/2., 
                          (this.coinEnHautADroite().getOrdonnee() + this.coinEnBasAGauche().getOrdonnee())/2. ) ;
    }
    
    /** Réduction dans les quatre directions ; le centre ne bouge pas
     * @param fr dans ]0..1[ facteur de réduction
     */ 
    public void fondre(double fr) {
        if ( !(fr >= 1) && !(fr <= 0) )
        {
        	/* Nous faisons fondre les 4 côtés en prenant la moitié de la largeur et de la hauteur pour éviter
        	 * que l'iceberg devienne un point/une singularité pour fr = 1/2                                   */

	        double temp_x = this.largeur()/2. * fr ;
	        double temp_y = this.hauteur()/2. * fr ;

	        // On ne peut pas utiliser les fonctions casser (réduction de la largeur/hauteur après un appel) qui modifie
	        // automatiquement le centre

	        this.coinEnHautADroite().deplacer(-temp_x, 0.0) ;
	        this.coinEnBasAGauche().deplacer(temp_x, 0.0) ;

	        this.coinEnBasAGauche().deplacer(0.0, temp_y) ;
	        this.coinEnHautADroite().deplacer(0.0, -temp_y) ;
        }	


    }
    
    /**
     * Casser une partie à droite
     * @param fr dans ]0..1[ facteur de réduction
     */
    public void casserDroite(double fr) {
        if ( !(fr >= 1) && !(fr <= 0) )
        {
        	double temp = this.largeur() * fr ;
        	this.coinEnHautADroite().deplacer(-temp, 0.0) ;
        }
    }
    
    /**
     * Casser une partie à gauche
     * @param fr dans ]0..1[ facteur de réduction
     */
    public void casserGauche(double fr) {
        if ( !(fr >= 1) && !(fr <= 0) )
        {
        	double temp = this.largeur() * fr ;
        	this.coinEnBasAGauche().deplacer(temp, 0.0) ;
        }
    }
    
    /**
     * Casser une partie en haut
     * @param fr dans ]0..1[ facteur de réduction
     */
    public void casserHaut(double fr) {
    	if ( !(fr >= 1) && !(fr <= 0) )
    	{
        	double temp = this.hauteur() * fr ;
        	this.coinEnHautADroite().deplacer(0.0, -temp) ;
        }
    }
    
   /**
     * Casser une partie en bas
     * @param fr dans ]0..1[ : définit le pourcentage supprimé
     */
    public void casserBas(double fr) {
         if ( !(fr >= 1) && !(fr <= 0) )
        {
        	double temp = this.hauteur() * fr ;
        	this.coinEnBasAGauche().deplacer(0.0, temp) ;
        }
    }

    /********************************************************************************************
     *                            FONCTIONS INTERMEDIAIRES RAJOUTEES                            *
     ********************************************************************************************/

   /**
   	 * Test si le coin en bas à gauche de l'iceberg est plus petit en abscisse que le paramètre
   	 * @param i l'iceberg avec lequel on compare le placement du point du coin gauche
   	 * @return vrai si l'iceberg qui appelle la méthode a son coin bas gauche inférieur en abscisse
   	 */
    private boolean coinBasGaucheInf(Iceberg2D i)
    {
    	return (this.coinEnBasAGauche().getAbscisse() <= i.coinEnBasAGauche().getAbscisse()) ;
    }

    /**
   	 * Test si le coin en haut à droite de l'iceberg est plus grand en ordonnée que le paramètre
   	 * @param i l'iceberg avec lequel on compare le placement du point du coin droit
   	 * @return vrai si l'iceberg qui appelle la méthode a son coin haut droit supérieur en ordonnée
   	 */
    private boolean coinHautDroitSup(Iceberg2D i)
    {
    	return (this.coinEnHautADroite().getOrdonnee() >= i.coinEnHautADroite().getOrdonnee() ) ;
    }

    // Fonction valeur absolue
    private static double absol(double d)
    {
    	double res = 0 ;
    	if (d < 0)
    	{
    		res = -d ;
    	}
    	else
    	{
    		res = d ;
    	}
    	return res ;
    }

    // Retourne le point inférieur
    private static Point inferieur(Point g, Point d)
    {
    	Point result ;

    	double infX = ( g.getAbscisse() < d.getAbscisse() ) ? g.getAbscisse() : d.getAbscisse() ;
    	double infY = ( g.getOrdonnee() < d.getOrdonnee() ) ? g.getOrdonnee() : d.getOrdonnee() ;

    	return result = new Point(infX, infY) ;
    }

    // Retourne le point supérieur
    private static Point superieur(Point g, Point d)
    {
    	Point result ;

    	double supX = ( g.getAbscisse() < d.getAbscisse() ) ? d.getAbscisse() : g.getAbscisse() ;
    	double supY = ( g.getOrdonnee() < d.getOrdonnee() ) ? d.getOrdonnee() : g.getOrdonnee() ;

    	return result = new Point(supX, supY) ;
    }
}
