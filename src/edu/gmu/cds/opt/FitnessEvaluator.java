/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.opt;

public interface FitnessEvaluator
{
    public double f(OptEntity ent);
    public OptEntity generateRandom();
    public OptEntity getPrototype();
}
