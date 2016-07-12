package refugee;

import java.awt.Color;
import ec.util.MersenneTwisterFast;
import java.util.ArrayList;
import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

class Refugee implements Steppable{
	private Int2D location;
	private int age;
	private int sex; //0 male, 1 female
	private int education; // ranked 0-4
	private boolean hasFamily;
	private int healthStatus = 0; //default 0 (alive), rank 0-2
	private double finStatus;
	private City home;
	private City goal;
	private Route route;
	MersenneTwisterFast random ;
	public Refugee(Int2D location, int finStatus, int sex, int education, int age, boolean hasFamily)
    {
       this.location = location;
       this.sex = sex;
       this.age = age;
       this.education = education;
       this.hasFamily = hasFamily;
       
       
    }
	
	 @Override
	    public void step(SimState state)
	    {
		 if(healthStatus == Constants.DEAD)
	        {
	            return;
	        }
		 else if (finStatus == 0.0){
			 return;
		 }
		 else{
		 //calc Goal
		 //move
		 }
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
	 
	 
	 public int getEducation(){
		 return education;
	 }
	 
	 public void setEducation(int education){
		 this.education = education;
	 }
	 
	 public boolean getFamily(){
		 return hasFamily;
	 }
	 
	 public void setFamily(boolean hasFamily){
		 this.hasFamily = hasFamily;
	 }
	    
	 
	 private double dangerCareWeight(){//0-1, young, old, or has family weighted more
		 double dangerCare = 0;
		 if (this.age < 12 || this.age > 60){
			 dangerCare += 0.3 + 0.2*random.nextDouble();
		 }
		 if (this.hasFamily){
			 dangerCare += (0.3+0.2*random.nextDouble());
		 }
		 return dangerCare;
	 }
	 
	 private double familyAbroadCareWeight(){ //0-1, if traveling without family, cares more
		 double familyCare = 0;
		 if (!this.hasFamily) familyCare = 0.8 + 0.2*random.nextDouble();
		 return familyCare;
	 }


}
