package refugee;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.vividsolutions.jts.geom.LineString;

import sim.field.continuous.Continuous2D;
import sim.field.geo.GeomVectorField;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.io.geo.ShapeFileImporter;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.geo.MasonGeometry;

class MigrationBuilder {
	
	//initialize world
	public static void initializeWorld(Migration sim, String popPath, String adminPath, String ageDistPath){
	/*
	 *     private double distance;
    private MigrationBuilder.Node start;
    private MigrationBuilder.Node end;
    private double speed;
    private int population;
    private double cost;
    private double transportLevel; 
    private double deaths; 
	 */
		String[] cityAttributes = {"NAME", "LOCX", "LOCY", "POP", "QUOTA", "VIOL", "ECON", "FAMILY"};
		String[] roadAttributes = {"START", "END", "SPEED", "POP", "COST", "TLEVEL", "DEATHS"};
		
        //age_dist = new HashMap<Integer, ArrayList<Double>>();
        sim.world_height = 9990;
        sim.world_width = 9390;

	    sim.roadNetwork = new Network();
	    sim.allRoadNodes = new SparseGrid2D(sim.world_width, sim.world_height);

	    sim.roadLinks = new GeomVectorField(sim.world_width, sim.world_height);
	    Bag roadAtt = new Bag(roadAttributes);
	    
	    GeomVectorField cities_vector = new GeomVectorField();
	    sim.cityGrid = new SparseGrid2D(sim.world_width, sim.world_height);
	    Bag cityAtt = new Bag(cityAttributes);

	    String[] files = {""};//shapefiles
	    Bag[] attfiles = {};
	    GeomVectorField[] vectorFields = {sim.roadLinks, cities_vector};
	    readInShapefile(files, attfiles, vectorFields);//read in attributes
	    }

	
	
    public static class Node
    {
        public Int2D location; 

        ArrayList<Edge> links;
        double weightOnLineString;//measures the weight on the line string from 0
        public HashSet<LineString> lineStrings = new HashSet<LineString>();
        public int index;
        public Node(Int2D l)
        {
            location = l;
            links = new ArrayList<Edge>();
        }

        public ArrayList<Edge> getLinks() {
            return links;
        }
        @Override
        public String toString()
        {
            return "(" + location.getX() + ", " + location.getY() + ")";
        }
        //
    }
    /*
     * this.location = location;
       this.population = population;
       this.quota = quota;
       this.milConflict = milConflict;
       this.economy = economy;
       this.familyPresence = familyPresence;
     */
    
    static void makeCities(GeomVectorField cities_vector, SparseGrid2D grid){
    Bag cities = cities_vector.getGeometries();
    	for (int i = 0; i < cities.size(); i++)
    	{
    	MasonGeometry cityinfo= (MasonGeometry)cities.objs[i];
    	String name = cityinfo.getStringAttribute("NAME");
    	Int2D location = new Int2D(cityinfo.getIntegerAttribute("LOCX"), cityinfo.getIntegerAttribute("LOCY"));
    	int population = cityinfo.getIntegerAttribute("POP");
    	int quota = cityinfo.getIntegerAttribute("QUOTA");
    	double violence = cityinfo.getDoubleAttribute("VIOL");
    	double economy = cityinfo.getDoubleAttribute("ECON");
    	double familyPresence = cityinfo.getDoubleAttribute("FAMILY");
    	
    	
    	City city = new City(location, population, quota, violence, economy, familyPresence);
    	
    	}
    }
    static void readInShapefile(String[] files, Bag[] attfiles, GeomVectorField[] vectorFields)
    {
        try
        {
            for(int i = 0; i < files.length; i++)
            {
            	Bag attributes = attfiles[i];
                String filePath = files[i];
                File file = new File(filePath);
                URL shapeURI = file.toURI().toURL();
                ShapeFileImporter.read(shapeURI, vectorFields[i], attributes);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

