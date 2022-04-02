/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package frame;
import algorithms.DLX;
import algorithms.DLXdatabase;
import algorithms.Greedy;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import parcels.Container;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import parcels.Cube;
import parcels.Parcel;
import parcels.Pentocube;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class for creating GUI with 3D visualisation
 */
public class Truck extends Application implements EventHandler<ActionEvent> {

    private static final int CAM_SPEED = 15;
    private static final int BOX_SIZE = 30;

    private Button fill;
    private  Button clear;
    private Button resetCam;
    ChoiceBox<String> algorithmChoice;
    ChoiceBox<String> sorting;
    Button individualColors;
    Button blockColors;
    Slider slider;
    Label noSolution;
    PerspectiveCamera pCamera;

    SpecialGroup group3D;
    ArrayList <Box> boxesIndividual;
    ArrayList <Box> boxesBlocks;
    Label percentage;
    Label valueParcels;
    PhongMaterial basicMat;

    RadioButton abcRB;
    RadioButton lptRB;

    Label aLbl;
    Label bLbl;
    Label cLbl;

    TextField amount1;
    TextField amount2;
    TextField amount3;
    TextField value1;
    TextField value2;
    TextField value3;
    int rotationX;
    int rotationY;
    int[] currentSolution = new int[1320];
    int[] currentBlocks = new int[1320];
    ArrayList<Integer> pieces = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {

        boxesIndividual = new ArrayList<>();
        boxesBlocks = new ArrayList<>();
        int transX = 0; int transY = 0; int transZ = 0;

        for(int x = 0; x<33; x++) {                 //height layer
            for (int y = 0; y < 8; y++) {           //depth layer
                for (int z = 0; z < 5; z++) {       //width layer
                    Box box = createBox(BOX_SIZE, transX, transY, transZ);
                    box.setTranslateX(transX);
                    boxesIndividual.add(box);
                    boxesBlocks.add(box);
                    transY += BOX_SIZE;
                }
                transZ += BOX_SIZE;
                transY = 0;
            }
            transX += BOX_SIZE;
            transZ = 0;
        }
        ArrayList <Box> layer1 = layerCreator(boxesIndividual,5);
        ArrayList <Box> layer2 = layerCreator(boxesIndividual,4);
        ArrayList <Box> layer3 = layerCreator(boxesIndividual,3);
        ArrayList <Box> layer4 = layerCreator(boxesIndividual,2);

        group3D = new SpecialGroup();
        pCamera = new PerspectiveCamera();

        //add boxes to 3D group
        for (Box box : boxesIndividual) {
            group3D.getChildren().add(box);
        }
        //SubScene containing 3D visualization
        SubScene subScene3D = new SubScene(group3D,1080,720,true, SceneAntialiasing.BALANCED);
        subScene3D.setFill(Color.WHITESMOKE);

        //setting view of the group
        group3D.translateXProperty().set(45);
        group3D.translateYProperty().set(200);
        group3D.translateZProperty().set(400);
        group3D.rotateAroundX(15);
        group3D.rotateAroundY(30);
        rotationY=0;

        //2D Panel
        Pane backPane = new Pane();
        backPane.setPrefSize(1380,720);

        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(300,720);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Font biggerLabels = new Font("Verdana", 14);

        Text choice = new Text("Choose parcels type");
        choice.setFont(biggerLabels);
        gridPane.add(new HBox(30, choice),0,0);

        abcRB = new RadioButton("A B C");
        lptRB = new RadioButton("L P T");

        abcRB.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        lptRB.setFont(Font.font("Verdana", FontWeight.NORMAL, 13));
        abcRB.setSelected(true);
        abcRB.setOnAction(this);
        lptRB.setOnAction(this);
        ToggleGroup radioButtons = new ToggleGroup();
        abcRB.setToggleGroup(radioButtons);
        lptRB.setToggleGroup(radioButtons);

        HBox radioBox = new HBox(30, abcRB, lptRB);
        gridPane.add(radioBox, 0,1);

        Label amount = new Label ("Amount");
        amount.setPrefSize(90, 5);
        amount.setAlignment(Pos.CENTER);

        Label value = new Label("Value");
        value.setPrefSize(100, 5);
        value.setAlignment(Pos.CENTER);

        gridPane.add(new HBox(9,new Text("  "), amount, value),0,2);

        amount1 = new TextField("90");
        amount2 = new TextField("90");
        amount3 = new TextField("90");

        amount1.setPrefSize(100, 20);
        amount2.setPrefSize(100, 20);
        amount3.setPrefSize(100, 20);

        value1 = new TextField("3");
        value2 = new TextField("4");
        value3 = new TextField("5");

        value1.setPrefSize(100, 20);
        value2.setPrefSize(100, 20);
        value3.setPrefSize(100, 20);

        aLbl = new Label("A");
        aLbl.setFont(biggerLabels);
        aLbl.setPrefSize(20,10);
        bLbl = new Label("B");
        bLbl.setFont(biggerLabels);
        bLbl.setPrefSize(20,10);
        cLbl = new Label("C");
        cLbl.setFont(biggerLabels);
        cLbl.setPrefSize(20,10);
        gridPane.add(new HBox(10, aLbl, amount1, value1),0,3);
        gridPane.add(new HBox(10, bLbl, amount2, value2),0,4);
        gridPane.add(new HBox(10, cLbl, amount3, value3),0,5);

        algorithmChoice = new ChoiceBox<>();
        algorithmChoice.getItems().add("DLX"); algorithmChoice.getItems().add("Greedy");
        HBox algorithmBox = new HBox(algorithmChoice);

        sorting = new ChoiceBox<>();
        sorting.getItems().add("Ascending volume"); sorting.getItems().add("Descending volume");
        sorting.getItems().add("Ascending ratio"); sorting.getItems().add("Descending ratio");
        sorting.getItems().add("No sorting");
        sorting.setValue("No sorting");
        HBox sortingBox = new HBox(sorting);

        gridPane.add(new VBox(10, new Text("Choose algorithm to be used:"), algorithmBox),0,6);
        gridPane.add(new VBox(10,new Text("Choose sorting option:"),sortingBox),0,7);
        fill = new Button("Fill truck");
        fill.setOnAction(this);

        noSolution = new Label("");
        gridPane.add(new HBox(5,fill, noSolution),0,9);


        Label valueLbl = new Label("Value of parcels:");
        Label filledLbl = new Label("Truck filled in:");
        valueLbl.setPrefSize(90,20);
        filledLbl.setPrefSize(90,20);
        percentage = new Label("--%");
        valueParcels = new Label("--");

        gridPane.add(new HBox(10, valueLbl, valueParcels),0,11);
        gridPane.add(new HBox(10, filledLbl, percentage),0,12);

        clear = new Button ("Clear truck");
        gridPane.add(new HBox(clear),0,13);
        clear.setOnAction(this);

        slider = new Slider(1, 5, 1);
        slider.adjustValue(5);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int layers = newValue.intValue();
            if (layers ==1){
                group3D.getChildren().clear();
                for (Box box : layer1) {
                    group3D.getChildren().add(box);
                }
            }else if (layers ==2){
                group3D.getChildren().clear();
                for(int i=0; i<layer2.size();i++){
                    group3D.getChildren().add(layer1.get(i));
                    group3D.getChildren().add(layer2.get(i));
                }
            }else if (layers ==3){
                group3D.getChildren().clear();
                for(int i=0; i<layer3.size();i++){
                    group3D.getChildren().add(layer1.get(i));
                    group3D.getChildren().add(layer2.get(i));
                    group3D.getChildren().add(layer3.get(i));
                }
            }else if (layers ==4){
                group3D.getChildren().clear();
                for(int i=0; i<layer4.size();i++){
                    group3D.getChildren().add(layer1.get(i));
                    group3D.getChildren().add(layer2.get(i));
                    group3D.getChildren().add(layer3.get(i));
                    group3D.getChildren().add(layer4.get(i));
                }
            }else if (layers ==5){
                group3D.getChildren().clear();
                for (Box box : boxesIndividual) {
                    group3D.getChildren().add(box);
                }
            }
        });

        gridPane.add(new HBox(10, new Text("Layers displayed:")), 0, 15);
        gridPane.add(new HBox(10,slider),0,16);

        resetCam = new Button("Reset view");
        gridPane.add(new HBox(resetCam),0,17);
        resetCam.setOnAction(this);

        Label controlsLbl = new Label("Controls");
        controlsLbl.setAlignment(Pos.CENTER);
        controlsLbl.setFont(Font.font("Verdana",FontWeight.BOLD, 14));
        Label controls = new Label("Q / E - zoom in/out\n" +
                "W / S - move up/down\n" +
                "A / D - move to the right/left\n" +
                "N / M - rotate horizontally \n");
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setWrapText(true);
        gridPane.add(new VBox(3,controlsLbl,controls),0,18);

        individualColors = new Button("Individual parcels view");
        blockColors = new Button("Type of parcels view");

        gridPane.add(new HBox(5, individualColors, blockColors), 0, 19);
        individualColors.setOnAction(this);
        blockColors.setOnAction(this);

        backPane.getChildren().add(gridPane);
        gridPane.setTranslateX(1080);

        subScene3D.setCamera(pCamera);

        Pane pane3D = new Pane();
        pane3D.setPrefSize(1080,720);
        pane3D.getChildren().add(subScene3D);
        backPane.getChildren().add(pane3D);

        Scene scene = new Scene(backPane);

        //CAMERA CONTROL WITH KEYS
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case W -> pCamera.setTranslateY(pCamera.getTranslateY() + 2*CAM_SPEED);
                case S -> pCamera.setTranslateY(pCamera.getTranslateY() - 2*CAM_SPEED);

                case M -> group3D.rotateAroundY(CAM_SPEED);
                case N -> group3D.rotateAroundY( CAM_SPEED * -1);

                case Q -> pCamera.setTranslateZ(pCamera.getTranslateZ() + 2*CAM_SPEED);
                case E -> pCamera.setTranslateZ(pCamera.getTranslateZ() - 2*CAM_SPEED);
                case D -> pCamera.setTranslateX(pCamera.getTranslateX() - 2*CAM_SPEED);
                case A -> pCamera.setTranslateX(pCamera.getTranslateX() + 2*CAM_SPEED);
            }
        });

        pCamera.setTranslateY(pCamera.getTranslateY() - 2* CAM_SPEED);
        pCamera.setTranslateY(pCamera.getTranslateY() - 2* CAM_SPEED);
        pCamera.setTranslateY(pCamera.getTranslateY() - 2* CAM_SPEED);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Knapsack");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    @Override
    public void handle(ActionEvent event) {
        //adjust ABC/LPT labels
        if (lptRB.isSelected()){
            aLbl.setText("L");
            bLbl.setText("P");
            cLbl.setText("T");
        }else if (abcRB.isSelected()) {
            aLbl.setText("A");
            bLbl.setText("B");
            cLbl.setText("C");
        }

        if(event.getSource()==fill){
            //read type of parcels
            String type = "c";
            if(lptRB.isSelected()) {
                type = "p";
            }
            String sortingChoice = sorting.getValue();
            if(sortingChoice.equals("no sorting")){ sortingChoice="";}
            noSolution.setText("");

            //arrays storing parcel settings
            int [] parcels = new int[3];
            int [] values = new int[3];
            parcels[0] = Integer.parseInt(amount1.getText());
            parcels[1] = Integer.parseInt(amount2.getText());
            parcels[2] = Integer.parseInt(amount3.getText());

            values[0] = Integer.parseInt(value1.getText());
            values[1] = Integer.parseInt(value2.getText());
            values[2] = Integer.parseInt(value3.getText());

            //read type of algorithm
            String algorithm = algorithmChoice.getValue();
            if(algorithm.equalsIgnoreCase("Greedy")) {
                Container container = new Container(33,8,5);
                Container byNumber =  new Container(33,8,5);
                Greedy g = new Greedy(type ,parcels[0], parcels[1], parcels[2], values[0], values[1], values[2] ,container , byNumber , sortingChoice);

                if(sortingChoice.equals("")) {
                    g.greedyExtended();
                }else{
                    g.greedy();}
                currentSolution = convertArr(g.getBY_NUMBER());
                currentBlocks = convertArr(g.getByID());

                makeListOfPiecesIDs(currentSolution);
                colorPiece(boxesIndividual,currentSolution);

                //delete empty boxes
                group3D.getChildren().clear();

                //add colored boxes to the group
                for (Box box : boxesIndividual) {
                    group3D.getChildren().add(box);}

                percentage.setText(g.getPercentFilled() + "%");
                valueParcels.setText(String.valueOf(container.getTotalValue()));
            }else if (algorithm.equals("DLX")){
                int length = 1 , width = 8 , height = 5;
                if (abcRB.isSelected()) {
                    length = 33;
                }
                Container container = new Container(length, width, height);
                Container byNumber = new Container(length, width, height);
                DLXdatabase dlxDatabase = new DLXdatabase( container , byNumber ,type  );
                dlxDatabase.run(type);

                Arrays.fill(currentBlocks , 0 );
                Arrays.fill(currentSolution , 0 );

                currentSolution = (DLX.solutionWithNumber != null ) ? DLX.solutionWithNumber : new int[1320];
                currentBlocks = (DLX.solutionWIthID != null ) ? DLX.solutionWIthID : new int[1320];

                //method to show label if there is no exact cover use currentBlocks and check zeros
                valueParcels.setText(String.valueOf(calculateValue(currentBlocks, type, values)));
                percentage.setText(calculatePercentage(currentBlocks) + "%");
                makeListOfPiecesIDs(currentSolution);
                colorPiece(boxesIndividual, currentSolution);

                //delete empty boxes
                group3D.getChildren().clear();

                //add colored boxes to the group
                for (Box box : boxesIndividual) {
                    group3D.getChildren().add(box);}
                if (checkExactCover(currentBlocks)){
                    noSolution.setText("No exact cover found.");}
            }

        }else if (event.getSource() ==clear) {
            cleanBoxes(boxesIndividual);
            group3D.getChildren().clear();
            for (Box box : boxesIndividual) {group3D.getChildren().add(box);}
            percentage.setText("--%");
            valueParcels.setText("--");
            slider.adjustValue(5);
        }else if (event.getSource() == resetCam){
            //location on screen
            pCamera.setTranslateZ(0);
            pCamera.setTranslateX(0);
            pCamera.setTranslateY(-90);
            //fixing rotations
            if(rotationY>360){
                rotationY= rotationY%360;}
            group3D.rotateAroundY(-1*rotationY);
            rotationY=0;
        }

        else if(event.getSource() == blockColors){
            group3D.getChildren().clear();
            for (int i = 0; i < boxesBlocks.size(); i++) {
                colorBox(boxesBlocks.get(i), currentBlocks[i]);
            }
            for (Box box : boxesBlocks) {
                group3D.getChildren().add(box);
            }
        }else if (event.getSource() == individualColors){
            group3D.getChildren().clear();
            makeListOfPiecesIDs(currentSolution);
            colorPiece(boxesIndividual,currentSolution);

            for (Box box : boxesIndividual) {
                group3D.getChildren().add(box);
            }
        }
    }

    /**
     * Inner class extending Group class of JavaFX to create and add methods for rotation around axes
     */
    class SpecialGroup extends Group {
        Rotate rotate;
        Transform transformR = new Rotate();

        /**
         * Method to rotate the group node around the X-axis
         * @param change angle of rotation
         */
        void rotateAroundX(int change){
            rotate = new Rotate(change, 495,75,120,Rotate.X_AXIS);
            rotationX += change;
            transformR = transformR.createConcatenation(rotate);
            this.getTransforms().clear();
            this.getTransforms().addAll(transformR);
        }

        /**
         * Method to rotate the group node around the Y-axis
         * @param change angle of rotation
         */
        void rotateAroundY(int change){
            rotate = new Rotate(change, 495,75,120,Rotate.Y_AXIS);
            rotationY += change;
            transformR = transformR.createConcatenation(rotate);
            this.getTransforms().clear();
            this.getTransforms().addAll(transformR);
        }
    }

    /**
     * Method to color boxes in an ArrayList according to 1D array solution
     * @param boxes ArrayList of boxes to be colored
     * @param colCode 1D array representing container with individual parcels
     */
    public void colorPiece(ArrayList<Box> boxes, int[] colCode){
        int currChecked;

        for(int i = pieces.get(1); i<pieces.size(); i++){

            currChecked = pieces.get(i);

            //generate a material with random color
            Color nextRandomCol = Color.rgb(54 + (int)(Math.random()*200),54 + (int)(Math.random()*200),54 + (int)(Math.random()*200));
            PhongMaterial myMat = new PhongMaterial();
            myMat.setDiffuseColor(nextRandomCol);

            //color boxes with currently checked ID in the ArrayList with the generated material
            for (int j = 0; j<1320; j++){
                if(colCode[j] == currChecked){
                    boxes.get(j).setMaterial(myMat);
                }
            }
        }
        //set empty boxes as white
        for (int j = 0; j<1320; j++) {
            if (colCode[j] == 0) {
                boxes.get(j).setMaterial(new PhongMaterial(Color.WHITE));
            }
        }
    }

    /**
     * Method converting a 3D array into a 1D array
     * @param dim3 3D array to be converted
     * @return one-dimensional array resulting from conversion
     */
    public int[] convertArr(int[][][] dim3){
        int[] dim1 = new int[dim3.length * dim3[0].length * dim3[0][0].length ];
        int q =0;
        for (int[][] ints : dim3) {
            for (int y = 0; y < dim3[0].length; y++) {
                for (int z = 0; z < dim3[0][0].length; z++) {
                    dim1[q] = ints[y][z];
                    q++;
                }
            }
        }
        return dim1;
    }

    /**
     * Method to create a box with desired size and orientation in space
     * @param size desired dimension of the box
     * @param x translation on X axis
     * @param y translation on Y axis
     * @param z translation on Z axis
     * @return the newly created box
     */
    public Box createBox(int size, int x, int y, int z){
        Box box = new Box (size, size, size);
        basicMat = new PhongMaterial();
        basicMat.setDiffuseColor(Color.FLORALWHITE);
        box.setMaterial(basicMat);

        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(z);
        return box;
    }
    /**
     * Method to reset the colour of boxes
     * @param boxes ArrayList of boxes that need to change the material changed
     * @return ArrayList of boxes of the same color
     */
    public ArrayList<Box> cleanBoxes(ArrayList<Box> boxes){

        for (Box box : boxes) {
            box.setMaterial(basicMat);
        }
        return boxes;
    }
    /**
     * Method to set material of a box depending on the integer corresponding to color codes
     * @param box box to be colored
     * @param c integer indicating parcel type (color)
     * @return colored box
     */
    public Box colorBox (Box box, int c){

        PhongMaterial greenMat = new PhongMaterial();
        greenMat.setDiffuseColor(Color.MEDIUMSEAGREEN);
        box.setMaterial(greenMat);

        PhongMaterial orangeMat = new PhongMaterial();
        orangeMat.setDiffuseColor(Color.ORANGE);
        box.setMaterial(orangeMat);

        PhongMaterial pinkMat = new PhongMaterial();
        pinkMat.setDiffuseColor(Color.HOTPINK);
        box.setMaterial(pinkMat);

        PhongMaterial emptyMat = new PhongMaterial();
        emptyMat.setDiffuseColor(Color.WHITE);
        box.setMaterial(emptyMat);

        if (c==-1){ box.setMaterial(greenMat);
        }else if (c==-2){ box.setMaterial(pinkMat);
        }else if (c==-3){ box.setMaterial(orangeMat);
        } else if (c==0) { box.setMaterial(emptyMat);}
        return box;
    }
    /**
     * Method to create a sorted ArrayList of all piece IDs for individual coloring
     * @param arr 1D array representing container with parcels
     */
    public void makeListOfPiecesIDs(int[] arr){
        for (int i = 1; i < arr.length; i++) {
            int j;
            for (j = 0; j < i; j++)
                if (arr[i] == arr[j]) {
                    pieces.add(arr[i]);
                    break;
                }
        }
        Collections.sort(pieces);
    }
    /**
     * Method to create individual layers of the truck
     * @param boxes ArrayList of all boxes
     * @param layerNo number of layer needed
     * @return separated layer
     */
    public ArrayList<Box> layerCreator( ArrayList<Box> boxes , int layerNo ){
        ArrayList<Box> layer = new ArrayList<>();
        for(int i = layerNo , j = 0  ; j < boxes.size() ; j++ , i++){
            if( j % 5 == 0 ){
                layer.add( boxes.get( i-1) );
            }
        }
        return layer;
    }
    /**
     * Method to count amount of parcels of certain type
     * @param solutionArray array representing all boxes in the truck and parcel IDs
     * @param idNum ID of parcel type to be counted
     * @param type specifies ABC/LPT parcel type
     * @return number of parcels
     */
    public int countPieces( int[] solutionArray , int idNum , String type){
        int number = 0;

        for (int j : solutionArray) {
            if (j == idNum)
                number++;
        }
        Parcel c ;
        if(type.equalsIgnoreCase("c")) {
            c = new Cube(idNum);
        }else
            c = new Pentocube(idNum);
        return (int)(number/ c.getVolume());
    }

    /**
     * Method to calculate value of parcels placed in the truck with DLX algorithm
     * @param solutionArray array representing all boxes in the truck and parcel IDs
     * @param type specifies ABC/LPT parcel type
     * @param values array with values of parcels from user input
     * @return value of parcels in truck
     */
    public int calculateValue( int[] solutionArray ,String type , int[] values ){
        int finalValue = 0 ;
        int[] ids = new int[]{-1,-2,-3};
        for (int i = 0 ; i < ids.length ; i++) {
            finalValue += countPieces(solutionArray , ids[i] , type) * values[i];
        }
        return finalValue;
    }

    /**
     * Method to calculate percentage of truck filled when using DLX algorithm
     * @param solutionArray array representing all boxes in the truck and parcel IDs
     * @return percentage of truck filled
     */
    public double calculatePercentage(int[] solutionArray){
        int filled = 0;
        for (int i :
                solutionArray) {
            if(i !=0)
                filled++;
        }
        return (double)filled / solutionArray.length * 100;
    }

    /**
     * Method to check if there is solution from DLX algorithm
     * @param solutionArr array representing all boxes in the truck and parcel IDs
     * @return boolean whether message should be shown
     */
    public boolean checkExactCover(int [] solutionArr){
        boolean showLabel = true;
        for (int j : solutionArr) {
            if (j != 0) {
                showLabel = false;
                break;
            }
        }
    return showLabel;
    }
}
