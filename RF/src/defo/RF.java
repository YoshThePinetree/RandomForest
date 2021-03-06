package defo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class RF {
///////////////////////////////////////////////////////////////////////////////////
	public void RandomForest(){	// RF algorithm method
		File file1 = new File("C:\\JavaIO\\Input\\Iris.txt");	// The file name
		File file2 = new File("C:\\JavaIO\\Input\\Iris_ans.txt");	// The file name
		int d = 4;	// the number of dimension of the data
//		int data1[][] = DataRead(file,d);	// Load the data
		double data1[][] = DataReadDouble(file1,d);	// Load the data
		int ans1[][] = DataRead(file2,1);	// Load the data
		System.out.println();

		///////////////////////////////
		// Paramter Set & Initiation //
		///////////////////////////////
		int n=data1.length;							// the number of elements
		int nTrain=100;							// the number of training data
		int nTest=n-nTrain;							// the number of testing data
		int rseed=1;								// random seed
		int b=5;									// the number of Decision Tree
		int m=2;									// the number of selected variables s.t. m<=d
		int nmin=4;									// the depth of Binary Tree
		int itemax=10000;								// the number of training for a tree

		// data normalization
		double data[][] = new double[n][d];
		int ans[] = new int[n];
//		data=NormStd.Normalization(data1);
//		data=NormStd.Standardization(data1);
		data=data1;
		for(int i=0; i<n; i++) {
			ans[i]=ans1[i][0];
		}

		// create instance
		Sfmt rnd = new Sfmt(rseed);
		Mat mat = new Mat();

		RFop rf = new RFop();
		RFop smp = rf.DivData(data, ans, nTrain, nTest, rseed);				// divide the data into training and testing
		RFop bsmp = rf.BootStrapSmp(smp.TrainData,smp.TrainAns,b,rseed);	// create dataset by bootstrap sampling from train data

		int AtrSet [][] = new int [b][m]; // the set of selected variables
		AtrSet = mat.SmplVecUniq(d, b, m, rseed);

		for(int i=0; i<b; i++) {
			for(int j=0; j<m; j++) {
				if(j==m-1) {
					System.out.printf("%d\n",AtrSet[i][j]);
				}else {
					System.out.printf("%d ",AtrSet[i][j]);
				}

			}
		}

		BDT bdt = new BDT();
//		bdt.ShowTree(tree, bsmp.Data[0], bsmp.Ans[0], d, nmin);
		System.out.println();


		//******RF Training******************************************************************
		List<BDT> treelist = new ArrayList<BDT>();	// the list for tree
		for(int i=0; i<b; i++) {
			BDT tree = bdt.GrowTree(bsmp.Data[i], bsmp.Ans[i], AtrSet[i], d, nmin, rseed, itemax);
			treelist.add(tree);

			System.out.println();

			for(int j=0; j<tree.G.size(); j++) {
				System.out.println(tree.G.get(j));
			}
			bdt.ShowTree(tree, bsmp.Data[i], bsmp.Ans[i], d, nmin);
		}

		//***********************************************************************************

		//******RF Testing*******************************************************************
		int treeOut [] = new int [b];
		int testAns [] = new int [nTest];
		int match [] = new int [nTest];
		Arrays.fill(match,0);

		System.out.println("Ans.Pred	Ans.Act");
		for(int i=0; i<nTest; i++) {
			for(int j=0; j<b; j++) {
				treeOut [j] = bdt.Evaluation(treelist.get(j), AtrSet[j], smp.TestData[i], bsmp.Ans[j], m, nmin);
			}
			testAns[i] = mat.MajorityVote(treeOut);

			if(testAns[i]==smp.TestAns[i]) {
				match[i]=1;
			}

			System.out.printf("%d\t", testAns[i]);
			System.out.printf("%d\n", smp.TestAns[i]);
		}

		String[] name = {"setosa","versicolor","virginica"};
		ConfusionMatrix(testAns, smp.TestAns, name);

		//***********************************************************************************

		//for(int i=0; i<smp.TestData.length; i++) {
		//	for(int j=0; j<smp.TestData[0].length; j++) {
		//		System.out.printf("%.1f ", smp.TestData[i][j]);
		//		if(j==smp.TestData[0].length-1) {
		//			System.out.printf("%d\n", smp.TestAns[i]);
		//		}
		//	}
		//}

	}


///////////////////////////////////////////////////////////////////////////////////

		/////////////
//******// Methods //**************************************************************
		/////////////

	//public int[][] DataRead(int n){
		public static int[][] DataRead(final File file, int d) {
	        List<ArrayList<Integer>> lists = new ArrayList<ArrayList<Integer>>();
	        for (int i = 0; i < d; i++) {
	            lists.add(new ArrayList<Integer>());
	        }
	        BufferedReader br = null;
	        try {
	            // Read the file, save data to List<Integer>
	            br = new BufferedReader(new FileReader(file));
	            String line = null;
	            while ((line = br.readLine()) != null) {
	                // Add nth integer to lists[n]
	                List<Integer> ints = parse_line(line,d);
	                for (int i = 0; i < d; i++) {
	                    (lists.get(i)).add(ints.get(i));
	                }
	            }

	            // convert lists to 2 Integer[]
	            Integer[] array1 = lists.get(0).toArray(new Integer[lists.size()]);
	            int n=array1.length;
	            int data[][] = new int[n][d];

	            int j=0;
	            while(j<d) {
	            	// convert lists to 2 Integer[]
	                Integer[] array = lists.get(j).toArray(new Integer[lists.size()]);
		            for(int i=0; i<n; i++) {
		                	data[i][j]=array[i];
		            }

		            j++;
	            }

	            return data;

	        } catch (Exception ex) {
	            System.out.println(ex);
	        } finally {
	            try {
	                if (br != null) {
	                    br.close();
	                }
	            } catch (Exception ex) {
	                // ignore error
	            }
	        }
	        return null;
	    }

	    // parse 2 integers as a line of String
	    private static List<Integer> parse_line(String line, int d) throws Exception {
	        List<Integer> ans = new ArrayList<Integer>();
	        StringTokenizer st = new StringTokenizer(line, " ");
	        if (st.countTokens() != d) {
	            throw new Exception("Bad line: [" + line + "]");
	        }
	        while (st.hasMoreElements()) {
	            String s = st.nextToken();
	            try {
	                ans.add(Integer.parseInt(s));
	            } catch (Exception ex) {
	                throw new Exception("Bad Integer in " + "[" + line + "]. " + ex.getMessage());
	            }
	        }
	        return ans;
	    }

	    public static double[][] DataReadDouble(final File file, int d) {
	        List<ArrayList<Double>> lists = new ArrayList<ArrayList<Double>>();
	        for (int i = 0; i < d; i++) {
	            lists.add(new ArrayList<Double>());
	        }
	        BufferedReader br = null;
	        try {
	            // Read the file, save data to List<Integer>
	            br = new BufferedReader(new FileReader(file));
	            String line = null;
	            while ((line = br.readLine()) != null) {
	                // Add nth integer to lists[n]
	                List<Double> ints = parse_line_double(line,d);
	                for (int i = 0; i < d; i++) {
	                    (lists.get(i)).add(ints.get(i));
	                }
	            }

	            // convert lists to 2 Integer[]
	            Double[] array1 = lists.get(0).toArray(new Double[lists.size()]);
	            int n=array1.length;
	            double data[][] = new double[n][d];

	            int j=0;
	            while(j<d) {
	            	// convert lists to 2 Integer[]
	                Double[] array = lists.get(j).toArray(new Double[lists.size()]);
		            for(int i=0; i<n; i++) {
		                	data[i][j]=array[i];
		            }

		            j++;
	            }

	            return data;

	        } catch (Exception ex) {
	            System.out.println(ex);
	        } finally {
	            try {
	                if (br != null) {
	                    br.close();
	                }
	            } catch (Exception ex) {
	                // ignore error
	            }
	        }
	        return null;
	    }

	    // parse 2 integers as a line of String
	    private static List<Double> parse_line_double(String line, int d) throws Exception {
	        List<Double> ans = new ArrayList<Double>();
	        StringTokenizer st = new StringTokenizer(line, " ");
	        if (st.countTokens() != d) {
	            throw new Exception("Bad line: [" + line + "]");
	        }
	        while (st.hasMoreElements()) {
	            String s = st.nextToken();
	            try {
	                ans.add(Double.parseDouble(s));
	            } catch (Exception ex) {
	                throw new Exception("Bad Integer in " + "[" + line + "]. " + ex.getMessage());
	            }
	        }
	        return ans;
	    }

	    private static void DataWriteDouble(String file_name, double data[][]) {
	    	int n = data.length;	// the number of rows
	    	int m = data[0].length;	// the number of colmuns

	    	try {
	            PrintWriter pw = new PrintWriter(file_name);
	            for(int i=0; i<n; i++) {
	            	for(int j=0; j<m; j++) {
	            		if(j<m-1) {
	                		pw.format("%.3f\t", data[i][j]);
	            		}else {
	                		pw.format("%.3f\n", data[i][j]);
	            		}
	                }
	            }

	            pw.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }

	    private static void DataWriteInteger(String file_name, int data[]) {
	    	int n = data.length;	// the number of rows

	    	try {
	            PrintWriter pw = new PrintWriter(file_name);
	            for(int i=0; i<n; i++) {
            		pw.format("%d\n", data[i]);
                }
	            pw.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }

	    private static int[] unique(int[] nums) {
	        return Arrays.stream(nums).distinct().toArray();
	    }



	    private static void ConfusionMatrix(int Pred[], int Act[], String name[]) {
	    	int n=name.length;	// the number of classes
	    	int m=Pred.length;	// the number of features
	    	int CM [][] = new int [n][n];
	    	for(int i=0; i<n; i++) {
	    		Arrays.fill(CM[i],0);
	    	}

	    	int a,b;

	    	for(int i=0; i<m; i++) {
	    		a = Act[i];
	    		b = Pred[i];
	    		CM[a][b] = CM[a][b] + 1;
	    	}

	    	for(int i=0; i<n; i++) {
	    		System.out.printf(name[i]+"\t");
	    		for(int j=0; j<n; j++) {
	    			if(j==n-1) {
			    		System.out.printf(" %d\n", CM[i][j]);
	    			}else {
			    		System.out.printf(" %d", CM[i][j]);
	    			}
		    	}
	    	}

	    }

}