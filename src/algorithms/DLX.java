/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of Algorithm X invented by Donald Knuth using Dancing Links.
 * It is used to solve exact cover problems.
 */

public class DLX {

    //FIELD
    int coveredNodes = 0;
    double minZeroSumColumns = 9999.00;
    public int[][] INPUT;
    public static ArrayList<Integer> Solutions = new ArrayList<>();
    public static int[] solutionWIthID;
    public static int[] solutionWithNumber;
    public static int[][] pil;
    List<Node> probableAnswers = new ArrayList<>();
    List<Node> gen0nodes = new ArrayList<>();
    List<Integer> coveredLines = new ArrayList<>();
    ArrayList<Integer> myParsedIntList = new ArrayList<>();
    ArrayList<Integer> minZeroSumSolution = new ArrayList<>();

    //CONSTRUCTOR
    /**
     * A constructor for the DLX.
     * @param DATABASE A binary matrix to be checked for solutions.
     */
    public DLX(int[][] DATABASE) {
        this.INPUT = DATABASE;
    }

    //NODE
    static Node head;
    static class Node {
        // Every node has a value assigned to it and a name
        int data;
        String name;

        Node(int dataGiven, String nameGiven) {
            this.data = dataGiven;
            this.name = nameGiven;
        }

        // Every node is linked in all 4 directions. We can move from node to node by (Some Node).right, (Some Node).left and so on...
        Node right;
        Node left;
        Node up;
        Node down;
    }

    //ALGORITHM X
    /**
     * This is the main function of the project. It calls all the helper functions.
     * Forms a DLL and calls the Exact Cover and Algorithm X functions.
     */
    void findSolution() {
        //Create Dancing Links
        formDLL();
        String[] temp;

        try {
            while (!recursionX(0)) {
                for (Node x : probableAnswers) {
                    temp = (x.name.split("_"));
                    int myInt = Integer.parseInt(temp[1]);
                    myParsedIntList.add(myInt);
                }

                //Here we pull in the whole dataBase
                DLXdatabase myLocalBase = new DLXdatabase();
                int[][] localDLXBase = myLocalBase.getDlxBase();
                pil = new int[myParsedIntList.size()][localDLXBase[0].length];


                for (int i = 0; i < myParsedIntList.size(); i++) {
                    pil[i] = localDLXBase[myParsedIntList.get(i)];
                }

                // Finding the best partial solution
                double testMin = nZeroSumColumns(pil);
                double nTotalColumns = localDLXBase[0].length - 1;         //No Piece IDs
                double nZeroColumns = nTotalColumns - testMin;
                double percentageFilled = (nZeroColumns / nTotalColumns) * 100.0;

                //PIL is our partial solution including Piece IDs -- We need to remove the first column

                if (testMin < minZeroSumColumns) {
                    minZeroSumColumns = testMin;
                    System.out.println("New best cover found! Filled: " + percentageFilled + "%");
                    minZeroSumSolution.addAll(myParsedIntList);
                }

                probableAnswers.clear(); // We clear all the probable answers since it wasn't a solution
                myParsedIntList.clear();
                uncoverPreviousLines();
                coveredLines.clear();
                firstCover();
            }
        } catch (StackOverflowError e) {
            System.out.println("Ouch! No exact cover!");

        }

    }
    /**
     * A recursive function of the Algorithm X.
     * @param generation Parameter that indicates in what level of a recursion algorithm currently is.
     * @return returns a boolean value which indicates whether the solution was found or not.
     */
    private boolean recursionX(int generation) {
        Node colMinOnes = findColMinNodes();
        Node temp = colMinOnes.down;
        probableAnswers.add(temp);

        if (generation == 0) {
            gen0handling();
            gen0nodes.add(temp);
        }

        generation++;
        if (colMinOnes.down != null) {
            while (temp.left != null) {
                temp = temp.left; // Here we go to the row headings
            }
            while (temp.right != null) {
                temp = temp.right; // We go through each node and close every single line that intersects

                // Delete the all the lines in the column
                deletingColumnLine(temp);
            }
        } else {
            System.out.println("No solutions found");
            return false;
        }

        if (coveredNodes < INPUT.length) {
            return recursionX(generation);
        } else {
            if (perfectCover()) {
                System.out.println("Solution has been found!");
                printTheAnswer();
                return true;
            }
        }
        return false;
    }
    /**
     * A helper function that helps to handle generation 0 situation in a recursive algorithm.
     */
    private void gen0handling() {
        for (Node x : gen0nodes) {
            restoreNode(x);
        }
    }
    /**
     * Helper function that takes care of the nodes closed in the first generation.
     */
    private void firstCover() {
        coveredNodes = 0;
        for (Node x : gen0nodes) {
            removeNode(x);
        }
    }
    /**
     * A function used to check for perfect cover in the current DLL
     * @return returns a boolean value whether the current doubly linked structure contains the exact cover.
     */
    private boolean perfectCover() {
        Node temp;
        int count = 0;
        for (Node probableAnswer : probableAnswers) {
            temp = probableAnswer;
            while (temp.left != null) { // go to the row name node
                temp = temp.left;
            }
            while (temp.right != null) {
                count++;
                temp = temp.right;
            }
        }
        if (count != INPUT[0].length) {
            return false;
        }

        System.out.println("We found the cover!");
        return true;
    }

