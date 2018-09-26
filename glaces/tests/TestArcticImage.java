package glaces.tests ;
import glaces.ArcticImage ;
import java.util.Scanner ;

public class TestArcticImage
{
    public static void main(String[] args)
    {
        int width = 300 ;
        int height = 300 ;
        
        ArcticImage arc = new ArcticImage(width, height) ;
        
        int[][] tab = new int[300][300] ;
        
        for ( int w = 0 ; w < 300 ; w++ )
        {
            for ( int h = 0 ; h < 300 ; h++ )
            {
                if ( h % 2 == 0 )
                {
                    tab[w][h] = 1 ;
                }
                else
                {
                    tab[w][h] = 0 ;
                }
            }
        }
        
        arc.setColors(tab) ;
        Scanner scan = new Scanner(System.in) ;
        scan.nextLine() ;
        // On veut juste que la fenêtre se ferme quand l'utilisateur tape une touche
        
        arc.fermer() ;
    }
}