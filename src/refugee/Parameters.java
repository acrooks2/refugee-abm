package refugee;

class Parameters {
	 public static double TEMPORAL_RESOLUTION = 1;//steps per hour
	 //-------Agent Care Weights-------//
	 public static double DANGER_CARE_WEIGHT = 0.2;
	 public static double FAMILY_ABROAD_CARE_WEIGHT = 0.2;
	 //-------Edge Weights-------//
	 public static double COST_WEIGHT = 2.0;
	 public static double RISK_WEIGHT = 1.5;
	 public static double DISTANCE_WEIGHT = 1.0;
	 public static double SPEED_WEIGHT = 1.0;
	 public static double POP_WEIGHT = 0.5;
	 public static double TRANSPORT_LEVEL_WEIGHT = 0.5; 

	 
	 public static double POP_BLOCK_METERS = 926.1;//Height and width of one population block.
	 public static int WORLD_TO_POP_SCALE = 10; //scale up from the population data for each household
	 public static double WALKING_SPEED = 5.1;//km per hour
	  //-------File paths-------//
	 public static String POP_PATH = "";
	 public static String ADMIN_PATH = "";
	 public static String AGE_DIST_PATH = "";
	 public static String ROAD_PATH = "";
	 public static String CITY_PATH = "";
	 
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
