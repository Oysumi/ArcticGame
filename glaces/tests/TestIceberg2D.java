package glaces.tests ;

import glaces.Iceberg2D ;
import geometrie.Point ;

/* Cette classe n'a pas besoin d'une javadoc étant donné les commentaires et la clarté des noms des méthodes */

public class TestIceberg2D
{
    public static void main ( String[] args )
    {
        /* Malheureusement, on ne peut pas ordonner les tests de façon logique de par la forte dépendance au constructeur
         * qui lui même utilise certaines méthodes */

        /* Permet de tester : 
         * - la méthode largeur
         * - la méthode hauteur
         * - la méthode surface
         * - la méthode estPlusGrosQue */
    	testEstPlusGrosQueVrai() ;
        testEstPlusGrosQueFaux() ;

        /* Permet de tester :
         * - uniquement la collision */
        testCollisionVrai() ;
        testCollisionFaux() ;

        /* Permet de tester :
         * - la méthode coinEnBasAGauche
         * - la méthode coinEnHautADroite
         * - les méthodes de cassage */
        testCasserDroite() ;
        testCasserGauche() ;
        testCasserHaut() ;
        testCasserBas() ;

        /* Permet de tester :
         * - la méthode coinEnBasAGauche
         * - la méthode coinEnHautADroite
         * - la méthode Centre
         * - la méthode fondre */
        testFondre() ;

        /* Permet de tester :
         * - le constructeur par fusion */
        testConstructeurFusion() ;

        /* Permet de tester :
         * - le constructeur principal */
        testConstructeur() ;
    }

    // On test si le constructeur par défaut fonctionne
    private static void testConstructeur()
    {
        // On va tester avec des coordonnées négatives et des points mal positionnés
        Point p1 = new Point(8., -9.) ;
        Point p2 = new Point(2., -2) ;

        // Passé de cette manière, p1 est normalement le point gauche et p2 le point droit
        // mais lors de la conversion en réels positif, il y aura un problème logique
        // càd un point en bas à gauche plus grand que le point supposé être en haut à droite
        Iceberg2D ice = new Iceberg2D(p1, p2) ;

        // Maintenant, on regarde si les coordonnées sont positives et si l'inversion des points a opéré
        Point p3 = new Point(2.,2.) ;
        Point p4 = new Point(8.,9.) ;

        // On test à la fois si les coordonnées sont positives, et si le changement de point s'est fait
        boolean conversionPosAbs = ( (ice.coinEnBasAGauche().getAbscisse() >= 0) && (ice.coinEnHautADroite().getAbscisse() >= 0) ) ;
        boolean conversionPosOrd = ( (ice.coinEnBasAGauche().getOrdonnee() >= 0) && (ice.coinEnHautADroite().getOrdonnee() >= 0) ) ;

        boolean permutGauche = ( p3.equals(ice.coinEnBasAGauche()) ) ;
        boolean permutDroite = ( p4.equals(ice.coinEnHautADroite()) ) ;

        assert conversionPosAbs : "Erreur : la conversion en reel positif des abscisses n'a pas opere." ;
        assert conversionPosOrd : "Erreur : la conversion en reel positif des ordonnees n'a pas opere." ;
        assert permutGauche : "Erreur : le coin bas gauche n'est pas celui attendu." ;
        assert permutDroite : "Erreur : le coin bas droit n'est pas celui attendu." ;
    }   


