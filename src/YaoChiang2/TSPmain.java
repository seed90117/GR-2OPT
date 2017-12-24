package YaoChiang2;

import java.util.ArrayList;
import java.util.Collections;

import Variable.Variable;
import YaoChiang2.DataPointArray;
import YaoChiang2.Pmain;

public class TSPmain {
	
	public static void run(){
		
		Pmain.Section = 10;     //區塊資料長度
		Pmain.modifyPoint = 0;  //修改區塊起始點
		
		Pmain.SectionDataSet = new ArrayList<DataPointArray>(); //區段資料集
		Pmain.ModifySection = new ArrayList<DataPointArray>();  //變動區段
		Pmain.Modify = new ArrayList<DataPointArray>();         //變動因子
		 
		//----------Parameter of internal----------//
		Pmain.TotalDistance = 0; //loop距離
		Pmain.BestDistance=Double.MAX_VALUE;
		
		Variable.Super_Generation_Path = new int[Variable.Data_Size + 1];
		
		Variable.STime = System.currentTimeMillis();
		
		
		GreedyMethod1(Pmain.DataSet);
			
		TwoOpt(Pmain.DataSet);
		
		FinalDistance();
		
		CreatSectionDataSet();
		
		//TwoOpt(Pmain.DataSet);
		
		for(int i=0;i<Variable.Generation;i++){
			
			PickUp2();
			PutDown();
		}
		
		//TwoOpt(Pmain.DataSet);
		Variable.ETime = System.currentTimeMillis();
		
		//---------showResult---------//
		
		FinalDistance();
		countTime(Variable.STime, Variable.ETime);
	}
	
	
	
	
	//-----------改良被選中區塊----------//  test OK
	public static void PutDown(){
		
		double OldDistatnce = AreaDistance(Pmain.Modify),
			   NewDistance;
		
		boolean unfinish = true;
		
		while(unfinish /*&& count < Variable.Data_Size*/){
			
			ArrayList<DataPointArray> Modify1 = new ArrayList<DataPointArray>();
			
			NewDistance = 0;
			
			for(DataPointArray point : Pmain.Modify)
			{
				if((point != Pmain.Modify.get(0)) && (point != Pmain.Modify.get(Pmain.Modify.size() - 1)))
				{
					Modify1.add(point);
				}
			}
			
			Collections.shuffle(Modify1);
			
			for(int i = 0; i < Modify1.size(); i++)
			{
				Pmain.Modify.remove(i + 1);
				Pmain.Modify.add(i + 1, new DataPointArray(Modify1.get(i).x, Modify1.get(i).y));
			}
			
			GreedyMethod1(Pmain.Modify);
			
			TwoOpt(Pmain.Modify);
			
			NewDistance = AreaDistance(Pmain.Modify);
			
			if(NewDistance<=OldDistatnce){
				unfinish = false;
			}
		}
		
		if(!unfinish){
		    for(int i=0;i<Pmain.Modify.size();i++)
		    {
			    DataPointCopy(Pmain.Modify, i, Pmain.DataSet, Pmain.modifyPoint + i);
		    }
		    FinalDistance();
		}		
	}

	public static double AreaDistance(ArrayList<DataPointArray> basic){
		
		double result=0;
		
		for(int i=0;i<basic.size()-1;i++)
			result += CountDistancelong(i, i+1,basic);
		
		return result;
		
	}
	
	public static void CreatSectionDataSet(){
		
		Pmain.Modify.clear();
		Pmain.SectionDataSet.clear();
		//Pmain.ModifySection.clear();
	
		for(int i=0;i<Pmain.Section;i++){
			
			//if(i >= 1 && i < Pmain.Section - 1)
				Pmain.Modify.add(new DataPointArray(0, 0));
			
			//Pmain.ModifySection.add(new DataPointArray(0, 0));//------here---------//檢查pickup and putdwon 's SectionDataSet
			Pmain.SectionDataSet.add(new DataPointArray(0, 0));
		}
	}
	
	//Test
	public static void PickUp2()
	{		
		int r = (int)(Math.random() * (Pmain.DataSet.size() - Pmain.Section - 1));
		
		Pmain.modifyPoint = r;
		
		for(int i = 0; i < Pmain.Section;i++)
		{
			DataPointCopy(Pmain.DataSet, r + i, Pmain.SectionDataSet, i);
		}
		
		ArrayCopy(Pmain.SectionDataSet, Pmain.Modify);
	}
	
	//----------選出路徑最長的區段-----------//  test-->OK
	public static void PickUp(){
		
		double areaTotalDistance = 0,
			   BadDistance = Double.MIN_VALUE;
		
		for(int i=0;i<Pmain.DataSet.size()/Pmain.Section;i++){
			
			areaTotalDistance=0;
		
			for(int j=1;j<Pmain.Section;j++){
				
				if(i*Pmain.Section+j<Pmain.DataSet.size()-1){
					DataPointCopy(Pmain.DataSet, i*Pmain.Section+j, Pmain.SectionDataSet, j);
					areaTotalDistance += CountDistancelong(i*Pmain.Section+j, i*Pmain.Section+j+1,Pmain.DataSet);
				}
			}
			if(areaTotalDistance>BadDistance){
				
				Pmain.modifyPoint = i*Pmain.Section;
				BadDistance = areaTotalDistance;
				ArrayCopy(Pmain.SectionDataSet, Pmain.Modify);
			}
		}
	}
	
	//複製資料點
	public static void DataPointCopy(ArrayList<DataPointArray> basic ,int Bpoint, ArrayList<DataPointArray> change,int Cpoint){
		
		change.get(Cpoint).x = basic.get(Bpoint).x;
		change.get(Cpoint).y = basic.get(Bpoint).y;
		
	}
	
