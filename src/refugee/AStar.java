package refugee;
import java.util.*;

import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Heap;
import sim.util.Int2D;

@SuppressWarnings("restriction")
public class AStar {

    /**
     * Assumes that both the start and end location are CityS as opposed to LOCATIONS
     * @param start
     * @param goal
     * @return
     */
	public static Network roadNetwork = MigrationBuilder.migrationSim.roadNetwork;
    static public Route astarPath(City start, City goal, double speed, Refugee refugee) {

//       
        // initial check
        long startTime = System.currentTimeMillis();
        if (start == null || goal == null) {
            System.out.println("Error: invalid City provided to AStar");
        }

        // containers for the metainformation about the Citys relative to the 
        // A* search
        HashMap<City, AStarCityWrapper> foundCitys =
                new HashMap<City, AStarCityWrapper>();


        AStarCityWrapper startCity = new AStarCityWrapper(start);
        AStarCityWrapper goalCity = new AStarCityWrapper(goal);
        foundCitys.put(start, startCity);
        foundCitys.put(goal, goalCity);

        startCity.gx = 0;
        startCity.hx = heuristic(start, goal);
        startCity.fx = heuristic(start, goal);

        // A* containers: allRoadCitys to be investigated, allRoadCitys that have been investigated
        HashSet<AStarCityWrapper> closedSet = new HashSet<>(10000),
                openSet = new HashSet<>(10000);
        PriorityQueue<AStarCityWrapper> openSetQueue = new PriorityQueue<>(10000);
        openSet.add(startCity);
        openSetQueue.add(startCity);
        while(openSet.size() > 0){ // while there are reachable allRoadCitys to investigate

            //AStarCityWrapper x = findMin(openSet); // find the shortest path so far
            AStarCityWrapper x = openSetQueue.peek();
            if(x == null)
            {
                AStarCityWrapper n = findMin(openSet);
            }
            if(x.city == goal ){ // we have found the shortest possible path to the goal!
                // Reconstruct the path and send it back.
                return reconstructRoute(goalCity, startCity, goalCity, speed);
            }
            openSet.remove(x); // maintain the lists
            openSetQueue.remove();
            closedSet.add(x);

            // check all the neighbors of this location
            Bag edges = roadNetwork.getEdgesOut(x.city);
            for( Object l: edges){
            	Edge e = (Edge)l;
                City n = (City) e.from();
                if( n == x.city )
                    n = (City) e.to();

                // get the A* meta information about this City
                AStarCityWrapper nextCity;
                if( foundCitys.containsKey(n))
                    nextCity = foundCitys.get(n);
                else{
                    nextCity = new AStarCityWrapper(n);
                    foundCitys.put( n, nextCity );
                }

                if(closedSet.contains(nextCity)) // it has already been considered
                    continue;

                // otherwise evaluate the cost of this City/edge combo
                RoadInfo edge = (RoadInfo)e.getInfo();
        		double edgeweight = edge.getDistance() * Parameters.DISTANCE_WEIGHT 
        				+ edge.getSpeed() * Parameters.SPEED_WEIGHT
        				+ edge.getPopulation() * Parameters.POP_WEIGHT
        				+ edge.getCost() * Parameters.COST_WEIGHT
        				+ edge.getTransportLevel() * Parameters.TRANSPORT_LEVEL_WEIGHT
        				+ edge.getDeaths() * Parameters.RISK_WEIGHT *refugee.dangerCare();
                
                double tentativeCost = x.gx + edgeweight; //changed from integer, still need to change the weighting of the edge weight
                boolean better = false;

                if(! openSet.contains(nextCity)){
                    openSet.add(nextCity);
                    openSetQueue.add(nextCity);
                    nextCity.hx = heuristic(n, goal);
                    better = true;
                }
                else if(tentativeCost < nextCity.gx){
                    better = true;
                }

                // store A* information about this promising candidate City
                if(better){
                    nextCity.cameFrom = x;
                    nextCity.gx = tentativeCost;
                    nextCity.fx = nextCity.gx + nextCity.hx;
                }
            }

//            if(foundCitys.size()%10000 == 0)
//                System.out.println("Time = " + System.currentTimeMillis());
        }
        //System.out.println("Searched " + foundCitys.size() + " Citys but could not find it");
        return null;
    }