    // On test si la fonction estPlusGrosQue est vrai pour certains paramètres
    private static void testEstPlusGrosQueVrai()
    {
        // Le premier iceberg est plus petit que le second
        Point p1 = new Point(0.,0.) ;
        Point p2 = new Point(1.,1.) ;

        Point p3 = new Point(0.,0.) ;
        Point p4 = new Point(40.,50.) ;

        Iceberg2D i1 = new Iceberg2D(p1,p2) ;
        Iceberg2D i2 = new Iceberg2D(p3,p4) ;

        // On recupere les coordonnees pour pouvoir tester le bon fonctionnement des méthodes
        // coinEnBasAGauche et coinEnHautADroite entre autre (mais aussi beaucoup d'autres)
        int largeurPrem = (int)p2.getAbscisse() - (int)p1.getAbscisse() ;
        int largeurSec = (int)p4.getAbscisse() - (int)p3.getAbscisse() ;
        int hauteurPrem = (int)p2.getOrdonnee() - (int)p1.getOrdonnee() ;
        int hauteurSec = (int)p4.getOrdonnee() - (int)p3.getOrdonnee() ;

        // On calcul la surface "à la main" pour pouvoir la comparer avec celle calculée par la méthode
        int surfacePrem = largeurPrem * hauteurPrem ;
        int surfaceSec = largeurSec * hauteurSec ;

        boolean hauteurFirst = ( i1.hauteur() == hauteurPrem ) ;
        boolean hauteurDeux = ( i2.hauteur() == hauteurSec ) ;
        boolean largeurFirst = ( i1.largeur() == largeurPrem ) ;
        boolean largeurDeux = ( i2.largeur() == largeurSec ) ;

        boolean surfaceFirst = ( surfacePrem == i1.surface() ) ;
        boolean surfaceDeux = ( surfaceSec == i2.surface() ) ;

        boolean plusGros = ( i2.estPlusGrosQue(i1) == (surfacePrem < surfaceSec) ) ;

        assert hauteurFirst : "Erreur : mauvais calcul de la hauteur" ;
        assert hauteurDeux : "Erreur : mauvais calcul de la hauteur" ;

        assert largeurFirst : "Erreur : mauvais calcul de la largeur" ;
        assert largeurDeux : "Erreur : mauvais calcul de la largeur" ;

        assert surfaceFirst : "Erreur : mauvais calcul de la surface" ;
        assert surfaceDeux : "Erreur : mauvais calcul de la surface." ;

        assert plusGros : "Erreur, le second argument est pourtant plus petit en taille." ;
    }

    // On test si la fonction estPlusGrosQue est faux pour certains paramètres
    private static void testEstPlusGrosQueFaux()
    {
        // Ici, le premier iceberg est plus volumineux. On recommence tous les tests.
        Point p1 = new Point(0.,0.) ;
        Point p2 = new Point(259.,745.) ;

        Point p3 = new Point(0.,0.) ;
        Point p4 = new Point(40.,50.) ;

        Iceberg2D i1 = new Iceberg2D(p1,p2) ;
        Iceberg2D i2 = new Iceberg2D(p3,p4) ;

        int largeurPrem = (int)p2.getAbscisse() - (int)p1.getAbscisse() ;
        int largeurSec = (int)p4.getAbscisse() - (int)p3.getAbscisse() ;
        int hauteurPrem = (int)p2.getOrdonnee() - (int)p1.getOrdonnee() ;
        int hauteurSec = (int)p4.getOrdonnee() - (int)p3.getOrdonnee() ;

        int surfacePrem = largeurPrem * hauteurPrem ;
        int surfaceSec = largeurSec * hauteurSec ;

        boolean hauteurFirst = ( i1.hauteur() == hauteurPrem ) ;
        boolean hauteurDeux = ( i2.hauteur() == hauteurSec ) ;
        boolean largeurFirst = ( i1.largeur() == largeurPrem ) ;
        boolean largeurDeux = ( i2.largeur() == largeurSec ) ;

        boolean surfaceFirst = ( surfacePrem == i1.surface() ) ;
        boolean surfaceDeux = ( surfaceSec == i2.surface() ) ;

        boolean plusGros = ( i2.estPlusGrosQue(i1) == (surfacePrem > surfaceSec) ) ;

        assert hauteurFirst : "Erreur : mauvais calcul de la hauteur" ;
        assert hauteurDeux : "Erreur : mauvais calcul de la hauteur" ;

        assert largeurFirst : "Erreur : mauvais calcul de la largeur" ;
        assert largeurDeux : "Erreur : mauvais calcul de la largeur" ;

        assert surfaceFirst : "Erreur : mauvais calcul de la surface" ;
        assert surfaceDeux : "Erreur : mauvais calcul de la surface." ;

        assert !plusGros : "Erreur, le premier argument est plus grand en taille." ;
    }

