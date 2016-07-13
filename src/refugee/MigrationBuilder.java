package refugee;

import java.util.ArrayList;
import java.util.HashSet;

import com.vividsolutions.jts.geom.LineString;

import sim.field.network.Edge;
import sim.util.Int2D;

class MigrationBuilder {
	
	//initialize world
	public void initializeWorld(Migration world, String popPath, String adminPath, String ageDistPath){
		
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
}
