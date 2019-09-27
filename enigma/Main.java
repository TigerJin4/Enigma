package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Wenhan Jin
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (!_input.hasNext("\\*+")) {
            throw error("No configuration");
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        String line = _input.nextLine();
        while (_input.hasNextLine() || line.isEmpty()) {
            while (line.isEmpty()) {
                _output.println();
                if (_input.hasNextLine()) {
                    line = _input.nextLine();
                } else {
                    break;
                }
            }
            if (line.isEmpty()) {
                break;
            }
            if (line.startsWith("*")) {
                setUp(m, line);
            }
            if (_input.hasNextLine()) {
                String msg = _input.nextLine().replaceAll(" ", "");
                while (msg.isEmpty()) {
                    _output.println();
                    msg = _input.nextLine().replaceAll(" ", "");
                }
                while (msg.matches("[A-Za-z0-9]+")) {
                    String out = m.convert(msg);
                    printMessageLine(out);
                    if (_input.hasNextLine()) {
                        msg = _input.nextLine();
                        if (!msg.startsWith("*")) {
                            msg = msg.replaceAll(" ", "");
                        }
                    } else {
                        break;
                    }
                }
                if (msg.isEmpty() || msg.startsWith("*")) {
                    line = msg;
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _config = _config.useDelimiter("\\s+");
            String alphabet = _config.next(".+");
            if (alphabet.matches("[A-Z]-[A-Z]")) {
                char first = alphabet.charAt(0);
                char last = alphabet.charAt(alphabet.length() - 1);
                _alphabet = new CharacterRange(first, last);
            } else {
                _alphabet = new ExtendedAlphabet(alphabet.toCharArray());
            }
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            ArrayList<Rotor> rotors = new ArrayList<Rotor>();

            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next(".+");
            String description = _config.next(".+");
            String cycle = "";

            while (_config.hasNext("(\\(.+\\))*")) {
                cycle += _config.next("(\\(.+\\))*");
            }
            Permutation perm = new Permutation(cycle, _alphabet);
            String rotortype = description.substring(0, 1);
            if (rotortype.equals("M")) {
                return new MovingRotor(name, perm, description.substring(1));
            } else if (rotortype.equals("R")) {
                return new Reflector(name, perm);
            } else {
                return new FixedRotor(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SET,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine m, String set) {
        Scanner settings = new Scanner(set);
        settings = settings.useDelimiter("\\s+");
        if (settings.hasNext("\\*+")) {
            settings.next("\\*+");
            String[] rotors = new String[m.numRotors()];
            for (int i = 0; i < rotors.length; i++) {
                rotors[i] = settings.next();
            }
            m.insertRotors(rotors);
            int numMR = 0;
            for (int i = 0; i < m.rotorsl().length; i++) {
                if (m.rotorsl()[i] instanceof MovingRotor) {
                    numMR += 1;
                }
            }
            if (m.numPawls() != numMR) {
                throw error("Wrong number of moving rotors");
            }
            if (settings.hasNext(".+")) {
                String setting = settings.next();
                m.setRotors(setting);
                if (setting.length() != m.numRotors() - 1) {
                    throw error("Wrong setting length");
                }
            }
            if ((settings.hasNext("(\\(.+\\))*"))) {
                String cycles = "";
                while (settings.hasNext("(\\(.+\\))*")) {
                    cycles += settings.next("(\\(.+\\))*");
                }
                Permutation plugboard = new Permutation(cycles, _alphabet);
                m.setPlugboard(plugboard);
            }
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            if (i % 5 == 0 && i != 0) {
                result = result + msg.substring(i - 5, i) + " ";
            }
            if (msg.length() - i < 6 && i % 5 == 0) {
                result = result + msg.substring(i);
                break;
            }
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}
