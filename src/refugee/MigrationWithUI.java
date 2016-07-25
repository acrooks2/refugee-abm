package refugee;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.dial.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.field.geo.GeomVectorField;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.FieldPortrayal2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.geo.GeomPortrayal;
import sim.portrayal.geo.GeomVectorFieldPortrayal;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.CircledPortrayal2D;
import sim.portrayal.simple.HexagonalPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.geo.MasonGeometry;
import sim.util.media.chart.ChartGenerator;
import sim.util.media.chart.TimeSeriesChartGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MigrationWithUI extends GUIState {
	Display2D display; // displaying the model
	JFrame displayFrame; // frame containing all the displays
	FieldPortrayal2D cityPortrayal = new SparseGridPortrayal2D();
	GeomVectorFieldPortrayal regionPortrayal = new GeomVectorFieldPortrayal();
	GeomVectorFieldPortrayal roadLinkPortrayal = new GeomVectorFieldPortrayal();
	ContinuousPortrayal2D refugeePortrayal = new ContinuousPortrayal2D();

	public MigrationWithUI(Migration sim) {
		super(sim);
	}

	@Override
	public void init(Controller c) {
		super.init(c);

		// set dimen and position of controller
		((Console) c).setSize(350, 80);
		((Console) c).setLocation(0, 680);

		display = new Display2D(1024, 760, this); // creates the display
		// display.setRefresRate(32);
		display.setScale(1.2);

		display.attach(regionPortrayal, "Regions");
		display.attach(roadLinkPortrayal, "Roads");
		display.attach(cityPortrayal, "Cities");
		display.attach(refugeePortrayal, "Refugees");

		displayFrame = display.createFrame();
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		displayFrame.setSize(1280, 1024);
		
		//deaths chart
	       Dimension dm = new Dimension(300,300);
	        Dimension dmn = new Dimension(300,300);
		TimeSeriesChartGenerator healthStatus;
        healthStatus = new TimeSeriesChartGenerator();
        healthStatus.createFrame();
        healthStatus.setSize(dm);
        healthStatus.setTitle("Health Status");
        healthStatus.setRangeAxisLabel("Number of People");
        healthStatus.setDomainAxisLabel("Hours");
        healthStatus.setMaximumSize(dm);
        healthStatus.setMinimumSize(dmn);
//        chartSeriesCholera.setMinimumChartDrawSize(400, 300); // makes it scale at small sizes
//        chartSeriesCholera.setPreferredChartSize(400, 300); // lets it be small

        healthStatus.addSeries(((Migration) this.state).totalDeadSeries, null);
        
        JFrame frameSeries = healthStatus.createFrame(this);
        frameSeries.pack();
        c.registerFrame(frameSeries);
        frameSeries.setVisible(true);


		// ((Console) c).pressPlay();
	}

	@Override
	public void start() {
		super.start();

		setupFixedPortrayals();
		setupMovingPortrayals();
	}

	public void setupFixedPortrayals() {

		// Adding the city portrayal
		cityPortrayal.setField(((Migration) state).cityGrid);
		cityPortrayal.setPortrayalForAll(new OvalPortrayal2D(new Color(255, 154, 146), 5.0, true));

		// Adding the road portrayal
		roadLinkPortrayal.setField(((Migration) state).roadLinks);
		roadLinkPortrayal.setPortrayalForAll(new GeomPortrayal(new Color(24, 28, 242), 1, true));

		// Adding the region portrayal
		regionPortrayal.setField(((Migration) state).regions);
		regionPortrayal.setPortrayalForAll(new GeomPortrayal(new Color(188, 195, 196), 1, true));
	}

	// display refresh each step
	public void setupMovingPortrayals() {
		
		cityPortrayal.setField(((Migration) state).cityGrid);
		cityPortrayal.setPortrayalForAll(new OvalPortrayal2D(){
			@Override
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
			City city = (City) object;
			paint = new Color(255, 154, 146);
			//System.out.println(city.getName() + " Population: "  + city.getRefugeePopulation());
			double scale = city.getScale()*200;
			System.out.println(scale);
			super.scale = scale;
			super.filled = true;
			super.draw(object, graphics, info);
			}
		});
		
		
		refugeePortrayal.setField(((Migration) this.state).world);
		refugeePortrayal.setPortrayalForAll(new OvalPortrayal2D() {
			@Override
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

				Refugee refugee = (Refugee) object;
				// System.out.println(refugee);
				paint = new Color(255, 20, 215);
				//super.draw(object, graphics, info);
				super.filled = true;
				super.scale = 3;
				super.draw(object, graphics, info);
			}
		});

		display.reset();
		display.setBackdrop(Color.WHITE);
		display.repaint();
	}

	@Override
	public void quit() {
		super.quit();

		if (displayFrame != null)
			displayFrame.dispose();
		displayFrame = null;
		display = null;

	}

	public static void main(String[] args) {
		MigrationWithUI ebUI = new MigrationWithUI(new Migration(System.currentTimeMillis()));
		Console c = new Console(ebUI);
		c.setVisible(true);
	}
}
