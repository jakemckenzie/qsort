/**
 * @author Jake McKenzie
 * Implementation of the qsort algorithm using mid and 1st pivot with an accompaning analysis of it.
 */

public class qsort {
    private static String first = "init";
    private static String mid = "mid";
    private static String unsorted = "unsorted";
    private static String sorted = "sorted";
    private static String unsafe = "unsafe";
    private static String safe = "safe";
	private static Integer[] init_A;
	private static Integer[] mid_A;
	private static long startTime;
	private static int[] size = {1000,10000,100000,1000000,10000000};
	private static StringBuilder sb;
	private static String brdSize;
	private static String fill;
	public static void main(String[] args) {
		writeTop();
		for (int s : size) {

			init_A = new Integer[s];
			mid_A = new Integer[s];

			shakeUrn(init_A);
			shakeUrn(mid_A);

			writeRow(s, first, unsorted, unsafe, test_qsort_init_unsorted());
			writeRow(s, first, sorted, unsafe, test_qsort_init_sorted());
			writeRow(s, mid, unsorted, unsafe, test_qsort_mid_unsorted());
			writeRow(s, mid, sorted, unsafe, test_qsort_mid_sorted());
			
			shakeUrn(init_A);
			shakeUrn(mid_A);
			
			writeRow(s, first, unsorted, safe, test_sf_qsort_unsorted());
			writeRow(s, first, sorted, safe, testsf_qsortSorted());
			writeRow(s, mid, unsorted, safe, test_sf_qsort_mid_unsorted());
			writeRow(s, mid, sorted, safe, test_sf_qsort_mid_sorted());
		}
		
		writeBottom();

	}

	public static void qsort_init(Integer[] a) {
		qsort_init(a, 0, a.length - 1);
	}

	public static void qsort_init(Integer[] a, int l, int r) {
		if (r > l) {
			int p = piv_init(a, l, r);
			qsort_init(a, l, p - 1);
			qsort_init(a, p + 1, r);
		}
	}

	public static int piv_init(Integer[] a, int l, int r) {
		Integer p = a[l];
		int i = l + 1;
		int j = r;
		while (i < j) {
			while (p.compareTo(a[i]) > 0 && i < r) {
				i++;
				if (i > r) break;
			}
			while (p.compareTo(a[j]) < 0) j--;
			if (i >= j) break;
			if (i <= j) {
				swap(a, i, j);
				i++;
				j--;
			}
		}
		swap(a, l, j);
		return j;
	}

	private static void sf_qsort_init(Integer[] a) {
		sf_qsort_init(a, 0, a.length - 1);
	}

	private static void sf_qsort_init(Integer[] a, int l, int r) {
		while (r > l) {
			int p = piv_init(a, l, r);
			if (p - l <= r - p) {
				sf_qsort_init(a, l, p - 1);
				l = p + 1;
			} else {
				sf_qsort_init(a, p + 1, r);
				r = p - 1;
			}
		}
	}

	public static void qsort_mid(Integer[] a) {
		qsort_mid(a, 0, a.length - 1);
	}

	public static void qsort_mid(Integer[] a, int l, int r) {
		if (r > l) {
			int p = piv_mid(a, l, r);
			qsort_mid(a, l, p - 1);
			qsort_mid(a, p + 1, r);
		}
	}

	public static int piv_mid(Integer[] a, int l, int r) {
		Integer p = a[l + (r - l) >> 1];
		int m = l + (r - l) >> 1;
		int i = l + 1;
		int j = r;
		swap(a, m, l);
		while (i <= j) {
			while (p.compareTo(a[i]) > 0) {
				i++;
				if (i > r) break;
			}
			while (p.compareTo(a[j]) < 0) --j;
			if (i <= j) {
				swap(a, i, j);
				i++;
				j--;
			}
		}
		if (j < i) {
            swap(a, l, j);
			return j;
		} else if (i == j) {
			swap(a, l, i - 1);
			return i--;
		}
		return j;
	}

