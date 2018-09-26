package glaces.tests ;

import glaces.Ocean ;
import geometrie.Point ;
import glaces.Iceberg2D ;

public class TestOcean
{
    public static void main(String[] args)
    {
        /* Dans le test du constructeur, on test aussi la méthode getCount, getWidth, getHeight et la bonne
         *  mise à jour de la variable "nbIcebergInit" qui permet de savoir combien d'Icebergs ont été
         * instanciés dans le tableau */
        testConstructeurDefaut() ;

        /* Cette fois ci, on test le second constructeur avec paramètres avec les mêmes tests que le précédent */
        testConstructeurParam() ;

        /* On test la méthode fondre en comparant les centres, mais aussi la méthode getIceberg qui retourne
         * le tableau d'icebergs de l'ocean ainsi que setIceberg */
        testFondre() ;

        /* On test la méthode getColors */
        testGetColors() ;
    }
    
    private static void testConstructeurDefaut()
    {
        // On crée un océan qui, par défaut, est de taille 300x3000 et qui contient 2 icebergs
        Ocean o = new Ocean() ;

        // On recrée cette fois un autre océan avec le second constructeur (à paramètres) avec 2 icebergs aléatoires
        Ocean oPrime = new Ocean(2, 500, 600) ;

        int nbreIceberg = 4 ;
        int width = 300 ;
        int height = 300 ;
        
        boolean resultNbIceberg = ( nbreIceberg == o.getCount() ) ;
        boolean resultWidth = ( width == o.getWidth() ) ;
        boolean resultHeight = ( height == o.getHeight() ) ;
        
        assert resultNbIceberg : "Erreur : le nombre d'iceberg a change." ;
        assert resultWidth : "Erreur : la largeur de l'ocean est modifiee" ;
        assert resultHeight : "Erreur : la hauteur de l'ocean est modifiee" ;
    }

    private static void testConstructeurParam()
    {
        // On crée un océan avec des paramètres précis
        int nbIceberg = 3 ;
        int width = 410 ;
        int height = 856 ;

        Ocean o = new Ocean(nbIceberg,width,height) ;

        // On test alors si l'océan a bien été crée avec les paramètres passés
        // Cela permet ainsi de tester de nouveau les méthodes width, height, getCount
        boolean largeur = ( o.getWidth() == width ) ;
        boolean hauteur = ( o.getHeight() == height ) ;
        boolean iceberg = ( o.getCount() == nbIceberg ) ;

        assert largeur : "Erreur : la largeur de l'ocean est modifiee" ;
        assert hauteur : "Erreur : la hauteur de l'ocean est modifiee" ;
        assert iceberg : "Erreur : l'ocean ne possede pas le bon nombre d'iceberg" ;
    }
    
