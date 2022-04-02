/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;

import parcels.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class is like a helper class for our DLX implementation.
 * It creates a database to be used by the DLX algorithm.
 */

public class DLXdatabase {

	//FIELD
	private Container container;
	private Container byNumber;
	private String type;
	private static int[][] dlxBase;
	ArrayList<int[]> database = new ArrayList<>();
	private final HashMap<Integer,ArrayList<Parcel>> ALL_ROTATIONS_CUBE = new Cube().allRotations();
	private final HashMap<Integer,ArrayList<Parcel>> ALL_ROTATIONS_PENTO = new Pentocube().allRotations();

	//CONSTRUCTORS
	/**
	 * Constructor using container and container byNumber
	 * @param container a container where parcels are represented by their IDs
	 * @param byNumber a container where parcels are represented by their creation number
	 * @param type type of parcels
	 */
	public DLXdatabase(Container container ,Container byNumber ,String type ){
		this.container = container ;
		this.byNumber = byNumber ;
		this.type = type ;
	}
	/**
	 * Empty 'default' constructors
	 */
	public DLXdatabase(){
	}

	//GETTERS
	/**
	 * Getter method for the DLXbase
	 * @return dlxBase database including pieceIDs in first column
	 */
	public int[][] getDlxBase() {
		return dlxBase;
	}
	/**
	 * Getter method that returns the database as a 2D array
	 * @return 2d array database
	 */
	int[][] getDatabase(){
		int[][] db = new int[database.size()][database.get(0).length];
		for (int i = 0 ; i < database.size() ; i++ ) {
			db[i] = database.get(i);
		}
		return db;
	}

	//HELPERS
	/**
	 * Convert 3d array into 1d array
	 * @param dim3 3d array to be converted
	 * @return 1d array after conversion
	 */
	public int[] convertArr(int[][][] dim3){
		int[] dim1 = new int[dim3.length * dim3[0].length * dim3[0][0].length ];
		int q =0;
		for (int x = 0; x <dim3.length ; x++) {
			for (int y = 0; y <  dim3[0].length; y++) {
				for (int z = 0; z <  dim3[0][0].length; z++) {
					if (dim3[x][y][z] == 0)
						dim1[q] = dim3[x][y][z];
					else
						dim1[q] = 1;
					q++;
				}
			}
		}
		return dim1;
	}
	/**
	 * Method to copy an array.
	 * @param src the original
	 * @param dest to coppy
	 * @return coppied array
	 */
	static int[] copyArray(int[] src , int[] dest){
		int[] array = new int[dest.length];
		array[0] = dest[0];
		System.arraycopy(src, 0, array, 1, src.length);
		return array;
	}
	/**
	 * Checks if inputArray has columns only containing 0s.
	 * The method returns true if the array has at least on column with only 0s.
	 * @param input the array that has to be investigated
	 * @return true/false depending
	 */
	static boolean zeroSumCol(int[][] input){
		int nColumns = input[0].length;
		int nRows = input.length;
		int count = 0;
		for(int i=0; i< nColumns ; i++){
			count=0;
			for(int j =0; j<nRows; j++){
				if(input[j][i]!=0){
					count++;
				}
			}
			if(count==0){
				return false;
			}
		}
		return true;
	}
	/**
	 * Method that converts an arraylist containing solution rows into a 2D array that can be used for visualization (with random values, for individual parcels).
	 * @param convertThis arraylist containing solution rows
	 * @return returns a 2D array with every individual piece with its own color ID.
	 */
	public static int[][] convertForVisualisation (ArrayList <Integer> convertThis){
		int[][] partialA = new int[convertThis.size()][dlxBase[0].length-1];
		for (int i = 0; i<convertThis.size();i++){
			partialA[i]=Arrays.copyOfRange(dlxBase[convertThis.get(i)], 1,dlxBase[0].length);
		}
		int random=1;
		for(int i = 0 ; i<partialA.length;i++) {
			for (int j = 0; j < partialA[0].length; j++) {
				if(partialA[i][j]!=0)
				{
					partialA[i][j]=random;
				}
				else {
					partialA[i][j]=0;
				}
			}
			random++;
		}
		return partialA;
	}

