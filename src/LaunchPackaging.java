/*
 * Launch Packaging application.
 * Most values hard codes as of now.
 * 
 * @author: Nitin Pradeep Kumar
 * */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.SwappingMutationOperator;

public class LaunchPackaging {

	private static final int NUMBER_OF_EVOLUTIONS = 5000;
	
	// The size of the population (number of chromosomes in the genotype)    
    private static final int SIZE_OF_POPULATION = 50;
    
    private Vessel vessel;
    private Stack<Container> stack;
    
    private ArrayList<Container> container;
    
    public LaunchPackaging(){

        initializeVessel();
        try {
			Genotype genotype = configureJGAP();
			evolve(genotype);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}   
        
    }


	private void initializeVessel() {
		Random random = new Random();
        int randomNumber;
    	vessel = new Vessel();
    	stack = new Stack<Container>();
    	
    	container = new ArrayList<Container>();
    	
    	for (int i=0 ; i<= Vessel.GRID_SIZE * Stack.STACK_MAX_SIZE; i++){
    	
    		randomNumber = 10 + random.nextInt(100);
    		Container c = new Container(i, randomNumber);
    		container.add(c);
    		
    		if (stack.put(c)){
    			
    		}else{
    			vessel.put(stack);
    			stack = new Stack<Container>();
    			stack.put(c);
    		}
    	}
    	System.out.println("Vessel after Initialization: ");
    	System.out.println(vessel);
	}
    
    
	/**
     * Setup JGAP.
     */
    private Genotype configureJGAP() throws InvalidConfigurationException {
		
    	Configuration gaConf = new DefaultConfiguration();
		
    	// Lower values means a better fitness
		Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
		gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());

		/* Only use the swapping operator. 
		 * And the size of the chromosome must remain constant
		 */
		gaConf.getGeneticOperators().clear();
		SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf);
		gaConf.addGeneticOperator(swapper);

        // We are only interested in the most fittest individual
        gaConf.setPreservFittestIndividual(true);
		gaConf.setKeepPopulationSizeConstant(false);

		gaConf.setPopulationSize(SIZE_OF_POPULATION);
        
		/*The number of chromosomes is the number of Containers we have. 
         * Every chromosome represents one Container.
         */
        int chromeSize = Vessel.GRID_SIZE * Stack.STACK_MAX_SIZE;
		Genotype genotype;

		// Setup the structure with which to evolve the solution of the problem.
        // An IntegerGene is used. This gene represents the index of a Container.
		IChromosome sampleChromosome = new Chromosome(gaConf, new IntegerGene(gaConf), chromeSize);
		gaConf.setSampleChromosome(sampleChromosome);
		
        // Setup the fitness function
		LoadFitnessFunction fitnessFunction = new LoadFitnessFunction();
		fitnessFunction.setVessel(this.vessel);
		gaConf.setFitnessFunction(fitnessFunction);

		/* Because the IntegerGenes are initialized randomly, it is neccesary to 
		 * set the values to the index. Values range from 0..total number of containers.
		 */
		genotype = Genotype.randomInitialGenotype(gaConf);
		List chromosomes = genotype.getPopulation().getChromosomes();
        for (Object chromosome : chromosomes) {
            IChromosome chrom = (IChromosome) chromosome;
            for (int j = 0; j < chrom.size(); j++) {
                Gene gene = chrom.getGene(j);
                gene.setAllele(j);
            }
        }
        
		return genotype;
	}
	
    
	private void evolve(Genotype agenotype) {
		// TODO Recheck
		double previousFittest = agenotype.getFittestChromosome().getFitnessValue();
		
		for (int i=0; i< NUMBER_OF_EVOLUTIONS; i++){
			
			// Just to know status
			if (i % 250 == 0)
				System.out.println("Number of Evolutions: " + i);
			
			agenotype.evolve();
			double fittness = agenotype.getFittestChromosome().getFitnessValue();
					
			if (fittness < previousFittest){
				System.out.println("New Fittness: " + fittness);
				previousFittest = fittness;
			}
			
		}
		
		IChromosome fittest = agenotype.getFittestChromosome();
		
		System.out.println("Fittness Value: " + fittest.getFitnessValue());
		
		printStack(fittest.getGenes());
	}


	private void printStack(Gene[] genes) {
		
		// TODO CHECK THIS METHOD.
		Vessel finalVessel = new Vessel();
		Stack<Container> stack = new Stack<Container>();
		
		for (Gene gene: genes){
			int index = (Integer) gene.getAllele();
			int stackId = index / Stack.STACK_MAX_SIZE;
			int containerPos = index % Stack.STACK_MAX_SIZE;
			
			int containerId = this.vessel.getStackList().get(stackId).get().get(containerPos).getId();
			int containerWeight = this.vessel.getStackList().get(stackId).get().get(containerPos).getWeight();
					
			Container c = new Container(containerId, containerWeight);
			if (stack.put(c)){
    			
    		}else{
    			finalVessel.put(stack);
    			stack = new Stack<Container>();
    			stack.put(c);
    		}
		}
		
		System.out.println("Final suggestions: ");
		System.out.println(finalVessel);
		
	}

    
	public static void main(String[] args){
		new LaunchPackaging();
	}
}
