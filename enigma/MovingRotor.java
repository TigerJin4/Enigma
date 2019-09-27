package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Wenhan Jin
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCH1.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notch1) {
        super(name, perm);
        this.permutation = perm;
        this.notches = notch1;
        this.notch = notches.toCharArray();
    }

    @Override
    boolean atNotch() {
        for (char i : notch) {
            if (i == alphabet().toChar(this.setting())) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        super.set(permutation.wrap(super.setting() + 1));
    }

    /** NOTCHES is a string that equals to the input notch1. */
    private String notches;
    /** PERMUTATION is a string that equals to the input perm. */
    private Permutation permutation;
    /** NOTCH is an array of chars that returned by toCharArray of notches. */
    private char[] notch;

}