    /**
     * Uses Djikstra to find the closest in the list of endCitys.  Returns the endCity that is closest.
     * @param start
     * @param endCitys
     * @param max_distance the maximum distance you want to search in the road network
     * @param check_capacity determines whether we chceck the capacity of Structure
     * @return
     */
  /*  public static Route getNearestCity(MigrationBuilder.City start, Map<MigrationBuilder.City, List<Structure>> endCitys, double max_distance, boolean check_capacity, double speed)
    {
        //        int[] cacheKey = new int[] {start.location.xLoc, start.location.yLoc, goal.location.xLoc, goal.location.yLoc};
//        if (cache.containsKey(cacheKey))
//            return cache.get(cacheKey);
//
        // initial check
        long startTime = System.currentTimeMillis();
        if (start == null || endCitys == null) {
            System.out.println("Error: invalid City provided to AStar");
        }

        // containers for the metainformation about the Citys relative to the
        // A* search
        HashMap<MigrationBuilder.City, AStarCityWrapper> foundCitys =
                new HashMap<MigrationBuilder.City, AStarCityWrapper>();


        AStarCityWrapper startCity = new AStarCityWrapper(start);
        //AStarCityWrapper goalCity = new AStarCityWrapper(goal);
        foundCitys.put(start, startCity);
        //foundCitys.put(goal, goalCity);

        startCity.gx = 0;
        startCity.hx = 0;
        startCity.fx = 0;

        // A* containers: allRoadCitys to be investigated, allRoadCitys that have been investigated
        HashSet<AStarCityWrapper> closedSet = new HashSet<>(),
                openSet = new HashSet<>();
        PriorityQueue<AStarCityWrapper> openSetQueue = new PriorityQueue<>(10000);


        openSet.add(startCity);
        openSetQueue.add(startCity);

        while(openSet.size() > 0){ // while there are reachable allRoadCitys to investigate

            //AStarCityWrapper x = findMin(openSet); // find the shortest path so far
            AStarCityWrapper x = openSetQueue.peek();
            //check if we have reached maximum route distance
            if(x.hx > max_distance)
                return null;
            if(x == null)
            {
                AStarCityWrapper n = findMin(openSet);
            }
            if(endCitys.containsKey(x.City)){ // we have found the shortest possible path to the goal!
                if(check_capacity)//check if this structure is already full!
                {
                    for(Structure structure: endCitys.get(x.City))
                        if(!(structure.getMembers().size() >= structure.getCapacity()))//means it is not full
                            return reconstructRoute(x, startCity, x, speed);
                }
                else // Reconstruct the path and send it back.
                    return reconstructRoute(x, startCity, x, speed);
            }
            openSet.remove(x); // maintain the lists
            openSetQueue.remove();
            closedSet.add(x);

            // check all the neighbors of this location
            for(Edge l: x.City.links){

                MigrationBuilder.City n = (MigrationBuilder.City) l.from();
                if( n == x.City )
                    n = (MigrationBuilder.City) l.to();

                // get the A* meta information about this City
                AStarCityWrapper nextCity;
                if( foundCitys.containsKey(n))
                    nextCity = foundCitys.get(n);
                else{
                    nextCity = new AStarCityWrapper(n);
                    foundCitys.put( n, nextCity );
                }

                if(closedSet.contains(nextCity)) // it has already been considered
                    continue;

                // otherwise evaluate the cost of this City/edge combo
                double tentativeCost = x.gx +  l.getWeight();
                boolean better = false;

                if(! openSet.contains(nextCity)){
                    openSet.add(nextCity);
                    openSetQueue.add(nextCity);
                    nextCity.hx = heuristic(x.City, nextCity.City) + x.hx;
                    better = true;
                }
                else if(tentativeCost < nextCity.gx){
                    better = true;
                }

                // store A* information about this promising candidate City
                if(better){
                    nextCity.cameFrom = x;
                    nextCity.gx = tentativeCost;
                    nextCity.fx = nextCity.gx + nextCity.hx;
                }
            }

//            if(foundCitys.size()%10000 == 0)
//                System.out.println("Time = " + System.currentTimeMillis());
        }
        //System.out.println("Searched " + foundCitys.size() + " Citys but could not find it");
        return null;
    }

    /**
     * Uses Djikstra to find all Citys within the distance that are a part of endCitys.  Returns the list of endCitys within the distance.
     * @param start
     * @param endCitys
     * @param max_distance the maximum distance you want to search in the road network
     * @return A list of Citys within the maximum distance sorted in ascending order by distance to start (index 0 means closest)
     */
    public static List<City> getCitiesWithinDistance(City start, Map endCities, double max_distance, double speed)
    {
        //        int[] cacheKey = new int[] {start.location.xLoc, start.location.yLoc, goal.location.xLoc, goal.location.yLoc};
//        if (cache.containsKey(cacheKey))
//            return cache.get(cacheKey);
//
        // initial check
        long startTime = System.currentTimeMillis();
        if (start == null || endCities == null) {
            System.out.println("Error: invalid City provided to AStar");
        }

        // containers for the metainformation about the Citys relative to the
        // A* search
        HashMap<City, AStarCityWrapper> foundCitys =
                new HashMap<City, AStarCityWrapper>();


        AStarCityWrapper startCity = new AStarCityWrapper(start);
        //AStarCityWrapper goalCity = new AStarCityWrapper(goal);
        foundCitys.put(start, startCity);
        //foundCitys.put(goal, goalCity);

        startCity.gx = 0;
        startCity.hx = 0;
        startCity.fx = 0;

        // A* containers: allRoadCitys to be investigated, allRoadCitys that have been investigated
        //was ArrayList
        HashSet<AStarCityWrapper> closedSet = new HashSet<>(),
                openSet = new HashSet<>();
        PriorityQueue<AStarCityWrapper> openSetQueue = new PriorityQueue<>(10000);//added


        openSet.add(startCity);
        openSetQueue.add(startCity);

        List<City> CitiesToReturn = new LinkedList<>();
        ListIterator<City> listIterator = CitiesToReturn.listIterator();//pointer to last position in the CitysToReturn

        while(openSet.size() > 0){ // while there are reachable allRoadCitys to investigate

            //AStarCityWrapper x = findMin(openSet); // find the shortest path so far
            AStarCityWrapper x = openSetQueue.peek();
            //check if we have reached maximum route distance
            if(x.hx > max_distance)
            {
                return CitiesToReturn;
            }
            if(x == null)
            {
                AStarCityWrapper n = findMin(openSet);
            }
            if(endCities.containsKey(x.city)){ // we have found the shortest possible path to the goal!
                listIterator.add(x.city);
            }

            openSet.remove(x); // maintain the lists
            openSetQueue.remove();
            closedSet.add(x);

            // check all the neighbors of this location
            Bag edges = roadNetwork.getEdges(x.city, null);
            for( Object l: edges){
            	Edge e = (Edge)l;
                City n = (City) e.from();
                if( n == x.city )
                    n = (City) e.to();

                // get the A* meta information about this City
                AStarCityWrapper nextCity;
                if( foundCitys.containsKey(n))
                    nextCity = foundCitys.get(n);
                else{
                    nextCity = new AStarCityWrapper(n);
                    foundCitys.put( n, nextCity );
                }

                if(closedSet.contains(nextCity)) // it has already been considered
                    continue;

                // otherwise evaluate the cost of this City/edge combo
                double tentativeCost = x.gx + (Integer) e.info;
                boolean better = false;

                if(! openSet.contains(nextCity)){
                    openSet.add(nextCity);
                    openSetQueue.add(nextCity);
                    nextCity.hx = heuristic(x.city, nextCity.city) + x.hx;
                    better = true;
                }
                else if(tentativeCost < nextCity.gx){
                    better = true;
                }

                // store A* information about this promising candidate City
                if(better){
                    nextCity.cameFrom = x;
                    nextCity.gx = tentativeCost;
                    nextCity.fx = nextCity.gx + nextCity.hx;//weight hx differently
                }
            }

//            if(foundCitys.size()%10000 == 0)
//                System.out.println("Time = " + System.currentTimeMillis());
        }
        //System.out.println("Searched " + foundCitys.size() + " Citys but could not find it");
        return CitiesToReturn;
    }

