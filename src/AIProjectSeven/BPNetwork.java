package AIProjectSeven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;
 

public class BPNetwork {
   /**
   * 网络的输入输出.
   */
   private double input[],output[]; 
   /**
    * 每层的中间输出,例如 Z[0][1], 表示0层的第1个神经元不经过激活函数的输出。
    */
   private double Z[][];
   /**
    * 每层的输出,例如 X[0][1],表示第0层的第1个神经元的输出。
    */
   private double X[][];
   /**
    * 每两层之间的各个神经元连接的权值 ,例如 weight[0][1][2],表示第0层第1个神经元到第1层第2个神经元之间连接输入输出的权值 
    */
   private double weight[][][];
   /**
    * 每次调整权值改变量,例如 deltaWeight[0][1][2],表示第0层第1个神经元到第1层第2个神经元之间神经元权值改变量
    */
   private double deltaWeight[][][];  
   /**
    * 神经元连接的权偏移量，例如 deviation[1][2],表示第1层第2个神经元的偏移量 
    * */ 
   private double deviation[][]; 
   /**
    * 该神经网络层数.
    */
   private int layers;  
   /**
    * 每一层的神经元数.
    */
   private int numberOfLayer[];
   /**
    * 真实结果 Y.
    */
   private double Y[];    
   
   /**
    * 每一层每个神经元的δ( delta delt 德尔塔值),用于反向传播,delta[1][2]表示第一层第二个节点的delta值。
    */
   private double delta[][];    
   /**
    * 学习率 η . 
    */
   private static double STEP = 0.2; 
   /**
    * 误差范围,e.
    */
   private static final double E = 0.005;    
   private static final double Error = 0.05; 
   private static double minResult;
   private static double maxResult;
    
	public static void setMinResult(double minResult) {
		BPNetwork.minResult = minResult;
	}
	 
	public static void setMaxResult(double maxResult) {
		BPNetwork.maxResult = maxResult;
	}


public void setSTEP(double i) {
	   this.STEP /= i;
   }
   
   
   /**
    * 自定义生成神经网络
    * @param layers  神经网络层数
    * @param nodeNumOfLayer  每层的神经元数
    */
   public BPNetwork(int layers,int nodeNumOfLayer[]){
	   if(layers<2){
		   System.out.println("At least 2 layers.");  //最起码要有输入输出的个数
		   return;
	   }
	   this.layers = layers;
	   this.numberOfLayer = nodeNumOfLayer; 
	   
       this.weight = new double[this.layers-1][1][1];
       this.deviation = new double[this.layers][1];
	   this.deltaWeight = new double[this.layers-1][1][1];
	   this.delta = new double[this.layers][1];
	   this.X = new double[this.layers][1];
	   this.Z = new double[this.layers][1];
	   
	   for(int i=0;i<nodeNumOfLayer.length-1;i++){
		   int left = this.numberOfLayer[i];
		   int right= this.numberOfLayer[i+1];
		   this.weight[i] = new double[left][right]; 
		   this.deltaWeight[i] = new double[left][right];
	   }
	   for(int i=0;i<nodeNumOfLayer.length;i++){
	       this.delta[i] = new double[nodeNumOfLayer[i]];
	       this.deviation[i] = new double[nodeNumOfLayer[i]];
	   }
	   this.initWeightAndDeviation();                         //init weight deviation matrix
	   
       for(int i=0;i<this.numberOfLayer.length;i++) {
    	   this.X[i] = new double[this.numberOfLayer[i]];  
    	   this.Z[i] = new double[this.numberOfLayer[i]];  
       }
    	  
       this.input = new double[this.numberOfLayer[0]];
       this.Y = new double[this.numberOfLayer[this.numberOfLayer.length-1]];
       this.output = new double[this.numberOfLayer[this.numberOfLayer.length-1]];
   } 
   
