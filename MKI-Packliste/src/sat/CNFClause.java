package sat;

import java.util.ArrayList;

public class CNFClause {

	public int[] condand;
	public int[] condor;
	public int[] effand;
	public int[] effor;

	/*
	 * Important: single conditions in condand, single effects in effor, else
	 * errors in CNF or crash
	 */
	public CNFClause(int[] condand, int[] condor, int[] effand, int[] effor) {
		this.condand = condand;
		this.condor = condor;
		this.effand = effand;
		this.effor = effor;
	}

	public ArrayList<int[]> getCNF(){
		//just one condition
		ArrayList<int[]> returnArray = new ArrayList<int[]>();
		int[] defaultValue={0};
		returnArray.add(defaultValue);
		if(((condand.length == 0) && (condor.length == 0)) || ((effand.length==0) && (effor.length==0))){
			return  returnArray;
		}
		else{
			returnArray.remove(0);
			int[] condition =new int[0];
			//just add all and conditions
			for(int i : condand){
				condition = extendInt(condition, (i*-1)) ;
			}
			//if there is or in the conditions we have to create special clauses
			if(condor.length>0){
					//==================================== generation algorithm for every possibility
					int[] superarray=condor;
					int number = superarray.length-1;
					for(int x=superarray.length; x>=0;x--){
						int[] array = superarray.clone();
						for(int  y=0; y<=number; y++){
							//in condition is the start, now add each term to it
							int[] localcond = condition;
							for(int z=0; z<array.length;z++){
								System.out.print(array[z] + ", ");
								localcond = extendInt(localcond, array[z]*-1);
							}
							//=========================================
							//now do the same as in the else part (add the effects)
							for(int i : effor){
								condition = extendInt(condition, i);
								//if there are ands, extract into several clauses
								if(effor.length>0){
									for(int j : effand){
										int[] conditionWithAnd = extendInt(condition, j);
										returnArray.add(conditionWithAnd);
									}
								}
								else{
									//when there is no and we are done here
									returnArray.add(condition);
								}
							}
							//========================================= algorithm end
							System.out.print("\n");
							if(y<array.length)array[y]=array[y]*-1;
						}
						if(x-1>0)superarray[x-1]=1;
						number--;
					}				
					//====================================
			}
			else{
				for(int i : effor){
					condition = extendInt(condition, i);
					//if there are ands, extract into several clauses
					if(effor.length>0){
						for(int j : effand){
							int[] conditionWithAnd = extendInt(condition, j);
							returnArray.add(conditionWithAnd);
						}
					}
					else{
						//when there is no and we are done here
						returnArray.add(condition);
					}
				}
			}
		}
		
		return returnArray;
	}

	// This method takes an int array and a number and returns an array of
	// orignal.size +1 with the number added at the end
	public int[] extendInt(int[] original, int extend) {
		if (original == null)
			original = new int[0];
		int[] returnIntArray = new int[original.length + 1];
		for (int i = 0; i < original.length; i++) {
			returnIntArray[i] = original[i];
		}
		returnIntArray[original.length] = extend;
		return returnIntArray;
	}

}
