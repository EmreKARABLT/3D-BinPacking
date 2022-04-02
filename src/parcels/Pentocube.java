/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package parcels;

import algorithms.MatrixMult;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * A class that is responsible for creating and managing the pentocubes
 */
public class Pentocube implements Parcel {

	int[][][] pentocube;
	private int value;
	private int volume ;
	private int id;
	private int pieceNumber ;
	private static int no = 1;
	private int length ;
	private int width ;
	private int height ;

	/**
	 * Default constructor for pentocubes
	 */
	public Pentocube(){}

	/**
	 * Constructor that creates 3D pentomino cubes "L" "P" and "T"
	 * @param id a variable used to recognise a pentocube type
	 */
	public Pentocube(int id){
		if( id == -1 ) {
			pentocube = new int[][][]{{{-1, -1}, {-1, 0}, {-1, 0}, {-1, 0}}};
		}
		if( id == -2 ) {
			pentocube = new int[][][]{ { {-2, 0 } , {-2, -2 } , {-2, -2 } } };
		}
		if( id == -3){
			pentocube = new int[][][]{ { { 0, -3, 0 } , { 0 ,-3, 0 } , { -3, -3, -3} } };
		}
		this.id = id;
		this.volume = 5;
		pieceNumber = no++;
		length = this.getLength();
		height = this.getHeight();
		width = this.getWidth();
	}

	/**
	 * Constructor that creates "L", "P", and "T" 3D pentocubes with a provided value
	 * @param id a variable used to recognise pentocube type
	 * @param value  the value of the pentocube
	 */
	public Pentocube(int id , int value ){
		this(id);
		this.value = value;
	}

