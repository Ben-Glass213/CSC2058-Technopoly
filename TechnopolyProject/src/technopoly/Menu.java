package technopoly;

import java.util.Scanner;

public class Menu {
	private String options[];
	private String header;
	private Scanner input;
	
	public Menu(String options[], String header) {
		this.header = header;
		this.options = options;
		this.input = new Scanner(System.in);
	}
	
	public int getChoice() {
		// DISPLAYS HEADER AND OPTIONS
		System.out.println(header);
		
		for (int i = 0; i < options.length; i++) {
			System.out.println((i+1) + " - " + options[i]);
		}
		boolean valid = false;
		int choice = 0;
		
		// GETS USER CHOICE
		while(valid != true) {
			System.out.print("Enter choice: ");
			try {
				choice = input.nextInt();
				if (choice <= 0 || choice > options.length) {
					System.out.println("Must be a value between 1 and " + options.length);
				}
				else {
					valid = true;
				}
			}
			catch(Exception exception) {
				System.out.println("Invalid input");
				input.nextLine();
			}
		}
		return choice;
	}
}
