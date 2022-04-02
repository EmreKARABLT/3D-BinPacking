/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package parcels;

/**
 * A class that is responsible for creating and managing the cargo space
 */
public class Container {

	private final int LENGTH;
	private final int WIDTH;
	private final int HEIGHT;
	private int totalValue = 0;
	private int[][][] container;

	/**
	 * Constructor that sets container's dimensions
	 * @param length provided length of container
	 * @param width provided width of the container
	 * @param height provided height of the container
	 */
	public Container(double length, double width, double height) {
		this.LENGTH = (int) length ;
		this.WIDTH = (int) width ;
		this.HEIGHT = (int) height ;
		this.container = buildContainer();
	}

	/**
	 * Method that creates an initial, empty container, where empty space is represented by 0
	 * @return 3D integer array that represents the empty container
	 */
	public int[][][] buildContainer() {
		int[][][] container = new int[LENGTH][WIDTH][HEIGHT];
		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < WIDTH; j++) {
				for (int k = 0; k < HEIGHT; k++) {
					container[i][j][k] = 0;
				}
			}
		}
		return container;
	}

	/**
	 * Overriden toString method of Object class that prints the state of the container
	 * @return a string representation of the int array (container)
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < this.HEIGHT; i++) {
			for (int j = 0; j < this.LENGTH; j++) {
				for (int k = 0; k < this.WIDTH; k++) {
					if (container[j][k][i] == 0)
						s.append(String.format("%3d%s", 0, " "));
					else
						s.append(String.format("%3d%s", container[j][k][i], " "));
				}
				s.append("| ");
			}
			s.append("\n");
		}
		System.out.printf("{ length : %d , width : %d , height : %d}\n" , LENGTH, WIDTH, HEIGHT);
		return s.toString();
	}

	/**
	 * A method that counts the amount of parcels of a given type that are present in the cargo space
	 * @param idNum the ID of the parcel's type
	 * @param type the type of the parcel
	 * @return the number of parcels of a given type
	 */
	public int countNumberOfGivenType(int idNum , String type){
		int number = 0;

		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < WIDTH; j++) {
				for (int k = 0; k < HEIGHT; k++) {
					if (container[i][j][k] == idNum)
						number++;
				}
			}
		}
		Parcel c ;
		if(type.equalsIgnoreCase("c")) {
			c = new Cube(idNum);

		}else
			c = new Pentocube(idNum);
		return (int)(number/ c.getVolume());
	}

	/**
	 * A method that is used to check the total value of the container, as it adds the value of the parcel and is used whenever a new parcel is added to the cargo space
	 * @param value the value of the parcel
	 */
	public void addValue(int value){ this.totalValue+= value ;}

	//GETTERS FOR CONTAINER'S INSTANCE FIELDS

	/**
	 *Getter for container's height
	 * @return container's height
	 */
	public int getHEIGHT(){ return HEIGHT; }

	/**
	 *Getter for container's width
	 * @return container's width
	 */
	public int getWIDTH(){ return WIDTH; }
	/**
	 *Getter for container's length
	 * @return container's length
	 */
	public int getLENGTH(){ return LENGTH; }

	/**
	 * Getter for the container's 3D representation
	 * @return 3D array that represents the container
	 */
	public int[][][] getContainer(){ return this.container; }

	/**
	 * Getter for the number at given position
	 * @param x x-coordinate in the container
	 * @param y y-coordinate in the container
	 * @param z z-coordinate in the container
	 * @return the number at given location
	 */
	public int getInt(int x , int y , int z){ return this.container[x][y][z]; }

	/**
	 * Getter for the number at the container
	 * @return the number at the container
	 */
	public int getTotalValue(){ return totalValue; }
	//SETTERS FOR CONTAINER'S INSTANCE FIELDS

	/**
	 * Setter for the number at given location
	 * @param x x-coordinate in the container
	 * @param y y-coordinate in the container
	 * @param z z-coordinate in the container
	 * @param number the number which will be assigned for given location
	 */
	public void setInt(int x , int y , int z , int number){ this.container[x][y][z] = number; }
}