    private static void testFondre()
    {
        // Pour tester fondre, nous allons comparer les centres des icebergs sur l'océan
        Ocean sea = new Ocean(2, 300, 300) ;
        
        // On déclare ainsi les points pour tester la fonction getIceberg en comparant le tableau obtenu avec ceux-ci
        Point firstBasGauche = new Point(5.,5.) ;
        Point firstHautDroit = new Point(15.,15.) ;

        Point secBasGauche = new Point(100.,100.) ;
        Point secHautDroit = new Point(200.,200.) ;

        // On instancie deux icebergs qui seront présents sur l'océan
        Iceberg2D iFirst = new Iceberg2D(firstBasGauche, firstHautDroit) ;
        Iceberg2D iSec = new Iceberg2D(secBasGauche, secHautDroit) ;
        
        // On test la méthode setIceberg (et getIceberg par la même occasion)
        sea.setIceberg(iFirst) ;
        sea.setIceberg(iSec) ;
        
        Iceberg2D[] icebergs = sea.getIceberg() ;

        // getIceberg : on test l'égalité des points d'iceberg du tableau retourné
        // On test d'abord le premier iceberg
        boolean firstCoinGauche = ( icebergs[0].coinEnBasAGauche().equals(firstBasGauche)) ;
        boolean firstCoinDroit = ( icebergs[0].coinEnHautADroite().equals(firstHautDroit)) ;

        assert firstCoinGauche : "Erreur : la méthode getIceberg ne retourne pas le bon tableau d'iceberg" ;
        assert firstCoinDroit : "Erreur : la méthode getIceberg ne retourne pas le bon tableau d'iceberg" ;

        // Ensuite le second iceberg
        boolean secCoinGauche = ( icebergs[1].coinEnBasAGauche().equals(secBasGauche)) ;
        boolean secCoinDroit = ( icebergs[1].coinEnHautADroite().equals(secHautDroit)) ;

        assert secCoinGauche : "Erreur : la méthode getIceberg ne retourne pas le bon tableau d'iceberg" ;
        assert secCoinDroit : "Erreur : la méthode getIceberg ne retourne pas le bon tableau d'iceberg" ;

        // On va tester la fonction fondre en comparant les centres et si les tailles sont bien différentes
        Point premierCentre = new Point(icebergs[0].centre()) ;
        Point deuxCentre = new Point(icebergs[1].centre()) ;
        
        // On récupère déjà la hauteur et la largeur de chaque iceberg avant l'appel à fondre
        double firstHauteur = icebergs[0].hauteur() ;
        double firstLargeur = icebergs[0].largeur() ;
        double secHauteur = icebergs[1].hauteur() ;
        double secLargeur = icebergs[1].largeur() ;

        // On appelle fondre et on réactualise le tableau d'iceberg en rappelant la méthode getIceberg
        sea.fondre(1./2.) ;
        icebergs = sea.getIceberg() ;

        boolean fondreFirst = ( (firstHauteur != icebergs[0].hauteur()) && (firstLargeur != icebergs[0].largeur()) ) ;
        boolean fondreSec = ( (secHauteur != icebergs[1].hauteur()) && (secLargeur != icebergs[1].largeur()) ) ;
        
        assert fondreFirst : "Erreur : fondre ne fonctionne pas car même hauteur ou largeur." ;
        assert fondreSec : "Erreur : fondre ne fonctionne pas car même hauteur ou largeur" ;

        boolean bonCentreFirst = premierCentre.equals(icebergs[0].centre()) ;
        boolean bonCentreDeux = deuxCentre.equals(icebergs[1].centre()) ;
        boolean resultCentre = bonCentreFirst && bonCentreDeux ;
        
        assert resultCentre : "Erreur : le milieu des icebergs a bouge donc fondre ne marche pas" ;

    }

    
    public static void testGetColors()
    {
        /* Ce test manque d'objectivité car basé sur un tableau pré-défini */
        int width = 20 ;
        int height = 20 ;
        int nbIceberg = 2 ;

        /* Commençons par créer un ocean et à déposer des icebergs dessus */
        /* Vu la complexité du test : on initalise un ocean de petite taille */
        Ocean sea = new Ocean(nbIceberg, width, height) ;
        Iceberg2D iFirst = new Iceberg2D (new Point(0.,0.), new Point(5.,7.)) ;
        Iceberg2D iSec = new Iceberg2D (new Point(9.,15.), new Point(20.,20.)) ;

        sea.setIceberg(iFirst) ;
        sea.setIceberg(iSec) ;

        /* On remplit arbitrairement un tableau qui permettra de comparer les valeurs */
        int[][] comparaison = new int[height][width] ;

        for ( int i = 0 ; i < height ; i++ )
        {
            for ( int j = 0 ; j < width ; j++ )
            {
                if ( (j < 5) && (i < 7) )
                {
                    comparaison[i][j] = 1 ;
                }
                else if ( (j >= 9) && (i >=15) )
                {
                    comparaison[i][j] = 1 ;
                }
                else
                {
                    comparaison[i][j] = 0 ;
                }
            }
        }
        
        /* On récupère le tableau de la méthode getColors pour faire une comparaison colonne par colonne, ligne par ligne */
        int[][] tabReturn = sea.getColors() ;

        /* On introduit un flag */
        boolean val = true ; 

        int i = 0 ;
        int j = 0 ;
        while ( (i < height) && val )
        {
            while ( (j < width) && val )
            {
                if ( tabReturn[i][j] != comparaison[i][j] )
                {
                    System.out.println(i + " " + j) ;
                    val = false ;
                }
                j++ ;
            }
            i++ ;
            j = 0 ;
        }

        assert val : "Erreur : le tableau retourne par getColors ne correspond pas." ;
    }
}