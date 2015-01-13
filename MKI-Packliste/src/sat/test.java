package sat;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] superarray={0,0,0,0,0,0,0,0};
		int number = superarray.length-1;
		boolean first = true;
		for(int k=superarray.length; k>=0;k--){
			int[] array = superarray.clone();
			for(int  j=0; j<=number; j++){
				for(int i=0; i<array.length;i++){
					System.out.print(array[i] + ", ");
				}
				System.out.print("\n");
				if(j<array.length)array[j]=1;
			}
			if(k-1>0)superarray[k-1]=1;
			number--;
		}				
	}

}
