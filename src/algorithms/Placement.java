/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;
import parcels.*;

/**
 * A class responsible for placing and removing parcels in the container at certain positions
 */
public class Placement {

	/**
	 * Method that places a cube in two containers: in one by its piece number and n the other by its ID (type)
	 * @param piece the parcel to be added
	 * @param rotation specific rotation of the parcel
	 * @param container the container where a piece is added by its type
	 * @param byNumber the container where a piece is added by its piece number
	 * @param x x-coordinate of where the piece is to be placed
	 * @param y y-coordinate of piece's position
	 * @param z z-coordinate of piece's position
	 */
	public static void addPiece(Parcel piece, Parcel rotation  , Container container ,Container byNumber, int x, int y, int z) {
		if (isValid(container, rotation, x, y, z)) {
			container.addValue(piece.getValue());
			byNumber.addValue(piece.getValue());
			for (int i = 0; i < rotation.getLength(); i++) {
				for (int j = 0; j < rotation.getWidth(); j++) {
					for (int k = 0; k < rotation.getHeight(); k++) {
						if (rotation.getInt(i, j, k) != 0) {
							byNumber.setInt(i + x, j + y, k + z, piece.getPieceNumber());
							container.setInt(i + x, j + y, k + z, piece.getId());
						}
					}
				}
			}
		}
	}

	/**
	 * Method that removes a last added package from two containers
	 * @param piece parcel to be removed
	 * @param container the cargo space from which piece is to be removed by its pieceNumber
	 * @param byNumber the cargo space from which a piece is represented by its type
	 */
	public static void removePiece(Parcel piece,Container container, Container byNumber ){
		container.addValue(-1 * piece.getValue());
		byNumber.addValue(-1 * piece.getValue());
		for (int i = 0; i < container.getLENGTH(); i++) {
			for (int j = 0; j < container.getWIDTH(); j++) {
				for (int k = 0; k < container.getHEIGHT(); k++) {
					if(byNumber.getInt(i,j,k) == piece.getPieceNumber()) {
						byNumber.setInt(i,j,k,0);
						container.setInt(i,j,k,0);
					}
				}
			}
		}

	}

	/**
	 * Method that checks whether it is valid to place a package in the container at a specific position
	 * @param container the container in which package is to be placed
	 * @param piece the package which is to be placed
	 * @param x x-coordinate of the position
	 * @param y y-coordinate of the position
	 * @param z z-coordinate of the position
	 * @return true if it is valid to place a piece
	 */
	public static boolean isValid(Container container , Parcel piece  , int x , int y , int z){
		for(int i = 0; i < piece.getLength(); i++) {
			for (int j = 0 ; j < piece.getWidth(); j++){
				for(int k = 0; k < piece.getHeight(); k++) {
					//It checks if it is out of bounds.
					if (x + piece.getLength() > container.getLENGTH() ||
							y + piece.getWidth() > container.getWIDTH() ||
							z + piece.getHeight() > container.getHEIGHT()) {
						return false;
					}
					//It checks if a square is already occupied
					if (piece.getInt( i, j, k) != 0 && container.getInt(x + i ,y + j ,z + k) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
