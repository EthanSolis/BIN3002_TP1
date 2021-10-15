import java.lang.Math.*;


public class Model {
	private double p0;			//p0 (probability of C or G in state 0)
	private double p1;			//p1 (probability of C or G in state 1)
	private double trans0;  	//transition 0 -> 1
	private double trans1;  	//transition 1 -> 0

	public Model (String chr, int genes) {
		double[] p = new double[2];
		double n = chr.length();
		double count = 0;
		for(int i=0; i<n; i++) {
			if(chr.charAt(i)=='C' || chr.charAt(i)=='G') {
				count++;
			}
		}

		
		p[0] = count/n;

		p[1] = (p[0]+1)/2;

		this.p0 = p[0]/2;
		this.p1 = p[1]/2;
		this.trans0 = genes/n;
		this.trans1 = genes/(p[0]*n);
	}
	
	public double getP0() {
		return p0;
	}

	public double getP1() {
		return p1;
	}

	public double getTrans0() {
		return trans0;
	}

	public double getTrans1() {
		return trans1;
	}

	//create segments meaning path of 0 and 1
	//penalty = 1/2log(n) ; we are using BIC
	int[] segmentation(String chr) {
		int g = chr.length();
		int[] segments = new int[g];
		double[] v0 = new double[g];
		double[] v1 = new double[g];
		
		//scores
		double wAT = Math.log((1-p1)/(1-p0)) + Math.log((1-trans1)/(1-trans0));
		double wCG = Math.log(p1/p0) + Math.log((1-trans1)/(1-trans0));
		
		//penalties
		double penalty0 = Math.log((1-trans0)/trans1);
		double penalty1 = Math.log((1-trans1)/trans0);


		
		
	
		v0[0] = 0;
		for(int i = 0; i<g; i++) {
			if(i != 0) {
				v0[i] = Math.max(v0[i-1],v1[i-1]-penalty0);
			}
			if(chr.charAt(i) == 'A' || chr.charAt(i) == 'T') {
				if(i==0) {
					v1[i] = wAT + (Math.log(trans0/trans1) - Math.log((1-trans1)/(1-trans0)));
				}
				else {
					v1[i] = wAT + Math.max(v1[i-1], v0[i-1]-penalty1);
				}
			}
			if(chr.charAt(i) == 'C' || chr.charAt(i) == 'G') {
				if(i==0) {
					v1[i] = wCG + (Math.log(trans0/trans1) - Math.log((1-trans1)/(1-trans0)));
				}
				else {
					v1[i] = wCG + Math.max(v1[i-1], v0[i-1]-penalty1);
				}
			}
			//smaller or bigger?
			//do i have to put this out of the loop in order for it to work?
			if(v0[i] > v1[i]) {
				segments[i] = 0;
			}
			else {
				segments[i] = 1;
			}
		}
		
		return segments;
	}

	//find new trans0 and trans1 from segments and sequence:
	public void apprentissage (int[] segments, String sequence) {
		int total0 = 0;
		int totaltr0 = 0;
		int tr01 = 0;
		int total1 = 0;
		int totaltr1 = 0;
		int tr10 = 0;

		int p0cg = 0;
		int p1cg = 0;
		double np0 = 0;
		double np1 = 0;

		//for transitions
		for(int i=0; i<segments.length-1; i++) {
			if(segments[i] == 0) {
				if(segments[i+1] == 0) {
					totaltr0 ++;
				}
				if(segments[i+1] == 1) {
					totaltr0 ++;
					tr01 ++;
				}
			}
			if(segments[i] == 1) {
				total1 ++;
				if(segments[i+1] == 0) {
					totaltr1 ++;
					tr10 ++;
				}
				if(segments[i+1] == 1) {
					totaltr1 ++;
				}
			}
		}

		//for p0 and p1
		for(int k=0; k<segments.length; k++) {
			if(segments[k] == 0) {
				total0 ++;
				if(sequence.charAt(k) == 'C' || sequence.charAt(k) == 'G') {
					p0cg ++;
				}
			}
			if(segments[k] == 1) {
				total1 ++;
				if(sequence.charAt(k) == 'C' || sequence.charAt(k) == 'G') {
					p1cg ++;
				}
			}
		}

		np0 = p0cg/total0;
		np1 = p1cg/total1;

		this.p0 = np0/2;
		this.p1 = np1/2;

		this.trans0 = tr01/totaltr0;
		this.trans1 = tr10/totaltr1;

	}
}