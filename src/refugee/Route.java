package refugee;
import sim.util.Int2D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


class Route
{
   // private List<Int2D> path;//list of places this person needs to go
    private double distance;
   // private Mapmaker.Node start;
   // private Mapmaker.Node end;
    private double speed;
    private int population;
    private double cost;
    private double transportLevel; 
    private double deaths; 

    public Route(double speed, double distance,  double cost, double transportLevel, double deaths)
    {
    	this.population = 0; //starts with no one traveling
        this.speed = speed;
        this.distance = distance;
        this.cost = cost;
        this.transportLevel = transportLevel;
        this.deaths = deaths;
        
    }

    /**
     * @return next location to move, null if no more moves
     */
    /*public Int2D getLocation(int index)
    {
        Int2D location = path.get(index);
        return location;
    }*/

    public double getTotalDistance()
    {
        return distance;
    }
    
    public double getSpeed()
    {
        return speed;
    }
    
    public double getPopulation()
    {
        return population;
    }
    
    public double getCost()
    {
        return cost;
    }
    
    public double getTransportLevel()
    {
        return transportLevel;
    }
    
    public double getDeaths()
    {
        return deaths;
    }

   /* public int getNumSteps()
    {
        return path.size();
    }

    public Mapmaker.Node getStart()
    {
        return start;
    }

    public Mapmaker.Node getEnd()
    {
        return end;
    }

    public Route reverse()
    {
        List<Int2D> reversedPath = new ArrayList<Int2D>(path.size());
        for(int i = path.size()-1; i >= 0; i--)
            reversedPath.add(path.get(i));
        return new Route(reversedPath, this.distance, this.end, this.start, speed);
    }

    public void addToEnd(Int2D location)
    {
        //convert speed to correct units
        double speed = this.speed;

        speed *= Parameters.TEMPORAL_RESOLUTION;//now km per step

        //convert speed to cell block per step
        speed = Parameters.convertFromKilometers(speed);

        double dist = location.distance(path.get(path.size()-1));
        while(speed < dist)
        {
            path.add(AStar.getPointAlongLine(path.get(path.size()-1), location, speed/dist));
            dist = path.get(path.size()-1).distance(location);
        }

        path.add(location);
    }

*/
}
