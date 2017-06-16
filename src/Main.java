public class Main {
    /**
     * This function takes a test case and solve the prints the solution along with the time it took
     * @param grid : a list of String where, 0,'.' are to be filled values,
     **/
    private static void solver(String grid) {
        
        Sudoku_Solver solver = new Sudoku_Solver();
        solver.search(solver.parseString(grid));
    }
    
    /**
     * This function test the sudoku solver over 5 easy test cases
     * Output is Big so at a time run only one test case. Remove comments from the test you want to run
     * Takes no parameter.
     **/
    private static void  testerEasy(){
        //String test1 = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
        //solver(test1);
        
        String test2 = "200080300060070084030500209000105408000000000402706000301007040720040060004010003";
        solver(test2);
        
        //String test3 = "000000907000420180000705026100904000050000040000507009920108000034059000507000000";
        //solver(test3);
        
        //String test4 = "030050040008010500460000012070502080000603000040109030250000098001020600080060020";
        //solver(test4);
        
        //String test5 = "020810740700003100090002805009040087400208003160030200302700060005600008076051090";
        //solver(test5);
    }
    
    /**
     * This function test the sudoku solver over 5 hard test cases
     * Takes no parameter.
     **/
    private static void  testerHard(){
        //String test1 = "85...24..72......9..4.........1.7..23.5...9...4...........8..7..17..........36.4.";
        //solver(test1);
        
        //String test2 ="..53.....8......2..7..1.5..4....53...1..7...6..32...8..6.5....9..4....3......97..";
        //solver(test2);
        
        //String test3 = "12..4......5.69.1...9...5.........7.7...52.9..3......2.9.6...5.4..9..8.1..3...9.4";
        //solver(test3);
        
        //String test4 ="...57..3.1......2.7...234......8...4..7..4...49....6.5.42...3.....7..9....18.....";
        //solver(test4);
        
        //String test5 = "7..1523........92....3.....1....47.8.......6............9...5.6.4.9.7...8....6.1.";
        //solver(test5);
        
    }
    
    /**
     * function to print the board at any point, solved semi-solved etc.
     * @param solution : grid to be printed
     **/
    public static void main(String args[]){
        testerEasy();
        testerHard();
    }
    
    
}