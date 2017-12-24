package Variable;


public class Variable
{
	//***********參數值**************//
		public static int Generation;									//代數
		public static int Data_Size;									//資料集大小
		public static long Time;										//總時間
		public static long STime;										//開始時間
		public static long ETime;										//結束時間
	
	//***********陣列**************//
		public static DataPoint[] Data;										//資料集
		public static int[] Super_Generation_Path;						//優秀路徑*
		public static double Super_Distance;							//紀錄每次執行的最好距離*
		
		
		public static double Bad_Super_Distance = 0;					//最差的距離
		public static double Good_Super_Distance = Double.MAX_VALUE;	//最好的距離
		public static int Good_Super_Distance_Int = Integer.MAX_VALUE;	//最好的距離(int)
		public static int[] Bad_Super_Generation_Path;					//最差的優秀路徑
		public static int[] Good_Super_Generation_Path;					//最好的優秀路徑
		public static double Super_Generation_Distance;				//紀錄每次執行的最好距離
}