    // On test si la fonction collision retourne vrai pour certains paramètres
    private static void testCollisionVrai()
    {
        // On crée deux iceberg qui se touchent en (6.0, 6.0)
        Point p1 = new Point(0.,0.) ;
        Point p2 = new Point(6.,6.) ;

        Point p3 = new Point(6.,6.) ;
        Point p4 = new Point(9.,9.) ;

        Iceberg2D i1 = new Iceberg2D(p1,p2) ;
        Iceberg2D i2 = new Iceberg2D(p3,p4) ;

        boolean vrai = i1.collision(i2) ;

        assert vrai : "Erreur, il y a pourtant collision entre les deux iceberg." ;
    }

    // On test si la fonction collision retourne faux pour certains paramètres
    private static void testCollisionFaux()
    {
        // Ici, les deux icebergs ne se touchent absolument pas
        Point p1 = new Point(0.,0.) ;
        Point p2 = new Point(7.,6.) ;

        Point p3 = new Point(8.,6.) ;
        Point p4 = new Point(9.,9.) ;

        Iceberg2D i1 = new Iceberg2D(p1,p2) ;
        Iceberg2D i2 = new Iceberg2D(p3,p4) ;

        boolean faux = i1.collision(i2) ;

        assert !faux : "Erreur, il n'y a pourtant pas de collision entre les deux iceberg." ;
    }

    // On test si la méthode casserDroite fonctionne avec certains paramètres
    private static void testCasserDroite()
    {
        Iceberg2D ice = new Iceberg2D( new Point(0.,0.), new Point(10., 10.) ) ;

        // On définit de manière arbitraire les bons points à obtenir
        Point bonBasGauche = new Point(0., 0.) ;
        Point bonHautDroit = new Point(5., 10.) ;

        ice.casserDroite(1./2.) ;

        // On profite pour tester coinEnBasAGauche et coinEnHautADroite
        boolean basGauche = bonBasGauche.equals(ice.coinEnBasAGauche()) ;
        boolean basDroit = bonHautDroit.equals(ice.coinEnHautADroite()) ;
        boolean vrai = ( basGauche && basDroit ) ;

        assert basGauche : "Erreur : le point retourne par la methode coinEnBasAGauche ne fonctionne pas" ;
        assert basDroit : "Erreur : le point retourne par la methode coinEnHautADroite ne fonctionne pas" ;
        assert vrai : "Erreur : le cassage de l'iceberg a droite n'est pas bon." ;
    }

    // On test si la méthode casserGauche fonctionne ou non selon certains paramètres
    private static void testCasserGauche()
    {
        Iceberg2D ice = new Iceberg2D( new Point(0.,0.), new Point(10., 10.) ) ;

        Point bonBasGauche = new Point(5., 0.) ;
        Point bonHautDroit = new Point(10., 10.) ;

        ice.casserGauche(1./2.) ;

        // On profite pour tester coinEnBasAGauche et coinEnHautADroite
        boolean basGauche = bonBasGauche.equals(ice.coinEnBasAGauche()) ;
        boolean basDroit = bonHautDroit.equals(ice.coinEnHautADroite()) ;
        boolean vrai = ( basGauche && basDroit ) ;

        assert basGauche : "Erreur : le point retourne par la methode coinEnBasAGauche ne fonctionne pas" ;
        assert basDroit : "Erreur : le point retourne par la methode coinEnHautADroite ne fonctionne pas" ;
        assert vrai : "Erreur : le cassage de l'iceberg a gauche n'est pas bon." ;
    }

