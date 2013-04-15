import java.util.Random;

public class LaunchPackaging {

	private static final int NUMBER_OF_EVOLUTIONS = 5000;
	
	// The size of the population (number of chromosomes in the genotype)    
    private static final int SIZE_OF_POPULATION = 50;
    
    
    public static void main(String[] args){
   
        Random random = new Random();
        int randomNumber;
    	Vessel vessel = new Vessel();
    	Stack<Container> stack = new Stack<Container>();
    	
    	for (int i=0 ; i< Vessel.GRID_SIZE * Stack.STACK_MAX_SIZE; i++){
    	
    		randomNumber = 1 + random.nextInt(100);;
    		Container c = new Container(i, randomNumber);
    		
    		if (stack.put(c)){
    			
    		}else{
    			vessel.put(stack);
    			stack = new Stack<Container>();
    			stack.put(c);
    		}
    		
    	}
    	
    	System.out.println(vessel);
    }
    
}
