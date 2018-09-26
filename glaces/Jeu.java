package glaces ;
import java.util.Scanner ;

public class Jeu
{   
    public Jeu()
    { } 
    
    public void jouer()
    {
        ArcticImage image = new ArcticImage(500,500) ;
        Ocean sea = new Ocean(4,500,500) ;
        image.setColors(sea.getColors()) ;

        Scanner scan = new Scanner(System.in) ;
        String choix ;
        boolean run = true ;

        final double posX = 11. ;
        final double posY = 19. ;

        System.out.println("---------------------") ;
        System.out.println("     JEU PINGOUIN    ") ;
        System.out.println("---------------------") ;

        while (run)
        {
            System.out.println("------------------------------------") ;
            System.out.println("Pressez Z / z pour avancer") ;
            System.out.println("Pressez Q / q pour aller a gauche") ;
            System.out.println("Pressez S / s pour reculer") ;
            System.out.println("Pressez D / d pour aller a droite\n") ;

            choix = scan.nextLine() ;
            run = false ;

            switch (choix)
            {
                case "Z":
                    if ( sea.getPing().getPosition().getOrdonnee() + sea.getPing().getSize() + posY < sea.getHeight() )
                    {
                        sea.getPing().deplacerOrdonnee(posY) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;
                case "z":
                    if ( sea.getPing().getPosition().getOrdonnee() + sea.getPing().getSize() + posY < sea.getHeight() )
                    {
                        sea.getPing().deplacerOrdonnee(posY) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;

                case "Q":
                    if ( sea.getPing().getPosition().getAbscisse() - sea.getPing().getSize() - posX > 0 )
                    {
                        sea.getPing().deplacerAbscisse(-posX) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;
                case "q":
                    if ( sea.getPing().getPosition().getAbscisse() - sea.getPing().getSize() - posX > 0 )
                    {
                        sea.getPing().deplacerAbscisse(-posX) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;

                case "S":
                    if ( sea.getPing().getPosition().getOrdonnee() > 0 )
                    {
                        sea.getPing().deplacerOrdonnee(-posY) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;
                case "s":
                    if ( sea.getPing().getPosition().getOrdonnee() > 0 )
                    {
                        sea.getPing().deplacerOrdonnee(-posY) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;

                case "D":
                    if ( sea.getPing().getPosition().getAbscisse() < sea.getWidth() )
                    {
                        sea.getPing().deplacerAbscisse(posX) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;
                case "d":
                    if ( sea.getPing().getPosition().getAbscisse() < sea.getWidth() )
                    {
                        sea.getPing().deplacerAbscisse(posX) ;
                    }
                    else
                    {
                        System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
                    }
                    run = true ;
                    break ;

                default:
                    break ;
            }
            image.setColors(sea.getColors()) ;
        }

        image.fermer() ;
        
    }
    
    public static void main(String[] args)
    {
        Jeu myGame = new Jeu() ;
        myGame.jouer() ;
    }
}