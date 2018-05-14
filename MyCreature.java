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
    float chromosome[] = new float[8];
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

    // [0.778465, 0.5896027, 0.37485516, 0.6267713, 0.2511134, 0.8214028, 0.68195444, 0.7151326]
    public MyCreature(){

    }
    // use this constructor to create new creature children.
    public MyCreature(MyCreature parent1, MyCreature parent2, float mutationProb){

        int chromosomeCross = rand.nextInt(chromosome.length);// 5
        int splitAmount = rand.nextInt(chromosome.length); // 3
        int i = 0;
        
        while(i < splitAmount){ // 0 1 2
            if(chromosomeCross == chromosome.length){
                chromosomeCross = 0;
            }
            chromosome[chromosomeCross] = parent1.getChromosome()[chromosomeCross]; // 5 = 5
            i++; // 0 -> 1
            chromosomeCross++; // 5 -> 6.
        }

        while(i < chromosome.length){
            if(chromosomeCross == chromosome.length){
                chromosomeCross = 0;
            }
            chromosome[chromosomeCross] = parent2.getChromosome()[chromosomeCross];
            i++;
            chromosomeCross++;
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
        
        float actions[] = new float[numExpectedActions];
        for(int i = 0; i < numExpectedActions; i++){
            actions[i] = 0;
        }


        for(int i = 0; i < numPercepts; i++){
            if(i == 4){
                actions[9] = percepts[i]*chromosome[6];
            } else {
                    
                if(percepts[i] == 3){
                    actions[i] = percepts[i]*chromosome[0];
                    actions[8-i] = percepts[i]*chromosome[1];
                    
                } else {
                    if(percepts[i] == 2){
                        actions[i] = percepts[i]*chromosome[2];
                        actions[8-i] = percepts[i]*chromosome[3];
                        
                    } else {
                        actions[i] = percepts[i]*chromosome[4];
                        actions[8-i] = percepts[i]*chromosome[5];
                    }
                }
            }
        }

        actions[10] = chromosome[7];
        /**
        
        // for every percept, use the value to determine the size of the move toward or away actions.
        for(int i = 0; i < numPercepts; i++){
            // if looking at middle square, influence eating action.
            if(i == 4 && percepts[i] != 0){
                actions[9] += percepts[4]*chromosome[9]*1.2;
            } else {
                if(percepts[i]!=0){
                    actions[i] += percepts[i]*chromosome[i];
                    actions[8-i] += percepts[i]*chromosome[i+11];
                }
            }
        }
        **/
      
        return actions;
    }


    
  
}
