package glaces ;
import java.util.Scanner ;

public class Jeu
{   
    // CONSTRUCTEUR INUTILE
    public Jeu()
    { } 
    
    // METHODES DE DEPLACEMENT
    public void moveForward(Ocean sea, double posY)
    {
        if ( sea.getPing().getPosition().getOrdonnee() + sea.getPing().getSize() + posY < sea.getHeight() )
        {
            sea.getPing().deplacerOrdonnee(posY) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    public void moveBackward(Ocean sea, double posY)
    {
        if ( sea.getPing().getPosition().getOrdonnee() > 0 )
        {
            sea.getPing().deplacerOrdonnee(-posY) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    public void moveRight(Ocean sea, double posX)
    {
        if ( sea.getPing().getPosition().getAbscisse() < sea.getWidth() )
        {
            sea.getPing().deplacerAbscisse(posX) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    public void moveLeft(Ocean sea, double posX)
    {
        if ( sea.getPing().getPosition().getAbscisse() - sea.getPing().getSize() - posX > 0 )
        {
            sea.getPing().deplacerAbscisse(-posX) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    // METHODE DE JEU
    public void jouer()
    {
        Jeu game = new Jeu() ;
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
                // AVANCER LE PINGOUIN
                case "Z":
                    game.moveForward(sea, posY) ;
                    run = true ;
                    break ;
                case "z":
                    game.moveForward(sea, posY) ;
                    run = true ;
                    break ;

                // DEMANDER AU PINGOUIN D'ALLER A GAUCHE
                case "Q":
                    game.moveLeft(sea, posX) ;
                    run = true ;
                    break ;
                case "q":
                    game.moveLeft(sea, posX) ;
                    run = true ;
                    break ;

                // FAIRE RECULER LE PINGOUIN
                case "S":
                    game.moveBackward(sea, posY) ;
                    run = true ;
                    break ;
                case "s":
                    game.moveBackward(sea, posY) ;
                    run = true ;
                    break ;

                // DEMANDER AU PINGOUIN D'ALLER A DROITE
                case "D":
                    game.moveRight(sea, posX) ;
                    run = true ;
                    break ;
                case "d":
                    game.moveRight(sea, posX) ;
                    run = true ;
                    break ;

                default:
                    break ;
            }
            if (run)
            {
                sea.moveFish() ;
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