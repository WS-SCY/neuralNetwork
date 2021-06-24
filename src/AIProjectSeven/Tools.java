package AIProjectSeven;

public class Tools {
   public static double sigmod(double target){
	   return 1/(1+Math.exp((-1)*(target)));
   }

   //µ¼º¯Êý
   public static double sigmod_(double target) {
	   return sigmod(target) * ( 1 - sigmod(target) );
   }
}
