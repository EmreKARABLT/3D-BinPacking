/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package parcels;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that is responsible for creating and managing the cubes
 */
public class Cube implements Parcel {

	private int length ;
	private int width ;
	private int height;
	private double volume;
	private int id;
	private int value;
	private int[][][] cube;
	private int pieceNumber ;
	private static int no = 1;

	/**
	 * Default constructor for cubes
	 */
	public Cube(){}

	/**
	 * Constructor that creates a 3D cube with given dimensions
	 * @param length length of the cube
	 * @param width width of the cube
	 * @param height height of the cube
	 */
	public Cube(int length ,int width, int height ){
		this.length = length;
		this.width =  width ;
		this.height = height ;
		this.volume = calculateVolume();
		this.cube = buildCube();
		pieceNumber = no++;
	}

	/**
	 * Constructor that creates 3D cubes of "A", "B" and "C" types
	 * @param id a variable used to recognise a cube type
	 */
	public Cube(int id){
		if(id == -1){ this.length = 2; this.width = 2; this.height = 4; }
		if(id == -2){ this.length = 2; this.width = 3; this.height = 4; }
		if(id == -3){ this.length = 3; this.width = 3; this.height = 3; }
		this.id = id;
		this.cube = buildCube();
		this.volume = calculateVolume();
		pieceNumber = no++;
	}

	/**
	 * Constructor that creates "A", "B", and "C" 3D cubes with a provided value
	 * @param id a variable used to recognise cube type
	 * @param value the value of the cube
	 */
	public Cube(int id , int value ){
		this(id);
		this.value = value;
	}

	/**
	 * This method creates the 3d array with dimensions of length x width x height.
	 * @return 3d array that represents the cube filled with its id
	 */
	public int[][][] buildCube(){
		cube = new int[length][width][height];
		for(int i = 0 ; i < length ; i++){
			for (int j = 0; j < width; j++) {
				for (int k = 0; k < height; k++) {
					cube[i][j][k] = id ;
				}
			}
		}
		return cube;
	}

	/**
	 * Method that calculates volume of the cube
	 * @return volume of a cube
	 */
	int calculateVolume(){ return this.length * this.width * this.height ;}



	//ROTATION
	/**
	 * Method that creates a hashmap that maps arraylists with all possible 3D rotation of each cubic parcel to its type (integer id)
	 * @return hashmap that represents all rotations of A,B,C parecles in 3D
	 */
	public HashMap< Integer , ArrayList<Parcel> > allRotations(){
		HashMap< Integer , ArrayList<Parcel> > rotations = new HashMap<>();
		int[] items = new int[]{-1,-2};
		for (int item : items) {

			ArrayList<Parcel> rotationsOfPiece = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					for (int k = 0; k < 4; k++) {
						Cube p = new Cube(item);
						p.rotateAroundX(i, p);
						p.rotateAroundY(j, p);
						p.rotateAroundZ(k, p);
						rotationsOfPiece.add(p);
					}
				}
			}
			rotations.put(item, removeDuplicates(rotationsOfPiece));
		}
		Parcel p = new Cube(-3);
		ArrayList<Parcel> cRotations = new ArrayList<>();
		cRotations.add(p);
		rotations.put(-3 , cRotations);

		return rotations;
	}
	/**
	 * Method that rotates a cube of this class by i*90 degrees around Z axis
	 * @param i the amount of times a cube is to be rotated around z axis
	 */
	public void rotateAroundX(int i , Parcel p){
		for (int j = 0; j < i; j++) {
			int temp = height;
			height = width;
			width = temp;
		}
		this.buildCube();
	}
	/**
	 * Method that rotates a cube of this class by i*90 degrees around Z axis
	 * @param i the amount of times a cube is to be rotated around z axis
	 */
	public void rotateAroundY(int i , Parcel p){
		for (int j = 0; j < i; j++) {
			int temp = length;
			length = height;
			height = temp;
		}
		this.buildCube();
	}
	/**
	 * Method that rotates a cube of this class by i*90 degrees around Z axis
	 * @param i the amount of times a cube is to be rotated around z axis
	 */
	public void rotateAroundZ(int i , Parcel p){
		for (int j = 0; j < i; j++) {
			int temp = width;
			width = length;
			length = temp;
		}
		this.buildCube();
	}
	/**
	 * Method that checks whether two parcels are equal in terms of their dimensions and IDs
	 * @param p1 1st parcel for comparison
	 * @param p2 2nd parcel for comparison
	 * @return true if all dimensions  and IDs of both parcels are exactly the same
	 */
	public boolean isEqual(Parcel p1, Parcel p2){
		if((p1.getHeight() != p2.getHeight()) || ( p1.getWidth() != p2.getWidth()) || (p1.getLength() != p2.getLength())){ return false; }

		for (int i = 0; i < p1.getLength(); i++) {
			for (int j = 0; j < p1.getWidth(); j++) {
				for (int k = 0; k < p1.getHeight(); k++) {
					if(p1.getInt(i,j,k) != p2.getInt(i,j,k)){
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Method that removes duplicates of parcels from a arraylist of parcels
	 * @param list the arraylist of parcels to be evaluated
	 * @return list without the same parcels (duplicates)
	 */
	public ArrayList<Parcel> removeDuplicates(ArrayList<Parcel> list){
		ArrayList<Parcel> l = list ;
		for (int i = 0; i < l.size()-1 ; i++) {
			for (int j = i + 1 ; j < l.size() ; j++) {
				if( isEqual( l.get(i) , l.get(j) )){
					l.remove(j);
					j--;
				}
			}
		}
		return l;
	}

	/**
	 * Method that returns an integer that represents a cube number/type at a given position of the cube
	 * @param x x-coordinate of the position
	 * @param y y-coordinate of the position
	 * @param z z-coordinate of the position
	 * @return an integer at the specified position
	 */
	public int getInt(int x , int y , int z){
		return this.cube[x][y][z];
	}

	//GETTERS FOR CUBE'S INSTANCE FIELDS
	/**
	 *Getter for cube's height
	 * @return cube's height
	 */
	public int getHeight(){return cube[0][0].length;}
	/**
	 *Getter for cube's width
	 * @return cube's width
	 */
	public int getWidth(){return cube[0].length;}
	/**
	 *Getter for cube's length
	 * @return cube's length
	 */
	public int getLength(){return cube.length;}
	/**
	 *Getter for cube's ID
	 * @return cube's ID
	 */
	public int getId(){return id;}
	/**
	 *Getter for cube's unique piece Number
	 * @return cube's unique piece Number
	 */
	public int getPieceNumber(){return pieceNumber;}
	/**
	 *Getter for cube's value
	 * @return cube's value
	 */
	public int getValue(){return value;}

	/**
	 * Getter for cube's volume
	 * @return cube's volume
	 */
	public double getVolume(){ return volume;}
	/**
	 * Getter for cube's value/volume ratio
	 * @return cube's value/volume ratio
	 */
	public double getValueVolumeRatio(){ return (value * 1.0)/volume; }
	/**
	 * Getter for the cube's 3D representation
	 * @return 3D array that represents the cube
	 */
	public int[][][] get3D(){ return cube;}

	/**
	 * Getter for cube's type
	 * @return cube's type ( a , b , c )
	 */
	public String getType() {
		return "C";}

	/**
	 * Setter for cubes 3D array
	 * @param cube 3D array that will be assigned as cube
	 */
	public void set3D(int[][][] cube){
		this.cube = cube;
		this.length = this.getLength();
		this.width = this.getWidth();
		this.height = this.getHeight();
	}



}
