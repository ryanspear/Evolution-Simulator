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
    float chromosome[] = new float[20];
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
      float actions[] = new float[numExpectedActions];
      int usedPercepts[] = new int[numPercepts];
      // I need to take my info from a random percept, as long as all percepts are used. will this work when I do my evolving?
      int actionCount = 0;
      for(int i = 0; i < 18; i+=2){
          int currentPercept = rand.nextInt(9);
          while(arrayContains(usedPercepts, currentPercept)){
                  currentPercept = rand.nextInt(9);
              }
              actions[actionCount] = chromosome[i]*percepts[currentPercept] + chromosome[i+1];
              actionCount++;
      }
      boolean pickRandom = true;
      int currentPercept = rand.nextInt(9);
      actions[9] = chromosome[18]*percepts[currentPercept] + chromosome[19];
      for(int i = 0; i < numExpectedActions-1; i++){
          if(actions[i] > 0.2){
              pickRandom = false;
              break;
          }
      }

      if(pickRandom){
          actions[10] = 1;
      } else {
          actions[10] = 0;
      }
      
      /**
      for(int i=0;i<numPercepts;i++) {
          actions[i] = percepts[i]*chromosome[chromosomeIndex] + chromosome[chromosomeIndex+1];
          chromosomeIndex+=2;
      }
      int eating[] = new int[3];
      for(int i = 0; i < eating.length; i++){
          eating[i] = rand.nextInt(9);
      }
      actions[9] = percepts[eating[0]] + percepts[eating[1]] + percepts[eating[2]];
      actions[10] = rand.nextFloat();

      **/

      
      return actions;
  }
  
}
