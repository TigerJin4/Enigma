package enigma;

import org.junit.Test;
import ucb.junit.textui;

import java.util.ArrayList;
import java.util.Scanner;

/** The suite of all JUnit tests for the enigma package.
 *  @author Wenhan Jin
 */
public class UnitTest {

    @Test
    public void machine1() {
        Alphabet upper = new CharacterRange('A', 'Z');
        Permutation p1 = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", upper);
        Permutation p2 = new Permutation(
                "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", upper);
        Permutation p3 = new Permutation(
                "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", upper);
        Permutation p4 = new Permutation(
                "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", upper);
        Permutation p5 = new Permutation("(AE) (BN) (CK) (DQ) "
                + "(FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", upper);
        Rotor I = new MovingRotor("I", p1, "Q");
        Rotor beta = new FixedRotor("Beta", p2);
        Rotor iii = new MovingRotor("III", p3, "V");
        Rotor iv = new MovingRotor("IV", p4, "J");
        Rotor B = new Reflector("B", p5);
        ArrayList<Rotor> L = new ArrayList<>();
        L.add(B);
        L.add(beta);
        L.add(iii);
        L.add(iv);
        L.add(I);
        Machine M = new Machine(upper, 5, 3, L);
        M.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)", upper));
        String[] sr = {"B", "Beta", "III", "IV", "I"};
        M.insertRotors(sr);
        M.setRotors("AXLE");
        System.out.println(M.convert(upper.toInt('B')));
        System.out.println(M.convert("Y"));
        System.out.println(M.convert("QVPQS OKOIL PUBKJ ZPISF XDW"));
    }

    @Test
    public void machine2() {
        Alphabet ac = new CharacterRange('A', 'C');

        Permutation p1 = new Permutation("", ac);
        Permutation p2 = new Permutation("", ac);
        Permutation p3 = new Permutation("", ac);
        Permutation p4 = new Permutation("", ac);
        Rotor I = new MovingRotor("I", p1, "C");
        Rotor beta = new Reflector("Beta", p2);
        Rotor iii = new MovingRotor("III", p3, "C");
        Rotor iv = new MovingRotor("IV", p4, "C");
        ArrayList<Rotor> L = new ArrayList<>();
        L.add(beta);
        L.add(iii);
        L.add(iv);
        L.add(I);
        Machine M = new Machine(ac, 4, 3, L);
        String[] sr = {"Beta", "III", "IV", "I"};
        M.insertRotors(sr);
        M.setRotors("AAAA");
        M.convert("AAAAA AAAAA AAAAA AAAA");
        System.out.print("string".substring(4, 6));
    }

    @Test
    public void maintest() {
        String s1 = "(AQ)(EP)";
        System.out.print(s1.matches("(\\([A-Z]+\\))*"));
    }

    @Test
    public void test() {
        String s = "(TD) (KC) (JZ)";
        s = s.replaceAll(" ", "");
        System.out.println(s);
        String[] a = s.split("\\)");
        String[] c = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + ")";
        }
        for (String b : c) {
            System.out.println(b);
        }

        String d = "(TD) (KC) (JZ)";
        String[] e = d.split(" ");
        for (String f : e) {
            System.out.println(f);
        }

    }

    @Test
    public void testreset() {
        String s = "d* B BETA I II III AAAA\n"
                + "*LBDA AMTAZ";
        Scanner scanner = new Scanner(s);
        System.out.print(scanner.next());
        if (scanner.hasNext("\\*+")) {
            scanner.reset();
        }
        System.out.print(scanner.next());
        System.out.print(scanner.nextLine());

        String d = "NHQOH LMIJI KLQHQ MNIK\n"
                + "\n";
        System.out.print(d);
        Scanner scanner1 = new Scanner(d);
        scanner1.next();
        System.out.print(scanner1.hasNextLine());
        System.out.print(scanner1.nextLine());
        System.out.print(scanner1.next("\\*+"));
        System.out.print(scanner1.hasNext("\\*+"));
    }

    @Test
    public void testmatch() {
        String msg = "FROM his shoulder Hiawatha";
        System.out.print(msg.matches("([A-Za-z0-9]+)(\\s+)"));
        Scanner a = new Scanner(msg);
        a = a.useDelimiter("\\s+");
        System.out.print(a.nextLine());
    }
    @Test
    public void testnext() {
        String d = "NHQOH LMIJI KLQHQ MNIK\n"
                + "\n";
        Scanner scanner = new Scanner(d);
        scanner.nextLine();
        System.out.print(scanner.hasNextLine());
    }

    @Test
    public void maintest1() {
        Main.main("testing/correct/default.conf",
                "testing/correct/trivial.inp");
    }

    @Test
    public void maintest2() {
        Main.main("testing/correct/default.conf",
                "testing/correct/trivial1.inp");
    }

    @Test
    public void maintest5() {
        Main.main("testing/correct/simple1.conf",
                "enigma/simple1.inp");
    }
    @Test
    public void maintest6() {
        Main.main("testing/correct/reduced.conf",
                "enigma/reduced.inp");
    }

    @Test
    public void maintest7() {
        Main.main("testing/correct/default.conf",
                "enigma/riptide.inp");
    }
    @Test
    public void maintest8() {
        Main.main("testing/correct/newchars.conf",
                "enigma/newchars.inp");
    }
    @Test
    public void maintest9() {
        Main.main("testing/correct/permuted.conf",
                "enigma/permuted.inp");
    }
    @Test
    public void maintest10() {
        Main.main("testing/correct/default.conf",
                "enigma/gitgood.inp");
    }
    @Test
    public void maintest11() {
        Main.main("testing/correct/default.conf",
                "enigma/multi1.inp");
    }

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class);
    }

}


