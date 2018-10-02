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
    public void moveForward(Ocean sea)
    {
        if ( sea.getPing().getOrdonnee() + sea.getPing().getSize() + sea.getPing().getSpeed() < sea.getHeight() )
        {
            sea.getPing().deplacerOrdonnee(sea.getPing().getSpeed()) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    public void moveBackward(Ocean sea)
    {
        if ( sea.getPing().getOrdonnee() > 0 )
        {
            sea.getPing().deplacerOrdonnee(-sea.getPing().getSpeed()) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    public void moveRight(Ocean sea)
    {
        if ( sea.getPing().getAbscisse() < sea.getWidth() )
        {
            sea.getPing().deplacerAbscisse(sea.getPing().getSpeed()) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    public void moveLeft(Ocean sea)
    {
        if ( sea.getPing().getAbscisse() - sea.getPing().getSize() - sea.getPing().getSpeed() > 0 )
        {
            sea.getPing().deplacerAbscisse(-sea.getPing().getSpeed()) ;
        }
        else
        {
            System.out.println("Votre pingouin se tape contre le bord de l'océan !\n") ;
        }
    }

    /*********************************************************************************************/
    /*********************************************************************************************/

    // Méthode d'affichage de texte
    private void printRules()
    {
        System.out.println("----------------------") ;
        System.out.println("     JEU PINGOUIN    ") ;
        System.out.println("----------------------\n") ;

        System.out.println("********************************************************************************") ;
        System.out.println(" Règles : Vous contrôlez un pingouin qui se déplace sur un océan d'icebergs") ;
        System.out.println(" et de poissons. Vous devez manger tous les poissons avant que votre pingouin") ;
        System.out.println(" ne meurt de faim. Sachez que les poissons changent de direction quand ils") ;
        System.out.println(" rencontrent un iceberg. Ainsi, plus il y a d'icebergs, plus il est facile") ;
        System.out.println(" de les manger. Toutefois, ces icebergs fondent à mesure du temps et cassent") ;
        System.out.println(" si vous montez dessus. Certains poissons se sont cachés en dessous par peur") ;
        System.out.println(" de votre terrifiant pingouin ! Ces derniers finissent par s'épuiser et par") ;
        System.out.println(" mourir au bout d'un certain nombre de tour. Plus vous mangez, plus vous") ;
        System.out.println(" grossissez, et plus vous êtes lent ! BON JEU !\n") ;
        System.out.println("********************************************************************************\n") ;
    }

    private void printControls()
    {
        System.out.println("------------------------------------") ;
        System.out.println("Pressez Z / z pour avancer") ;
        System.out.println("Pressez Q / q pour aller a gauche") ;
        System.out.println("Pressez S / s pour reculer") ;
        System.out.println("Pressez D / d pour aller a droite") ;
        System.out.println("------------------------------------") ;
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

            choix = scan.nextLine() ;
            run = false ;

            switch (choix)
            {
                // AVANCER LE PINGOUIN
                case "Z":
                    game.moveForward(sea) ;
                    run = true ;
                    break ;
                case "z":
                    game.moveForward(sea) ;
                    run = true ;
                    break ;

                // DEMANDER AU PINGOUIN D'ALLER A GAUCHE
                case "Q":
                    game.moveLeft(sea) ;
                    run = true ;
                    break ;
                case "q":
                    game.moveLeft(sea) ;
                    run = true ;
                    break ;

                // FAIRE RECULER LE PINGOUIN
                case "S":
                    game.moveBackward(sea) ;
                    run = true ;
                    break ;
                case "s":
                    game.moveBackward(sea) ;
                    run = true ;
                    break ;

                // DEMANDER AU PINGOUIN D'ALLER A DROITE
                case "D":
                    game.moveRight(sea) ;
                    run = true ;
                    break ;
                case "d":
                    game.moveRight(sea) ;
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

            /* CONDITIONS DE TEST POUR LA FIN DU JEU */

            // On vérifie si il y a encore des poissons vivants dans l'océan
            if ( sea.areFishsAllDead() )
            {
                run = false ;
                if ( sea.getPing().ateEnough(sea) )
                {
                    System.out.println("*****************************************************") ;
                    System.out.println("FELICITATIONS ! VOUS AVEZ MANGE ASSEZ DE POISSONS !") ;
                    System.out.println("Rejouer ? O/N") ;
                    choix = scan.nextLine() ;
                    System.out.println("*****************************************************\n") ;
                }
                else
                {
                    System.out.println("*******************************************************") ;
                    System.out.println("GAME OVER ! VOUS N'AVEZ PAS MANGE ASSEZ DE POISSONS !") ;
                    System.out.println("Rejouer ? O/N") ;
                    choix = scan.nextLine() ;
                    System.out.println("*******************************************************\n") ;
                }

                if ( choix.equals("O") || choix.equals("o") )
                {
                    run = true ;
                    sea = new Ocean(4,500,500) ;
                    image.setColors(sea.getColors()) ;
                }
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