    /**
     * Uses Djikstra to find the closest in the list of endCitys.  Returns the endCity that is closest.
     * @param start
     * @param distance the target distance to stop
     * @return
     */
    public static Route getCityAtDistance(City start, double distance, double speed)
    {
        //        int[] cacheKey = new int[] {start.location.xLoc, start.location.yLoc, goal.location.xLoc, goal.location.yLoc};
//        if (cache.containsKey(cacheKey))
//            return cache.get(cacheKey);
//
        // initial check
        long startTime = System.currentTimeMillis();
        if (start == null) {
            System.out.println("Error: invalid City provided to AStar");
        }

        // containers for the metainformation about the Citys relative to the
        // A* search
        HashMap<City, AStarCityWrapper> foundCitys =
                new HashMap<City, AStarCityWrapper>();


        AStarCityWrapper startCity = new AStarCityWrapper(start);
        //AStarCityWrapper goalCity = new AStarCityWrapper(goal);
        foundCitys.put(start, startCity);
        //foundCitys.put(goal, goalCity);

        startCity.gx = 0;
        startCity.hx = 0;
        startCity.fx = 0;

        // A* containers: allRoadCitys to be investigated, allRoadCitys that have been investigated
        HashSet<AStarCityWrapper> closedSet = new HashSet<>(),
                openSet = new HashSet<>();
        PriorityQueue<AStarCityWrapper> openSetQueue = new PriorityQueue<>(10000);

        openSet.add(startCity);
        openSetQueue.add(startCity);

        while(openSet.size() > 0){ // while there are reachable allRoadCitys to investigate

            //AStarCityWrapper x = findMin(openSet); // find the shortest path so far
            AStarCityWrapper x = openSetQueue.peek();

            //check if we have reached maximum route distance
            if(x.hx > distance)////we are at the distance!!!
            {
                return reconstructRoute(x, startCity, x, speed);
            }
            if(x == null)
            {
                AStarCityWrapper n = findMin(openSet);
            }
            openSet.remove(x); // maintain the lists
            openSetQueue.remove();
            closedSet.add(x);

            // check all the neighbors of this location
            Bag edges = roadNetwork.getEdges(x.city, null);
            for( Object l: edges){
            	Edge e = (Edge)l;
                City n = (City) e.from();
                if( n == x.city )
                    n = (City) e.to();

                // get the A* meta information about this City
                AStarCityWrapper nextCity;
                if( foundCitys.containsKey(n))
                    nextCity = foundCitys.get(n);
                else{
                    nextCity = new AStarCityWrapper(n);
                    foundCitys.put( n, nextCity );
                }

                if(closedSet.contains(nextCity)) // it has already been considered
                    continue;

                // otherwise evaluate the cost of this City/edge combo
                double tentativeCost = x.gx + (Integer) e.info;
                boolean better = false;

                if(! openSet.contains(nextCity)){
                    openSet.add(nextCity);
                    openSetQueue.add(nextCity);
                    nextCity.hx = heuristic(x.city, nextCity.city) + x.hx;
                    better = true;
                }
                else if(tentativeCost < nextCity.gx){
                    better = true;
                }

                // store A* information about this promising candidate City
                if(better){
                    nextCity.cameFrom = x;
                    nextCity.gx = tentativeCost;
                    nextCity.fx = nextCity.gx + nextCity.hx;
                }
            }

//            if(foundCitys.size()%10000 == 0)
//                System.out.println("Time = " + System.currentTimeMillis());
        }
        //System.out.println("Searched " + foundCitys.size() + " Citys but could not find it");
        return null;
    }

