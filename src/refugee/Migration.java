package refugee;

import ec.util.MersenneTwisterFast;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import sim.engine.MakesSimState;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.geo.GeomVectorField;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Int2D;
import sim.util.distribution.Poisson;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

class Migration extends SimState{
	
	public Continuous2D world;
	public SparseGrid2D world2;
    public SparseGrid2D worldPopResolution;//all agents within each km grid cell
    public SparseGrid2D cityGrid;
    public  Network roadNetwork = new Network();
    public GeomVectorField roadLinks;
    public GeomVectorField cityPoints;

    //public GeomVectorField refugees;
    public GeomVectorField adminBoundaries;//TODO may not be needed
    public GeomVectorField adminShape;//TODO may not be needed
    public SparseGrid2D allRoadNodes;//cities for now
    //public DoubleGrid2D road_cost; //accumulated cost to get to nearest node on the road network
    //public IntGrid2D admin_id;//contains id for each location (939 x 990)
    
    
    //keys are the admin id for each country and the bag has all the residents in that admin area
    //public Map<Integer, Bag> admin_id_sle_residents = new HashMap<>();
    
    public int pop_width;
    public int pop_height;
    public int world_width;
    public int world_height;
    public int total_scaled_pop = 0; //number of agents in the model (scaled from total_pop)
    public long total_pop = 0; //actual population (not scaled)
    
    /*charts
     * can include: number dead, specifics for each country, etc.
     */
    
    public Bag refugees;
    public Bag cities = new Bag();
    public Map<Integer, City> cityList = new HashMap<>();
    
   // public Map<Integer, List<MovementPattern>> movementPatternMap = new HashMap<>();
    
    public Migration(long seed)
    {
        super(seed);
    }
    
    @Override
    public void start()
    {
    	super.start();
    	refugees = new Bag();
    	MigrationBuilder.initializeWorld(this);
    	for (Object refugee: refugees){
    		Refugee r = (Refugee)refugee;
    		//System.out.println(r.getHome().getName());
    	}
    	
    	
    	//charts
    	

        Steppable chartUpdater = new Steppable()
        {
            @Override
            public void step(SimState simState)
            {
            	long cStep = simState.schedule.getSteps();

                /* refugee decides on goal
                 * refugee decides on a route
                 * move the refugee
                 * 
                 */
            }
        };
        this.schedule.scheduleRepeating(chartUpdater);
        
        
    }
    
   /* Steppable movementManager = new Steppable()
    {
        private long lastTime;

        @Override
        public void step(SimState simState)
        {
            long cStep = simState.schedule.getSteps();
            if(cStep % Math.round(24.0/Parameters.TEMPORAL_RESOLUTION) == 0)//only do this on the daily
            {
                long now = System.currentTimeMillis();
                if(lastTime != 0)
                    System.out.println("Day " + cStep/24 + "[" + (now-lastTime)/1000 + " secs ]");
                moveResidents();
                System.out.println("Managing population flow [" + (System.currentTimeMillis()-now)/1000 + " sec]");
            }
        }
        
        private void moveResidents(){
        	for (Refugee r: refugees){
        		
        	}
        }
        }
    };
    this.schedule.scheduleRepeating(movementManager);
    */
    
    @Override
    public void finish()
    {
	    System.out.println("Finishing...");
        super.finish();
    }
    
    public static void main(String[] args){

        // doLoop(Landscape.class, args);
    	long seed = System.currentTimeMillis();
        Migration simState = new Migration(seed);
            long io_start = System.currentTimeMillis();
            simState.start();
            long io_time = (System.currentTimeMillis()-io_start)/1000;
            System.out.println("io_time = " + io_time);
            Schedule schedule = simState.schedule;
            while(true) {
                if(!schedule.step(simState)) {
                    break;
                }
            }

            //create our run directory
            

            //write output data to output
            //create directory output if not exists
            //write effective reproductive rates


            //write run meta data to a json object and save it

           // doLoop(Migration.class, args);
            System.exit(0);
        
    }

    

}
