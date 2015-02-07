import java.util.Scanner;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scn = new Scanner( System.in );
		System.out.println("Skolko danykiv?");
		int x = scn.nextInt();
		System.out.println("Skolko pechenek?");
		int y = scn.nextInt();
		System.out.println("Nam nuzhno " + x*y*100 + " pechiva");
	}

}
