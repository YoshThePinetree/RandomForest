package defo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mat {

		public double[][] ScalaMultiply(double X[][], double a){
			int n=X.length;
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					X[i][j] = X[i][j] * a;
				}
			}
			return X;
		}

		public double[][] Sum(double X[][], double Y[][]){
			int n=X.length;
			double Z [][] = new double [n][n];
			for(int i=0; i<n; i++) {
		    	for(int j=0; j<n; j++) {
		    		Z[i][j] = X[i][j] + Y[i][j];
		    	}
	    	}
	    	return Z;
		}

		public double[][] WeightedSum(double X[][], double Y[][], double a, double b){
			int n=X.length;
			double Z [][] = new double [n][n];
			for(int i=0; i<n; i++) {
		    	for(int j=0; j<n; j++) {
		    		Z[i][j] = (a*X[i][j]) + (b*Y[i][j]);
		    	}
	    	}
	    	return Z;
		}

	 	public double[][] CopyUpLow(double X[][]){
	    	int n=X.length;
	    	double Y [][] = new double [n][n];
	    	Y=X;
	    	for(int i=0; i<n; i++) {
		    	for(int j=0; j<n; j++) {
		    		Y[j][i] = X[i][j];
		    	}
	    	}
	    	return Y;
	    }

	    public double[][] AssignDiag(double X[][], double diag){
	    	int n=X.length;
	    	for(int i=0; i<n; i++) {
		    	X[i][i]=diag;
	    	}
	    	return X;
	    }

	    public double FindMedian(double X[][]){
	    	int n=X.length;
	    	double median=0;
	    	List<Double> list = new ArrayList<Double>();

	    	for(int i=0; i<n; i++) {
				for(int j=i+1; j<n; j++) {
					list.add(X[i][j]);
				}
	    	}

	    	//for(Double str :list){
	    	//	  System.out.println(str);
    		//}

	    	Double[] array = list.toArray(new Double[list.size()]);
	    	Arrays.sort(array);

	    	int m=array.length/2;

	    	if(0 != (array.length%2)){
				median = array[m];
			}
			else{
				median = (array[m-1] + array[m])/2.0;
			}


	    	return median;
	    }

	    public double FindMin(double X[][]){
	    	int n=X.length;
	    	double min=X[0][1];

	    	for(int i=0; i<n; i++) {
				for(int j=i+1; j<n; j++) {
					min=Math.min(min, X[i][j]);
				}
	    	}

	    	return min;
	    }

	    public double FindVecMaxExp(double X[], int m){
	    	int n=X.length;
	    	double max = -999999999;

	    	for(int i=0; i<n; i++) {
	    		if(i!=m) {
		    		max = Math.max(max, X[i]);
	    		}
	    	}

	    	return max;
	    }

	    public int FindVecArgMax(double X[]){
	    	int n=X.length;
	    	int ind=0;
	    	double max = -999999999;

	    	for(int i=0; i<n; i++) {
	    		if(X[i]>max) {
	    			max=X[i];
	    			ind=i;
	    		}
	    	}

	    	return ind;
	    }

	    int val;
	    int ind;
	    public Mat MaxVecInt(int X[]) {
	    	Mat mat = new Mat();
	    	int n=X.length;
	    	int index=0;
	    	int max = -999999999;

	    	for(int i=0; i<n; i++) {
	    		if(X[i]>max) {
	    			max=X[i];
	    			index=i;
	    		}
	    	}
	    	mat.val=max;
	    	mat.ind=index;

	    	return mat;
	    }

	    public boolean Find (List<Integer> X, int a){
	    	int n=X.size();
	    	boolean ind=false;
	    	for(int i=0; i<n; i++) {
	    		if(X.get(i)==a) {
	    			ind = true;
	    		}
	    	}
	    	return ind;
	    }

	    public int [] FindbyInd (int X[], int a){
	    	int n=X.length;
	    	int ind [] = new int [n];
	    	Arrays.fill(ind,0);
	    	for(int i=0; i<n; i++) {
	    		if(X[i]==a) {
	    			ind[i] = 1;
	    		}
	    	}
	    	return ind;
	    }

	    public int MajorityVote(int X[]) {
	    	int p=0;
	    	int n=X.length;
	    	int nums [] = unique(X);
	    	int m=nums.length;

	    	int max=0;
	    	int count=0;
	    	for(int i=0; i<m; i++) {
	    		for(int j=0; j<n; j++) {
	    			if(X[j]==nums[i]) {
	    				count++;
	    			}
	    		}
	    		if(count>max) {
	    			p=nums[i];
	    		}
	    		count=0;
	    	}
	    	return p;
	    }

	    // uniquely sample the integer data from 0~d-1 and return the matrix Y
	    // Y is n*m matrix: n is the number of features, m is the number of attributes
	    public int [][] SmplVecUniq(int d, int n, int m, int rseed){
	    	int Y [][] = new int [n][m];
	    	Sfmt rnd = new Sfmt(rseed);
	    	Mat mat = new Mat();
	    	int count = 0;
	    	int a;

	    	for(int i=0; i<n; i++) {
	    		List<Integer> list = new ArrayList<Integer>();
	    		while(count<m) {
	    			if(count==0) {
	    				Y[i][count]=rnd.NextInt(d);
	    				list.add(Y[i][count]);

	    			}else {
	    				a = rnd.NextInt(d);
	    				while(mat.Find(list, a)) {
	    					a = rnd.NextInt(d);
	    				}
	    				Y[i][count]= a;
	    				list.add(Y[i][count]);
	    			}
	    			count++;
	    		}
	    		count = 0;
	    	}

	    	return Y;
	    }

	    private static int[] unique(int[] nums) {
	        return Arrays.stream(nums).distinct().toArray();
	    }

}
