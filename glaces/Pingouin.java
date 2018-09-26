package glaces ;
import geometrie.Point ;

public class Pingouin
{
    private Point position ;
    private int size ; // carré 
    
    public Pingouin()
    {
        this.position = new Point(0.,0.) ;
        this.setSize(0) ;
    }
    
    public Pingouin(Point pos, int size)
    {
        this.position = new Point(pos) ;
        this.setSize(size) ;
    }
    
    private void setSize(int size)
    {
        this.size = size ;
    }
    
    public void deplacerAbscisse(double x)
    {
        this.position.deplacer(x, 0.) ;
    }
    
    public void deplacerOrdonnee(double y)
    {
        this.position.deplacer(0., y) ;
    }
    
    public Point getPosition()
    {
        return this.position ;
    }   
    
    public int getSize()
    {
        return this.size ;
    }
    
}