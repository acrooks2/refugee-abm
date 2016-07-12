package refugee;

import java.util.HashMap;
import java.util.HashSet;

import sim.util.Bag;
import sim.util.Int2D;

class City {
	private Int2D location;
	private int quota; //1
	private int milConflict; //2
	private int economy; //3
	private int familyPresence; //2
	private HashSet<Refugee> refugees;
	//private HashMap<City, Route> cachedPaths;
	//need name, get name, set name
    private MigrationBuilder.Node nearestNode;
	
	public City(Int2D location, int quota, int milConflict, int economy, int familyPresence)
    {
       this.location = location;
       this.quota = quota;
       this.milConflict = milConflict;
       this.economy = economy;
       this.familyPresence = familyPresence;
       
    }
	
	public Int2D getLocation() {
	    return location;
	 }

	 public void setLocation(Int2D location) {
	    this.location = location;
	 }	 
	 
	public int getPopulation() {
		return refugees.size();
	 }

		 
	 public int getQuota() {
		    return quota;
		 }

	 public void setQuota(int quota) {
		    this.quota = quota;
	 }	 
		 
	 public int getMilConflict(){
		 return milConflict;
	 }
	 
	 public void setMilConflict(int milConflict){
		 this.milConflict = milConflict;
	 }
	 
	 public int getEconomy(){
		 return economy;
	 }
	 
	 public void setEconomy(int economy){
		 this.economy = economy;
	 }
	 
	 
	 public int getFamilyPresence(){
		 return familyPresence;
	 }
	 
	 public void setFamilyPresence(int familyPresence){
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