	/**
	 * Method that creates a hashmap that maps arraylists with all possible 3D rotation of each cubic parcel to its type (integer id)
	 * @return hashmap that represents all rotations of A,B,C parecles in 3D
	 */
	@Override
	public HashMap<Integer, ArrayList<Parcel>> allRotations() {

		HashMap< Integer , ArrayList<Parcel> > rotations = new HashMap<>();
		int[] items = new int[]{-1,-2,-3};
		for(int c = 0 ; c < items.length ; c++) {
			ArrayList<Parcel> rotationsOfPiece = new ArrayList<>();
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					for (int k = 0; k < 4; k++) {
						Pentocube p = new Pentocube( items[c] );
						p.rotateAroundX(i,p);
						p.rotateAroundY(j, p);
						p.rotateAroundZ(k, p);
						rotationsOfPiece.add(p);
					}
				}
			}
			rotations.put( items[c] , removeDuplicates(rotationsOfPiece ));
		}
		return rotations;
	}
	/**
	 * Method that rotates a cube of this class by i*90 degrees around Z axis
	 * @param amountOfRotation the amount of times a cube is to be rotated around z axis
	 */
	public void rotateAroundZ(int amountOfRotation , Parcel p) {
		int[][][] ppp = p.get3D();
		int cos = (int)Math.cos(Math.PI/2 * amountOfRotation);
		int sin = (int)(Math.sin(Math.PI/2 * amountOfRotation));
		int[][] rotationZ = new int[][]{ { cos , -1 * sin , 0 , 0 }, { sin , cos , 0 , 0}, {0 , 0 , 1 , 0} , { 0 , 0 , 0 , 1}};

		for (int i = 0; i < amountOfRotation; i++) {

			int temp = this.length;
			this.length = this.width;
			this.width = temp;
		}

		ArrayList<int[]> newCoordinates = new ArrayList<>();
		for (int x = 0; x < ppp.length; x++) {
			for (int y = 0; y < ppp[0].length; y++) {
				for (int z = 0; z < ppp[0][0].length; z++) {
					int[][] rotated = MatrixMult.multiplyMatrices( new int[][]{{ x , y, z  , ppp[x][y][z] }} ,rotationZ );
					newCoordinates.add(rotated[0]);
				}
			}
		}
		for (int j = 0; j < 3; j++) {
			int min = 0 ;
			for (int[] newCoordinate : newCoordinates) {
				if (newCoordinate[j] < min)
					min = newCoordinate[j];
			}
			for (int[] newCoordinate : newCoordinates) {
				newCoordinate[j] += Math.abs(min);
			}
		}
		int[] dimensions = new int[3];
		for (int j = 0; j < 3; j++) {
			int max = newCoordinates.get(0)[j] ;
			for (int[] newCoordinate : newCoordinates) {
				if (newCoordinate[j] > max )
					max = newCoordinate[j];
			}
			dimensions[j] = max+1 ;

		}
		int[][][] arr = new int[dimensions[0]][ dimensions[1] ][dimensions[2] ];


		for(int i = 0 ; i < arr.length * arr[0].length * arr[0][0].length; i++) {
			arr[ newCoordinates.get(i)[0] ][ newCoordinates.get(i)[1] ][ newCoordinates.get(i)[2] ] = newCoordinates.get(i)[3];
		}
		p.set3D(arr);
	}
	/**
	 * Method that rotates a cube of this class by i*90 degrees around X axis
	 * @param amountOfRotation the amount of times a cube is to be rotated around x-axis
	 */
	public void rotateAroundX(int amountOfRotation , Parcel p) {
		int cos = (int)Math.cos(Math.PI/2 * amountOfRotation);
		int sin = (int)(Math.sin(Math.PI/2 * amountOfRotation));
		int[][] rotationX = new int[][]{ { 1 , 0 , 0 , 0 }, { 0 , cos , sin , 0}, {0 , -1 * sin , cos , 0} , { 0 , 0 , 0 , 1}};
		int[][][] ppp = p.get3D();

		for (int i = 0; i < amountOfRotation; i++) {

			int temp = this.length;
			this.length = this.height;
			this.height = temp;
		}

		ArrayList<int[]> newCoordinates = new ArrayList<>();
		for (int x = 0; x < ppp.length; x++) {
			for (int y = 0; y < ppp[0].length; y++) {
				for (int z = 0; z < ppp[0][0].length; z++) {
					int[][] rotated = MatrixMult.multiplyMatrices( new int[][]{{ x , y, z  , ppp[x][y][z] }} ,rotationX );
					newCoordinates.add(rotated[0]);
				}
			}
		}
		for (int j = 0; j < 3; j++) {
			int min = 0 ;
			for (int[] newCoordinate : newCoordinates) {
				if (newCoordinate[j] < min)
					min = newCoordinate[j];
			}
			for (int[] newCoordinate : newCoordinates) {
				newCoordinate[j] += Math.abs(min);
			}
		}
		int[] dimensions = new int[3];
		for (int j = 0; j < 3; j++) {
			int max = newCoordinates.get(0)[j] ;
			for (int[] newCoordinate : newCoordinates) {
				if (newCoordinate[j] > max )
					max = newCoordinate[j];
			}
			dimensions[j] = max+1 ;

		}
		int[][][] arr = new int[dimensions[0]][ dimensions[1] ][dimensions[2] ];

		for(int i = 0 ; i < arr.length * arr[0].length * arr[0][0].length; i++) {
			arr[ newCoordinates.get(i)[0] ][ newCoordinates.get(i)[1] ][ newCoordinates.get(i)[2] ] = newCoordinates.get(i)[3];
		}
		p.set3D(arr);
	}
	/**
	 * Method that rotates a cube of this class by i*90 degrees around Y axis
	 * @param amountOfRotation the amount of times a cube is to be rotated around Y axis
	 */
	public void rotateAroundY(int amountOfRotation , Parcel p) {
		int cos = (int)Math.cos(Math.PI/2 * amountOfRotation);
		int sin = (int)(Math.sin(Math.PI/2 * amountOfRotation));
		int[][] rotationY = new int[][]{ { cos , 0 , -1 * sin , 0 }, { 0 , 1 , 0 , 0}, {sin , 0 , cos , 0} , { 0 , 0 , 0 , 1}};
		int[][][] ppp = p.get3D();

		for (int i = 0; i < amountOfRotation; i++) {

			int temp = this.length;
			this.length = this.width;
			this.width = temp;
		}

		ArrayList<int[]> newCoordinates = new ArrayList<>();
		for (int x = 0; x < ppp.length; x++) {
			for (int y = 0; y < ppp[0].length; y++) {
				for (int z = 0; z < ppp[0][0].length; z++) {
					int[][] rotated = MatrixMult.multiplyMatrices( new int[][]{{ x , y, z  , ppp[x][y][z] }} ,rotationY );
					newCoordinates.add(rotated[0]);
				}
			}
		}
		for (int j = 0; j < 3; j++) {
			int min = 0 ;
			for (int[] newCoordinate : newCoordinates) {
				if (newCoordinate[j] < min)
					min = newCoordinate[j];
			}
			for (int[] newCoordinate : newCoordinates) {
				newCoordinate[j] += Math.abs(min);
			}
		}
		int[] dimensions = new int[3];
		for (int j = 0; j < 3; j++) {
			int max = newCoordinates.get(0)[j] ;
			for (int[] newCoordinate : newCoordinates) {
				if (newCoordinate[j] > max )
					max = newCoordinate[j];
			}
			dimensions[j] = max+1 ;

		}
		int[][][] arr = new int[dimensions[0]][ dimensions[1] ][dimensions[2] ];

		for(int i = 0 ; i < arr.length * arr[0].length * arr[0][0].length; i++) {
			arr[ newCoordinates.get(i)[0] ][ newCoordinates.get(i)[1] ][ newCoordinates.get(i)[2] ] = newCoordinates.get(i)[3];
		}
		p.set3D(arr);
	}

	/**
	 * Method that checks whether two parcels are equal in terms of their dimensions and IDs
	 * @param p1 1st parcel for comparison
	 * @param p2 2nd parcel for comparison
	 * @return true if all dimensions  and IDs of both parcels are exactly the same
	 */
	public boolean isEqual(Parcel p1, Parcel p2){


		if((p1.getHeight() != p2.getHeight()) || ( p1.getWidth() != p2.getWidth()) || (p1.getLength() != p2.getLength())){ return false; }

		for (int i = 0; i < p2.getLength(); i++) {
			for (int j = 0; j < p2.getWidth(); j++) {
				for (int k = 0; k < p2.getHeight(); k++) {
					if(p1.getInt(i,j,k) != p2.getInt(i,j,k)){
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Method that removes duplicates of parcels from an ArrayList of parcels
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
	 * Method that returns an integer that represents a pentocube number/type at a given position in the pentocube
	 * @param x x-coordinate of the position
	 * @param y y-coordinate of the position
	 * @param z z-coordinate of the position
	 * @return an integer at the specified position
	 */
	public int getInt(int x , int y , int z){
		return this.pentocube[x][y][z];
	}

	//GETTERS FOR CUBE'S INSTANCE FIELDS
	/**
	 *Getter for pentocube's height
	 * @return pentocube's height
	 */
	public int getLength(){ return pentocube.length; }
	/**
	 *Getter for pentocube's width
	 * @return pentocube's width
	 */
	public int getWidth(){ return pentocube[0].length; }
	/**
	 *Getter for pentocube's length
	 * @return pentocube's length
	 */
	public int getHeight(){ return pentocube[0][0].length; }
	/**
	 *Getter for pentocube's ID
	 * @return pentocube's ID
	 */
	public int getId(){ return id; }
	/**
	 *Getter for pentocube's unique piece Number
	 * @return pentocube's unique piece Number
	 */
	public int getPieceNumber(){ return pieceNumber; }
	/**
	 *Getter for pentocube's value
	 * @return pentocube's value
	 */
	public int getValue(){ return value; }
	/**
	 * Getter for pentocube's volume
	 * @return pentocube's volume
	 */
	public double getVolume(){ return volume; }
	/**
	 * Getter for pentocube's value/volume ratio
	 * @return pentocube's value/volume ratio
	 */
	public double getValueVolumeRatio(){ return (value*1.0)/volume; }
	/**
	 * Getter for pentocube's type
	 * @return pentocube's type ( a , b , c )
	 */
	public String getType() {
		return "P";}
	/**
	 * Getter for the pentocube's 3D representation
	 * @return 3D array that represents the pentocube
	 */
	public int[][][] get3D(){ return pentocube; }
	/**
	 * Setter for pentocubes 3D array
	 * @param pentocube 3D array that will be assigned as pentocube
	 */
	public void set3D(int[][][] pentocube) { this.pentocube = pentocube;	}



}

