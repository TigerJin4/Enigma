package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Wenhan Jin
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        rotorsL = new Rotor[numRotors()];
        HashMap<String, Rotor> rotorsMap = new HashMap<String, Rotor>();
        for (Rotor r : _allRotors) {
            rotorsMap.put(r.name().toUpperCase(), r);
        }
        for (int i = 0; i < rotors.length; i++) {
            String key = rotors[i].toUpperCase();
            if (rotorsMap.containsKey(key)) {
                rotorsL[i] = rotorsMap.get(key);
            } else {
                throw error("Invalid rotor name");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < rotorsL.length; i++) {
            rotorsL[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] checkadv = new boolean[rotorsL.length];
        for (int i = 0; i < rotorsL.length; i++) {
            checkadv[i] = rotorsL[i].atNotch();
        }
        rotorsL[rotorsL.length - 1].advance();
        ArrayList<Rotor> advRotor = new ArrayList<Rotor>();
        for (int i = 1; i < rotorsL.length - 1; i++) {
            if ((rotorsL[i - 1].rotates()
                    && (checkadv[i])) || (checkadv[i + 1])) {
                advRotor.add(rotorsL[i]);
            }
        }
        for (Rotor r : advRotor) {
            r.advance();
        }
        int cOut = c;
        if (_plugboard != null) {
            cOut = _plugboard.permute(cOut);
        }
        for (int j = rotorsL.length - 1; j > -1; j--) {
            cOut = rotorsL[j].convertForward(cOut);
        }
        for (int k = 1; k < rotorsL.length; k++) {
            cOut = rotorsL[k].convertBackward(cOut);
        }
        if (_plugboard != null) {
            cOut = _plugboard.permute(cOut);
        }
        return cOut;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "");
        msg = msg.toUpperCase();
        String[] letterarray = msg.split("");
        String[] outputarray = new String [letterarray.length];
        String output = "";
        int[] letterint = new int[letterarray.length];
        for (int i = 0; i < letterint.length; i++) {
            letterint[i] = _alphabet.toInt(letterarray[i].charAt(0));
        }
        char[] temp = new char [letterint.length];
        for (int i = 0; i < letterarray.length; i++) {
            letterint[i] = convert(letterint[i]);
            temp[i] = _alphabet.toChar(letterint[i]);
            outputarray[i] = Character.toString(temp[i]);
        }
        for (int k = 0; k < outputarray.length; k++) {
            output += outputarray[k];
        }
        return output;
    }

    /**
     *
     * @return  an array of rotors for a configured machine
     */
    Rotor[] rotorsl() {
        return rotorsL;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of my selected rotors. */
    private int _numRotors;

    /** Number of pawls. */
    private int _numPawls;

    /** A collection of all rotors in the machine. */
    private Collection<Rotor> _allRotors;

    /** Optional plugboard permutation for the machine. */
    private Permutation _plugboard;

    /** An array of my selected rotors. */
    private Rotor[] rotorsL;
}
