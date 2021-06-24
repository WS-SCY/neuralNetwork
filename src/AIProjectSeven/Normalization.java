package AIProjectSeven;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Normalization {
	public static void main(String[] args) throws FileNotFoundException {
		File trainingFile = new File("D:\\data2021219\\java-learning\\java-learning\\src\\AIProjectSeven\\training.txt"); 
    	File testingFile = new File("D:\\data2021219\\java-learning\\java-learning\\src\\AIProjectSeven\\testing.txt"); 
    	Scanner sc = new Scanner(trainingFile); 
 		int TrainNum = 20,TestNum = 5;
 		Double trainInput1[],trainInput2[],trainOutput[];
 		Double testInput1[],testInput2[],testOutput[];
 		Double MaxInput1,MaxInput2,MinInput1,MinInput2,MaxOutput,MinOutput;
 		trainInput1 = new Double[TrainNum];
 		trainInput2 = new Double[TrainNum];
 		trainOutput = new Double[TrainNum];
 		testInput1 = new Double[TestNum];
 		testInput2 = new Double[TestNum];
 		testOutput = new Double[TestNum];
 		
 		
 		sc.nextLine();
 		for(int i = 0;i<TrainNum;i++) {
 			trainInput1[i] = sc.nextDouble();
 			trainInput2[i] = sc.nextDouble();
 			trainOutput[i] = sc.nextDouble();
 		}
 		MaxInput1 = Main.Max(trainInput1);
 		MaxInput2 = Main.Max(trainInput2);
 		MinInput1 = Main.Min(trainInput1);
 		MinInput2 = Main.Min(trainInput2);
 		MaxOutput = Main.Max(trainOutput);
 		MinOutput = Main.Min(trainOutput);
 		
 		sc = new Scanner(testingFile);
 		sc.nextLine();
 		for(int i = 0;i<TestNum;i++) {
 			testInput1[i] = sc.nextDouble();
 			testInput2[i] = sc.nextDouble();
 			testOutput[i] = sc.nextDouble();
 		}
 		
 		MaxInput1 = Math.max(MaxInput1,Main.Max(testInput1));
 		MaxInput2 = Math.max(MaxInput2,Main.Max(testInput2));
 		MinInput1 = Math.min(MinInput1,Main.Min(trainInput1));
 		MinInput2 = Math.min(MinInput2,Main.Min(trainInput2));
 		MaxOutput = Math.max(MaxOutput,Main.Max(trainOutput));
 		MinOutput = Math.min(MinOutput,Main.Min(trainOutput));
 		
 		System.out.println("training");
 		for(int i = 0;i<TrainNum;i++) {
 			System.out.println( (trainInput1[i] - MinInput1)/(MaxInput1 - MinInput1) + "   " 
 					+ (trainInput2[i] - MinInput2)/(MaxInput2 - MinInput2) + "   " 
 					+ (trainOutput[i] - MinOutput)/(MaxOutput - MinOutput) + "   "
 					);
 		}
 		
 			
 		System.out.println("testing");
 		for(int i = 0;i<TestNum;i++) {
 			System.out.println( (testInput1[i] - MinInput1)/(MaxInput1 - MinInput1) + "   " 
 					+ (testInput2[i] - MinInput2)/(MaxInput2 - MinInput2) + "   " 
 					+ (testOutput[i] - MinOutput)/(MaxOutput - MinOutput) + "   "
 					);
 		}
 		
	}
}
