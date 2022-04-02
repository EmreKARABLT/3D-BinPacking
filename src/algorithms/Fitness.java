/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;
import parcels.*;
/**
 * The fitness class that outlines all fitness factors used by other algorithms to decide upon a best move
 */
public class Fitness {

	private double numberOfTotalEmptySpaces = 2.7606648407812573;
	private double touchingWallX = 1.7654480501469845;
	private double touchingWallY = 3.707996950353847;
	private double touchingWallZ = 4.9962611343868994;
	private double numberOfConnectedCubeSides = 1.020085486082527;
	private double numberOfGapsBetweenCubesDirX = 3.5355999277551624;
	private double numberOfGapsBetweenCubesDirY = 0.9833954701065628;
	private double numberOfGapsBetweenCubesDirZ = 3.515945952595877;
	private double value = 0.3769664036457415;

	/**
	 * A default constructor for fitness class
	 */
	public Fitness(){}

	/**
	 * Method that counts the total amount of empty spaces of dimension 0.5 x 0.5 x 0.5 [m] in the cargo space, which we want to maximize while placing next piece
	 * @param container object of the Container class that is a 3D array representing the state of the cargo space
	 * @return number of total empty spaces
	 */
	public int numberOfTotalEmptySpaces(Container container){
		int emptyHoleCounter = 0;

		for (int i = 0; i < container.getLENGTH(); i++) {
			for (int j = 0; j < container.getWIDTH(); j++) {
				for (int k = 0; k < container.getHEIGHT(); k++) {
					if (container.getInt(i,j,k) == 0)
						emptyHoleCounter--;
				}
			}
		}
		return emptyHoleCounter;
	}

	/**
	 * Method that counts the number of 0.5 x 0.5 grids on the back wall of the container that are touching packages (cubes and pentocubes)
	 * @param container the cargo space to be evaluated
	 * @return the number of side units of the wall being touched by packages
	 */
	public int touchingWallX(Container container){
		int touchingSidesCounter = 0;
		for (int j = 0; j < container.getWIDTH(); j++) {
			for (int k = 0; k < container.getHEIGHT(); k++) {
				if (container.getInt(0,j,k) != 0 )
					touchingSidesCounter++;
			}
		}

		return touchingSidesCounter;
	}

	/**
	 * Method that counts the number of 0.5 x 0.5 grids on the left wall of the container that are touching packages (cubes and pentocubes)
	 * @param container the cargo space to be evaluated
	 * @return the number of side units of the wall being touched by packages
	 */
	public int touchingWallY(Container container){
		int touchingSidesCounter = 0;
		for (int i = 0; i < container.getLENGTH(); i++) {
			for (int k = 0; k < container.getHEIGHT(); k++) {
				if (container.getInt(i,0,k) != 0 )
					touchingSidesCounter++;
			}
		}

		return touchingSidesCounter;
	}

	/**
	 * Method that counts the number of 0.5 x 0.5 grids on the bottom wall of the container that are touching packages (cubes and pentocubes)
	 * @param container the cargo space to be evaluated
	 * @return the number of side units of the wall being touched by packages
	 */
	public int touchingWallZ(Container container){
		int touchingSidesCounter = 0;
		for (int i = 0; i < container.getLENGTH(); i++) {
			for (int j = 0; j < container.getWIDTH(); j++) {
				if (container.getInt(i,j,0) != 0 )
					touchingSidesCounter++;

			}
		}
		return touchingSidesCounter;
	}