    //DANCING LINKS
    /**
     * A function that converts a given binary matrix to a two-dimensional doubly linked list.
     */
    private void formDLL() {
        // Create head node
        head = new Node(-999, "SigmaGames");

        // Create x-axis
        Node pointer = head;
        for (int i = 0; i < INPUT[0].length; i++) {
            addNodeR(pointer, i, "X_" + i);
            pointer = pointer.right;
        }

        // Create y-axis
        pointer = head;
        for (int i = 0; i < INPUT.length; i++) {
            addNodeD(pointer, i, "Y_" + i);
            pointer = pointer.down;
        }

        // Iterate through the INPUT and add a node where there is one
        for (int i = 0; i < INPUT.length; i++) {
            for (int j = 0; j < INPUT[0].length; j++) {
                if (INPUT[i][j] != 0) linkNode(i, j, "N_" + i + "_" + j);
            }
        }
    }

    //DANCING LINKS - NODE ACTION
    /**
     * A helper function for forming the DLX. It uncovers a single node.
     *
     * @param node A node to be uncovered.
     */
    private void restoreNode(Node node) {
        node.up.down = node;
        if (node.down != null) {
            node.down.up = node;
        }
    }
    /**
     * A helper function for forming the DLX. It covers a single node.
     * @param node A node to be covered.
     */
    private void removeNode(Node node) {
        node.up.down = node.down;
        if (node.down != null) {
            node.down.up = node.up;
        }
    }
    /**
     * Helper function for linking nodes. It adds node in the right direction of the given node.
     *
     * @param currentN  A current node
     * @param dataGiven Data that will be assigned to the node.
     * @param nameGiven Name that will be given to the node.
     */
    private void addNodeR(Node currentN, int dataGiven, String nameGiven) {
        Node nodeToBeAdded = new Node(dataGiven, nameGiven);
        nodeToBeAdded.left = currentN;
        currentN.right = nodeToBeAdded;
    }
    /**
     * Helper function for linking nodes. It adds node in the downwards direction of the given node.
     *
     * @param currentN  A current node
     * @param dataGiven Data that will be assigned to the node.
     * @param nameGiven Name that will be given to the node.
     */
    private void addNodeD(Node currentN, int dataGiven, String nameGiven) {
        Node nodeToBeAdded = new Node(dataGiven, nameGiven);
        nodeToBeAdded.up = currentN;
        currentN.down = nodeToBeAdded;
    }
    /**
     * A function that links node to the place that we declare.
     *
     * @param rowGiven  A row in which we add a new node.
     * @param colGiven  A column in which we add a new node.
     * @param nameGiven Name that will be given to the node.
     */
    private void linkNode(int rowGiven, int colGiven, String nameGiven) {
        Node linkingNode = new Node(1, nameGiven);
        Node pointer;
        /////////////////////////////////////////////
        int pointerInt = 0;
        pointer = head;
        while (pointerInt <= colGiven) {
            pointer = pointer.right;
            pointerInt++;
        }
        while (pointer.down != null) {
            pointer = pointer.down;
        }
        //Link up, down
        linkingNode.up = pointer;
        pointer.down = linkingNode;
        /////////////////////////////////////////////
        pointer = head;
        pointerInt = 0;
        while (pointerInt <= rowGiven) {
            pointer = pointer.down;
            pointerInt++;
        }
        while (pointer.right != null) {
            pointer = pointer.right;
        }
        //Link left, right
        linkingNode.left = pointer;
        pointer.right = linkingNode;
        /////////////////////////////////////////////
    }
    /**
     * Helper function for Algorithm X that finds the column with the least amount of 1's in the DLL structure.
     * @return returns a node that is in the column where there is the least amount of 1's.
     */
    private Node findColMinNodes() {
        Node pointer = head.right;
        Node catchNodes;
        Node minNode = head;
        int counter;
        int min = INPUT.length;             //amount of rows
        while (pointer != null) {
            counter = 0;
            catchNodes = pointer;
            while (catchNodes.down != null) {
                catchNodes = catchNodes.down;
                counter++;
            }
            if (counter < min && counter != 0) {
                minNode = pointer;
                min = counter;
            }
            pointer = pointer.right;
        }
        return minNode;
    }

