------------------------------------------
Project 1.1 - Phase 3
Date - 16 january 2022
Authors -  Pie de Boer, Emre Karabulut, Agata Oskroba, Liutauras Padimanskas, Samantha Cijntje, Liwia Padowska and Jadon Smith
------------------------------------------
Overview:
In this repository you will find the code for Project 1.1 - Phase 3 of group 25.  
Besides that, the file for the presentation can be found here.
The goal of phase 3 was to optimize the packing of a three-dimensional cargo space.
Furthermore, we had to visualise our optimal solution in a virtual 3D model.

To be able to start with the 3D knapsack problem we created a database for the given 3D shapes. Furthermore, we implemented following algorithms to solve the mentioned problem:

Greedy Algorithm

Greedy Algorithm in the hopes of arriving at a globally optimum solution, makes a locally optimal choice. Clearly, greedy algorithms don't always produce the best results, however they often produce mid-to-high-quality solutions. The selling point of the algorithm is that it can also be applied to various different situations. The greedy algorithm uses a breadth-first searching technique and it doesn’t backtrack. Our version of Greedy algorithm evaluates only one parcel’s placement at a time and is not aware of the next parcel. It does so based on the fitness score that is calculated according to the following factors:

1) Total empty space: the number of empty spaces of dimensions 0.5x0.5x0.5 in the cargo space
2) Touching x wall: the number of 0.5x0.5 grids on the left wall of the container that are touching packages
3) Touching y wall: the number of 0.5x0.5 grids on the front wall of the container that are touching packages
4) Touching z wall: the number of 0.5x0.5 grids on the bottom wall of the container that are touching packages
5) Number of touching parcel sides: the number of connected 0.5x0.5 sides of a parcel to another parcel
6) Number of x gaps between parcels: the number of gaps between parcels looking from the x-direction
7) Number of y gaps between parcels: the number of gaps between parcels looking from the y-direction
8) Number of z gaps between parcels: the number of gaps between parcels looking from the z-direction
9) Total container value: the total value of all parcels in the container 

Extended Greedy Algorithm

Extended Greedy Algorithm works almost absolutely the same as the above mentioned algorithm. The only difference is that before putting a parcel in a cargo space it evaluates all of the given parcel types and picks the best one according to the fitness values scores previously mentioned. It does so until it runs out of some sort of parcel type.

Genetic Algorithm

A genetic algorithm uses the evolutionary generational cycle to provide a high-quality solution to these problems that improves over time. In our case Genetic Algorithm is used to optimize the performance of the previous algorithm. It lets us to find out the importance of each fitness test. Pseudocode for this algorithm:
    initialize random first generation
    for (g = 1 to N(gen))
          	for (i(pop) = 0 to N(pop))
                    	Simulates the 3D-Packing with i
        	end for
      	    for(i(pop) = 0 to N(pop))
                    	Pair up consecutive pairs
                    	Save the fitter one according to the value from each simulation
        	end for
      	    for (i(pool) = 0 to N(pool))
                    	Pair up fit individuals
                    	for (i = 0 to 4)
                	        	Discrete Crossover: Create new individual using i(n) and i(n+1) genes
                	        	Mutate
                    	end for
    Move 4 new generated individuals to g+1
          	end for
    end for

Algorithm X

Algorithm X is an algorithm developed by Donald Knuth for finding all the solutions to an exact cover problem represented by a given binary matrix. However, we further extended it to evaluate not only the exact cover but also the scenarios where the exact cover is not possible or takes too long to find the solution. The algorithm is non-deterministic, depth-first, recursive and uses backtracking. More information on the algorithm and data structure used to tackle this problem can be found in Donald Knuth's paper "Dancing Links" (2000).

User Instructions:
For further reference besides this README, check the JavaDoc folder.
This project was built on Java v16.0.2.
To run the project on your machine run the Truck.java file provided in the src/frame folder.

After the frame "Knapsack" pops up:
-> Choose the parcel type.
-> Input the amount and values of the selected parcels.
-> In the drop-down menu choose the algorithm you want to see running.
-> Choose the sorting method for the database.
-> Finally, you can fill the truck.
-> Furthermore, you can clear the truck to see other solutions for other inputs.
-> To specify which layers should be displayed use the slider.
-> For the controls, look at the control section in the bottom-right corner.

Extract the provided 25-FinalCode.zip. Follow the instructions listed above.
