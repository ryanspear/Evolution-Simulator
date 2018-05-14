import cosc343.assig2.World;
import cosc343.assig2.Creature;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


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
    private final int _numTurns = 50;
    private final int _numGenerations = 100;
    int generationTrack = 0;
    float[] avgFitnessData = new float[_numGenerations+1];
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

        System.out.println("Queen's chromosome: " + Arrays.toString(queenCreature.getChromosome()));
        
        
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

        MyCreature child = new MyCreature();
        float mutationProb = 0.03f;
        for(int i = 0; i < numCreatures; i++){

            MyCreature parent1 = tournamentSelection(old_population, numCreatures, 5);
            MyCreature parent2 = tournamentSelection(old_population, numCreatures, 5);
            while(Arrays.equals(parent1.getChromosome(), parent2.getChromosome())){
                parent2 = tournamentSelection(old_population, numCreatures, 5);
            }
            if(parent2.getFitness() > parent1.getFitness()){
                child = new MyCreature(parent2, parent1, mutationProb);
            } else {
            
                child = new MyCreature(parent1, parent2, mutationProb);
            }
            
            new_population[i] = child;
            
            System.out.println("Parent1's chromosome: " + Arrays.toString(parent1.getChromosome()));
            System.out.println("Parent2's chromosome: " + Arrays.toString(parent2.getChromosome()));
            System.out.println("Child's chromosome: " + Arrays.toString(child.getChromosome()));
        }
        // elitism here.

        for(int i = 0; i < 3; i++){       
        new_population[i] = tournamentSelection(old_population, numCreatures, 10);
        }
        new_population[0] = queenCreature;

        // Having some way of measuring the fitness, you should implement a proper
        // parent selection method here and create a set of new creatures.  You need
        // to create numCreatures of the new creatures.  If you'd like to implement
        // elitism, you can use old creatures in the next generation.  This
        // example code uses all the creatures from the old generation in the
        // new generation.

        // Increments the number of generations and stores average fitness in
        // array to be graphed.
        generationTrack++;
        avgFitnessData[generationTrack] = avgFitness;
        System.out.println("Generation: " + generationTrack);
        // When at generation 500, graph the results.
        if(generationTrack==_numGenerations){
            System.out.println(Arrays.toString(avgFitnessData));
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new GraphingData(avgFitnessData));
            f.setSize(2000, 1200);
            f.setLocation(200, 200);
            f.setVisible(true);
        }


        // Return new population of creatures.
        return new_population;
    }

    /**
     * Inner class is used for graphing the Average Fitness over generation.
     *
     */
    public class GraphingData extends JPanel {
        // Array of data.
        float[] data;

        // Preferred padding size for the graph to display.
        final int PADDING = 50;

        /**
         * Constructor sets the data array.
         */
        public GraphingData(float[] data){
            this.data = data;
        }

        /**
         * Method draws and displays the data array as a graph.
         * @param g graphics object required for graphing data.
         */
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            // Create and initialise Graphics2D object for the graph.
            Graphics2D g2 = (Graphics2D)g;

            // Set width and height;
            int width = getWidth();
            int height = getHeight();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


            // Sets background to white.
            g2.setPaint(Color.white);
            g2.fillRect(0,0,width,height);

            // Draws the two axes.
            g2.setPaint(Color.black);
            g2.drawLine(PADDING, PADDING, PADDING, height-PADDING);
            g2.drawLine(PADDING, height-PADDING, width-PADDING, height-PADDING);

            //Draws graph labels and max fitness.
            g.drawString("Average Fitness Over Generations", (width/2)-2*PADDING, PADDING/2);
            g.drawString("(Pool = 1/5, mutation = 0.03)", (width/2)-2*PADDING+10, PADDING/2+15);
            g.drawString("Average Fitness", 10, PADDING-10);
            g.drawString("Generation", width/2-25, height-5);
            g.drawString("Highest Average Fitness = " + Float.toString(getMax()), (width/2)-2*PADDING, height/2);


            // Sets the increment size for x-axis and scale for y-axis.
            double xInc = (double)(width - 2*PADDING)/(data.length-1);
            double scale = (double)(height - 2*PADDING)/getMax();

            // Draws x axis points and labels.
            for(int i = 0; i < data.length; i++){
                g2.draw(new Line2D.Double(i*xInc+PADDING, height-PADDING-10, i*xInc+PADDING, height-PADDING+10));
                g.drawString(Integer.toString(i), ((int)xInc*i + PADDING)-10, height-PADDING+30);
            }

            // Draws and connects data points.
            g2.setPaint(Color.blue);
            double lastY = height-PADDING;
            double lastX = PADDING;
            for(int i = 0; i < data.length; i++) {
                double x = PADDING + i*xInc;
                double y = height - PADDING - scale*data[i];
                g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
                g2.draw(new Line2D.Double(lastX, lastY, x, y));
                lastY = y;
                lastX = x;
            }
        }

        /**
         * Method for returning the greatest value of our fitness data.
         * @return highest value in the data array.
         */
        private float getMax() {
            float max = -Integer.MAX_VALUE;
            for(int i = 0; i < data.length; i++) {
                if(data[i] > max)
                    max = data[i];
            }
            return max;
        }
    }
  
}
