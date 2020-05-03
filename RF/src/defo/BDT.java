package defo;

import java.util.ArrayList;
import java.util.List;

public class BDT {	// binary decision tree class

	List<Double> V = new ArrayList<Double>();	// arrays in the list
	List<Integer[]> N = new ArrayList<Integer[]>();
	List<Integer> P = new ArrayList<Integer>();
	List<Double> G = new ArrayList<Double>();

    // growing tree method
    public BDT GrowTree(double X[][], int A[], int Atr[], int d, int nmin, int rseed) {
    	BDT calc = new BDT();
    	int n=X.length;			// the number of elements
    	int m=Atr.length;	// the number of selected attributes
    	Sfmt rnd = new Sfmt(rseed);
		List<Integer> parents = new ArrayList<Integer>();	// parents of the next level
		List<Double> cdt = new ArrayList<Double>();	// arrays in the list
		List<Integer[]> node = new ArrayList<Integer[]>();
		List<Double> gini = new ArrayList<Double>();	// arrays in the list

    	for(int i=0; i<nmin; i++) {	// loop for tree depth
    		if(i==0) {	// at the loot node: assign all features
    			Integer array [] = new Integer[n];
    			for(int j=0; j<n; j++) {
    				array[j] = j;
    			}
				node.add(array);		// set all features to the root node
				gini.add(Gini(A));		// calculate Gini coefficient for the root node

				int bdr = rnd.NextInt(n);		// the selected border
				double val = X[bdr][Atr[i]];	// a vlue of bifracation
				cdt.add(val);					// set the border value
				List<Integer> left = new ArrayList<Integer>();	// the left node (unsatisfy the condition)
				List<Integer> right = new ArrayList<Integer>();	// the right node (satisfy the condition)
				int cl=0, cr=0;

				// assign each set of nodes to the next level tree
				for(int j=0; j<n; j++) {
					if(X[j][Atr[i]] < val) {
						left.add(j);
						cl++;
					}else {
						right.add(j);
						cr++;
					}
				}
				if(cl==0) {
					//left = null;
					gini.add(null);
				}else {
					int Al [] = new int [left.size()];
					for(int j=0; j<Al.length; j++) {
						Al[j] = A[left.get(j)];
					}
					gini.add(Gini(Al));
				}
				node.add(left.toArray(new Integer[left.size()]));
				if(cr==0){
					//right = null;
					gini.add(null);
				}else {
					int Ar [] = new int [right.size()];
					for(int j=0; j<Ar.length; j++) {
						Ar[j] = A[right.get(j)];
					}
					gini.add(Gini(Ar));
				}
				node.add(right.toArray(new Integer[right.size()]));

				parents.add(0);

    		}else {	// except the root node
    			for(int k=0; k<Math.pow(2,(i-1)); k++) {	// loop until 2^i
    				int l = parents.get(k);	// the parent number of this node
    				int [] p = {((l*2)+(k+1)),((l*2)+(k+2))}; 	// the number of these node

    				// loop for the left and right children
    				for(int q=0; q<2; q++) {
        				if(node.get(p[q]).length!=0) {
        					Integer[] L = node.get(p[q]);
        					int bdr = rnd.NextInt(L.length);		// the selected border in L
        					double val = X[L[bdr]][Atr[i]];			// a value of bifracation
        					cdt.add(val);					// set the border value
        					List<Integer> left = new ArrayList<Integer>();	// the left node (unsatisfy the condition)
        					List<Integer> right = new ArrayList<Integer>();	// the right node (satisfy the condition)
        					int cl=0, cr=0;

        					// assign each set of nodes to the next level tree
        					for(int j=0; j<L.length; j++) {
        						if(X[L[j]][Atr[i]] < val) {
        							left.add(L[j]);
        							cl++;
        						}else {
        							right.add(L[j]);
        							cr++;
        						}
        					}
        					if(cl==0) {
        						//left = null;
        						gini.add(null);
        					}else {
        						int Al [] = new int [left.size()];
        						for(int j=0; j<Al.length; j++) {
        							Al[j] = A[left.get(j)];
        						}
        						gini.add(Gini(Al));
        					}
        					node.add(left.toArray(new Integer[left.size()]));
        					if(cr==0){
        						//right = null;
        						gini.add(null);
        					}else {
        						int Ar [] = new int [right.size()];
        						for(int j=0; j<Ar.length; j++) {
        							Ar[j] = A[right.get(j)];
        						}
        						gini.add(Gini(Ar));
        					}
        					node.add(right.toArray(new Integer[right.size()]));

        					parents.add(p[q]);
        				}
    				}
    			}
    		}
    	}

    	calc.V = cdt;
    	calc.N = node;
    	calc.P = parents;
    	calc.G = gini;
    	return calc;
    }