    /**
     * Takes the information about the given City n and returns the path that
     * found it.
     * @param n the end point of the path
     * @return an Route from start to goal
     */
    static Route reconstructRoute(AStarCityWrapper n, AStarCityWrapper start, AStarCityWrapper end, double speed)
    {
        List<Int2D> result = new ArrayList<>(20);

        //adjust speed to temporal resolution
        speed *= Parameters.TEMPORAL_RESOLUTION;//now km per step

        //convert speed to cell block per step
        speed = Parameters.convertFromKilometers(speed);

        double mod_speed = speed;//
        double totalDistance = 0;
        AStarCityWrapper x = n;

        //start by adding the last one
        result.add(0, x.city.location);

        if(x.cameFrom != null)
        {
            x = x.cameFrom;

            while (x != null)
            {
                double dist = x.city.location.distance(result.get(0));

                while(mod_speed < dist)
                {
                    result.add(0, getPointAlongLine(result.get(0), x.city.location, mod_speed/dist));
                    dist = x.city.location.distance(result.get(0));
                    mod_speed = speed;
                }
                mod_speed -= dist;

                x = x.cameFrom;

                if(x != null && x.cameFrom != null)
                    totalDistance += x.city.location.distance(x.cameFrom.city.location);
            }
        }

        result.add(0, start.city.location);
        return new Route(result, totalDistance, start.city, end.city, Parameters.WALKING_SPEED);
    }
    
