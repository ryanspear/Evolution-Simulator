import cosc343.assig2.World;
import cosc343.assig2.Creature;
import java.util.*;

/**
* The MyWorld extends the cosc343 assignment 2 World.  Here you can set 
* some variables that control the simulations and override functions that
* generate populations of creatures that the World requires for its
* simulations.
*
* @author  
* @version 1.0
* @since   2017-04-05 
*/
public class MyWorld extends World {
 
  /* Here you can specify the number of turns in each simulation
   * and the number of generations that the genetic algorithm will 
   * execute.
   */
    private final int _numTurns = 100;
    private final int _numGenerations = 500;
    Random rand = new Random();
  

  
    /* Constructor.  
   
       Input: worldType - specifies which simulation will be running
       griSize - the size of the world
       windowWidth - the width (in pixels) of the visualisation window
       windowHeight - the height (in pixels) of the visualisation window
       repeatableMode - if set to true, every simulation in each
       generation will start from the same state
    */
    public MyWorld(int worldType, int gridSize, int windowWidth, int windowHeight, boolean repeatableMode) {   
        // Initialise the parent class - don't remove this
        super(worldType, gridSize, windowWidth,  windowHeight, repeatableMode);

        // Set the number of turns and generations
        this.setNumTurns(_numTurns);
        this.setNumGenerations(_numGenerations);
      
      
    }
 
    /* The main function for the MyWorld application

     */
    public static void main(String[] args) {
        // Here you can specify the grid size, window size and whether to run
        // in repeatable mode or not
        int gridSize = 24;
        int windowWidth =  1600;
        int windowHeight = 900;
        boolean repeatableMode = false;
     
        /* Here you can specify world type - there are two to
           choose from: 1 and 2.  Refer to the Assignment2 instructions for
           explanation of the world type formats.
        */
        int worldType = 1;     
     
        // Instantiate MyWorld object.  The rest of the application is driven
        // from the window that will be displayed.
        MyWorld sim = new MyWorld(worldType, gridSize, windowWidth, windowHeight, repeatableMode);
    }
  

    /* The MyWorld class must override this function, which is
       used to fetch a population of creatures at the beginning of the
       first simulation.  This is the place where you need to  generate
       a set of creatures with random behaviours.
  
       Input: numCreatures - this variable will tell you how many creatures
       the world is expecting
                            
       Returns: An array of MyCreature objects - the World will expect numCreatures
       elements in that array     
    */  
    @Override
    public MyCreature[] firstGeneration(int numCreatures) {
        System.out.println("NumCreatures: " + numCreatures);
        int numPercepts = this.expectedNumberofPercepts();
        int numActions = this.expectedNumberofActions();
      
        // This is just an example code.  You may replace this code with
        // your own that initialises an array of size numCreatures and creates
        // a population of your creatures
        MyCreature[] population = new MyCreature[numCreatures];
        for(int i=0;i<numCreatures;i++) {
            population[i] = new MyCreature(numPercepts, numActions);     
        }
        return population;
    }

    public MyCreature tournamentSelection(MyCreature[] old_population, int populationSize, int n){
        MyCreature best = null;
        MyCreature ind = null;
        for(int i = 0; i < n; i++){
            ind = old_population[rand.nextInt(populationSize)];
            if((best == null) || ind.getFitness() > best.getFitness()){
                best = ind;
            }
        }

        return best;
    }
  
            /* The MyWorld class must override this function, which is
       used to fetch the next generation of creatures.  This World will
       proivde you with the old_generation of creatures, from which you can
       extract information relating to how they did in the previous simulation...
       and use them as parents for the new generation.
  
       Input: old_population_btc - the generation of old creatures before type casting. 
       The World doesn't know about MyCreature type, only
       its parent type Creature, so you will have to
       typecast to MyCreatures.  These creatures 
       have been participating in a simulation and their state
       can be queried to evaluate their fitness
       numCreatures - the number of elements in the old_population_btc
       array
                        
                            
       Returns: An array of MyCreature objects - the World will expect numCreatures
       elements in that array.  This is the new population that will be
       used for the next simulation.  
            */  
    @Override
    public MyCreature[] nextGeneration(Creature[] old_population_btc, int numCreatures) {
        // Typcast old_population of Creatures to array of MyCreatures
        MyCreature[] old_population = (MyCreature[]) old_population_btc;
        // Create a new array for the new population
        MyCreature[] new_population = new MyCreature[numCreatures];

        int fitness = 0;
        int highestFitness = 0;
        MyCreature queenCreature = new MyCreature(9, 11);
        // Here is how you can get information about the old creatures and how
        // well they did in the simulation
        float avgLifeTime=0f;
        int avgFitness = 0;
        int nSurvivors = 0;
        for(MyCreature creature : old_population) {
            // The energy of the creature.  This is zero if a creature starved to
            // death, non-negative otherwise.  If this number is non-zero, but the 
            // creature is dead, then this number gives the energy of the creature
            // at the time of death.
            int energy = creature.getEnergy();
            fitness += energy/5;
            // This querry can tell you if the creature died during the simulation
            // or not.  
            boolean dead = creature.isDead();
        
            if(dead) {
                // If the creature died during simulation, you can determine
                // its time of death (in units of turns)
                int timeOfDeath = creature.timeOfDeath();
                avgLifeTime += (float) timeOfDeath;
                fitness += timeOfDeath;
            } else {
                nSurvivors += 1;
                avgLifeTime += (float) _numTurns;
                fitness += _numTurns;
            }
            creature.setFitness(fitness);
            if(creature.getFitness() > queenCreature.getFitness()){
                queenCreature = creature;
            }
            avgFitness += fitness;
        }
        
        
        // Right now the information is used to print some stats...but you should
        // use this information to access creatures' fitness.  It's up to you how
        // you define your fitness function.  You should add a print out or
        // some visual display of the average fitness over generations.
        avgLifeTime /= (float) numCreatures;
        avgFitness /= numCreatures; 
        System.out.println("Simulation stats:");
        System.out.println("  Survivors    : " + nSurvivors + " out of " + numCreatures);
        System.out.println("  Avg life time: " + avgLifeTime + " turns");
        System.out.println("  Avg Fitness  : " + avgFitness);
        System.out.println("  Highest Fitness: " + queenCreature.getFitness());
        
        float mutationProb = 0.005f;
        for(int i = 0; i < numCreatures; i++){

            MyCreature parent1 = tournamentSelection(old_population, numCreatures, 20);
            MyCreature parent2 = tournamentSelection(old_population, numCreatures, 20);

            // two parents are picked, mix their chromosomes here to create a new child.
            MyCreature child = new MyCreature(parent1, parent2, mutationProb);

            new_population[i] = child;

    
        }
        // elitism here.
        
        new_population[rand.nextInt(numCreatures)] = tournamentSelection(old_population, numCreatures, 25);
        new_population[rand.nextInt(numCreatures)] = tournamentSelection(old_population, numCreatures, 25);
        new_population[rand.nextInt(numCreatures)] = queenCreature;
        
        System.out.println("new_population: " + new_population.length);
     
        // Having some way of measuring the fitness, you should implement a proper
        // parent selection method here and create a set of new creatures.  You need
        // to create numCreatures of the new creatures.  If you'd like to implement
        // elitism, you can use old creatures in the next generation.  This
        // example code uses all the creatures from the old generation in the
        // new generation.

        // Return new population of creatures.
        return new_population;
    }
  
}
