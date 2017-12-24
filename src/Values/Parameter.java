package Values;

public class Parameter {

	private int generation;
	private double sizeX;
	private double sizeY;
	private double addNumber;
	private static Parameter instance = null;
	private Parameter(){}
	
	public static synchronized Parameter getInstance() {
		if (instance == null) {
			instance = new Parameter();
		}
		return instance;
	}
	
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	
	
	public void setPointParameter(double sizeX, double sizeY, double addNumber) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.addNumber = addNumber;
	}
	
	public int getGeneration() {
		return this.generation;
	}
	
	public double getSizeX () {
		return this.sizeX;
	}
	
	public double getSizeY () {
		return this.sizeY;
	}
	
	public double getAddNumber () {
		return this.addNumber;
	}
}
