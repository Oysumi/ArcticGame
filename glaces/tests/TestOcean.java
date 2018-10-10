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

    }
    
    private static void testConstructeurDefaut()
    {
        // On crée un océan qui, par défaut, est de taille 300x3000 et qui contient 2 icebergs
        Ocean o = new Ocean() ;

        // On recrée cette fois un autre océan avec le second constructeur (à paramètres) avec 2 icebergs aléatoires
        Ocean oPrime = new Ocean(2, 500, 600) ;

        int nbreIceberg = 2 ;
        int width = 300 ;
        int height = 300 ;

        assert (o.getCount()==nbreIceberg):"Mauvais nombre d'icebergs" ;
        assert (o.getWidth()==width):"Mauvaise largeur" ;
        assert (o.getHeight()==height):"Mauvaise hauteur" ;

        assert (oPrime.getCount()==nbreIceberg):"Mauvais nombre d'icebergs" ;
        assert (oPrime.getWidth()==width+200):"Mauvaise largeur" ;
        assert (oPrime.getHeight()==height+300):"Mauvaise hauteur" ;

        o.fondre(2/3) ;
        oPrime.fondre(1/5) ;

        assert (o.getCount()==nbreIceberg):"Nombre d'icebergs modifié" ;
        assert (o.getWidth()==width):"Largeur modifiée" ;
        assert (o.getHeight()==height):"Hauteur modifiée" ;

        assert (oPrime.getCount()==nbreIceberg):"Nombre d'icebergs modifié" ;
        assert (oPrime.getWidth()==width+200):"Largeur modifiée" ;
        assert (oPrime.getHeight()==height+300):"Hauteur modifiée" ;
    }

    private static void testConstructeurParam()
    {
        // On crée un océan avec des paramètres précis
        int nbIceberg = 3 ;
        int width = 410 ;
        int height = 856 ;

        Ocean o = new Ocean(nbIceberg,width,height) ;

        // On test alors si l'océan a bien été crée avec les paramètres passés

        assert (o.getCount()==nbIceberg):"Mauvais nombre d'icebergs" ;
        assert (o.getWidth()==width):"Mauvaise largeur" ;
        assert (o.getHeight()==height):"Mauvaise hauteur" ;

        o.fondre(2/3) ;

        assert (o.getCount()==nbIceberg):"Nombre d'icebergs modifié" ;
        assert (o.getWidth()==width):"Largeur modifiée" ;
        assert (o.getHeight()==height):"Hauteur modifiée" ;
    }
    
    // Pour tester fondre, il faudrait un accès aux centre des icebergs ce qui est impossible
    // Pour tester getColors, il faudrait que nos constructeurs ne soient pas soumis à l'aléatoire pour la position des poissons
}