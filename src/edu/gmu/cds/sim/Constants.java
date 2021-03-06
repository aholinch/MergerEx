/**************************
 * MergerEx - see LICENSE
 **************************/
package edu.gmu.cds.sim;

/**
 * This set of constants simplifies the conversion from physical units
 * to simulation units and vice versa.
 *
 * @aholinch
 */
public class Constants 
{
    public static final double G = 6.673e-11;
    public static final double solarMassToKg = 1.98892e30;
    public static final double metersPerMpc = 3.08568025e22;
    public static final double kmPerMpc = 3.08568025e19; // or m per kpc
    public static final double degToRad = Math.PI/180.0d;

    public static final double MU_kg = 1.0e11*solarMassToKg;
    public static final double DU_m = 15.0*kmPerMpc; // meters in a kpc
    public static final double TU_s = Math.sqrt(DU_m*DU_m*DU_m/(G*MU_kg));

    public static final double MU_sm = 1.0e11;
    public static final double DU_mpc = 15.0d/1000.0d;

    public static final double VEL_KMS = DU_m/1000.0d/TU_s;
    public static final double A_MSS = DU_m/TU_s/TU_s;

    public static final double A0_MKS = 1e-10;
    public static final double A0 = A0_MKS/A_MSS;

    public static final double DEFAULT_EPS = Math.sqrt(0.1d);
}
