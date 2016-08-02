/* 
ID: ehu
LANG: JAVA
PROG: cowroute*/ 
import java.io.*;
import java.util.*;
import java.lang.*;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.xy.XYSeries;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.field.geo.GeomVectorField;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Bag;
import java.io.*;
import java.util.*;

public class cowroute {
	static int a = 422;
   public static void main(String[] args) throws IOException {
		long seed = System.currentTimeMillis();
		Migration simState = new Migration(seed);
		long io_start = System.currentTimeMillis();
		simState.start();
		long io_time = (System.currentTimeMillis() - io_start) / 1000;
		System.out.println("io_time = " + io_time);
		Schedule schedule = simState.schedule;
System.out.println("Hello2 = " + a);
System.exit(0);
   }
   
public cowroute (long b) {
	a = 222;
}

}