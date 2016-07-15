package refugee;

import java.awt.Color;
import ec.util.MersenneTwisterFast;
import java.util.ArrayList;
import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Int2D;

class Refugee implements Steppable{
	private Int2D location;
	private int age;
	private int sex; //0 male, 1 female
	private ArrayList<Refugee> family; 
	private Route route;
    private int routePosition;
	private int healthStatus = 0; //default 0 (alive), rank 0-2
	private double finStatus;
	private City home;
	private City currentcity;
	private City goal;
	private Location position;
	MersenneTwisterFast random ;
	public Refugee(Int2D location, double finStatus, int sex, int age, ArrayList<Refugee> family)
    {
       this.location = location;
       this.sex = sex;
       this.age = age;
       this.family = family;
       
       
    }
	
	 @Override
	    public void step(SimState state)
	    {
		 
	        Migration migrationSim = (Migration) state;
		 if(healthStatus == Constants.DEAD)
	        {
	            return;
	            //change color, or indicate dead
	        }
		 else if (finStatus == 0.0){
			 return;
		 }
		 else{
			 ArrayList<City> citylist = null; //change later when cities included in map
		     City goalCity = calcGoalCity(citylist);
		     if(this.location != goalCity.getLocation()){
				 //AStar determine route
		    	 setGoal(currentcity, goalCity, Parameters.WALKING_SPEED);
	             if(route == null)
	                 return;
	             if(routePosition < route.getNumSteps())
	                {
	                    Int2D nextStep = route.getLocation(routePosition++);
	                    this.setLocation(nextStep);
	                    updatePositionOnMap(migrationSim);
	                }
		     }
			
		 }
	    }
	 
	 
	 
	 public City calcGoalCity(ArrayList<City> citylist){ //returns the best city
		 City bestCity = null;
		 double max = Double.POSITIVE_INFINITY;
		 for (City city: citylist){
			 double cityDesirability = dangerCare()*city.getViolence() 
					 + familyAbroadCare()*city.getFamilyPresence() 
					 + city.getEconomy() + city.getPopulation();
			 if (city.getRefugeePopulation() + family.size() >= city.getQuota()) //if reached quota, desirability is 0 
				 cityDesirability = 0;
			 if (cityDesirability > max){				 
				 max = cityDesirability;
				 bestCity = city;
			 }
			 
		 }
		 return bestCity;
	 }
	 
	    private void setGoal(City from, City to, double speed)
	    {
	        this.goal = to;
	        if(speed < 20)
	            this.route = from.getRoute(to, speed, this);
	        this.routePosition = 0;
	    }
	    
	    public void updatePositionOnMap(Migration migrationSim)
	    {
	        double randX = migrationSim.random.nextDouble();
	        double randY = migrationSim.random.nextDouble();
	        migrationSim.world.setObjectLocation(this, new Double2D(location.getX() + randX, location.getY() + randY));
	        migrationSim.worldPopResolution.setObjectLocation(this, location.getX()/10, location.getY()/10);
	    }
	 
	 
	//get and set
	 public Int2D getLocation() {
	    return location;
	 }

	 public void setLocation(Int2D location) {
	    this.location = location;
	 }	 
	 
	 public double getFinStatus() {
		    return finStatus;
		 }

	 public void setFinStatus(int finStatus) {
		    this.finStatus = finStatus;
	 }	 
		 
	 public int getAge(){
		 return age;
	 }
	 
	 public void setAge(int age){
		 this.age = age;
	 }
	 
	 public int getSex(){
		 return sex;
	 }
	 
	 public void setSex(int sex){
		 this.sex = sex;
	 }
	 
	 
	 public ArrayList<Refugee> getFamily(){
		 return family;
	 }
	 
	 public void setFamily(ArrayList<Refugee> family){
		 this.family = family;
	 }
	    
	 
	 public double dangerCare(){//0-1, young, old, or has family weighted more
		 double dangerCare = 1.0;
		 if (this.age < 12 || this.age > 60){
			 dangerCare += Parameters.DANGER_CARE_WEIGHT*random.nextDouble();
		 }
		 if (this.family.size() > 0){
			 dangerCare += (Parameters.DANGER_CARE_WEIGHT*random.nextDouble());
		 }
		 return dangerCare;
	 }
	 
	 public double familyAbroadCare(){ //0-1, if traveling without family, cares more
		 double familyCare = 1.0;
		 if (this.family.size() == 1) familyCare += Parameters.FAMILY_ABROAD_CARE_WEIGHT*random.nextDouble();
		 return familyCare;
	 }


}
