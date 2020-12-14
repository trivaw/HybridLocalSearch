package com.tutorial.tsp.ga;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Driver {

	public final int cityNumber = 53;
	private int setNumber;
	private double tMax = 1616;
	private int populationSize=400;
	private int bestScore;
	private int bestIndex;
	private int worstScore=9999999;
	private int worstIndex;

	private final int[] cityScore = new int[cityNumber];
	private int[] setScore;
	public int[] cityCluster = new int[cityNumber];
	private int score[]= new int [populationSize];
	
	private int[][] clusters;
	private int data[][];
	private double[][] cities = new double[cityNumber][2];
	public double m_Distance[][] = new double[cityNumber][cityNumber];
	private List<Integer> population[]= new ArrayList [populationSize];
	

	
	List<Integer> randomSet=new ArrayList<Integer>();
	List<Integer> randomPath = new ArrayList<Integer>();
//	List<Integer> order = new ArrayList<Integer>(populationSize);
	List<Integer> firstWinner = new ArrayList<Integer>();
	List<Integer> secondWinner = new ArrayList<Integer>();
	List<Integer> offspring = new ArrayList<Integer>();





	public static void main(String[] args) {
		Driver d = new Driver();
		d.run_one_time();
		d.generateRandomPopulation();
		d.firstStep();
		System.out.println();
		System.out.println("We will do the crossover between the winning chromosomes");
		d.crossover();
	}
	
	public void crossover() {
		List<Integer> uniqueElementsWinnerOne = new ArrayList<Integer>();
		List<Integer> uniqueElementsWinnerTwo = new ArrayList<Integer>();
		List<Integer> commonlElementsOrderOne = new ArrayList<Integer>();
		List<Integer> commonlElementsOrderTwo = new ArrayList<Integer>();
		
		uniqueElementsWinnerOne.addAll(firstWinner);
		uniqueElementsWinnerOne.removeAll(secondWinner);
		System.out.println("uniqueElementsWinnerOne are "+uniqueElementsWinnerOne);
		
		System.out.println();
		
		uniqueElementsWinnerTwo.addAll(secondWinner);
		uniqueElementsWinnerTwo.removeAll(firstWinner);
		System.out.println("uniqueElementsWinnerTwo are "+uniqueElementsWinnerTwo);
		
		System.out.println();
		
		commonlElementsOrderOne.addAll(firstWinner);		
		commonlElementsOrderOne.retainAll(secondWinner);
		System.out.println("commonlElements order one "+commonlElementsOrderOne);
		
		System.out.println();
		
		commonlElementsOrderTwo.addAll(secondWinner);		
		commonlElementsOrderTwo.retainAll(firstWinner);
		System.out.println("commonlElements order two "+commonlElementsOrderTwo);

		
		/*
		 *  if we have only 4 common elements or less, we don't need to do the crossover, 
		 *  we just add the unique elements in the genome .
		 *  we consider the origin as a two common elements
		 */
		if (commonlElementsOrderOne.size()>=3) {
			
			if (!commonlElementsOrderOne.equals(commonlElementsOrderTwo )) {
				
				Random r = new Random();
				int crosspoint = r.nextInt(commonlElementsOrderOne.size()-2)+1;
				System.out.println("crosspoint "+crosspoint);
				
			}
		}
		
		
		
	}
	
	public void firstStep() {
		System.out.println("winner one ");
		pickTwoRandomSolutions(firstWinner);
		System.out.println("winner two ");
		pickTwoRandomSolutions(secondWinner);
		 while(firstWinner.equals(secondWinner)){
			 System.out.println("the new second winner is ");
			 secondWinner.clear();
			 pickTwoRandomSolutions(secondWinner);
		    }
	}
	
	public void pickTwoRandomSolutions(List<Integer> winner) {
		Random rn = new Random();
		int randomIndexOne = rn.nextInt(populationSize);
		int randomIndexTwo=rn.nextInt(populationSize);
		    while(randomIndexOne==randomIndexTwo){
				randomIndexTwo=rn.nextInt(populationSize);
		    }
//		System.out.println("randomIndexOne"+randomIndexOne);
//		System.out.println("randomIndexTwo"+randomIndexTwo);
//		System.out.println("population[randomIndexOne]"+population[randomIndexOne]);
//		System.out.println("population[randomIndexTwo]"+population[randomIndexTwo]);
//		System.out.println("score[randomIndexOne]"+score[randomIndexOne]);
//		System.out.println("score[randomIndexTwo]"+score[randomIndexTwo]);
		Winner(randomIndexOne, randomIndexTwo, winner);
		System.out.println(winner);

	}
	
	private void Winner(int indexOne, int indexTwo, List<Integer> winner) {
		if (score[indexOne]>score[indexTwo]) {
			winner.addAll(population[indexOne]);
			System.out.println("the score of the winner is "+ score[indexOne]);
		}
		else {
			winner.addAll(population[indexTwo]);
			System.out.println("the score of the winner is "+ score[indexTwo]);
		}
	}
	
	

	
	public void generateRandomPopulation() {

		for (int i = 0; i < populationSize; i++) {
			population[i] = new ArrayList<Integer>();
			population[i] = generateRandomCandidate();
			score[i]= calculate_path_profit(population[i]);
			System.out.println("-----------------------------------");	
			System.out.println("The score is "+ score[i]);
	        System.out.println(" Population is "+population[i]);
	        System.out.println("-----------------------------------");	
//			saveBestScore (score[i], i);
	        saveWorstScore (score[i], i);
	       
		}
//		 System.out.println("worst score is "+worstScore);
//		 System.out.println("worst index is "+worstIndex);
//		System.out.println("-----------------------------------");	
//		System.out.println("Best Score is "+ bestScore);
//        System.out.println("Best Population is "+population[bestIndex]);
//        System.out.println("-----------------------------------");	
	}

    private void saveBestScore (int pScore, int index) {
    	if(pScore>bestScore) {
    		bestScore=pScore;
    		bestIndex=index;
    	}
    }
    
    private void saveWorstScore (int pScore, int index) {
    	if(pScore<worstScore) {
    		worstScore=pScore;
    		worstIndex=index;
    	}
    }
    
    

	
public List<Integer> generateRandomCandidate() {
	generateRandomSet();
	generateInitialSolution();
	return generate_first_path();
}


	
	private void verify_clear_list(List<Integer> list) {
		if (list.size() > 0) {
			list.clear();
		}
	}
	
	public void generateRandomSet() {

		initialisePath();
		Collections.shuffle(randomSet.subList(1, setNumber));
//		System.out.println(randomSet);
	}
	
	
	private void initialisePath() {
		verify_clear_list(randomSet);
		for (int i = 0; i < setNumber; i++) {
			randomSet.add(i);
		}
		randomSet.add(0);
	}
	
	public void generateInitialSolution() {
		verify_clear_list(randomPath);
		randomPath.add(0);
		int index = 0;
		for (int i = 1; i < setNumber; i++) {
			index = findNearstPoint(index, randomSet.get(i));
			randomPath.add(index);
		}
		randomPath.add(0);
//		System.out.println(randomPath);

	}
	
	private int findNearstPoint(int pointA, int clusterB) {
		double distance;
//			int currentProfit;
		int index = clusters[clusterB][0];
		distance = m_Distance[pointA][index];
		for (int i = 1; i < clusters[clusterB].length; i++) {
			if (distance > m_Distance[pointA][clusters[clusterB][i]]) {
				distance = m_Distance[pointA][clusters[clusterB][i]];
				index = clusters[clusterB][i];
			}
		}
		return index;
	}
	
	
	public List<Integer> generate_first_path() {
		List<Integer> candidate = new ArrayList<Integer>();
		double timeBudget;
		double lastDistance;
		double nextDistance;
//		verify_clear_list(candidate);
		candidate.add(0);
		candidate.add(randomPath.get(1));
		timeBudget = tMax - (m_Distance[randomPath.get(0)][randomPath.get(1)]);

		for (int i = 1; i < randomPath.size() - 1; i++) {
			nextDistance = m_Distance[randomPath.get(i)][randomPath.get(i + 1)];
			lastDistance = m_Distance[randomPath.get(i + 1)][0];
			timeBudget = timeBudget - nextDistance - lastDistance;

			if (timeBudget > 0) {

				candidate.add(randomPath.get(i + 1));
			}

		}
		candidate.add(0);
		return candidate;
	}
	
	private int calculate_path_profit(List<Integer> path) {
		int profit_path = 0;
		for (int i = 0; i < path.size() ; i++) {
			profit_path = profit_path + cityScore[path.get(i)] ;
		}
//		System.out.println("The new profit is " + profit_path);
		return profit_path;
	}
	
	
	private double calculate_path_distance(List<Integer> pPath) {
		double distance = 0;
		for (int i = 0; i < pPath.size() - 1; i++) {
			distance = distance + m_Distance[pPath.get(i)][pPath.get(i + 1)];
		}
//		System.out.println("path distance is  " + distance);
		return distance;
	}

	
	public void run_one_time() {
		read_data();
		fillDistanceMatrix();
		prepare_data();
	}
	
	private void fillDistanceMatrix() {
		for (int i = 0; i < cityNumber; i++) {
			for (int j = 0; j < cityNumber; j++) {
				m_Distance[i][j] = calculateDistanceBetweenPoints(i, j);
//System.out.print(m_Distance[i][j]+" ");
			}
//System.out.println();
//System.out.println();


		}
	}
	
	private double calculateDistanceBetweenPoints(int a, int b) {
		double x1 = cities[a][0];
		double x2 = cities[b][0];
		double y1 = cities[a][1];
		double y2 = cities[b][1];
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}
		
	// First step, we read the data from csv file and txt file	

		public void read_data() {
			read_txt_file();


			readCSVfile("one.csv");



		}
		
		
		// By this method we read the coordinates of each point
		public void readCSVfile(String fileName) {
			int i = 0;
			File file = new File(fileName);
			try {
				Scanner inputStream = new Scanner(file);
				inputStream.next();
				while (inputStream.hasNext()) {
					String data = inputStream.next();
					String values[] = data.split(",");
					for (int j = 0; j < 2; j++) {
						cities[i][j] = Double.parseDouble(values[j + 1]);
					}
					i++;
				}
				inputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	public void prepare_data() {
		fill_setScore();
		fill_clusters();
		fill_cityScore();
		fill_points_cluster();
	}

	private void fill_points_cluster() {
		int index;
		for (int i = 0; i < data.length; i++) {
			for (int j = 2; j < data[i].length; j++) {
				index = data[i][j] - 1;
				cityCluster[index] = i;

//					System.out.println("index " + index + "  " + points_cluster[index]);
			}

		}

	}

	// fill_set_cluster is : each cluster has its vertices****set_cluster

	private void fill_clusters() {
		clusters = new int[setNumber][];
		for (int i = 0; i < setNumber; i++) {
			clusters[i] = new int[data[i].length - 2];
			for (int j = 0; j < (data[i].length - 2); j++) {
				clusters[i][j] = data[i][j + 2] - 1;

			}
		}
	}

	private void fill_cityScore() {
		int index;
		for (int i = 0; i < data.length; i++) {
			for (int j = 2; j < data[i].length; j++) {
				index = data[i][j] - 1;
				cityScore[index] = data[i][1];
			}

		}

	}

	// fill_set_profit is : each cluster has its profit ****set_profit
	private void fill_setScore() {
		setScore = new int[setNumber];
		for (int i = 0; i < setNumber; i++) {
			setScore[i] = data[i][1];
//					System.out.println(set_profit[i]);
		}
	}

	public void read_txt_file() {
		Read_data_set fileReader = new Read_data_set();
		data = fileReader.read("file.txt");
		setNumber = data.length;
	}

}
