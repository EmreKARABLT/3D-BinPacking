/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package parcels;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interface for all parcel types.
 */
public interface Parcel {
	int[][][] get3D();
	void set3D(int[][][] cube);
	int getInt(int x , int y , int z);
	int getHeight();
	int getWidth();
	int getLength();
	int getId();
	int getPieceNumber();
	int getValue();
	double getVolume();
	double getValueVolumeRatio();
	HashMap<Integer, ArrayList<Parcel>> allRotations();
	void rotateAroundX( int amountOfRotation , Parcel p);
	void rotateAroundY( int amountOfRotation , Parcel p);
	void rotateAroundZ( int amountOfRotation , Parcel p);
	ArrayList<Parcel> removeDuplicates(ArrayList<Parcel> list);
	boolean isEqual(Parcel parcel, Parcel parcel1);
	String getType();

}
