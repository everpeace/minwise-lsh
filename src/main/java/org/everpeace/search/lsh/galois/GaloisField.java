package org.everpeace.search.lsh.galois;

/**
 * Galois field, GF(2<sup>DEGREE</sup>)
 *
 * @author everpeace _at_ gmail _dot_ com
 *
 * @param <T>
 *            representaiton data type of element in the GF.
 */
public abstract class GaloisField<T extends Number> {
	public final int DEGREE;
	public final T PRIMITIVE_POLYNOMIAL;

	/**
	 * @param dEGREE
	 * @param pRIMITIVEPOLYNOMIAL
	 */
	public GaloisField(int dEGREE, T pRIMITIVEPOLYNOMIAL) {
		super();
		DEGREE = dEGREE;
		PRIMITIVE_POLYNOMIAL = pRIMITIVEPOLYNOMIAL;
	}

	/**
	 * Addition on this GF(2^<sup>DEGREE</sup>
	 *
	 * @param a
	 *            an element in this GF(2^<sup>DEGREE</sup>
	 * @param b
	 *            an element in this GF(2^<sup>DEGREE</sup>
	 * @return a+b
	 */
	public abstract T add(final T a, final T b);

	/**
	 * Multiplication on this GF(2^<sup>DEGREE</sup>
	 *
	 * @param a
	 *            an element in this GF(2^<sup>DEGREE</sup>
	 * @param b
	 *            an element in this GF(2^<sup>DEGREE</sup>
	 * @return a*b
	 */
	public abstract T mul(final T a, final T b);

}
