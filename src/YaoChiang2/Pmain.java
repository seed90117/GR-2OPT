package YaoChiang2;

import java.util.ArrayList;

import YaoChiang2.DataPointArray;

public class Pmain {
	
	//-----------Parameter of input------------//
	public static int Section = 10;     //區塊資料長度
	public static int modifyPoint = 0;  //修改區塊起始點
	
	public static ArrayList<DataPointArray> DataSet = new ArrayList<DataPointArray>();	      //數入資料集
	public static ArrayList<DataPointArray> SectionDataSet = new ArrayList<DataPointArray>(); //區段資料集
	public static ArrayList<DataPointArray> ModifySection = new ArrayList<DataPointArray>();  //變動區段
	public static ArrayList<DataPointArray> Modify = new ArrayList<DataPointArray>();         //變動因子
	public static ArrayList<DataPointArray> result = new ArrayList<DataPointArray>();
	 
	//----------Parameter of internal----------//
	public static double TotalDistance; //loop距離
	public static double BestDistance=Double.MAX_VALUE;
}
