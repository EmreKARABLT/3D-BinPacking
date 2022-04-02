/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;
import parcels.*;
import java.util.*;

/**
 * This class includes both the greedy and the extended greedy algorithms that aim to solve a 3D packing problem
 */
public class Greedy{

	private final HashMap<Integer,ArrayList<Parcel>> ALL_ROTATIONS_CUBE = new Cube().allRotations();
	private final HashMap<Integer,ArrayList<Parcel>> ALL_ROTATIONS_PENTO = new Pentocube().allRotations();
	private final String TYPE;
	private final int A_L;
	private final int B_P;
	private final int C_T;
	private final int VALUE_AL;
	private final int VALUE_BP;
	private final int VALUE_CT;
	private final Container CONTAINER;
	private final Container BY_NUMBER;
	private ArrayList<Parcel> parcels;
	private Parcel bestPiece ;
	private final String SORTING;
	private double percentFilled;
	Fitness fitness_c = new Fitness();

	/**
	 * Constructor for the greedy algorithm with parameter variables to be provided from user regerding the amount, value and type of parcels
	 * @param type type of parcel, either cube or pentocube
	 * @param a_l amount of A/L parcels provided by user
	 * @param b_p amount of B/P parcels provided by user
	 * @param c_t amount of C/T parcels provided by user
	 * @param value_al value of A/L parcel provided
	 * @param value_bp value of B/P parcel provided
	 * @param value_ct value of C/T parcel provided
	 * @param container a container where parcels are represented by their IDs
	 * @param byNumber a container where parcels are represented by their creation number
	 * @param sorting type of sorting
	 */
	public Greedy(String type, int a_l, int b_p, int c_t, int value_al, int value_bp, int value_ct , Container container , Container byNumber , String sorting){
		this.TYPE = type;
		this.A_L = a_l;
		this.B_P = b_p;
		this.C_T = c_t;
		this.VALUE_AL = value_al;
		this.VALUE_BP = value_bp;
		this.VALUE_CT = value_ct;
		this.CONTAINER = container ;
		this.BY_NUMBER = byNumber;
		this.SORTING = sorting;
	}

	/**
	 * A greedy algorithm method that finds an answer to the knapsack problem by evaluating all possible rotations and
	 * placements of a certain parcel type as compared to other present in the input list to determine the best move each time it makes a next decision
	 */
	public void greedyExtended() {
		Input input = new Input(TYPE, A_L, B_P, C_T, VALUE_AL, VALUE_BP, VALUE_CT);
		parcels = input.getINPUT();
		while (parcels.size() > 0) {
			int[] xyz = bestMove( parcels );

			if(TYPE.equalsIgnoreCase("C"))
				Placement.addPiece( bestPiece , ALL_ROTATIONS_CUBE.get( bestPiece.getId() ).get( xyz[3] ) , CONTAINER, BY_NUMBER, xyz[0], xyz[1], xyz[2]);
			else
				Placement.addPiece( bestPiece , ALL_ROTATIONS_PENTO.get( bestPiece.getId() ).get( xyz[3] ) , CONTAINER, BY_NUMBER, xyz[0], xyz[1], xyz[2]);

			parcels.remove(bestPiece);


		}
		percentFilled =  100 -((1.0 * fitness_c.numberOfTotalEmptySpaces(CONTAINER)) / -13.20);
		percentFilled=Math.round(percentFilled*100.0)/100.0;

		CONTAINER.getContainer();
	}

	/**
	 * A greedy algorithm method that finds an answer to the knapsack problem by evaluating all possible rotations and
	 * placements of each parcel to determine the best move each time it makes a next decision
	 */
	public void greedy() {
		Input input = new Input(TYPE, A_L, B_P, C_T, VALUE_AL, VALUE_BP, VALUE_CT);
		if(SORTING.equalsIgnoreCase("descending Volume"))
			parcels = input.sortDescendingVolume();
		else if(SORTING.equalsIgnoreCase("ascending Volume"))
			parcels = input.sortAscendingVolume();
		else if(SORTING.equalsIgnoreCase("descending ratio"))
			parcels = input.sortDescendingRatio();
		else
			parcels = input.sortAscendingRatio();

		while (parcels.size() > 0) {
			int[] xyz = bestMove( parcels.get(0) );
			if(TYPE.equalsIgnoreCase("C"))
				Placement.addPiece( bestPiece , ALL_ROTATIONS_CUBE.get( bestPiece.getId() ).get( xyz[3] ) , CONTAINER, BY_NUMBER, xyz[0], xyz[1], xyz[2]);
			else
				Placement.addPiece( bestPiece , ALL_ROTATIONS_PENTO.get( bestPiece.getId() ).get( xyz[3] ) , CONTAINER, BY_NUMBER, xyz[0], xyz[1], xyz[2]);

			parcels.remove(bestPiece);
		}
		percentFilled =  100 -((1.0 * fitness_c.numberOfTotalEmptySpaces(CONTAINER)) / -13.20);
		percentFilled=Math.round(percentFilled*100.0)/100.0;
		BY_NUMBER.getContainer();
	}

