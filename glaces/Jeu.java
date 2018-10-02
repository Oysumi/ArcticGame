package glaces ;
import java.util.Scanner ;

public class Jeu
{   
    // CONSTRUCTEUR INUTILE
    public Jeu()
    { } 
    
    /*********************************************************************************************/
    /*********************************************************************************************/

    // METHODES DE DEPLACEMENT
    public void moveForward(Ocean sea, double posY)
    {
        if ( sea.getPing().getOrdonnee() + sea.getPing().getSize() + posY < sea.getHeight() )
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
        if ( sea.getPing().getOrdonnee() > 0 )
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
        if ( sea.getPing().getAbscisse() < sea.getWidth() )
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
        if ( sea.getPing().getAbscisse() - sea.getPing().getSize() - posX > 0 )
        {
            sea.getPing().deplacerAbscisse(-posX) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    /*********************************************************************************************/
    /*********************************************************************************************/

    // Méthode d'affichage de texte
    public void printRules()
    {
        System.out.println("----------------------") ;
        System.out.println("     JEU PINGOUIN    ") ;
        System.out.println("----------------------\n") ;

        System.out.println("********************************************") ;
        System.out.println(" Règles : Vous contrôlez un pingouin qui") ;
        System.out.println(" se déplace sur un océan d'icebergs et") ;
        System.out.println(" de poissons. Vous devez manger tous les") ;
        System.out.println(" poissons avant que votre pingouin meurt") ;
        System.out.println(" de faim ou que les icebergs fondent tous.") ;
        System.out.println("********************************************\n") ;
    }

    public void printControls()
    {
        System.out.println("------------------------------------") ;
        System.out.println("Pressez Z / z pour avancer") ;
        System.out.println("Pressez Q / q pour aller a gauche") ;
        System.out.println("Pressez S / s pour reculer") ;
        System.out.println("Pressez D / d pour aller a droite\n") ;
    }

    /*********************************************************************************************/
    /*********************************************************************************************/
    
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

        // Facteurs de déplacement
        final double posX = 11. ;
        final double posY = 19. ;

        // Ce compteur permet de savoir quand il faut faire fondre les icebergs
        int nbTour = 0 ;
        final int fondreSeuil = 10 ;
        final double fondreQuotient = 1./10. ;

        printRules() ;

        while (run)
        {
            printControls() ;

            System.out.println("tour numéro " + nbTour) ;

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

            // On incrémante le compteur de tours
            nbTour += 1 ;

            // On délace les poissons après un déplacement
            sea.moveFish() ;

            // On réactualise le tableau de l'océan et donc l'image à l'écran
            image.setColors(sea.getColors()) ;

            // Pour faire fondre les icebergs de l'océan, on attend d'avoir joué 5 tours
            if ( nbTour % fondreSeuil == 0 ) // fondreSeuil = 5
            {
                sea.fondre(fondreQuotient) ;
            }

            // On vérifie si le pingouin n'est pas mort
            if ( !sea.getPing().isAlive() )
            {
                run = false ;
                System.out.println("**********************************************") ;
                System.out.println("GAME OVER ! VOTRE PINGOUIN EST MORT DE FAIM !") ;
                System.out.println("Rejouer ? O/N") ;
                choix = scan.nextLine() ;
                System.out.println("**********************************************\n") ;

                if ( choix.equals("O") || choix.equals("o") )
                {
                    run = true ;
                    sea = new Ocean(4,500,500) ;
                    image.setColors(sea.getColors()) ;
                }
            }
        }

        image.fermer() ;
        
    }
    
    /*********************************************************************************************/
    /*********************************************************************************************/

    public static void main(String[] args)
    {
        Jeu myGame = new Jeu() ;
        myGame.jouer() ;
    }
}