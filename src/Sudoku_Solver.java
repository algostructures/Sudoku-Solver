import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.*;
import java.util.stream.IntStream;

public class Sudoku_Solver {
    /*
     Grid is Formatted as 
         0  1   2 |  3  4  5 |  6  7  8  
         9  10 11 | 12 13 14 | 15 16 17 
         18 19 20 | 21 22 23 | 24 25 26 
         ------------------------------
         27 28 29 | 30 31 32 | 33 34 35 
         36 37 38 | 39 40 41 | 42 43 44 
         45 46 47 | 48 49 50 | 51 52 53
         ------------------------------
         54 55 56 | 57 58 59 | 60 61 62 
         63 64 65 | 66 67 68 | 69 70 71 
         72 73 74 | 75 76 77 | 78 79 80
     **/
    
    /**
     * units : a unit is a square, row, column which can't haveone than one occurrence of a number. 
     * peers of x : set(unique values) of numbers which share a unit with x
     * unitList : List of all units(row,column,box) a for number which   
     **/
    static FileWriter fw;
    static BufferedWriter bw;
    static PrintWriter out;
    
    
    private ArrayList<HashSet<Integer>> unitList;
    private HashMap<Integer, HashSet<Integer>> units;
    private HashMap<Integer, HashSet<Integer>> peers;
    
    private final int ROWS = 9;
    private final int COLS = 9;
    private final int SQR = 9;

