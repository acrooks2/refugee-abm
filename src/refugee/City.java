package refugee;

import sim.util.Int2D;

class City {
	private Int2D location;
	private int population;
	private int quota; //1
	private int milConflict; //2
	private int economy; //3
	private int familyPresence; //2
	
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
		return population;
	 }

	public void setPopulation(int population) {
		this.population = population;
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
	 
	 
	    
}
