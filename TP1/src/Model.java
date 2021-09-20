import java.lang.Math.*;


public class Model {
	private double p0;
	private double p1;
	private double trans0;
	private double trans1;
	// trans 0 = 0->1, trans 1 = 1->0
	void getGC(String chr) {
		float[] p = new float[2];
		int count = 0;
		for(int i=0; i<chr.length(); i++) {
			if(chr.charAt(i)=='C' || chr.charAt(i)=='G') {
				count++;
			}
		}
		p[0] = count/chr.length();
		p[1] = (p[0]+1)/2;
		this.p0 = p[0];
		this.p1 = p[1];
	}
	public double getp0() {
		return this.p0;
	}
	
	public void setp0(double val) {
		this.p0 = val;
	}
	public double getp1() {
		return this.p1;
	}
	
	public void setp1(double val) {
		this.p1 = val;
	}
	public double getTrans0() {
		return this.trans0;
	}
	
	public void setTrans0(double val) {
		this.trans0 = val;
	}
	public double getTrans1() {
		return this.trans1;
	}
	
	public void setTrans1(double val) {
		this.trans1 = val;
	}
	
	int[] segmentation(String chr) {
		int g = chr.length();
		int[] segments = new int[g];
		
		for(int i = 0; i<g; i++) {
			
		}
		
		return segments;
	}
}
