package refugee;

class Parameters {
	 public static double TEMPORAL_RESOLUTION = 1;//steps per hour
	 
	 public static double POP_BLOCK_METERS = 926.1;//Height and width of one population block.
	 public static int WORLD_TO_POP_SCALE = 10; //scale up from the population data for each household
	 public static double WALKING_SPEED = 5.1;//km per hour
	  //-------File paths-------//
	 
	//population flow parameters
	 
	 
	 
	    public static double convertToKilometers(double val)
	    {
	        return (val * (Parameters.POP_BLOCK_METERS/Parameters.WORLD_TO_POP_SCALE))/1000.0;
	        
	    }
	    
	    public static double convertFromKilometers(double val)
	    {
	        return (val*1000.0)/(Parameters.POP_BLOCK_METERS/Parameters.WORLD_TO_POP_SCALE);
	    }
}
