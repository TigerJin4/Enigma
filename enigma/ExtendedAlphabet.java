package enigma;

import static enigma.EnigmaException.*;

/** An Extended Alphabet consisting of the any
 * Unicode characters except '(', ')', '-', '*'.
 *  @author Wenhan Jin
 */
class ExtendedAlphabet extends Alphabet {


    /** An extendedalphabet contructed from
     * CHARLIST excluding '(', ')', '-', '*'.
     * @parameter */
    ExtendedAlphabet(char[] charlist) {
        for (char c : charlist) {
            if ((c == '(') || (c == ')') || (c == '-') || (c == '*')) {
                throw error("prohibited characters");
            }
        }
        _charlist = charlist;
    }

    @Override
    int size() {
        return _charlist.length;
    }

    @Override
    boolean contains(char ch) {
        for (char c : _charlist) {
            if (ch == c) {
                return true;
            }
        }
        return false;
    }

    @Override
    char toChar(int index) {
        if (index >= size()) {
            throw error("character index out of range");
        }
        return _charlist[index];
    }

    @Override
    int toInt(char ch) {
        for (int i = 0; i < size(); i++) {
            if (_charlist[i] == ch) {
                return i;
            }
        }
        throw error("character out of range");
    }

    /** _CHARLIST is an array of char that contains the extended alphabet.  */
    private char[] _charlist;
}