    /**
     * Gets a point a certain percent a long the line
     * @param start
     * @param end
     * @param percent the percent along the line you want to get.  Must be less than 1
     * @return
     */
    public static Int2D getPointAlongLine(Int2D start, Int2D end, double percent)
    {
        return new Int2D((int)Math.round((end.getX()-start.getX())*percent + start.getX()), (int)Math.round((end.getY()-start.getY())*percent + start.getY()));
    }

    /**
     * Measure of the estimated distance between two Citys.
     * @return notional "distance" between the given allRoadCitys.
     */
    static double heuristic(City x, City y) {
        return x.location.distance(y.location);
    }

    /**
     *  Considers the list of Citys open for consideration and returns the City 
     *  with minimum fx value
     * @param set list of open Citys
     * @return
     */
    static AStarCityWrapper findMin(Collection<AStarCityWrapper> set) {
        double min = Double.MAX_VALUE;
        AStarCityWrapper minCity = null;
        for (AStarCityWrapper n : set) {
            if (n.fx < min) {
                min = n.fx;
                minCity = n;
            }
        }
        return minCity;
    }

    /**
     * A wrapper to contain the A* meta information about the Citys
     *
     */
    static class AStarCityWrapper implements Comparable<AStarCityWrapper>{

        // the underlying City associated with the metainformation
        City city;
        // the City from which this City was most profitably linked
        AStarCityWrapper cameFrom;
        double gx, hx, fx;

        public AStarCityWrapper(City n) {
            city = n;
            gx = 0;
            hx = 0;
            fx = 0;
            cameFrom = null;
        }

        @Override
        public int compareTo(AStarCityWrapper aStarCityWrapper) {
            return Double.compare(this.hx, aStarCityWrapper.hx);
        }
    }
}