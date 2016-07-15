package refugee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import sim.util.Bag;
import sim.util.Int2D;

class City {
	Int2D location;
	private int quota; //1
	private int population;
	private double violence; //2
	private double economy; //3
	private double familyPresence; //2
	private HashSet<Refugee> refugees;
	//private HashMap<City, Route> cachedPaths;
	//need name, get name, set name
    private MigrationBuilder.Node nearestNode;
    protected HashMap<City, Route> cachedPaths;
	
	public City(Int2D location, int population, int quota, double violence, double economy, double familyPresence)
    {
       this.location = location;
       this.population = population;
       this.quota = quota;
       this.violence = violence;
       this.economy = economy;
       this.familyPresence = familyPresence;
       
    }
	
	public Int2D getLocation() {
	    return location;
	 }

	 public void setLocation(Int2D location) {
	    this.location = location;
	 }	 
	 
	 public int getPopulation(){
		return population;
	 }
	 
	public int getRefugeePopulation() {
		return refugees.size();
	 }

		 
	 public int getQuota() {
		    return quota;
		 }

	 public void setQuota(int quota) {
		    this.quota = quota;
	 }	 
		 
	 public double getViolence(){
		 return violence;
	 }
	 
	 public void setViolence(double violence){
		 this.violence = violence;
	 }
	 
	 public double getEconomy(){
		 return economy;
	 }
	 
	 public void setEconomy(double economy){
		 this.economy = economy;
	 }
	 
	 
	 public double getFamilyPresence(){
		 return familyPresence;
	 }
	 
	 public void setFamilyPresence(double familyPresence){
		 this.familyPresence = familyPresence;
	 }
	 
	 public void addMembers(Bag people)
	 {
	     refugees.addAll(people);
	 }

	 public void addMember(Refugee r)
	 {
		 refugees.add(r);
	 }

	 public HashSet<Refugee> getMembers()
	 {
	    return refugees;
	 }
	 
	    public void setNearestNode(MigrationBuilder.Node node)
	    {
	        nearestNode = node;
	    }

	    public MigrationBuilder.Node getNearestNode()
	    {
	        return nearestNode;
	    }
	    
	    public void cacheRoute(Route route, City destination)
	    {
	        cachedPaths.put(destination, route);
	    }

	    public Map<City, Route> getCachedRoutes()
	    {
	        return cachedPaths;
	    }
	    
	    public Route getRoute(City destination, double speed, Refugee refugee)
	    {
	        if(cachedPaths.containsKey(destination))//means we have this path cached
	        {
	            Route route = cachedPaths.get(destination);
	            return route;
	        }
	        else
	        {
	            //check if the route has already been cached for the other way (destination -> here)
	            if(destination.getCachedRoutes().containsKey(this))
	            {
	                Route route;
	                if(destination.getRoute(this, speed, refugee) != null)
	                    route = destination.getRoute(this, speed, refugee).reverse();//be sure to reverse the route
	                else
	                    route = null;
	                cachedPaths.put(destination, route);
	                return route;
	            }
	            else
	            {
	                Route route;
	               /* if(this.getLocation().distance(destination.getLocation()) > Parameters.convertFromKilometers(80))
	                {
	                    ArrayList<Int2D> path = new ArrayList<>();
	                    path.add(destination.getLocation());
	                    route = new Route(path, this.getLocation().distance(destination.getLocation()), this.getNearestNode(), destination.getNearestNode(), 10000);
	                }
	                else*/
	                    route = AStar.astarPath(this.getNearestNode(), destination.getNearestNode(), speed, refugee);
	                cachedPaths.put(destination, route);
	                return route;
	            }
	        }
	    }
	 //include route saver - route from this city to another, but personalized (each agent)
	    
}
