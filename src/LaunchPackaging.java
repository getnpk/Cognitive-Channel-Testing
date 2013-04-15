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

	private static final int NUMBER_OF_EVOLUTIONS = 2000;
	
	// The size of the population (number of chromosomes in the genotype)    
    private static final int SIZE_OF_POPULATION = 50;
    
    private Vessel vessel;
    private Stack<Container> stack;
    private Genotype genotype;
    
    private ArrayList<Container> container;
    
    public LaunchPackaging(){

        initializeVessel();
        try {
			genotype = configureJGAP();
		} catch (InvalidConfigurationException e) {
			// TODO Fix me.
			e.printStackTrace();
		}
        
        evolve(genotype);
    }


	private void initializeVessel() {
		Random random = new Random();
        int randomNumber;
    	vessel = new Vessel();
    	stack = new Stack<Container>();
    	
    	container = new ArrayList<Container>();
    	
    	for (int i=0 ; i<= Vessel.GRID_SIZE * Stack.STACK_MAX_SIZE; i++){
    	
    		randomNumber = 1 + random.nextInt(100);;
    		Container c = new Container(i, randomNumber);
    		container.add(c);
    		
    		if (stack.put(c)){
    			
    		}else{
    			vessel.put(stack);
    			stack = new Stack<Container>();
    			stack.put(c);
    		}
    	}
    	System.out.println(vessel);
	}
    
    
	private Genotype configureJGAP() throws InvalidConfigurationException {
		
		Configuration gaConf = new DefaultConfiguration();
		
		/* Specify a fitness evaluator where lower values means a 
		 * better fitness (MIN)
		 */
		Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
		gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());

		/* 
		 * Use the swapping operator and the size of the 
		 * chromosome must remain constant (MUTATION)
		 */
		gaConf.getGeneticOperators().clear();
		SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf);
		gaConf.addGeneticOperator(swapper);
		
		// Selection of fittest child and population optimisation.
        gaConf.setPreservFittestIndividual(true);
		gaConf.setKeepPopulationSizeConstant(false);
		
		// Set the child chromosome size.
		gaConf.setPopulationSize(SIZE_OF_POPULATION);
		
        // The total number of chromosomes in our case.
        int chromeSize = Vessel.GRID_SIZE * Stack.STACK_MAX_SIZE;
		Genotype genotype;
		
		/* 
		 * Setup the structure with which to evolve the solution of the problem.
		 * An IntegerGene is used. This gene represents the index of container?
		 */
		IChromosome sampleChromosome = new Chromosome(gaConf, new IntegerGene(gaConf), chromeSize);
		gaConf.setSampleChromosome(sampleChromosome);
		
        // Setup the fitness function
		LoadFitnessFunction fitnessFunction = new LoadFitnessFunction();
		fitnessFunction.setVessel(vessel);
		gaConf.setFitnessFunction(fitnessFunction);
		
		/* 
		 * TODO RECHECK
		 * Because the IntegerGenes are initialised randomly, 
		 * it is necessary to set the values to the index. Values range from 0 to
		 * total number of containers.
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
	
	private void evolve(Genotype genotype) {
		// TODO Auto-generated method stub
		double previousFittest = genotype.getFittestChromosome().getFitnessValue();
		
		for (int i=0; i< NUMBER_OF_EVOLUTIONS; i++){
			
			// Just to know status
			if (i % 250 == 0)
				System.out.println("Number of Evolutions: " + i);
			
			genotype.evolve();
			double fittness = genotype.getFittestChromosome().getFitnessValue();
			
			if (fittness < previousFittest){
				previousFittest = fittness;
			}
			
		}
		IChromosome fittest = genotype.getFittestChromosome();
		
		System.out.println("Fittness Value: " + fittest.getFitnessValue());
		
		printStack(fittest.getGenes());
	}


	private void printStack(Gene[] genes) {
		
		Vessel vessel = new Vessel();
		Stack<Container> stack = new Stack<Container>();
		
		for (Gene gene: genes){
			int index = (Integer) gene.getAllele();
			Container c = this.container.get(index);
			
			if (stack.put(c)){
    			
    		}else{
    			vessel.put(stack);
    			stack = new Stack<Container>();
    			stack.put(c);
    		}
		}
		
		System.out.println(vessel);
		
	}

    
	public static void main(String[] args){
		new LaunchPackaging();
	}
}
