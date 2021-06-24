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
   * ������������.
   */
   private double input[],output[]; 
   /**
    * ÿ����м����,���� Z[0][1], ��ʾ0��ĵ�1����Ԫ������������������
    */
   private double Z[][];
   /**
    * ÿ������,���� X[0][1],��ʾ��0��ĵ�1����Ԫ�������
    */
   private double X[][];
   /**
    * ÿ����֮��ĸ�����Ԫ���ӵ�Ȩֵ ,���� weight[0][1][2],��ʾ��0���1����Ԫ����1���2����Ԫ֮���������������Ȩֵ 
    */
   private double weight[][][];
   /**
    * ÿ�ε���Ȩֵ�ı���,���� deltaWeight[0][1][2],��ʾ��0���1����Ԫ����1���2����Ԫ֮����ԪȨֵ�ı���
    */
   private double deltaWeight[][][];  
   /**
    * ��Ԫ���ӵ�Ȩƫ���������� deviation[1][2],��ʾ��1���2����Ԫ��ƫ���� 
    * */ 
   private double deviation[][]; 
   /**
    * �����������.
    */
   private int layers;  
   /**
    * ÿһ�����Ԫ��.
    */
   private int numberOfLayer[];
   /**
    * ��ʵ��� Y.
    */
   private double Y[];    
   
   /**
    * ÿһ��ÿ����Ԫ�Ħ�( delta delt �¶���ֵ),���ڷ��򴫲�,delta[1][2]��ʾ��һ��ڶ����ڵ��deltaֵ��
    */
   private double delta[][];    
   /**
    * ѧϰ�� �� . 
    */
   private static double STEP = 0.2; 
   /**
    * ��Χ,e.
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
    * �Զ�������������
    * @param layers  ���������
    * @param nodeNumOfLayer  ÿ�����Ԫ��
    */
   public BPNetwork(int layers,int nodeNumOfLayer[]){
	   if(layers<2){
		   System.out.println("At least 2 layers.");  //������Ҫ����������ĸ���
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
    * �Զ�������������
    * @param layers  ���������
    * @param nodeNumOfLayer  ÿ�����Ԫ��
    */
   public boolean trainByOne(double data[],double label){
	   for(int j=0;j<data.length;j++)   //ע�������ź�input
		    input[j] = data[j];
	   Y[0] = label;
	   forward_propagating();      //���򴫲���ȡoutput
 	   System.out.println("���򴫲�ֵΪ"+ output[0]+"  ׼ȷֵΪ" + Y[0]);
	   //�жϾ��ȣ��������
	   if( Math.abs(Y[0] - output[0]) > Error ) {
		   //���̫�󣬷��򴫲�
		   backard_propagating();
		   return false;
	   }
	   return true;
   } 
   
   
   public boolean calResult(double data[],double label){
	   for(int j=0;j<data.length;j++)   //ע�������ź�input
		    input[j] = data[j]; 
	   Y[0] = label;
	   forward_propagating();      //���򴫲���ȡoutput
	   double  pridictY = output[0] *(maxResult - minResult) + minResult;
	   double  realY = Y[0] *(maxResult - minResult) + minResult;
	   System.out.println("���򴫲�ֵΪ"+ String.format( "%.2f" , pridictY  )+"  ׼ȷֵΪ" + String.format( "%.2f" , realY )+"  ������Ϊ" + String.format("%.2f", Math.abs(pridictY-realY)/realY )); 
	   return true;
   } 
   
   
   /**
    * ��ʼ��Ȩ�ؾ���
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
    * ���򴫲�����,�������output��
    */
   private void forward_propagating(){
	   for(int i=0;i<input.length;i++)
		   Z[0][i] = X[0][i] = input[i];         //�����0����Ԫ�����  
	   
	   for(int k=1;k<X.length;k++){         //����
		   for(int j=0;j<X[k].length;j++){  //�����ϲ���Ԫ
			   double U = 0;
			   for(int i=0;i<X[k-1].length;i++)  //�ϲ���Ԫ
				   U += X[k-1][i] * weight[k-1][i][j]; 
			   Z[k][j] = U + deviation[k][j];
			   X[k][j] = Tools.sigmod(Z[k][j]);                                     
		   }
	   }
	   
	   for(int i=0;i<output.length;i++)
		   output[i] = X[X.length-1][i]; 
   }
   
   /**
    * ���򴫲�,���޸�ÿ��Ȩֵ
    */
   private void backard_propagating(){ 
	   System.out.println("���򴫲�**********************************************"); 
	   
	   // ����ƽ���������  Loss(X) = (X-Y)^2  
	   int m = X.length-1;
	   
	   //��ʼ����������
	   for(int i = 0;i<delta[m].length;i++)
		   delta[m][i] =  2 * ( output[i] - Y[i]  ) * Tools.sigmod_(Z[m][i]);
	   
	   //�������ز����� 
	   for(int i = m-1;i>=0;i--) { //i��
		   for(int j = 0;j < X[i].length;j++) { //��j����Ԫ
			   double tem = 0;
			   for(int k = 0;k < X[i+1].length; k++) { //��һ��ĵ�k����Ԫ
				   tem += (weight[i][j][k] * delta[i+1][k]); 
			   }
			   delta[i][j] = tem * Tools.sigmod_(Z[i][j]);
		   }
	   }
	   
	 //����deviation����
	   for(int i = 1;i<m;i++) { //���� 		   
		   for(int j = 0;j<deviation[i].length;j++) {//����
			   deviation[i][j] = deviation[i][j] -  STEP*delta[i][j];  
		   } 
	   }
	   
	 //����weight����
	   for(int i = 0;i<m-1;i++) { //���� 		   
		   for(int j = 0;j<numberOfLayer[i];j++) {//��Ԫ����
			   for(int k = 0;k<numberOfLayer[i+1];k++) { //��һ����Ԫ�ĸ���
				   //��i���j->k 
				   weight[i][j][k] = weight[i][j][k] - STEP * delta[i+1][k] * X[i][j];
			   }
		   } 
	   } 
   } 
}
