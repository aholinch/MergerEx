/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.ui.search;

import edu.gmu.cds.sim.SimSummary;
import edu.gmu.cds.sim.StateInfo;
import edu.gmu.cds.ui.ScatterPanel;

public interface SimulationHandler 
{
    public void runSimulation(StateInfo info, ScatterPanel panel, int numParticles, boolean animate, boolean runFast);
    
    public void runRandomSimulation(ScatterPanel panel, int numParticles, boolean animate);
    
    public void runRandomSimulations(ScatterPanel panels[], int numParticles, boolean animate);
    
    public void runSimulation(StateInfo info, ScatterPanel panel, boolean animate);
    
    public void runRandomSimulation(ScatterPanel panel, boolean animate);
    
    public void runRandomSimulations(ScatterPanel panels[], boolean animate);
    
    /**
     * Add the related StateInfo to the selected list.
     * 
     * @param panel
     */
    public void selectSimulation(ScatterPanel panel);
    
    /**
     * Remove the related StateInfo from the selected list.
     * 
     * @param panel
     */
    public void unselectSimulation(ScatterPanel panel);
    
    /**
     * Add the related StateInfo to the selected list.
     * 
     * @param panel
     */
    public void selectSimulation(StateInfo info);
    
    /**
     * Remove the related StateInfo from the selected list.
     * 
     * @param panel
     */
    public void unselectSimulation(StateInfo info);
    
    public void incrementEnhanced();
    
    public SimSummary getSimSummary();
    
    /**
     * Refresh the number of enhanced, evaluated, selected, and mergerwars comps.
     */
    public void updateSummary();
    
}