	/**
	 * Method that converts an arraylist containing solution rows into a 2D array that can be used for visualization (with piece ID's)
	 * @param convert arrayList containing solution rows
	 * @return returns a 2D array with every piece with the according piece ID.
	 */
	public static int[][] convertForEachPiece (ArrayList <Integer> convert){
		int[][] partial = new int[convert.size()][dlxBase[0].length-1];
		for (int i = 0; i<convert.size();i++){
			partial[i]=Arrays.copyOfRange(dlxBase[convert.get(i)], 1,dlxBase[0].length);
		}
		for(int i = 0 ; i<partial.length;i++) {
			for (int j = 0; j < partial[0].length; j++) {

				if(partial[i][j]!=0)
				{
					partial[i][j]=dlxBase[convert.get(i)][0];

				}
				else {
					partial[i][j]=0;
				}
			}
		}

		return partial;
	}

	//DATABASE
	/**
	 * Create the dataBase. All pieces in all possible spots.
	 * @param pieces an arraylist containing parcels
	 */
	public void createTheDatabase(ArrayList<Parcel> pieces) {
		ArrayList<Parcel> rotations;
		Parcel piece = new Cube();
		if(type.equalsIgnoreCase("c"))
			piece = new Pentocube();

		ArrayList<Parcel> uniqueList = piece.removeDuplicates(pieces);

		for (Parcel parcel : uniqueList) {

			if (parcel.getType().equalsIgnoreCase("C")) {
				rotations = ALL_ROTATIONS_CUBE.get(parcel.getId());
			} else {
				rotations = ALL_ROTATIONS_PENTO.get(parcel.getId());
			}

			int[] arr = new int[container.getLENGTH() *  container.getWIDTH() * container.getHEIGHT() + 1 ];
			arr[0] = parcel.getId();
			for (int r = 0; r < rotations.size(); r++) {
				for (int x = 0; x < container.getLENGTH() ; x++) {
					for (int y = 0; y < container.getWIDTH() ; y++) {
						for (int z = 0; z < container.getHEIGHT() ; z++) {
							int count = 0;
								Placement.addPiece(parcel, rotations.get(r), container, byNumber, x, y, z);
								int[] convertedArray = convertArr(container.getContainer());
								for (int i : convertedArray) {
									if (i != 0)
										count++;
								}
								if (count == parcel.getVolume()) {
									arr = copyArray(convertedArray, arr);
									database.add(arr);
								}
								Placement.removePiece(parcel, container, byNumber);
						}
					}
				}
			}
		}
	}

	//TESTING
	/**
	 * Run the DLX with the database.
	 * @param s type parcel to run DLX with
	 */
	public void run(String s){

		dlxBase = new int[1][1];
		DLXdatabase dlxDatabase = new DLXdatabase(this.container , this.byNumber,s);
		dlxDatabase.createTheDatabase( new Input(s , 1 , 1, 1, 3,4,5 ).getINPUT());

		//including pieceID in first column
		dlxBase = dlxDatabase.getDatabase();

		int[][] modifiedDLX2 = new int[ Math.max(1 , dlxBase.length) ][ Math.max( 1 , dlxBase[0].length-1) ];

		if(zeroSumCol(dlxBase)) {
			System.out.println("There might be an exact cover...");
			for (int i = 0; i < dlxBase.length; i++) {
				modifiedDLX2[i] = Arrays.copyOfRange(dlxBase[i], 1, dlxBase[0].length);
			}
			DLX run = new DLX(modifiedDLX2);
			run.findSolution();
		}
		else {
			System.out.println("No exact cover.");
		}

	}
	/**
	 * Main method for testing and analyzing run times.
	 * Dimensions of the container and parcel type are provided here.
	 * @param args default parameter
	 */
	public static void main(String[] args) {
		//Starting Time
		double startingT = System.currentTimeMillis();

		//Testing DlX with different container size and parcel type
		int length =  1, width = 8, height =5 ;
		String type = "p";
		Container container = new Container(length, width, height);
		Container byNumber = new Container(length, width, height);
		DLXdatabase dlxDatabase = new DLXdatabase(container ,byNumber , type);
		dlxDatabase.run(type);

		//Execution Time
		double finishingT = System.currentTimeMillis();
		System.out.println("Execution time = " + (finishingT - startingT));
	}
}
