/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;
import parcels.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The class that manages user input for the amount and type of parcels to be placed in the cargo space, as well as their values
 */
public class Input {
	private final ArrayList<Parcel> INPUT = new ArrayList<>();
	private final int A;
	private final int B;
	private final int C;
	private final int VALUE_AL;
	private final int VALUE_BP;
	private final int VALUE_CT;
	private final String TYPE;

	/**
	 * Constructor for input class that creates the number of packages of A/L, B/P, C/T type based on user input
	 * @param type type of parcels
	 * @param a_l number of A/L parcels
	 * @param b_p number of B/P parcels
	 * @param c_t number of C/T parcels
	 * @param value_al value assigned to A/L parcels
	 * @param value_bp value assigned to B/P parcels
	 * @param value_ct value assigned to C/T parcels
	 */
	public Input(String type, int a_l, int b_p, int c_t, int value_al, int value_bp, int value_ct) {
		this.A = a_l;
		this.B = b_p;
		this.C = c_t;
		this.VALUE_AL = value_al;
		this.VALUE_BP = value_bp;
		this.VALUE_CT = value_ct;
		this.TYPE = type;
		create();
	}

	/**
	 * Method that creates an ArrayList with a provided number and types of cubes/pentocubes
	 */
	public void create() {
		if (TYPE.equalsIgnoreCase("c")) {
			for (int i = 0; i < A; i++) {
				INPUT.add(new Cube(-1, VALUE_AL));
			}
			for (int i = 0; i < B; i++) {
				INPUT.add(new Cube(-2, VALUE_BP));
			}
			for (int i = 0; i < C; i++) {
				INPUT.add( new Cube(-3, VALUE_CT));
			}

		}
		if (TYPE.equalsIgnoreCase("p")) {
			for (int i = 0; i < A; i++) {
				Parcel pentoL = new Pentocube(-1, VALUE_AL);
				INPUT.add(pentoL);
			}
			for (int i = 0; i < B; i++) {
				Parcel pentoP = new Pentocube(-2, VALUE_BP);
				INPUT.add(pentoP);
			}
			for (int i = 0; i < C; i++) {
				Parcel pentoT = new Pentocube(-3, VALUE_CT);
				INPUT.add(pentoT);
			}

		}
	}
	//GETTERS FOR INPUT'S INSTANCE FIELDS

	/**
	 * A getter for the input arraylist with all parcel types and amounts provided by user
	 * @return input arraylist
	 */
	public ArrayList<Parcel> getINPUT() {
		return INPUT;
	}

   //////////////SORTING METHODS///////////////////////////

	/**
	 * Sorts an arraylist of parcels in descending order of their volumes
	 * @return sorted arraylist of parcels
	 */
	public ArrayList<Parcel> sortDescendingVolume() {
		Collections.sort(INPUT, DescendingVolume);
		return INPUT;
	}

	/**
	 * Sorts an arraylist of parcels in ascending order of their volumes
	 * @return sorted arraylist of parcels
	 */
	public ArrayList<Parcel> sortAscendingVolume() {
		Collections.sort(INPUT, AscendingVolume);
		return INPUT;
	}

	/**
	 * Sorts an arraylist of parcels in descending order of their value-to-volume ratio
	 * @return sorted arraylist of parcels
	 */
	public ArrayList<Parcel> sortDescendingRatio() {
		Collections.sort(INPUT, DescendingRatio);
		return INPUT;
	}

	/**
	 * Sorts an arraylist of parcels in ascending order of their value-to-volume ratio
	 * @return sorted arraylist of parcels
	 */
	public ArrayList<Parcel> sortAscendingRatio() {
		Collections.sort(INPUT, AscendingRatio);
		return INPUT;
	}

	///////////////COMPARING METHODS/////////////////////////////////

	/**
	 * compares volumes of parcels with descending order
	 */
	public static Comparator<Parcel> DescendingVolume = new Comparator<Parcel>() {
		public int compare(Parcel s1, Parcel s2) {
			double volume1 = 100*s1.getVolume();
			double volume2 = 100*s2.getVolume();
			return (int)(volume2 - volume1);
		}
	};

	/**
	 * Compares volumes of parcels with ascending order
	 */
	public static Comparator<Parcel> AscendingVolume = new Comparator<Parcel>() {
		public int compare(Parcel s1, Parcel s2) {
			double volume1 = 100*s1.getVolume();
			double volume2 = 100*s2.getVolume();
			return (int)(volume1 - volume2);
		}
	};

	/**
	 * Compares ratios of parcels with descending order
	 */
	public static Comparator<Parcel> DescendingRatio = new Comparator<Parcel>() {
		public int compare(Parcel s1, Parcel s2) {
			double ratio1 = 100 * s1.getValueVolumeRatio();
			double ratio2 = 100 * s2.getValueVolumeRatio();
			return (int)(ratio2 - ratio1);
		}
	};

	/**
	 * Compares ratios of parcels with ascending order
	 */
	public static Comparator<Parcel> AscendingRatio = new Comparator<Parcel>() {
		public int compare(Parcel s1, Parcel s2) {
			double ratio1 = 100 * s1.getValueVolumeRatio();
			double ratio2 = 100 * s2.getValueVolumeRatio();
			return (int)(ratio1 - ratio2);
		}
	};

}