    // On test si la méthode casserHaut fonctionne ou non selon certains paramètres
    private static void testCasserHaut()
    {
        Iceberg2D ice = new Iceberg2D( new Point(0.,0.), new Point(10., 10.) ) ;

        Point bonBasGauche = new Point(0., 0.) ;
        Point bonHautDroit = new Point(10., 5.) ;

        ice.casserHaut(1./2.) ;

        // On profite pour tester coinEnBasAGauche et coinEnHautADroite
        boolean basGauche = bonBasGauche.equals(ice.coinEnBasAGauche()) ;
        boolean basDroit = bonHautDroit.equals(ice.coinEnHautADroite()) ;
        boolean vrai = ( basGauche && basDroit ) ;

        assert basGauche : "Erreur : le point retourne par la methode coinEnBasAGauche ne fonctionne pas" ;
        assert basDroit : "Erreur : le point retourne par la methode coinEnHautADroite ne fonctionne pas" ;
        assert vrai : "Erreur : le cassage de l'iceberg en haut n'est pas bon." ;
    }

    // On test si la fonction casserBas fonctionne ou non selon certains paramètres
    private static void testCasserBas()
    {
        Iceberg2D ice = new Iceberg2D( new Point(0.,0.), new Point(10., 10.) ) ;

        Point bonBasGauche = new Point(0., 5.) ;
        Point bonHautDroit = new Point(10., 10.) ;

        ice.casserBas(1./2.) ;

        // On profite pour tester coinEnBasAGauche et coinEnHautADroite
        boolean basGauche = bonBasGauche.equals(ice.coinEnBasAGauche()) ;
        boolean basDroit = bonHautDroit.equals(ice.coinEnHautADroite()) ;
        boolean vrai = ( basGauche && basDroit ) ;

        assert basGauche : "Erreur : le point retourne par la methode coinEnBasAGauche ne fonctionne pas" ;
        assert basDroit : "Erreur : le point retourne par la methode coinEnHautADroite ne fonctionne pas" ;
        assert vrai : "Erreur : le cassage de l'iceberg en bas n'est pas bon." ;
    }

    // On test si la fonction fondre fonctionne ou non selon certains paramètres
    private static void testFondre()
    {
        Iceberg2D ice = new Iceberg2D( new Point(0.,0.), new Point(10., 10.) ) ;

        Point bonBasGauche = new Point(2.5, 2.5) ;
        Point bonHautDroit = new Point(7.5, 7.5) ;
        Point bonCentre = new Point(5., 5.) ;

        ice.fondre(1./2.) ;

        // On profite pour tester coinEnBasAGauche, coinEnHautADroite et centre
        boolean basGauche = bonBasGauche.equals(ice.coinEnBasAGauche()) ;
        boolean basDroit = bonHautDroit.equals(ice.coinEnHautADroite()) ;
        boolean centre = bonCentre.equals(ice.centre()) ;
        boolean vrai = ( basGauche && basDroit && centre ) ;

        assert basGauche : "Erreur : le point retourne par la methode coinEnBasAGauche ne fonctionne pas" ;
        assert basDroit : "Erreur : le point retourne par la methode coinEnHautADroite ne fonctionne pas" ;
        assert centre : "Erreur : le centre retourne par la methode Centre ne correspond pas" ;
        assert vrai : "Erreur : la fonte de l'iceberg n'est pas bonne." ;
    }

    // On test si le constructeur par fusion fonctionne
    private static void testConstructeurFusion()
    {
        Iceberg2D ice = new Iceberg2D( new Point(0.,0.), new Point(10., 10.) ) ;
        Iceberg2D iceCol = new Iceberg2D( new Point(5.,5.), new Point(20., 22.) ) ;

        Iceberg2D fusion = new Iceberg2D(ice, iceCol) ;

        Point basGauche = new Point(0.,0.) ;
        Point hautDroit = new Point(20., 22.) ;

        boolean coinGauche = basGauche.equals(fusion.coinEnBasAGauche()) ;
        boolean coinDroit = hautDroit.equals(fusion.coinEnHautADroite()) ;

        boolean vrai = coinGauche && coinDroit ;

        assert coinGauche : "Erreur : le coin gauche n'est pas bon." ;
        assert coinDroit : "Erreur, le coin droit n'est pas bon." ;
        assert vrai : "Erreur : le constructeur par fusion ne fonctionne pas." ;
    }
}