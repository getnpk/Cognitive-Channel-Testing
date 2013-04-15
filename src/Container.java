/*
 * The component to be loaded.
 * @author: nitin pradeep kumar
 * 
 * */
public class Container implements Comparable<Container>{

	private int id;
	private int weight;
	
	public Container(int id, int weight){
		this.id = id;
		this.weight = weight;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	public void setWeight(int newWeight){
		this.weight = newWeight;
	}

	@Override
	public int compareTo(Container other) {
		//Highest weight first
		if (this.weight > other.weight)
			return -1;
		else if (this.weight < other.weight)
			return 1;
		else
			return 0;	
	}
	
	public String toString(){
		return String.format("Container ID:%d Weight: %d", this.id, this.weight);
	}
}
