import java.util.ArrayList;
import java.util.Collections;

/*
 * Containers are arranged in Stacks of equal size.
 * 
 * */

public class Stack<T extends Container>{
	
	public static final int STACK_MAX_SIZE = 4;
	
	private ArrayList<T> stack;
	
	public Stack(){
		stack = new ArrayList<T>();
	}
	
	public boolean put(T t){
		if (stack.size() == STACK_MAX_SIZE)
			return false;
		else{
			stack.add(t);
			//Stacks should always have heavier elements at bottom.
			Collections.sort(stack);
			return true;
		}
	}
	
	public ArrayList<T> get(){
		return stack;
	}
	
	public String toString(){
		return String.format("%s", stack);
	}
}