	/**
	 * Method used by the greedy algorithm that determines the best move for a current list of parcels that is evaluated, by placing all
	 * of their rotations to all possible placements in the cargo space and finding the best placement with the highest fitness value
	 * @param pieces a current list of parcels that is evaluated by the greedy algorithm
	 * @return an array with the best rotation of chosen parcel and best x,y,z location of it to be placed in the cargo space
	 */
	public int[] bestMove(ArrayList<Parcel> pieces) {
		ArrayList<Parcel> rotations  ;
		ArrayList<Parcel> leftOvers= new ArrayList<>();
		leftOvers.addAll(pieces);
		int[] best = new int[4];

		bestPiece = pieces.get(0);
		double fitness = 0 ;
		double bestFitness = -9999;
		for(int p = 0 ; p < pieces.size()-1 ; p++) {
			while(p != 0  && isEqual( pieces.get(p-1) , pieces.get(p)  ) && p < pieces.size()-1 ){
				p++;
			}
			if (TYPE.equalsIgnoreCase("C")) {
				rotations = ALL_ROTATIONS_CUBE.get(pieces.get(p).getId());
			}else{
				rotations = ALL_ROTATIONS_PENTO.get(pieces.get(p).getId());
			}
			for (int x = 0; x < CONTAINER.getLENGTH(); x++) {
				for (int y = 0; y < CONTAINER.getWIDTH(); y++) {
					for (int z = 0; z < CONTAINER.getHEIGHT(); z++) {
						for (int r = 0; r < rotations.size(); r++) {
							if (Placement.isValid(CONTAINER, rotations.get(r), x, y, z)) {
								Placement.addPiece(pieces.get(p), rotations.get(r), CONTAINER, BY_NUMBER, x, y, z);
								fitness = fitness_c.calculateFitness(BY_NUMBER, rotations.get(r), x, y, z);
								Placement.removePiece(pieces.get(p), CONTAINER, BY_NUMBER);
								if (fitness > bestFitness) {
									bestPiece = pieces.get(p);
									bestFitness = fitness;
									best[0] = x;
									best[1] = y;
									best[2] = z;
									best[3] = r;
								}
							}
						}
					}
				}
			}
		}
		return best;
	}

	/**
	 * Method used by the greedy algorithm that determines the best move for a current parcel that is evaluated, by placing all
	 * of its rotations to all possible placements in the cargo space and finding the best placement with the highest fitness value
	 * @param piece the current parcel
	 * @return an array with the best rotation of chosen parcel and best x,y,z location of it to be placed in the cargo space
	 */
	public int[] bestMove(Parcel piece) {
		ArrayList<Parcel> rotations  ;
		int[] best = new int[4];
		bestPiece = piece ;
		double fitness = 0 ;
		double bestFitness = -9999;
		if (TYPE.equalsIgnoreCase("C")) {
			rotations = ALL_ROTATIONS_CUBE.get(piece.getId());
		}else{
			rotations = ALL_ROTATIONS_PENTO.get(piece.getId());
		}

		for (int x = 0; x < CONTAINER.getLENGTH(); x++) {
			for (int y = 0; y < CONTAINER.getWIDTH(); y++) {
				for (int z = 0; z < CONTAINER.getHEIGHT(); z++) {
					for (int r = 0; r < rotations.size(); r++) {
						if (Placement.isValid(CONTAINER, rotations.get(r), x, y, z)) {
							Placement.addPiece(piece, rotations.get(r), CONTAINER, BY_NUMBER, x, y, z);
							fitness = fitness_c.calculateFitness(BY_NUMBER, rotations.get(r), x, y, z);
							Placement.removePiece(piece, CONTAINER, BY_NUMBER);
							if (fitness > bestFitness) {
								bestFitness = fitness;
								best[0] = x;
								best[1] = y;
								best[2] = z;
								best[3] = r;
							}

						}

					}
				}
			}
		}
		return best;
	}

	/**
	 * Method that checks whether two parcels are equal in terms of their dimensions and IDs
	 * @param p1 1st parcel for comparison
	 * @param p2 2nd parcel for comparison
	 * @return true if all dimensions  and IDs of both parcels are exactly the same
	 */
	public static boolean isEqual(Parcel p1, Parcel p2){


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
	 * Method that removes duplicates of parcels from an Arraylist of parcels
	 * @param list the arraylist of parcels to be evaluated
	 * @return list without the same parcels (duplicates)
	 */
	public static ArrayList<Parcel> removeDuplicates(ArrayList<Parcel> list){
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
	 * Getter for the percentage of the cargo space that is filled with parcels
	 * @return the percentage filled
	 */
	public double getPercentFilled() {return this.percentFilled;}

	/**
	 * Getter for the byNumber container
	 * @return a 3D array representing the cargo space where every piece can be distinguished by its piece number
	 */
	public int[][][] getBY_NUMBER() { return BY_NUMBER.getContainer(); }

	/**
	 * Getter for the byID container
	 * @return a 3D array representing the cargo space where every parcel type can be distinguished by its ID
	 */
	public int[][][] getByID() { return CONTAINER.getContainer(); }

	/**
	 * A setter of fitness class
	 * @param fitness the fitness class
	 */
	void setFitness_c( Fitness fitness  ) { this.fitness_c = fitness; }
}
