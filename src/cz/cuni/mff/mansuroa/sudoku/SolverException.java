package cz.cuni.mff.mansuroa.sudoku;

/**
 * Doslo k chybe pri reseni sudoku
 * @author Alexandr Mansurov <alexander.mansurov@gmail.com>
 */
class SolverException extends Exception {

    public SolverException(String no_solution_exists) {
        super(no_solution_exists);
    }
    
    public SolverException(Exception e) {
        super(e);
    }
    
}