	/**
	 * Method that counts the number of side units (0.5 x 0.5 is a unit) of a current cube touching different cubes in a cargo space
	 * @param container the cargo space to be evaluated
	 * @param cube the current package that is being evaluated
	 * @param x the x position of the package in the cargo space
	 * @param y the y position of the package in the cargo space
	 * @param z the z position of the package in the cargo space
	 * @return the amount of side units of a package that touch other packages
	 */
	public int numberOfConnectedCubeSides(Container container, Parcel cube, int x, int y, int z){
		int pieceNum = cube.getPieceNumber();
		int cubeSidesCounter = 0;
		int leftX , rightX , closerY , furtherY , upperZ , lowerZ ;
		for (int i = x; i < Math.min(container.getLENGTH() , cube.getLength() + x ); i++) {
			for (int j = y; j < Math.min(container.getLENGTH() , cube.getWidth() + y ); j++) {
				for (int k = z; k < Math.min(container.getLENGTH() ,cube.getHeight() + z ); k++) {
					if(container.getInt(i,j,k) == pieceNum){
						leftX = Math.max(0,i-1);
						rightX = Math.min(container.getLENGTH()-1,i+1);
						closerY = Math.max(0,j-1);
						furtherY = Math.min(container.getWIDTH()-1, j+1);
						upperZ = Math.max(0,k-1);
						lowerZ = Math.min(container.getHEIGHT()-1, k+1);
						if (    (container.getInt(leftX,j,k) != pieceNum && container.getInt(leftX,j,k) != 0)  ||
								(container.getInt(rightX,j,k) != pieceNum && container.getInt(rightX,j,k) != 0) ||
								(container.getInt(i,closerY,k) != pieceNum && container.getInt(i,closerY,k) != 0) ||
								(container.getInt(i,furtherY,k) != pieceNum && container.getInt(i,furtherY,k) != 0) ||
								(container.getInt(i,j,upperZ) != pieceNum && container.getInt(i,j,upperZ) != 0) ||
								(container.getInt(i,j,lowerZ) != pieceNum && container.getInt(i,j,lowerZ) != 0)
						) {
							cubeSidesCounter++;
						}
					}
				}
			}
		}
		return cubeSidesCounter;
	}

	/**
	 * Method that counts the amount of gaps that occur between separate packages, looking from the x direction
	 * @param container the cargo space to be evaluated
	 * @return the amount of gaps
	 */
	public int numberOfGapsBetweenCubesDirX(Container container){
		int gaps = 0;
		for (int i = container.getLENGTH()-1; i >= 0; i--) {
			boolean firstCube = false;
			for (int j = container.getWIDTH()-1; j >= 0 ; j--) {
				for (int k = container.getHEIGHT()-1; k >= 0 ; k--) {
					if(container.getInt(i,j,k) != 0){
						firstCube = true;
					}
					if(firstCube && container.getInt(i,j,k) == 0){

						gaps--;
					}
				}
				firstCube=false;
			}
		}
		return gaps;
	}

	/**
	 * Method that counts the amount of gaps that occur between separate packages, looking from the y direction
	 * @param container the cargo space to be evaluated
	 * @return the amount of gaps
	 */
	public int numberOfGapsBetweenCubesDirY(Container container){
		int gaps = 0;
		for (int i = container.getWIDTH()-1; i >= 0; i--) {
			boolean firstCube = false;
			for (int j = container.getHEIGHT()-1; j >= 0 ; j--) {
				for (int k = container.getLENGTH()-1; k >= 0 ; k--) {
					if(container.getInt(k,i,j) != 0){
						firstCube = true;
					}
					if(firstCube && container.getInt(k,i,j) == 0){

						gaps--;
					}
				}
				firstCube=false;
			}
		}
		return gaps;
	}

	/**
	 * Method that counts the amount of gaps that occur between separate packages, looking from the z direction
	 * @param container the cargo space to be evaluated
	 * @return the amount of gaps
	 */
	public int numberOfGapsBetweenCubesDirZ(Container container){
		int gaps = 0;
		for (int i = container.getHEIGHT()-1; i >= 0; i--) {
			boolean firstCube = false;
			for (int j = container.getLENGTH()-1; j >= 0 ; j--) {
				for (int k = container.getWIDTH()-1; k >= 0 ; k--) {
					if(container.getInt(j,k,i) != 0){
						firstCube = true;
					}
					if(firstCube && container.getInt(j,k,i) == 0){

						gaps--;
					}
				}
				firstCube=false;
			}
		}
		return gaps;
	}

	/**
	 * The method is returning the summed value of all packages present in the container
	 * @param container the cargo space to be evaluated
	 * @return the total value of all packages
	 */
	public int value(Container container){
		return container.getTotalValue();
	}

