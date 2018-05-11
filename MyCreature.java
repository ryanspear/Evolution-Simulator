import cosc343.assig2.Creature;
import java.util.*;

/**
 * The MyCreate extends the cosc343 assignment 2 Creature.  Here you implement
 * creatures chromosome and the agent function that maps creature percepts to
 * actions.  
 *
 * @author  
 * @version 1.0
 * @since   2017-04-05 
 */
public class MyCreature extends Creature {

    // Random number generator
    Random rand = new Random();
    float chromosome[] = new float[11];
    int fitness = 0;
    /* Empty constructor - might be a good idea here to put the code that 
       initialises the chromosome to some random state   
  
       Input: numPercept - number of percepts that creature will be receiving
       numAction - number of action output vector that creature will need
       to produce on every turn
    */
    public MyCreature(int numPercepts, int numActions) {
        for(int i = 0; i < chromosome.length; i++){
            chromosome[i] = rand.nextFloat();

        }
    }
    // use this constructor to create new creature children.
    public MyCreature(MyCreature parent1, MyCreature parent2, float mutationProb){

        int chromosomeCross = rand.nextInt(chromosome.length);

        for(int i = 0; i < chromosomeCross; i++){
            chromosome[i] = parent1.getChromosome()[i];
        }
        for(int i = chromosomeCross; i < chromosome.length; i++){
            chromosome[i] = parent2.getChromosome()[i];
        }

        float odds = rand.nextFloat();

        // random mutation
        if(odds < mutationProb){
            int randomAllele = rand.nextInt(chromosome.length);
            chromosome[randomAllele] = rand.nextFloat();
        }
    }

        
    
    public float[] getChromosome(){
        return this.chromosome;
    }

    public void setChromosome(float[] chromosome){
        this.chromosome = chromosome;
    }
    
    public void setFitness(int fitness){
        this.fitness = fitness;
    }

    public int getFitness(){
        return this.fitness;
    }

    public boolean arrayContains(int array[], int target){
        for(int i = 0; i < array.length; i++){
            if(array[i] == target){
                return true;
            }
        }

        return false;
    }
      
  
  
  /* This function must be overridden by the MyCreature class, because it implements
     the AgentFunction which controls creature behavoiur.  This behaviour
     should be governed by the model (that you need to come up with) that is
     parameterise by the chromosome.  
  
     Input: percepts - an array of percepts
     numPercepts - the size of the array of percepts depend on the percept
     chosen
     numExpectedAction - this number tells you what the expected size
     of the returned array of percepts should bes
     Returns: an array of actions 
  */
    @Override
    public float[] AgentFunction(int[] percepts, int numPercepts, int numExpectedActions) {
        // This is where your chromosome gives rise to the model that maps
        // percepts to actions.  This function governs your creature's behaviour.
        // You need to figure out what model you want to use, and how you're going
        // to encode its parameters in a chromosome.
      
        // At the moment, the actions are chosen completely at random, ignoring
        // the percepts.  You need to replace this code.

        
        boolean allZero = true;
        float actions[] = new float[numExpectedActions];
        for(int i = 0; i < numExpectedActions; i++){
            actions[i] = 0;
        }

        
        // for every percept, use the value to determine the size of the move toward or away actions.
        for(int i = 0; i < numPercepts-1; i++){
            // if looking at middle square, influence eating action.
            if(i == 4 && percepts[i] != 0){
                allZero = false;
                actions[9] = percepts[4]*chromosome[9]*3;
            } else {
                if(percepts[i]!=0){
                    allZero = false;
                    actions[i] = percepts[i]*chromosome[i];
                    actions[8-i] = percepts[i]*chromosome[8-i];
                }
            }
        }

        if(allZero){
            actions[10] = 1;
        } else {
            actions[10] = chromosome[10];
        }

        
      
        return actions;
    }
  
}
