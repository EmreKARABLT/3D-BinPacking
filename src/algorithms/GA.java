/**
 * @author Group 25 (Pie De Boer, Liwia Padowska, Emre Karabulut, Liutauras Padimanskas, Agata Oskroba, Samantha Cijntje, Jadon Smith )
 * @version 17.01.22
 */
package algorithms;
import parcels.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
/**
 * This class includes the genetic algorithm that finds the optimal weights for the 3D packing problem.
 */
public class GA {

	private final Random RANDOM = new Random();

	private final double MUTATION_RATE = 0.1;
	private final int POPULATION_SIZE = 64;
	private final int GENES = 9;

	private final double[][] CHROMOSOMES = new double[POPULATION_SIZE][GENES];
	private final double[] SCORES = new double[POPULATION_SIZE]; // Scores that bots get will be stored here

	private int generation = 1 ;

	/**
	 * The constructor for the genetic algorithm.
	 * Initializes the first generation.
	 */
	public GA() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			for (int j = 0; j < GENES; j++) {
				CHROMOSOMES[i][j]= RANDOM.nextDouble()*5; // Some values from 0 (incl.) to 1 (excl.)
			}
		}

	}

	/**
	 * The method creates new generations using the previous generation.
	 */
	public void newGen() {
		System.out.println("--------------------" + generation);
		ArrayList<double[]> matingPool = new ArrayList<>();

		getScores(CHROMOSOMES);
		for (int i = 0; i < POPULATION_SIZE; i = i+2){
			if (SCORES[i]> SCORES[i+1])
				matingPool.add(CHROMOSOMES[i]);
			else
				matingPool.add(CHROMOSOMES[i+1]);
		}

		//discrete crossover
		for(int z = 0; z < matingPool.size(); z = z+2){
			double[] mother = matingPool.get(z);
			double[] father = matingPool.get(z+1);
			for (int k = 0; k < 4  ; k++) {
				double[] child1 = new double[GENES];
				for (int i = 0; i < child1.length; i++) {
					if (RANDOM.nextDouble() < 0.5) {
						child1[i] = mother[i];
					} else {
						child1[i] = father[i];
					}
					if(RANDOM.nextDouble() < MUTATION_RATE){
						child1[i]= RANDOM.nextDouble()*5;
					}
				}
				CHROMOSOMES[k] = child1;
			}
		}
		System.out.println(Arrays.deepToString(CHROMOSOMES));
		generation++;
	}

	/**
	 * Following method runs the greedy algorithm with given weight ( simulates 3D Packing problem )
	 * @param chromosomes for first generation randomized weights, for the rest evaluated generations
	 */
	public void getScores(double[][] chromosomes) {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			Fitness fitness = new Fitness();
			fitness.setWeights(chromosomes[i]);
			Container c1 = new Container(33, 8, 5), c2 = new Container(33, 8, 5);
			System.out.println(Arrays.toString(chromosomes[i]));

			Greedy greedy = new Greedy("p" ,
					264,264,264,
					3 ,4 ,5 ,
					c1,c2,
					"descending ratio");
			greedy.setFitness_c(fitness);
			greedy.greedy();
			SCORES[i] = c1.getTotalValue();
			if(SCORES[i] > 1230 )
				System.out.println("1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  \n 1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230  1230\n");
		}
	}

	/**
	 * Main method used to run Genetic algorithm
	 * @param args default parameter
	 */
	public static void main(String[] args) {
		int iteration = 10;
		double starting_ = System.currentTimeMillis();
		GA ga = new GA();
		while( ga.generation <= 15 ) {
			ga.newGen();
		}
		double ending_ = System.currentTimeMillis();
		double average = (ending_ - starting_) / iteration;
		System.out.printf("Mutation rate = %.2f  ,  population size = %d  ,  %s method used  ,  Average Execution Time after %d iterations is %.2f ms "
				, ga.MUTATION_RATE, ga.POPULATION_SIZE, "discrete crossover"  , iteration , average);
	}
}
