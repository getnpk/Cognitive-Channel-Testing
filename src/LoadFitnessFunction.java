/*
 * @author: Nitin Pradeep Kumar
 * */

import java.util.ArrayList;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class LoadFitnessFunction extends FitnessFunction{

	private Vessel vessel;
	
	public void setVessel(Vessel vessel){
		this.vessel = vessel;
	}
	
	private int[] indexes;
	/*
	 * Fitness function: A lower value implies better results.
	 * Weight should be evenly distributed among stacks in Vessel.
	 * The distribution of weights in Vessel should be more or less uniform.
	 * */
	@Override
	protected double evaluate(IChromosome aSubject) {
		// TODO Verification
		int eval = 0;
		int i = 0;
		
		int totalWeight = vessel.getTotalVesselWeight();
		int totalAverage = totalWeight / Vessel.GRID_SIZE;
		
		indexes = new int[Vessel.GRID_SIZE];
		
		ArrayList<Stack<Container>> repo = vessel.getStackList();
		
		for (Stack<Container> s: repo){
			indexes[i] = Math.abs(totalAverage - s.getTotalStackWeight() / Stack.STACK_MAX_SIZE);
			i++;
		}
		
		for (int j : indexes)
			eval += j;
		
		return eval;
	}

}