   /**
    * 自定义生成神经网络
    * @param layers  神经网络层数
    * @param nodeNumOfLayer  每层的神经元数
    */
   public boolean trainByOne(double data[],double label){
	   for(int j=0;j<data.length;j++)   //注入输入信号input
		    input[j] = data[j];
	   Y[0] = label;
	   forward_propagating();      //正向传播获取output
 	   System.out.println("正向传播值为"+ output[0]+"  准确值为" + Y[0]);
	   //判断精度，如果大于
	   if( Math.abs(Y[0] - output[0]) > Error ) {
		   //误差太大，反向传播
		   backard_propagating();
		   return false;
	   }
	   return true;
   } 
   
   
   public boolean calResult(double data[],double label){
	   for(int j=0;j<data.length;j++)   //注入输入信号input
		    input[j] = data[j]; 
	   Y[0] = label;
	   forward_propagating();      //正向传播获取output
	   double  pridictY = output[0] *(maxResult - minResult) + minResult;
	   double  realY = Y[0] *(maxResult - minResult) + minResult;
	   System.out.println("正向传播值为"+ String.format( "%.2f" , pridictY  )+"  准确值为" + String.format( "%.2f" , realY )+"  相对误差为" + String.format("%.2f", Math.abs(pridictY-realY)/realY )); 
	   return true;
   } 
   
   
   /**
    * 初始化权重矩阵
    */
   private void initWeightAndDeviation(){
	   Random random = new Random();
	   for(int i=0;i<this.weight.length;i++)
		   for(int j=0;j<this.weight[i].length;j++) {
			   this.deviation[i][j] = (random.nextDouble()-0.5)*6; 
			   for(int k=0;k<this.weight[i][j].length;k++){ 
				   this.weight[i][j][k] = (random.nextDouble()-0.5)*6;  
			   }
		   }
			   
   } 
   
   /**
    * 正向传播过程,结果存在output中
    */
   private void forward_propagating(){
	   for(int i=0;i<input.length;i++)
		   Z[0][i] = X[0][i] = input[i];         //计算第0层神经元的输出  
	   
	   for(int k=1;k<X.length;k++){         //层数
		   for(int j=0;j<X[k].length;j++){  //本层上层神经元
			   double U = 0;
			   for(int i=0;i<X[k-1].length;i++)  //上层神经元
				   U += X[k-1][i] * weight[k-1][i][j]; 
			   Z[k][j] = U + deviation[k][j];
			   X[k][j] = Tools.sigmod(Z[k][j]);                                     
		   }
	   }
	   
	   for(int i=0;i<output.length;i++)
		   output[i] = X[X.length-1][i]; 
   }
   
   /**
    * 反向传播,并修改每层权值
    */
   private void backard_propagating(){ 
	   System.out.println("反向传播**********************************************"); 
	   
	   // 采用平方差算误差  Loss(X) = (X-Y)^2  
	   int m = X.length-1;
	   
	   //初始化输出层误差
	   for(int i = 0;i<delta[m].length;i++)
		   delta[m][i] =  2 * ( output[i] - Y[i]  ) * Tools.sigmod_(Z[m][i]);
	   
	   //计算隐藏层的误差 
	   for(int i = m-1;i>=0;i--) { //i层
		   for(int j = 0;j < X[i].length;j++) { //第j个神经元
			   double tem = 0;
			   for(int k = 0;k < X[i+1].length; k++) { //下一层的第k个神经元
				   tem += (weight[i][j][k] * delta[i+1][k]); 
			   }
			   delta[i][j] = tem * Tools.sigmod_(Z[i][j]);
		   }
	   }
	   
	 //参数deviation更新
	   for(int i = 1;i<m;i++) { //层数 		   
		   for(int j = 0;j<deviation[i].length;j++) {//个数
			   deviation[i][j] = deviation[i][j] -  STEP*delta[i][j];  
		   } 
	   }
	   
	 //参数weight更新
	   for(int i = 0;i<m-1;i++) { //层数 		   
		   for(int j = 0;j<numberOfLayer[i];j++) {//神经元个数
			   for(int k = 0;k<numberOfLayer[i+1];k++) { //下一层神经元的个数
				   //第i层的j->k 
				   weight[i][j][k] = weight[i][j][k] - STEP * delta[i+1][k] * X[i][j];
			   }
		   } 
	   } 
   } 
}
