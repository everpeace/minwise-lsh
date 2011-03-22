package org.everpeace.search.lsh.galois;

/**
 * GF(2<sup>64</sup>).
 *
 * @author everpeace _at_ gmail _dot_ com
 * */
public class GaloisField64
        extends GaloisField<Long> {

    /*
     * デフォルトの原始多項式
     * x<sup>64</sup> + x<sup>4</sup> + x<sup>3</sup> + x + 1
     */
    private final static long P64 = 0x000000000000001BL;

    /**
     * longで表現された多項式を用いて位数 2^d のガロア体を生成する<br/>
     * <strong>Pの原始性はチェックしていないので注意</strong>
     *
     * @param P
     */
    public GaloisField64(Long P) {
        super(64, P);
    }

    @Override
    public Long mul(final Long a, final Long b) {
        if (a == 0 || b == 0) {
            return 0L;
        }
        long ret = 0;
        long temp_b = b;
        for (int i = 0; i < DEGREE; i++) {
            // bに x^i 乗の項が含まれていたら
            if ((temp_b & BIT[i]) != 0) {
                // a * x^iを足す
                ret ^= mul_rootpow(a, i);
            }
        }
        return ret;
    }

    /**
     * return a^p on GF(2^D)
     *
     * @param a
     * @param p
     * @return
     */
    public Long pow(final long a, final long p) {
        if (p < 0) {
            throw new UnsupportedOperationException("p must be positive. [p = " + p + " ]");
        }
        long ret = 1L;
        for (int i = 0; i < p; i++) {
            ret = mul(ret, a);
        }
        return ret;
    }

    @Override
    public Long add(Long a, Long b) {
        // addition of GF(2^D) is XOR.
        return a ^ b;
    }

    /**
     * 原始根をxとして,x*aを計算する
     *
     * @param a
     * @return x * a on GF(2^D) (x is primitive root)
     */
    public long mul_root(final Long a) {
        // 基本的にa倍は左シフトするだけ
        long ret = a << 1;
        // aに63次の項があれば桁あふれするので x^64 = P を足す(xorする);
        if ((a & BIT[DEGREE - 1]) != 0) {
            ret ^= PRIMITIVE_POLYNOMIAL;
        }
        return ret;
    }

    /**
     * 原始根をxとして,a*x^iを計算する.
     *
     * @param a
     * @param i
     * @return a*x^i on GF(2^D) (x is primitive root)
     */
    private long mul_rootpow(final long a, int i) {
        // x^64 以降は必要ないのでエラーを出す。
        if (i >= 64) {
            throw new UnsupportedOperationException("i must be less than " + 64 + "[" + i + "]");
        }
        // x^0 = 1 なので aを返す
        if (i == 0)
            return a;

        long ret = a;
        for (int j = 1; j <= i; j++) {
            ret = mul_root(ret);
        }
        return ret;
    }

    public String polynomialRepresentationOf(long a) {
        final StringBuffer ret = new StringBuffer();
        int exp = DEGREE - 1;
        boolean firstTermflag = true;
        // 63(D-1)次からtempを左にシフトしながら最上位ビットだけ見ていく
        for (long temp = a; temp != 0; temp <<= 1, exp--) {
            if ((temp & BIT[DEGREE - 1]) != 0) {// 最上位ビットだけ見て0でなかったら
                if (firstTermflag == false) {
                    ret.append("+");

                } else {
                    firstTermflag = false;
                }
                // その次数の項を付け加える
                switch (exp) {
                case 0:
                    ret.append("1");
                    break;
                case 1:
                    ret.append("x");
                    break;
                default:
                    ret.append("x^");
                    ret.append(exp);
                    break;
                }
            }
        }
        return ret.toString();
    }

    public String getPrimitiveString() {
        return polynomialToString(this.PRIMITIVE_POLYNOMIAL);
    }

    private String polynomialToString(final long P) {
        final StringBuffer ret = new StringBuffer();
        // x^64は常につける。
        ret.append("x^" + DEGREE);
        int exp = DEGREE - 1;
        // 63次からtempを左にシフトしながら最上位ビットだけ見ていく
        for (long temp = P; temp != 0; temp <<= 1, exp--) {
            if ((temp & BIT[DEGREE - 1]) != 0) {// 最上位ビットだけ見て0でなかったら
                // その次数の項を付け加える
                switch (exp) {
                case 0:
                    ret.append("+1");
                    break;
                case 1:
                    ret.append("+x");
                    break;
                default:
                    ret.append("+x^");
                    ret.append(exp);
                    break;
                }
            }
        }
        return ret.toString();
    }

    /**
     * 各ビットだけ見るためのマスク
     */
    private static final long[] BIT =
            { 1L << 0, 1L << 1, 1L << 2, 1L << 3, 1L << 4, 1L << 5, 1L << 6, 1L << 7, 1L << 8,
                    1L << 9, 1L << 10, 1L << 11, 1L << 12, 1L << 13, 1L << 14, 1L << 15,
                    1L << 16, 1L << 17, 1L << 18, 1L << 19, 1L << 20, 1L << 21, 1L << 22,
                    1L << 23, 1L << 24, 1L << 25, 1L << 26, 1L << 27, 1L << 28, 1L << 29,
                    1L << 30, 1L << 31, 1L << 32, 1L << 33, 1L << 34, 1L << 35, 1L << 36,
                    1L << 37, 1L << 38, 1L << 39, 1L << 40, 1L << 41, 1L << 42, 1L << 43,
                    1L << 44, 1L << 45, 1L << 46, 1L << 47, 1L << 48, 1L << 49, 1L << 50,
                    1L << 51, 1L << 52, 1L << 53, 1L << 54, 1L << 55, 1L << 56, 1L << 57,
                    1L << 58, 1L << 59, 1L << 60, 1L << 61, 1L << 62, 1L << 63 };

    public static GaloisField64 DEFAULT_GALOIS_FIELD64 = new GaloisField64(P64);
}