	/**
	 * The method that calculates the fitness score based on the previously created fitness factors
	 * @param container a cargo space to be evaluated
	 * @param piece a current piece that is to be placed
	 * @param x the x-coordinate of piece's placement
	 * @param y the y-coordinate of piece's placement
	 * @param z the z-coordinate of piece's placement
	 * @return the fitness score
	 */
	public double calculateFitness(Container container, Parcel piece,int x , int y ,int z ){
		return numberOfTotalEmptySpaces(container) *  numberOfTotalEmptySpaces
				+ this.touchingWallX(container) * touchingWallX
				+ this.touchingWallY(container) * touchingWallY
				+ this.touchingWallZ(container) * touchingWallZ
				+ this.numberOfConnectedCubeSides(container,piece,x,y,z) * numberOfConnectedCubeSides
				+ this.numberOfGapsBetweenCubesDirX(container) * numberOfGapsBetweenCubesDirX
				+ this.numberOfGapsBetweenCubesDirY(container) * numberOfGapsBetweenCubesDirY
				+ this.numberOfGapsBetweenCubesDirZ(container)  * numberOfGapsBetweenCubesDirZ
//				+ this.value(container) * value
				;
	}

	/**
	 * The method that sets all weights of the fitness factors in a fitness function
	 * @param weights an array containing all weights of fitness factors
	 */
	void setWeights(double[] weights){
		setNumberOfTotalEmptySpaces(weights[0]);
		setTouchingWallX(weights[1]);
		setTouchingWallY(weights[2]);
		setTouchingWallZ(weights[3]);
		setNumberOfConnectedCubeSides(weights[4]);
		setNumberOfGapsBetweenCubesDirX(weights[5]);
		setNumberOfGapsBetweenCubesDirY(weights[6]);
		setNumberOfGapsBetweenCubesDirZ(weights[7]);
		setValue(weights[8]);
	}

	/**
	 * Setter for the weight of the total number of empty spaces fitness factor
	 * @param numberOfTotalEmptySpaces the weight of the fitness factor
	 */
	void setNumberOfTotalEmptySpaces( double numberOfTotalEmptySpaces ){ this.numberOfTotalEmptySpaces =  numberOfTotalEmptySpaces;}

	/**
	 * Setter for the weight of the number of cubes touching back wall fitness factor
	 * @param touchingWallX the weight of the fitness factor
	 */
	void setTouchingWallX( double touchingWallX ){ this.touchingWallX =  touchingWallX;}
	/**
	 * Setter for the weight of the number of cubes touching left wall fitness factor
	 * @param touchingWallY the weight of the fitness factor
	 */
	void setTouchingWallY( double touchingWallY ){ this.touchingWallY =  touchingWallY;}
	/**
	 * Setter for the weight of the number of cubes touching bottom wall fitness factor
	 * @param touchingWallZ the weight of the fitness factor
	 */
	void setTouchingWallZ( double touchingWallZ ){ this.touchingWallZ =  touchingWallZ;}
	/**
	 * Setter for the weight of the number of connected cube sides factor
	 * @param numberOfConnectedCubeSides the weight of the fitness factor
	 */
	void setNumberOfConnectedCubeSides( double numberOfConnectedCubeSides ){ this.numberOfConnectedCubeSides =  numberOfConnectedCubeSides; }
	/**
	 * Setter for the weight of the number of gaps in respect to X-direction factor
	 * @param numberOfGapsBetweenCubesDirX the weight of the fitness factor
	 */
	void setNumberOfGapsBetweenCubesDirX( double numberOfGapsBetweenCubesDirX ){ this.numberOfGapsBetweenCubesDirX =  numberOfGapsBetweenCubesDirX;}
	/**
	 * Setter for the weight of the number of gaps in respect to Y-direction factor
	 * @param numberOfGapsBetweenCubesDirY the weight of the fitness factor
	 */
	void setNumberOfGapsBetweenCubesDirY( double numberOfGapsBetweenCubesDirY ){ this.numberOfGapsBetweenCubesDirY =  numberOfGapsBetweenCubesDirY;}
	/**
	 * Setter for the weight of the number of gaps in respect to Z-direction factor
	 * @param numberOfGapsBetweenCubesDirZ the weight of the fitness factor
	 */
	void setNumberOfGapsBetweenCubesDirZ( double numberOfGapsBetweenCubesDirZ ){ this.numberOfGapsBetweenCubesDirZ =  numberOfGapsBetweenCubesDirZ;}
	/**
	 * Setter for the weight of the value factor
	 * @param value the weight of the fitness factor
	 */
	void setValue( double value){ this.value =  value;}
}