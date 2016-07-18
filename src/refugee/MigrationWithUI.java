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
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.geo.MasonGeometry;
import sim.util.media.chart.ChartGenerator;
import sim.util.media.chart.TimeSeriesChartGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MigrationWithUI extends GUIState
{
    Display2D display; //displaying the model
    JFrame displayFrame; //frame containing all the displays


    public MigrationWithUI(Migration sim)
    {
        super(sim);
    }

    @Override
    public void init(Controller c)
    {
        super.init(c);

        //set dimen and position of controller
        ((Console)c).setSize(350, 80);
        ((Console)c).setLocation(0, 680);


        display = new Display2D(1180, 1180, this); //creates the display
        //display.setRefresRate(32);
        //display.setScale(2);

        displayFrame = display.createFrame();
        c.registerFrame(displayFrame);
        displayFrame.setVisible(true);
        displayFrame.setSize(1870, 1180);
    }

    @Override
    public void start()
    {
        super.start();

        setupPortrayals();
    }

    public void setupPortrayals()
    {

      /*  GeomVectorFieldPortrayal adminShapePortrayal = new GeomVectorFieldPortrayal();
        adminShapePortrayal.setField(((EbolaABM) state).adminShape);
        adminShapePortrayal.setPortrayalForAll(new GeomPortrayal(new Color(0.42f, 0.42f, 0.42f, 0.5f), 2.0, true) {
            @Override
            public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
            {
                MasonGeometry mg = (MasonGeometry)object;

                String countryString = mg.getStringAttribute("ISO");
                int country;
                if(countryString.equals("GIN"))
                    country = Parameters.GUINEA;
                else if(countryString.equals("LBR"))
                    country = Parameters.LIBERIA;
                else
                    country = Parameters.SL;
                HashMap<Integer, Integer> temp = ((EbolaABM)state).adminInfectedTotals.get(country);
                int admin_id = mg.getIntegerAttribute("IPUMSID");
                int cases = 0;
                if(temp.containsKey(admin_id))
                    cases = temp.get(admin_id);

                if(cases == 0)
                    paint = Color.WHITE;
                if(cases > 0 && cases <= 10)
                    paint = new Color(0.9529412f, 0.79607844f, 0.7372549f);
                else if(cases > 10 && cases <= 50)
                    paint = new Color(227, 137, 109);
                else if(cases > 50 && cases <= 100)
                    paint = new Color(206, 82, 66);
                else if(cases > 100 && cases <= 250)
                    paint = new Color(196, 6, 3);
                else if(cases > 250 && cases <= 500)
                    paint = new Color(128, 25, 32);
                else if(cases > 500)
                    paint = new Color(49, 15, 14);

                super.draw(object, graphics, info);
            }
        });
        display.attach(adminShapePortrayal, "Admin Shape");
        */

       /*
          FieldPortrayal2D citiesportrayal = new SparseGridPortrayal2D();
        
        citiesportrayal.setField(((Migration)state).cityGrid);
       
        //citiesportrayal.setPortrayalForAll(new RectanglePortrayal2D(new Color(0, 128, 255), 8.0, true));
        citiesportrayal.setPortrayalForAll(new RectanglePortrayal2D(new Color(0, 128, 255), 8.0, true));
        {
            @Override
            public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
            {
                City city = (City) object;
                    //paint = new Color(216, 10, 255);
                    
                    paint = new Color(227, 137, 109);
                super.draw(object, graphics, info);
            }
        });
        */
        GeomVectorFieldPortrayal citiesportrayal = new GeomVectorFieldPortrayal();
        citiesportrayal.setField(((Migration) state).cityPoints);
        //roadLinkPortrayal.setPortrayalForAll(new GeomPortrayal(new Color(0.42f, 0.42f, 0.42f, 0.5f), 2.0, true));
        citiesportrayal.setPortrayalForAll(new GeomPortrayal(new Color(0, 0, 255), 2.0, true));
        
        display.attach(citiesportrayal, "Cities");

//        FieldPortrayal2D urbanPortrayal = new SparseGridPortrayal2D();
//        urbanPortrayal.setField(((EbolaABM)state).urbanAreasGrid);
//        urbanPortrayal.setPortrayalForAll(new RectanglePortrayal2D(new Color(255, 21, 19), 1.0, false));
//        display.attach(urbanPortrayal, "Urban Area");

        //---------------------Adding the road portrayal------------------------------
        GeomVectorFieldPortrayal roadLinkPortrayal = new GeomVectorFieldPortrayal();
        roadLinkPortrayal.setField(((Migration) state).roadLinks);
        //roadLinkPortrayal.setPortrayalForAll(new GeomPortrayal(new Color(0.42f, 0.42f, 0.42f, 0.5f), 2.0, true));
        roadLinkPortrayal.setPortrayalForAll(new GeomPortrayal(new Color(216, 10, 255), 0.1, true));
        display.attach(roadLinkPortrayal, "Roads");

        ContinuousPortrayal2D refugeePortrayal = new ContinuousPortrayal2D();

        refugeePortrayal.setField(((Migration)this.state).world);
        refugeePortrayal.setPortrayalForAll(new OvalPortrayal2D()
        {
            public void draw (Object object, Graphics2D graphics, DrawInfo2D info)
            {
                Refugee refugee = (Refugee)object;
                paint = new Color(255, 20, 215);
                super.draw(object, graphics, info);
            }
        });
        display.attach(refugeePortrayal, "Refugees");

        //will need
      /*  GeomVectorFieldPortrayal boundaryPortrayal = new GeomVectorFieldPortrayal();
        boundaryPortrayal.setField(((EbolaABM)state).adminBoundaries);
        boundaryPortrayal.setPortrayalForAll(new GeomPortrayal(Color.BLACK, true));
        display.attach(boundaryPortrayal, "Boundaries");
        */
    }

    @Override
    public void quit()
    {
        super.quit();

        if (displayFrame != null)
            displayFrame.dispose();
        displayFrame = null;
        display = null;

    }

    public static void main(String[] args)
    {
        MigrationWithUI ebUI = new MigrationWithUI(new Migration(System.currentTimeMillis()));
        Console c = new Console(ebUI);
        c.setVisible(true);
    }
}