    // showing tree method
    public void ShowTree(BDT tree, double data[][], int ans[], int d, int nmin) {
		System.out.println();
		for(int i=0; i<tree.V.size(); i++) {
			System.out.println(tree.V.get(i));
		}

		int l=0;
		for(int i=0; i<=nmin; i++) {
			for(int j=l; j<(l + Math.pow(2,i)); j++) {
				System.out.println();
				System.out.println("Node " + j);
				Integer k [] = tree.N.get(j);
				for(int p=0; p<k.length; p++) {
					for(int q=0; q<d; q++) {
						if(q==(d-1)) {
							System.out.printf("%.1f ",data[k[p]][q]);
							System.out.println(ans[k[p]]);
						}else {
							System.out.printf("%.1f ",data[k[p]][q]);
						}
					}
				}
			}
			l = (int) (l + Math.pow(2,i));
		}
    }

    // calculating Gini coefficient method
    private double Gini(int A[]) {
    	double gini = 0;
    	int n=A.length;
    	double sums=0;
    	double sum=0;
    	List<Integer> list = new ArrayList<Integer>();
    	Mat mat = new Mat();

    	// create the class list
    	for(int i=0; i<n; i++) {
    		if(i==0) {
    			list.add(A[i]);
    		}else {
    			int count=0;
    			boolean judge=true;
    			while(count<list.size()) {
    				if(list.get(count) == A[i]) {
    					judge=false;
    				}
    				count++;
    			}
    			if(judge==true) {
    				list.add(A[i]);
    			}
    		}
    	}

    	int ind [] = new int [n];
    	for(int i=0; i<list.size(); i++) {
    		ind = mat.FindbyInd(A, list.get(i));
    		for(int j=0; j<n; j++) {
    			sum = sum + ind[j];
    		}
    		sums = sums + Math.pow((sum/n),2);
    		sum=0;
    	}

    	gini = 1 - sums;

    	return gini;
    }

    // return 1 answer from 1 feature by 1 tree
    public int Evaluation(BDT tree, int Atr[], double X[], int A[], int m, int nmin){
		int ans=0;
		int nc=0;						// node counter
		int atc [] = new int [nmin];	// attribure counter
		BDT bdt = new BDT();
		Mat mat = new Mat();

		int count=0;
		for(int i=0; i<nmin; i++) {	// create attibute counter vector
			atc[i] = count;
			count++;
			if(count==m) {
				count=0;
			}
		}

		boolean loop=true;
		count = 0;
		while(loop){
			if(X[Atr[atc[count]]] < tree.V.get(nc)) {	// bifracation to the left node
				nc = (2*nc) + 1;
			}else {										// bifracation to the right node
				nc = (2*nc) + 2;
			}
			count++;

			if(nc >= ((Math.pow(2,nmin+1) - 1) - (Math.pow(2,nmin)))) {
				loop=false;
			}
		}

		if(tree.N.get(nc).length==0) {
			if(nc%2==0) {
				nc--;
			}else {
				nc++;
			}
		}

		Integer k [] = tree.N.get(nc);

		int node [] = new int[k.length];
		for(int i=0; i<node.length; i++) {
			node[i] = A[k[i]];
			//System.out.println(k[i]);
		}
		double gini = bdt.Gini(node);

		if(gini==0) {		// if the node set is full of one number
			ans = node[0];
		}else {				// otherwise take a majority vote
			ans = mat.MajorityVote(node);
		}

		return ans;
	}
}



