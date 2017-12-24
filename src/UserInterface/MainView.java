package UserInterface;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import IO.DrawPanel;
import IO.LoadFile;
import IO.SaveFile;
import Values.BestSolution;
import Values.DataSet;
import Values.Parameter;
import Variable.Variable;
import YaoChiang2.TSPmain;

public class MainView extends JFrame {
	
	/**
	 * M10456012
	 * Kevin Yen
	 * kelly10056040@gmail.com
	 */
	private static final long serialVersionUID = 1L;
	private boolean isLoad = false;
	private String dataSetName = "";
	private BestSolution bestSolution = null;
	private DrawPanel drawPanel = new DrawPanel();
	private DataSet dataSet = DataSet.getInstance();
	
	//*****宣告介面*****//
	Container cp = this.getContentPane();
	
	//*****宣告元件*****//
	JLabel generationLabel = new JLabel("Generation:");
	
	JLabel dataSetLabel = new JLabel("DateSet: ");
	JLabel runTimeLabel = new JLabel("Running Time: ");
	JLabel distanceLabel = new JLabel("Best Distance: ");
	JLabel fitnessLabel = new JLabel("Best Fitness: ");
	JLabel avgRunTimeLabel = new JLabel("Avg Time: ");
	JLabel avgDistanceLabel = new JLabel("Avg Distance: ");
	
	JTextField generationTextField = new JTextField("100");
	JTextField computerRunTextField = new JTextField("30");
	
	JButton loadFileButton = new JButton("Load File");
	JButton startButton = new JButton("Starts");
	JButton drawButton = new JButton("Draw");
	
	JCheckBox isMacCheckBox = new JCheckBox("Using Mac");
	JCheckBox isInteger = new JCheckBox("Output Integer");
	JCheckBox isComputerRunCheckBox = new JCheckBox("Computer Run");
	
	JTextArea output = new JTextArea();
	
	JPanel show = new JPanel();
	
	JFileChooser open = new JFileChooser();
	
	JScrollPane sp = new JScrollPane(output,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	MainView()
	{
		//*****設定介面*****//
		this.setSize(1000, 800);
		this.setLayout(null);
		this.setTitle("GR-2OPT");
		
		//*****設定元件位置*****//
		generationLabel.setBounds(700, 30, 80, 30);
		generationTextField.setBounds(780, 30, 90, 30);
		
		isMacCheckBox.setBounds(700, 220, 120, 30);
		isInteger.setBounds(820, 220, 150, 30);
		loadFileButton.setBounds(700, 250, 200, 30);
		startButton.setBounds(700, 290, 200, 30);
		drawButton.setBounds(700, 330, 200, 30);
		isComputerRunCheckBox.setBounds(700, 370, 150, 30);
		computerRunTextField.setBounds(850, 370, 40, 30);
		dataSetLabel.setBounds(700, 400, 250, 30);
		runTimeLabel.setBounds(700, 420, 250, 30);
		distanceLabel.setBounds(700, 440, 250, 30);
		fitnessLabel.setBounds(700, 460, 250, 30);
		avgRunTimeLabel.setBounds(700, 480, 250, 30);
		avgDistanceLabel.setBounds(700, 500, 250, 30);
		
		show.setBounds(20, 20, 650, 500);
		output.setBounds(20, 530, 950, 200);
		sp.setBounds(20, 530, 950, 200);
		
		//*****設定JPanel邊框*****//
		show.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		output.setEditable(false);//JTextArea不可編輯
		output.setLineWrap(true);//JTextArea自動換行
		
		cp.add(generationLabel);
		cp.add(generationTextField);
		cp.add(startButton);
		cp.add(loadFileButton);
		cp.add(show);
		cp.add(sp);
		cp.add(runTimeLabel);
		cp.add(distanceLabel);
		cp.add(fitnessLabel);
		cp.add(isMacCheckBox);
		cp.add(isComputerRunCheckBox);
		cp.add(computerRunTextField);
		cp.add(avgRunTimeLabel);
		cp.add(avgDistanceLabel);
		cp.add(isInteger);
		cp.add(drawButton);
		cp.add(dataSetLabel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		loadFileButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LoadFile loadFile = new LoadFile();
				String tmpStr = loadFile.loadfile(open, show, isMacCheckBox.isSelected());
				if (!tmpStr.equals("")) {
					dataSetName = tmpStr;
				}
				dataSetLabel.setText("DateSet: " + dataSetName);
				if (!dataSetLabel.getText().equals("DateSet: "))
					isLoad = true;
			}});
		
		startButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				LoadFile loadFile = new LoadFile();
				SaveFile saveFile = new SaveFile();
				if (isLoad) {
					
					if (dataSet.getIsAuto()) {
						for (int i=0; i<dataSet.getCount(); i++) {
							loadFile.autoLoadFile(show, isMacCheckBox.isSelected(), i);
							computerRunAlgorithm(isInteger.isSelected(),i);
							saveFile.saveFile(dataSet.getDataSet(i), bestSolution, isMacCheckBox.isSelected());
						}
					} else {
						initialOutPut();
						if (isComputerRunCheckBox.isSelected()) {
							computerRunAlgorithm(isInteger.isSelected(), 0);
						}
						else
							runAlgorithm(isInteger.isSelected());
					}
				}
			}});
		drawButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (bestSolution != null)
					showResult(bestSolution, output);
			}});
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainView();
	}

	private void runAlgorithm(boolean isInteger) {
		// 記錄開始時間
		long startTime = System.currentTimeMillis();
		// 設定參數
		setParameter(0);
		// 執行GR-2OPT
		TSPmain.run();
		this.bestSolution = getBestSolution();
		// 記錄結束時間
		long endTime = System.currentTimeMillis();
		runTimeLabel.setText("Running Time: " + getRunTime(startTime, endTime) + " s");
		showResult(this.bestSolution, output);
	}
	
	private void computerRunAlgorithm(boolean isInteger, int count) {
		int runTime = Integer.parseInt(computerRunTextField.getText());
		long startTime = 0;
		long endTime = 0;
		double totalDistance = 0;
		double totalTime = 0;
		ArrayList<Integer> bestPath = null;
		double bestDistance = -1;
		double bestTime = 0;
		for (int i=0; i<runTime; i++) {
			// 記錄開始時間
			startTime = System.currentTimeMillis();
			// 設定參數
			setParameter(count);
			// 執行GR-2OPT
			TSPmain.run();
			this.bestSolution = getBestSolution();
			// 記錄結束時間
			endTime = System.currentTimeMillis();
			// 記錄最佳
			if (i==0) {
				bestPath = this.bestSolution.solution;
				bestDistance = this.bestSolution.distance;
				bestTime = getRunTime(startTime, endTime);
			} else {
				if (bestDistance > this.bestSolution.distance) {
					bestPath = this.bestSolution.solution;
					bestDistance = this.bestSolution.distance;
					bestTime = getRunTime(startTime, endTime);
				}
			}
			totalDistance += bestSolution.distance;
			totalTime += getRunTime(startTime, endTime);
		}
		this.bestSolution = new BestSolution();
		this.bestSolution.solution = bestPath;
		this.bestSolution.distance = bestDistance;
		this.bestSolution.fitness = 1/bestDistance;
		this.bestSolution.time = bestTime;
		this.bestSolution.avgDistance = totalDistance/runTime;
		this.bestSolution.avgTime = totalTime/runTime;
		showResult(this.bestSolution, this.output);
		this.runTimeLabel.setText("Running Time: " + bestTime + " s");
		this.avgRunTimeLabel.setText("Avg Time: " + totalTime/runTime + " s");
		this.avgDistanceLabel.setText("Avg Distance: " + totalDistance/runTime);
	}
	
	private void setParameter(int count) {
		Parameter parameter = Parameter.getInstance();
		if (dataSet.getIsAuto()) {
			int generation = 500;
			parameter.setGeneration(generation);
			Variable.Generation = generation;
		} else {
			parameter.setGeneration(Integer.parseInt(generationTextField.getText()));
			Variable.Generation = parameter.getGeneration();
		}
	}
	
	private void initialOutPut() {
		runTimeLabel.setText("Run Time: ");
		distanceLabel.setText("Best Distance: ");
		fitnessLabel.setText("Best Fitness: ");
		avgRunTimeLabel.setText("Avg Time: ");
		avgDistanceLabel.setText("Avg Distance: ");
	}

	private void showResult(BestSolution bestChromosome, JTextArea output) {
		// 清空畫布
		this.drawPanel.drawpanel(show);
		int size = bestChromosome.solution.size()-1;
		String out = this.dataSetLabel.getText() + "\nBest Path: \n";
		for (int i=0; i<size; i++) {
			if (i==size-1) {
				this.drawPanel.drawline(bestChromosome.solution.get(i), bestChromosome.solution.get(i+1), show);
				out += bestChromosome.solution.get(i) + " - " + bestChromosome.solution.get(i+1) + "\n";
			} else {
				this.drawPanel.drawline(bestChromosome.solution.get(i), bestChromosome.solution.get(i+1), show);
				out += bestChromosome.solution.get(i) + " - ";
			}
		}
		out += "Best Distance: " + bestChromosome.distance;
		this.distanceLabel.setText("Best Distance: " + bestChromosome.distance);
		this.fitnessLabel.setText("Best Fitness: " + bestChromosome.fitness);
		output.setText(out);
	}
	
	private double getRunTime(long start, long end) {
		double startTime = start;
		double endTime = end;
		return (endTime - startTime)/1000;
	}
	
	private BestSolution getBestSolution() {
		BestSolution bestSolution = new BestSolution();
		bestSolution.distance = Variable.Super_Distance;
		bestSolution.fitness = 1/bestSolution.distance;
		bestSolution.solution = new ArrayList<>();
		for (int i : Variable.Super_Generation_Path) {
			bestSolution.solution.add(i);
		}
		return bestSolution;
	}
}