	//複製陣列
	public static void ArrayCopy(ArrayList<DataPointArray> basic , ArrayList<DataPointArray> sub){
		
		for(int i=0;i<basic.size();i++){
				DataPointCopy(basic,i,sub,i);
		}
	}
	
	//耗時計算
	public static void countTime(long start , long end){
		Variable.Time += end - start;
	}
	
	//總距離
	public static void FinalDistance(){
		
		Pmain.TotalDistance = 0;
		
		for(int i=0;i<Pmain.DataSet.size()-1;i++){
			
			Pmain.TotalDistance += CountDistancelong(i, i+1, Pmain.DataSet);
		}
		if(Pmain.TotalDistance<Pmain.BestDistance)
		{
			Pmain.BestDistance = Pmain.TotalDistance;
			Variable.Super_Distance = Pmain.TotalDistance;
			Pmain.result = Pmain.DataSet;
			
			//顯示
				for(int i = 0; i < Pmain.result.size(); i++)
				{
					for(int j = 0; j < Variable.Data_Size; j++)
					{
						if((Variable.Data[j].x == Pmain.result.get(i).x) && (Variable.Data[j].y == Pmain.result.get(i).y))
						{
							Variable.Super_Generation_Path[i] = j;
							break;
						}
					}
				}
			
		}
			
		//System.out.println("TotalDistance:"+Pmain.TotalDistance);
	}
	
	//2-OPT
	public static void TwoOpt(ArrayList<DataPointArray> basic){
		
		double DistanceAB,
			   DistanceCD,
			   DistanceAC,
			   DistanceBD;
		
		int stop=0;
		int count = 0;
		int Size = basic.size() * 2;
		
		while((stop==0) && (count < Size)){
			stop=1;
			count++;
			for(int i=0;i<basic.size()-3;i++){
				
				for(int j=i+2;j<basic.size()-1;j++){
				
						DistanceAB = CountDistancelong(i,i+1,basic);
						DistanceCD = CountDistancelong(j, j+1,basic);
						DistanceAC = CountDistancelong(i, j,basic);
						DistanceBD = CountDistancelong(i+1, j+1,basic);
					
						if((DistanceAB+DistanceCD)>(DistanceAC+DistanceBD)){
							
							if(stop!=0)
								stop=0;
							
							PointExchange(j, i+1,basic);
						}
				}
			}
		}
	}
	
	//檢視所有資料點 0為初始
	public static void ShowAllPonts(int first){
		
		int count=0;
		
		if(first!=0)
			System.out.println("------");
		
		for(int i=0;i<Pmain.DataSet.size();i++){
			System.out.println(count++ +" "+Pmain.DataSet.get(i).x+","+Pmain.DataSet.get(i).y);
		}
	}
	
	
	//兩點交換
	public static void PointExchange(int point1,int point2,ArrayList<DataPointArray> basic){
		
		double tempX,tempY;
		
		tempX = basic.get(point1).x;
		tempY = basic.get(point1).y;
		basic.get(point1).x = basic.get(point2).x;
		basic.get(point1).y = basic.get(point2).y;
		basic.get(point2).x = tempX;
		basic.get(point2).y = tempY;
	}
	
	//計算兩點距離
	public static double CountDistance(int point1,int point2,ArrayList<DataPointArray> basic){
		
		double x1,y1,x2,y2,result;
		
		x1 = basic.get(point1).x;
		y1 = basic.get(point1).y;
		x2 = basic.get(point2).x;
	    y2 = basic.get(point2).y;
	    
	    result = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	    
		return result;
	}
	
	public static long CountDistancelong(int point1,int point2,ArrayList<DataPointArray> basic){
		
		double x1,y1,x2,y2;
		long result;
		
		x1 = basic.get(point1).x;
		y1 = basic.get(point1).y;
		x2 = basic.get(point2).x;
	    y2 = basic.get(point2).y;
	    
	    result = (long)Math.round(java.awt.Point.distance(x1, y1, x2, y2));
//	    result = (long)(Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)));
	    
		return result;
	}
	
	//找出最近鄰居為下一個拜訪城市
	public static void GreedyMethod(ArrayList<DataPointArray> basic){
		
		double DistanceAB,BestDistance;
		
		int exchange=0;
		
		for(int i=0;i<basic.size()-2;i++){
			
			BestDistance = Double.MAX_VALUE;
			
			/*if(Math.random() < 0.9)
			{*/
				for(int j=i+1;j<basic.size()-1;j++){
					
					DistanceAB = CountDistancelong(i,j,basic);
						
					if(DistanceAB<BestDistance){
							
						BestDistance = DistanceAB;
						exchange = j;
				    }
				}
			/*}
			else
			{
				exchange = i + 2;
			}*/
			
			PointExchange(i+1, exchange, basic);
		}	
	}
	
	//找出最近鄰居為下一個拜訪城市
	public static void GreedyMethod1(ArrayList<DataPointArray> basic){
		
		double DistanceAB,BestDistance;
		
		int exchange=0;
		
		for(int i=0;i<basic.size()-2;i++){
			
			BestDistance = Double.MAX_VALUE;
			
			if(Math.random() < 0.9)
			{
				for(int j=i+1;j<basic.size()-1;j++){
					
					DistanceAB = CountDistancelong(i,j,basic);
						
					if(DistanceAB<BestDistance){
							
						BestDistance = DistanceAB;
						exchange = j;
				    }
				}
			}
			else
			{
				exchange = i + 2;
			}
			
			PointExchange(i+1, exchange, basic);
		}	
	}
}
