package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Wenhan Jin
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        _cycleslist = cyclesplit(cycles);
    }

    /** Cyclesplit takes a string that contains "(" and ")"s and split them
     *  into an array of strings of with parentheses.
     * @param S as a string
     * @return String[]
     */
    private String[] cyclesplit(String S) {
        S = S.replaceAll(" ", "");
        String[] a = S.split("\\)");
        String[] result = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + ")";
        }
        return result;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += cycle;
    }


    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int cIn = wrap(p);
        char pIn = _alphabet.toChar(cIn);
        char pOut = permute(pIn);
        int cOut = _alphabet.toInt(pOut);
        return cOut;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int cIn = wrap(c);
        char pIn = _alphabet.toChar(cIn);
        char pOut = invert(pIn);
        int cOut = _alphabet.toInt(pOut);
        return cOut;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        String pstring = Character.toString(p);
        char output = p;
        for (String cycle : _cycleslist) {
            if (cycle.contains(pstring)) {
                if (cycle.indexOf(p) == cycle.length() - 2) {
                    output = cycle.charAt(1);
                } else {
                    output = cycle.charAt(cycle.indexOf(p) + 1);
                }
            }
        }
        return output;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        String cstring = Character.toString(c);
        char output = c;
        for (String cycle : _cycleslist) {
            if (cycle.contains(cstring)) {
                if (cycle.indexOf(c) == 1) {
                    output = cycle.charAt(cycle.length() - 2);
                } else {
                    output = cycle.charAt(cycle.indexOf(c) - 1);
                }
            }
        }
        return output;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycles of this permutation. */
    private String _cycles;
    /** Array of strings that contains individual cycle.  */
    private String[] _cycleslist;
}
