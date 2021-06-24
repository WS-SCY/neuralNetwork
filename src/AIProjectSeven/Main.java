package AIProjectSeven;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner; 

public class Main {
	 public static void testInput(Double [] name) {
		 for(Double i:name) {
			 System.out.println(i);
		 }
	 }
	 public static Double Max(Double[] a) {
		 Double ans = Double.MIN_VALUE;
		 for(Double i :a)
			 if(i>ans) ans = i;
		 return ans;
	 }
	 
	 public static Double Min(Double[] a) {
		 Double ans = Double.MAX_VALUE;
		 for(Double i :a )
			 if(i<ans) ans = i;
		 return ans;
	 }
	 
     public static void main(String args[]) throws IOException{ 
 		// 导入数据
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
 		MaxInput1 = Max(trainInput1);
 		MaxInput2 = Max(trainInput2);
 		MinInput1 = Min(trainInput1);
 		MinInput2 = Min(trainInput2);
 		MaxOutput = Max(trainOutput);
 		MinOutput = Min(trainOutput);
 		
 		sc = new Scanner(testingFile);
 		sc.nextLine();
 		for(int i = 0;i<TestNum;i++) {
 			testInput1[i] = sc.nextDouble();
 			testInput2[i] = sc.nextDouble();
 			testOutput[i] = sc.nextDouble();
 		}
 		
 		/***  初始化 3层神经网络，输入层2，隐藏层6，输出层1  **/
 		String weightPos= "D:\\data2021219\\java-learning\\java-learning\\src\\AIProjectSeven\\weight.txt";
 		BPNetwork bpnn = new BPNetwork(5,new int[]{2,6,10,4,1});
 		
 		
 		bpnn.setMaxResult(98.8);
 		bpnn.setMinResult(50.2);

 		
        /***  训练  **/
 		int round = 0;
 		while(round++ > -1) {
 			System.out.println("Round : "+round);
// 			任取一个数据进行前向传播，如果不符合要求，反向传播。 
 			//随机取一个进行训练。
 			int chosen = (int) Math.round(Math.random()*TrainNum)%TrainNum; 
 			
 			Double x1 = trainInput1[chosen], x2 = trainInput2[chosen],y = trainOutput[chosen];
 			
 			bpnn.trainByOne(new double[]{x1,x2} , y);

 			if(round == 50000) break;
 		} 
 		
 		System.out.println("测试数据");
 		for(int i = 0;i<TestNum;i++) {
 			Double x1 = testInput1[i], x2 = testInput2[i],y = testOutput[i];
 			bpnn.calResult(new double[]{x1,x2} , y);
 		}
 		
 		 
     }
}
