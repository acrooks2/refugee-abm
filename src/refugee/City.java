package refugee;

import sim.util.Int2D;

class City {
	private Int2D location;
	private int quota; //ranked
	private int milConflict; //ranked
	private int economy; //ranked
	private int family; //ranked
	private int terrain; //ranked
	
	public City(Int2D location, int quota, int milConflict, int economy, int family, 
			int jobs, int terrain)
    {
       this.location = location;
       this.quota = quota;
       this.milConflict = milConflict;
       this.economy = economy;
       this.family = family;
       this.terrain = terrain;
       
    }
	
	public Int2D getLocation() {
	    return location;
	 }

	 public void setLocation(Int2D location) {
	    this.location = location;
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
	 
	 
	 public int getFamily(){
		 return family;
	 }
	 
	 public void setFamily(int family){
		 this.family = family;
	 }
	 
	 
	 public int getTerrain(){
		 return terrain;
	 }
	 
	 public void setTerrain(int terrain){
		 this.terrain = terrain;
	 }
	    
}
