package defo;

import java.util.Arrays;

public class RFop {

	double[][][] Data;
	int[][] Ans;
	double [][] TrainData;
	int [] TrainAns;
	double [][] TestData;
	int [] TestAns;

	public RFop BootStrapSmp(double X[][], int A[], int b, int rseed){
		RFop calc = new RFop();	// return arrays as a class
		Sfmt rnd = new Sfmt(rseed);
		int n=X.length;
		int d=X[0].length;
		double Y [][][] = new double [b][n][d];		// the sampled data matrix
		int V [][] = new int [b][n];				// the corresponding answer

		int l;
		for(int k=0; k<b; k++) {
			for(int i=0; i<n; i++) {
				l = rnd.NextInt(n);			// pick an element from X randomly
				for(int j=0; j<d; j++) {
					Y[k][i][j] = X[l][j];
				}
				V[k][i] = A[l];
			}
		}

		calc.Data=Y;
		calc.Ans=V;

		return calc;
	}

	public RFop DivData(double X[][], int A[], int a, int b, int rseed) {	// randomly divide data in ratio as a:b
		RFop calc = new RFop();	// return arrays as a class
		Sfmt rnd = new Sfmt(rseed);
		int n = X.length;
		int d = X[0].length;
		double  Y[][] = new double [a][d];
		int y [] = new int [a];
		double Z [][] = new double [b][d];
		int z [] = new int [b];
		boolean c[] = new boolean[n];		// the counter vector for already chosen element, false: not chosen, true: chosen
		Arrays.fill(c,false);

		boolean stop = true;
		int count = 0;
		int l;
		while(stop){		// loop for create Y
			l = rnd.NextInt(n);
			while(c[l]==true) {			// try until the unchosen elemet is hit
				l = rnd.NextInt(n);
			}

			c[l]=true;	// register lth element in c

			for(int j=0; j<d; j++) {
				Y[count][j] = X[l][j];
			}
			y[count] = A[l];

			count++;

			if(count==a) {		// if Y is fulfilled, break the while loop
				stop=false;
			}
		}

		count=0;
		for(int i=0; i<n; i++) {
			if(c[i]==false) {
				for(int j=0; j<d; j++) {
					Z[count][j] = X[i][j];
				}
				z[count] = A[i];
				count++;
			}
		}

		calc.TrainData=Y;
		calc.TrainAns=y;
		calc.TestData=Z;
		calc.TestAns=z;

		return calc;
	}

}