    /**
     * Constructor to Initialize unitList, units, peers 
     * @param no parameters
     * */
    public Sudoku_Solver() {
        try {
            fw = new FileWriter("output.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bw = new BufferedWriter(fw);
        out = new PrintWriter(bw);
        unitList = new ArrayList<>();
        units = new HashMap<>();
        peers = new HashMap<>();

        @SuppressWarnings("unchecked")
        HashSet<Integer>[][] squareBoxUnits = (HashSet<Integer>[][]) Array.newInstance(HashSet.class, 3, 3);

        /*
          creating a square box, there are 9 boxes in 9*9 Sudoku.
          9 HashSet for every place in 9 boxes
          Each place can will have set of possible values 1-9
          */
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                squareBoxUnits[i][j] = new HashSet<>();

        for (int i = 0; i < ROWS; i++) {
            HashSet<Integer> row = new HashSet<>();
            HashSet<Integer> column = new HashSet<>();
            for (int j = 0; j < COLS; j++) {
                row.add(i*ROWS + j);
                column.add(j*COLS + i);
                squareBoxUnits[i/3][j/3].add(i*SQR + j);
            }
            unitList.add(row);
            unitList.add(column);
        }

        for (int i = 0; i < 3; i++) {
            unitList.addAll(Arrays.asList(squareBoxUnits[i]).subList(0, 3));
        }

        for (int i = 0; i < ROWS; i ++) {
            for (int j = 0; j < COLS; j++) {
                for (HashSet<Integer> unit : unitList) {
                    if (unit.contains(i*ROWS + j)) {
                        if (units.get(i*ROWS + j)  == null) {
                            HashSet<Integer> temp = new HashSet<Integer>();
                            units.put(i*ROWS + j, temp);
                        }
                        HashSet<Integer> temp = units.get(i*ROWS + j);
                        temp.addAll(unit);
                        units.put(i*ROWS + j, temp);
                    }
                }
            }
        }

        for (Map.Entry<Integer, HashSet<Integer>> entry : units.entrySet()) {
            Integer key = entry.getKey();
            HashSet<Integer> value = entry.getValue();
            HashSet<Integer> temp = new HashSet<Integer>();
            for (Integer i : value) {
                temp.add(i);
                temp.remove(key);
                peers.put(key, temp);
            }
        }

    }
    
    
    /**
     * Eliminates all other values from values.get(key), except v
     * @param values : A Map Where Key is value between 0-80, value is possible 1-9 values
     * @param key : It is the key where we need to remove all other values except d
     * @param v : The value to be assigned to  key, a single integer 1-9
     * @return : Modified grid, values Map, return null if contradiction is detected
     **/
    private MyCustomHashMap<Integer, HashSet<Integer>> assign(MyCustomHashMap<Integer, HashSet<Integer>> values, int key, int v) {
        //DeepCopy
        if (values == null)
            return null;
        HashSet<Integer> valueSet = new HashSet<Integer>(values.get(key));
        valueSet.remove(v);
        for (Integer i : valueSet) {
            if (eliminate(values, key, i) == null)
                return null;
        }
        out.println("Assigning");
        boardPrinter(values);
        return values;
    }
    
    
    /**
     *  It eliminates the value v, from key, if reduced to one value remove this v from all peers
     *  @param values : A Map Where Key is value between 0-80, value is possible 1-9 values
     *  @param key : the key for the set from the value v to be deleted
     *  @param v : the value to be deleted
     *  @return : Modified Map, Or null if contradiction is found
     **/
    private MyCustomHashMap<Integer, HashSet<Integer>> eliminate(MyCustomHashMap<Integer, HashSet<Integer>> values, int key, int v) {
        if (!values.get(key).contains(v))
            return values;  // if v is already deleted from here
        HashSet<Integer> temp = values.get(key);
        temp.remove(v);
        values.put(key, temp);

        if (values.get(key).isEmpty())
            return null; //contradiction set is null, Last value removed
        else if (values.get(key).size() == 1) {
            int value2 = (Integer)values.get(key).toArray()[0];
            HashSet<Integer> peerSet = peers.get(key);
            for (Integer peer: peerSet) {
                //if set is reduced to one then remove this value from all other peers
                if (eliminate(values, peer, value2) == null)
                    return null; 
            }
        }

        HashSet<Integer> unitSet = units.get(key);
        HashSet<Integer> containsSet  = new HashSet<Integer>();
        for (Integer unit: unitSet) {
            if (values.get(unit).contains(v))
                containsSet.add(unit);
        }

        if (containsSet.isEmpty())
            return null;
        else if (containsSet.size() == 1) {
            // reduced to single value, assignment 
            int unit = (Integer)containsSet.toArray()[0];
            if (assign(values, unit, v) == null)
                return null;
        }
        out.println("Elimination :");
        boardPrinter(values);
        return values;
    }
    

    /**
     * This methods searches, when all values are not reduced to one, if more than one try assignning each value
     * Starts position with minimum values.
     * @param values : the grid to be searched, backtracked, using DFS
     * @return : The Successful Assignment, The Solution   
     **/
    MyCustomHashMap<Integer, HashSet<Integer>> search(MyCustomHashMap<Integer, HashSet<Integer>> values) {
        if (values == null)
            return null;
        boolean isSolved = true;
        int minKey = 0;
        int minValue = Integer.MAX_VALUE;
        for (Map.Entry<Integer, HashSet<Integer>> entry: values.entrySet()) {
            int domainSize = entry.getValue().size();
            if ( domainSize != 1) {
                isSolved = false;
            }
            if (domainSize > 1) {
                if (domainSize < minValue) {
                    minValue = domainSize;
                    minKey = entry.getKey();
                }
            }

        }

        if (isSolved)  {
            out.println("Final Soltion :");
            boardPrinter(values);
            out.flush();
            out.close();
            return values;
        }

        HashSet<Integer> possibleValues = values.get(minKey);
        for (Integer assignment: possibleValues) {
            MyCustomHashMap<Integer, HashSet<Integer>> solvedValue =  search(assign(deepCloneValues(values), minKey, assignment));
            if (solvedValue != null){
                //System.out.println("Branching : ");
                //boardPrinter(solvedValue);
                return solvedValue;
            }

        }
        return null;
    }
    
    
    /**
     * A utility function to create deep copy of the grid.
     *  @param Takes the original grid to be copied
     *  @return Returns the deep copy of grid.
     **/
    private MyCustomHashMap<Integer, HashSet<Integer>> deepCloneValues(MyCustomHashMap<Integer, HashSet<Integer>> original) {
        MyCustomHashMap<Integer, HashSet<Integer>> cloned = new MyCustomHashMap<Integer, HashSet<Integer>>(new HashSet<Integer>());
        for (Map.Entry<Integer, HashSet<Integer>> entry: original.entrySet()) {
            HashSet<Integer> temp = cloned.get(entry.getKey());
            HashSet<Integer> clonedHashSet = new HashSet<Integer>(entry.getValue());
            cloned.put(entry.getKey(), clonedHashSet);
        }

        return cloned;
    }
    
    
    /**
     * A Utility function to, initialize the the grid 
     * @param Takes no parameter
     * @return the initialized grid 
     **/
    private MyCustomHashMap<Integer, HashSet<Integer>> initializeValues() {
        MyCustomHashMap<Integer, HashSet<Integer>> values = new MyCustomHashMap<Integer, HashSet<Integer>>(new HashSet<Integer>());
        IntStream.range(0, ROWS*COLS).forEach(i-> IntStream.range(1, COLS + 1).forEach(j ->
                values.computeIfAbsent(i, k -> new HashSet<>()).add(j)));
        return values;
    }
    

    /**
     * The function to convert string input to a grid of key value pairs
     * @param grid : a String where, 0 are to be filled values
     * @return a grid of key value pairs  
     **/
    public MyCustomHashMap<Integer, HashSet<Integer>> parseString(String grid) {
        MyCustomHashMap<Integer, HashSet<Integer>> values = initializeValues();
        char[] gridValues = grid.toCharArray();
        out.println("Initial Grid");
        assert gridValues.length == 81;
        for (int i = 0; i < ROWS*COLS; i++) {
            if (gridValues[i] != '0' && gridValues[i] != '.') {
                MyCustomHashMap<Integer, HashSet<Integer>> clonedValues = deepCloneValues(values);
                values = assign(clonedValues, i, Integer.parseInt(gridValues[i]+""));
            }
        }
        boardPrinter(values);
        return values;
    }
    
    /**
     * function to print the board at any point, solved semi-solved etc.
     * @param solution : grid to be printed
     **/
    public void boardPrinter(MyCustomHashMap<Integer, HashSet<Integer>> solution){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                out.printf("%25s", solution.get(i*9+j).toString());
            }
            out.println();
        }
     }
}