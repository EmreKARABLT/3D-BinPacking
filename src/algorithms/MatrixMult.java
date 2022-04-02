/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;

/**
 * The class handles 2D matrix multiplications used to perform 3D rotations of the pentocubes
 */
public class MatrixMult {

    /**
     *A method that performs matrix multiplication on 2D integer matrices
     * @param firstMatrix the first matrix to be multiplied
     * @param secondMatrix the second matrix to be multiplied
     * @return the outcome of the multiplication
     */
    public static int[][] multiplyMatrices(int[][] firstMatrix, int[][]secondMatrix) {
        int[][] multiplication = new int[firstMatrix.length][secondMatrix[0].length];

        if(!isValidMultiplication(firstMatrix,secondMatrix)){
            return multiplication;
        }
        for(int i = 0 ; i < firstMatrix.length ; i++ ){
            for(int j = 0 ; j < secondMatrix[0].length ; j++){
                for(int k = 0 ; k < firstMatrix[0].length ; k++ ){
                    multiplication[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return multiplication;
    }

    /**
     * A method that checks whether it is valid to multiply the provided matrices
     * @param firstMatrix the first matrix to be multiplied
     * @param secondMatrix the second matrix to be multiplied
     * @return true if its is valid to multiply matrices provided
     */
    public static boolean isValidMultiplication(int[][] firstMatrix , int[][]secondMatrix){
        if(firstMatrix[0].length != secondMatrix.length ) return false;

        if(firstMatrix.length == 0 || secondMatrix.length == 0 || firstMatrix[0].length == 0 ) return false;
        return true;
    }
}
