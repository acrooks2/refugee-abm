package refugee;

import java.util.HashMap;
import java.util.HashSet;

import sim.util.Bag;
import sim.util.Int2D;

class City {
	private Int2D location;
	private int quota; //1
	private int population;
	private double violence; //2
	private double economy; //3
	private double familyPresence; //2
	private HashSet<Refugee> refugees;
	//private HashMap<City, Route> cachedPaths;
	//need name, get name, set name
    private MigrationBuilder.Node nearestNode;
	
	public City(Int2D location, int population, int quota, int violence, int economy, int familyPresence)
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
	 
	 //include route saver - route from this city to another, but personalized (each agent)
	    
}