    //DANCING LINKS - ROWS AND COLUMNS
    /**
     * A helper function for deletingColumnLine(). It covers the whole row line of nodes.
     * @param line Indication of which line to cover.
     */
    private void coverRLine(int line) {
        Node temp = head;
        //int pointer = 0;
        // Here we go to the line we need

        for (int i = 0; i < line; i++) {
            temp = temp.down;
        }
        if (temp != null) {
            coveredLines.add(line);
            temp = temp.right; // Go out of column so we don't remove it
            while (temp != null) {
                removeNode(temp);
                temp = temp.right;
            }
        }
    }
    /**
     * A helper function to uncover the whole row line of nodes.
     * @param line Indication of which line to uncover.
     */
    private void uncoverRLine(int line) {
        Node temp = head;
        // Here we go to the line we need
        for (int i = 0; i < line; i++) {
            temp = temp.down;
        }
        if (temp != null) {
            temp = temp.right; // Go out of column so we don't replace it
            while (temp != null) {
                restoreNode(temp);
                temp = temp.right;
            }
        }
    }
    /**
     * Helper function that uncovers all previously closed nodes.
     */
    private void uncoverPreviousLines() {
        if (coveredLines.size() > 0) {
            int covLineSize = coveredLines.size() - 1;
            while (covLineSize != 0) {
                uncoverRLine(coveredLines.get(covLineSize));
                covLineSize--;
            }
        }
    }
    /**
     * A helper function for dancing links. It deletes all the rows intersecting with the given node.
     * @param tempGiven A node that we need to delete all intersections with.
     */
    private void deletingColumnLine(Node tempGiven) {
        while (tempGiven.up != null) {
            tempGiven = tempGiven.up;
        }
        while (tempGiven.down != null) {
            tempGiven = tempGiven.down;
            coveredNodes++;
            String[] stringArr = tempGiven.name.split("_");
            coverRLine(Integer.parseInt(stringArr[1]) + 1);
        }
    }

    //HELPERS
    /**
     * This method sums each column of given 2D array
     * @param arr 2D array which will be used to create 1D array
     * @return 1D array that is derived by adding all digits in a column
     */
    int[] sumColumns(int[][] arr) {
        int[] summed = new int[arr[0].length];
        for (int j = 0; j < summed.length; j++) {
            int sum = 0 ;
            for (int[] row : arr) {
                    sum += row[j];
            }
            summed[j] = sum ;
        }
        return summed;
    }
    /**
     * Helper function that evaluates how good a partial solution is.
     * @param input A partial solution that is evaluated.
     * @return returns an amount of columns which contain only 0's.
     */
    private int nZeroSumColumns(int[][] input) {
        int nColumns = input[0].length;
        int count;
        int zeroSumColumnCount = 0;

        for (int i = 0; i < nColumns; i++) {
            count = 0;
            for (int[] ints : input) {
                if (ints[i] != 0) {
                    count++;
                }
            }
            if (count == 0) {
                zeroSumColumnCount++;
            }

        }

        return zeroSumColumnCount;
    }
    /**
     * Adding the same array side by side until reaching the size of the array which contains the copied arrays
     * @param smaller The array which will be copied
     * @param truck The array contains the smaller array several times
     * @return The array which stores copied arrays
     */
    int[] copyLayers(int[] smaller , int[] truck){
        for (int i = 0; i < truck.length / smaller.length; i++) {
            System.arraycopy(smaller, 0, truck, i * (smaller.length), smaller.length);
        }
        return truck;
    }
    /**
     * Updates the instance field 'solution' such that it can be used for visualization.
     */
    private void printTheAnswer() {
        String[] temp;
        if(Solutions.size() > 0 )
            Solutions = new ArrayList<>();
        for (Node probableAnswer : probableAnswers) {
            temp = probableAnswer.name.split("_");
            Solutions.add(Integer.valueOf(temp[1]));
        }

        //Pre Work
        int[] truckWithID = new int[1320];
        int[] truckWithNumber = new int[1320];

        //Work
        int[][] solutionsArrayWithID = DLXdatabase.convertForEachPiece(Solutions);
        int[][] solutionsArrayWithNumber = DLXdatabase.convertForVisualisation(Solutions);

        int [] smallerWithId = sumColumns(solutionsArrayWithID);
        int [] smallerWithNumber = sumColumns(solutionsArrayWithNumber);

        //Stores the solution for visualization
        solutionWIthID = copyLayers( smallerWithId , truckWithID);
        solutionWithNumber = copyLayers( smallerWithNumber , truckWithNumber);

    }
}