	private static void sf_qsort_mid(Integer[] a) {
		sf_qsort_mid(a, 0, a.length - 1);
	}
    /**
     * Sometimes this will run in ~150 seconds when there are 10^7 elements but most of the time
     * it runes in 3 to 5 seconds. I do not think this is necisarily a bug but a nature of the safe
     * qsort mid requirements. 
     */
	private static void sf_qsort_mid(Integer[] a, int l, int r) {
		while (r > l) {
			int p = piv_mid(a, l, r);
			if (p - l <= r - p) {
				sf_qsort_mid(a, l, p - 1);
				l = p + 1;
			} else {
				sf_qsort_mid(a, p + 1, r);
				r = p - 1;
			}
		}
	}
    /**
     * Never leave home without xorswap. This is my favourite way to deal with memory issues. 
     * Credit goes to george marsaglia for discovery the algorithm, from FSU (my beloved uni
     * back in stomping grounds back in florida, go seminoles!), who happens to be one of my 
     * favourite computer scientists.
     * 
     * Essentially it is a way of swapping two variables without a temporary variable. 
     * 
     * This avoids stack overflow because the stack is reduced significantly for each recursive call.
     * This method alone is called 10 times in this program, the stack contribution here is reduced by
     * a 1/3. That is very significant, thus no stack overflows. I've increased the size of N and 
     * the stack still does not overflow so it can withstand 10^8 size arrays.
     */
	private static void swap(Integer[] a, int i, int j) {
		a[i] = a[i] ^ a[j];
		a[j] = a[j] ^ a[i];
		a[i] = a[i] ^ a[j];
	}
	
	private static void writeTop() {
		sb = new StringBuilder();
		fill = "| %-9d | %-5s | %-9s | %-7s | %-7.3f |%n";
		sb.append(String.format("|===========|=======|===========|=========|=========|%n"));
		sb.append(String.format("|     N     |  piv  |  sorted?  |  safe?  |  time?  |%n"));
		System.out.print(sb.toString());
	}

	private static void writeRow(int size, String piv, String sort, String sf, double tm) {
		fill = "| %-9d | %-5s | %-9s | %-7s | %-7.3f |%n";
		String s = String.format(fill, size, piv, sort, sf, tm);
		writeBottom();
		System.out.print(s.toString());
	}

	private static void writeBottom() {
		String s = String.format("|===========|=======|===========|=========|=========|%n", brdSize);
		System.out.print(s.toString());
	}

	private static void shakeUrn(Integer[] a) {
		for (int i = 0; i < a.length; i++) a[i] = (int)XORShift128plus();
	}

	private static double test_qsort_init_unsorted() {
		startTime = System.currentTimeMillis();
		qsort_init(init_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private static double test_qsort_mid_unsorted() {

		startTime = System.currentTimeMillis();
		qsort_mid(mid_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private static double test_qsort_init_sorted() {

		startTime = System.currentTimeMillis();
		qsort_init(init_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private static double test_qsort_mid_sorted() {

		startTime = System.currentTimeMillis();
		qsort_mid(mid_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private static double test_sf_qsort_unsorted() {

		startTime = System.currentTimeMillis();
		sf_qsort_init(init_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private static double test_sf_qsort_mid_unsorted() {

		startTime = System.currentTimeMillis();
		sf_qsort_mid(mid_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private static double testsf_qsortSorted() {

		startTime = System.currentTimeMillis();
		sf_qsort_init(init_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}

	private static double test_sf_qsort_mid_sorted() {

		startTime = System.currentTimeMillis();
		sf_qsort_mid(mid_A);
		return (System.currentTimeMillis() - startTime) / 1000.0;
	}
	/**
     * I implemented these in my data structures course to achieve better randomness. In my genetic algorithm assignment it improved the runtime of
     * that program significantly by injecting better randomness into the distribution of choices the algorithm was making when choosing traits and
     * children. Here all it does is generate a better seed for the random number generator in java.
     * 
     * "The goal of practical random number generation should be to compute it in as little space,
     * and as little time, while still producing good results that satisfy statistical tests" ~  Melissa O'Neill
     * Harvey Mudd College
     * 
     * XORShift satifies all of Mellisa's requirements. She talks about XORshift at timestamp 30:22.
     * https://youtu.be/45Oet5qjlms?t=30m22s
     * https://en.wikipedia.org/wiki/Xorshift
     * https://www.javamex.com/tutorials/random_numbers/xorshift.shtml#.Wtmgwi7wZEY
     */
	private static long XORShift() {
        long x = System.currentTimeMillis();
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        return x;
    }
    private static long XORShift128plus() {
        long x = System.currentTimeMillis();
        long y = XORShift();    
        x ^= (x << 23);
        long z = x ^ y ^ (x >> 26);
        return z + x;
    }

}