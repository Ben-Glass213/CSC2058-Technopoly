package technopoly;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TechnopolyApp {

	private static Square[] board = new Square[40];
	private static Card[] chance = new Card[8];
	private static Card[] community = new Card[8];
	private static Company[] compInfo = new Company[22];
	private static VPNISP[] vpnIspInfo = new VPNISP[2];
	private static InternetDomain[] internetDInfo = new InternetDomain[4];
	private static Player[] players;
	private static Dice die;
	private static int turn = 0;
	private static int numOfDevelops = 0;
	private static boolean finished = false;

	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to Technopoly! \n");
		String header = "Menu";
		String[] options = { "Start Game", "Load Save", "Rules", "Exit" };
		Menu menu = new Menu(options, header);

		int quitOption = options.length;
		int choice;

		do {
			choice = menu.getChoice();
			if (choice == quitOption) {
				exit();
				System.out.println("Goodbye!");
				finished = true;
			} else if (choice == 1) {
				beginGame();
			} else if (choice == 2) {
				loadGame();
			} else if (choice == 3) {
				rules();
			}
		} while (!finished);

	}

	private static void beginGame() throws Exception {
		if (players == null) {
			addGameData();
		}

		System.out.println();
		if (players == null) {
			registerPlayers();
		}

		// Menu Options:
		String[] options = { "Roll Dice", "Trade", "Develop", "View Player Statistics", "Next Turn", "Save Game",
				"Exit" };
		// Menu creation:
		String header = "Ok, Here are your game options: ";
		Menu menu = new Menu(options, header);

		int quitOption = options.length;
		int choice;
		finished = false;

		do {
			System.out.println("\nPlayer: " + players[turn].getName());
			choice = menu.getChoice();
			if (choice == quitOption) {
				exit();
				System.out.println("Goodbye!");
				finished = true;
			} else {
				processOption(choice);

			}
		} while (!finished);
	}

	private static void processOption(int choice) throws Exception {
		switch (choice) {
		case 1:
			if (players.length == 0) {
				System.out.println("Please register players first");
			}
			rollDice();
			System.out.println();
			break;

		case 2:
			trade();
			System.out.println();
			break;
		case 3:
			develop();
			System.out.println();
			break;
		case 4:
			playerStats();
			System.out.println();
			break;
		case 5:
			nextTurn();
			System.out.println();
			break;
		case 6:
			players[turn].setTurn(true);
			saveGame();
			System.out.println();
			break;
		case 7:
			exit();
			System.out.println();
			break;

		default:
			System.out.println("Input Error");
			break;
		}

	}

	private static void registerPlayers() throws Exception {
		Scanner input = new Scanner(System.in);
		int playerCount = 0;
		Player[] temp = new Player[8];
		String name = "";
		while (true) {
			System.out.println("Enter player " + (playerCount + 1) + "'s name (Enter '*' to stop): ");
			boolean valid = false;
			while (!valid) {
				name = input.nextLine();
				if (name.equals("")) {
					System.out.println(
							"The name entered was blank. Please enter player " + (playerCount + 1) + "'s name again: ");
				} else {
					valid = true;
				}
			}
			if (name.equals("*")) {
				if (playerCount >= 2) {
					break;
				} else {
					System.out.println("At least 2 players must be registered");
				}
			} else {

				Player player = new Player(name);
				temp[playerCount] = player;
				playerCount++;
			}
			if (playerCount == 8) {
				System.out.println("Player limit reached");
				break;
			}
		}

		players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			players[i] = temp[i];
		}

		// input.close();
	}

	private static void rollDice() throws Exception {

		if (players.length == 0) {
			System.out.println("Please register players first");
		}

		String name = players[turn].getName();
		System.out.println("\nRolling dice for Player: " + name);

		die = new Dice();
		die.rollDice();
		int score = die.getRollScore();

		System.out.println("First dice: " + die.getResult1());
		System.out.println("Second dice: " + die.getResult2());
		System.out.println("Roll result: " + score);

		if (die.getResult1() == die.getResult2()) {

			System.out.println("\nYou rolled doubles!");

			if (players[turn].getServerCrash() == true) {
				System.out.println("The server crash is over!");
				players[turn].setServerCrash(false);
			}

			else {
				System.out.println("That means you get to roll again\n");
				die = new Dice();
				die.rollDice();
				int secondScore = die.getRollScore();

				System.out.println("First dice: " + die.getResult1());
				System.out.println("Second dice: " + die.getResult2());
				System.out.println("Roll result: " + secondScore);

				score = score + secondScore;

				if (die.getResult1() == die.getResult2()) {

					System.out.println("You rolled doubles again! Oh no, this caused the server to crash!");
					serverCrash();

				}

			}
		}

		if (players[turn].getServerCrash() == false) {

			int boardMove = score + players[turn].getSquareID();
			players[turn].setSquareID(boardMove);

			if (boardMove >= 40) {
				System.out.println("\nYou passed pass go and gained 50 developers!");
				int newDevelopers = players[turn].getDevelopers() + 50;
				players[turn].setDevelopers(newDevelopers);
				System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
				boardMove = boardMove - 40;
				players[turn].setSquareID(boardMove);
			}

			Square currentSqu = board[boardMove];

			if (currentSqu.getSquareType() == Type.COMPANY) {
				Company currentCom = (Company) currentSqu;
				System.out.println("\nThat lands you on Square: " + currentSqu.getSquareName() + ", Company Type: "
						+ currentCom.getCompanyType());
			} else {
				System.out.println("\nThat lands you on Square: " + currentSqu.getSquareName());
			}
			players[turn].setCurrentSquare(currentSqu);
			if (currentSqu.getSquareType() == Type.COMPANY || currentSqu.getSquareType() == Type.VPN
					|| currentSqu.getSquareType() == Type.INTERNETDOMAINS || currentSqu.getSquareType() == Type.ISP) {
				if (((OwnableSquare) currentSqu).getOwner() == null) {
					buySquare(boardMove, name);
				} else if (((OwnableSquare) currentSqu).getOwner().getPlayerId() == players[turn].getPlayerId()) {
					System.out.println("You already own this square");
				} else {
					payRent(boardMove);
				}
			} else if (currentSqu.getSquareType() == Type.CHANCE || currentSqu.getSquareType() == Type.COMCHEST) {
				drawCard(currentSqu, name);

			} else if (currentSqu.getSquareType() == Type.COFFEEBREAK
					|| currentSqu.getSquareType() == Type.CHECKSERVER) {
				System.out.println("Nothing happens");
			} else if (currentSqu.getSquareType() == Type.LOSEDEVELOPERS) {
				loseDevelopers(currentSqu);
			} else if (currentSqu.getSquareType() == Type.SERVERCRASH) {
				serverCrash();
			} else if (currentSqu.getSquareType() == Type.GO) {
				System.out.println("You reached pass go!");
				System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
			}

		} else if ((players[turn].getServerCrash() == true) && (die.getResult1() != die.getResult2())) {
			int rolls = players[turn].getRolls();
			players[turn].setRolls(rolls + 1);
			if (rolls >= 3) {
				Scanner input = new Scanner(System.in);
				String response = "";
				if (players[turn].getDevelopers() > 50) {
					System.out.println("You have " + players[turn].getDevelopers() + " developers available");
					System.out.println("\nWould you like to give up 50 developers to get out of jail? (Y/N): ");
					response = input.nextLine();
					if (response.equals("Y") || response.equals("y")) {
						int newDev = players[turn].getDevelopers() - 50;
						players[turn].setDevelopers(newDev);
						System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
						players[turn].setServerCrash(false);
						System.out.println("Your server has been rebooted!");
						players[turn].setRolls(0);
					} else {
						System.out.println("Ok, your server will remain crashed");
						players[turn].setRolls(0);
					}
				}

			} else {
				System.out.println("\nSorry, you didn't roll a double so your server is still crashed");
			}

		}

		nextTurn();
	}

	private static void loseDevelopers(Square currentSqu) {
		int currentDevelopers = players[turn].getDevelopers();

		if (currentSqu.getSquareName() == "Developers laid off") {
			int loss = currentSqu.getSquareCost();
			int loss = currentSqu.getSquareCost();
			System.out.println("Oh no! " + loss + " Developers have been laid off!");
			int newDevelopers = currentDevelopers - loss;
			players[turn].setDevelopers(newDevelopers);
			System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
		} else if (currentSqu.getSquareName() == "Developer Strike!") {
			int loss = currentSqu.getSquareCost();
			System.out.println("Oh no! " + loss + " developers are on strike!");
			int newDevelopers = currentDevelopers - loss;
			players[turn].setDevelopers(newDevelopers);
			System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
		}
	}

	private static void payRent(int boardMove) {
		int rent = 0;
		Player owner = ((OwnableSquare) board[boardMove]).getOwner();
		if (board[boardMove].getSquareType() == Type.COMPANY) {
			rent = ((Company) board[boardMove]).getRentCost();
		} else if (board[boardMove].getSquareType() == Type.ISP) {
			int numOwned = 0;
			for (int i = 0; i < owner.getOwnedSquares().size(); i++) {
				if (owner.getOwnedSquares().get(i).getSquareType() == Type.ISP) {
					numOwned++;
				}
			}
			switch (numOwned) {
			case 1:
				rent = ((VPNISP) board[boardMove]).getRent1();
				break;
			case 2:
				rent = ((VPNISP) board[boardMove]).getRent2();
				break;
			}
		} else if (board[boardMove].getSquareType() == Type.INTERNETDOMAINS) {
			int numOwned = 0;
			for (int i = 0; i < owner.getOwnedSquares().size(); i++) {
				if (owner.getOwnedSquares().get(i).getSquareType() == Type.INTERNETDOMAINS) {
					numOwned++;
				}
			}
			switch (numOwned) {
			case 1:
				rent = ((InternetDomain) board[boardMove]).getRent1();
				break;
			case 2:
				rent = ((InternetDomain) board[boardMove]).getRent2();
				break;
			case 3:
				rent = ((InternetDomain) board[boardMove]).getRent3();
				break;
			case 4:
				rent = ((InternetDomain) board[boardMove]).getRent4();
				break;
			}
		}

		players[turn].setDevelopers(players[turn].getDevelopers() - rent);
		owner.setDevelopers(owner.getDevelopers() + rent);

		System.out.println(owner.getName() + " owns this square! " + players[turn].getName() + ", you lost " + rent
				+ " developers due to rent!");
		System.out.println(owner.getName() + ", you gained " + rent + " developers!");
	}

	private static void buySquare(int boardMove, String currentPlayer) throws Exception {
		if (players.length == 0) {
			System.out.println("Please register players first");
		}
		String name = currentPlayer;
		Square currentSqu = board[boardMove];
		String cSquName = currentSqu.getSquareName();
		Scanner input = new Scanner(System.in);
		boolean validInput = false;
		String ans = "";
		System.out.println("The cost of this square is: " + currentSqu.getSquareCost() + " developers");
		System.out.println("You have " + players[turn].getDevelopers() + " developers avaliable");
		if (players[turn].getDevelopers() > currentSqu.getSquareCost()) {
			while (validInput != true) {
				System.out.println("\nWould you like to allocate developers to this square (Y/N): ");
				try {
					ans = input.nextLine();
					if (ans.equals("Y") || ans.equals("y")) {
						players[turn].addOwnedSquare((OwnableSquare) board[boardMove]);
						players[turn].setDevelopers(players[turn].getDevelopers() - board[boardMove].getSquareCost());
						((OwnableSquare) board[boardMove]).setOwner(players[turn]);
						System.out
								.println("Congratulations " + name + ", you own: " + board[boardMove].getSquareName());
						System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
						validInput = true;
					} else if (ans.equals("N") || ans.equals("n")) {
						System.out.println("Ok, no developers have been allocated to " + cSquName);
						validInput = true;
					}
				} catch (Exception exception) {
					System.out.println("Invalid input");
				}
			}
		} else {
			System.out
					.println("Sorry " + name + ", You currently don't have enough developers to purchase " + cSquName);
			System.out.println(
					"\nWould you like to sell any of your current owned squares to get more developers? (Y/N)");
			Scanner input2 = new Scanner(System.in);
			String answer2 = "";
			boolean vInput = false;
			while (vInput != true) {
				try {
					answer2 = input2.nextLine();
					if (answer2.equals("Y")) {
						sell();
						vInput = true;
					} else if (answer2.equals("N")) {
						vInput = true;
					}

				} catch (Exception exception) {
					System.out.println("Invalid input");
				}

			}
		}
	}

	private static void drawCard(Square currentS, String currentPlayer) throws Exception {
		if (players.length == 0) {
			System.out.println("Please register players first");
		}

		System.out.println("Drawing a card...");
		Square currentSqu = currentS;

		int randomNum = (int) (Math.random() * (7 - 1 + 1) + 1);

		if (currentSqu.getSquareType() == Type.CHANCE) {

			System.out.println("\nYou got the card: " + chance[randomNum].getDescript());

			if (chance[randomNum].getGain() > 0) {
				System.out.println("You gained " + chance[randomNum].getGain() + " developers!");
				int currentDevelopers = players[turn].getDevelopers();
				int newDevelopers = currentDevelopers + chance[randomNum].getGain();
				players[turn].setDevelopers(newDevelopers);
				System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
			}

			if (chance[randomNum].getLoss() > 0) {
				System.out.println("You lost " + chance[randomNum].getLoss() + " developers!");
				int currentDevelopers = players[turn].getDevelopers();
				int newDevelopers = currentDevelopers - chance[randomNum].getLoss();
				players[turn].setDevelopers(newDevelopers);
				System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
			}

			if (chance[randomNum].getCardId() == 1) { // Advance to go card (move to pass go)
				System.out.println("Moving to Pass Go...");
				players[turn].setCurrentSquare(board[0]);
			}

			if (chance[randomNum].getCardId() == 2) { // Advance to closest Internet Domain card
				System.out.println("Advancing to closest Internet Domain...");
				int currentSquID = currentSqu.getSquareId();
				if (currentSquID < 5) {
					players[turn].setCurrentSquare(board[5]);
				} else if (currentSquID >= 5 && currentSquID < 15) {
					players[turn].setCurrentSquare(board[15]);
				} else if (currentSquID >= 15 && currentSquID < 25) {
					players[turn].setCurrentSquare(board[25]);
				} else {
					players[turn].setCurrentSquare(board[35]);
				}
				Square cSquare = players[turn].getCurrentSquare();
				int cSquareID = cSquare.getSquareId() - 1;
				String cSquareName = cSquare.getSquareName();
				Type cSquareType = cSquare.getSquareType();
				System.out.println(
						"\nYour new position on the board is square: " + cSquareName + " Type: " + cSquareType);
				buySquare(cSquareID, players[turn].getName());
			}

			if (chance[randomNum].getCardId() == 3) { // Advance to closest ISP/VPN card
				System.out.println("Advancing to closest ISP/VPN...");
				int currentSquID = currentSqu.getSquareId();
				if (currentSquID >= 12 && currentSquID < 28) {
					players[turn].setCurrentSquare(board[28]);
				} else {
					players[turn].setCurrentSquare(board[12]);
				}
				Square cSquare = players[turn].getCurrentSquare();
				int cSquareID = cSquare.getSquareId() - 1;
				String cSquareName = cSquare.getSquareName();
				Type cSquareType = cSquare.getSquareType();
				System.out.println(
						"\nYour new position on the board is square: " + cSquareName + " Type: " + cSquareType);
				buySquare(cSquareID, players[turn].getName());
			}

			if (chance[randomNum].getCardId() == 5) { // Server repair card
				System.out.println("You can use this card to reboot the server if it crashes");
				players[turn].setRebootCard(true);
			}

			if (chance[randomNum].getCardId() == 6) { // Player moves back 3 squares card
				System.out.println("Moving back three squares...");
				int currentSID = currentSqu.getSquareId();
				int newID = currentSID - 4; // (take away 4 bc ID starts from 1 but square array starts from 0)
				players[turn].setCurrentSquare(board[newID]);
				Square cSquare = players[turn].getCurrentSquare();
				int cSquareID = cSquare.getSquareId() - 1;
				String cSquareName = cSquare.getSquareName();
				Type cSquareType = cSquare.getSquareType();
				System.out.println(
						"\nYour new position on the board is square: " + cSquareName + " Type: " + cSquareType);

				if (cSquare.getSquareType() == Type.COMPANY || cSquare.getSquareType() == Type.INTERNETDOMAINS
						|| cSquare.getSquareType() == Type.ISP || cSquare.getSquareType() == Type.VPN) {
					buySquare(cSquareID, players[turn].getName());
				} else if (currentSqu.getSquareName() == "Developer Strike!") {
					loseDevelopers(currentSqu);
				} else if (cSquare.getSquareType() == Type.COMCHEST || cSquare.getSquareType() == Type.CHANCE) {
					drawCard(cSquare, players[turn].getName());
				}
			}

			if (chance[randomNum].getCardId() == 7) { // Server crash card
				serverCrash();
			}

		} else if (currentSqu.getSquareType() == Type.COMCHEST) {

			System.out.println("You got the card: " + community[randomNum].getDescript());

			if (community[randomNum].getGain() > 0) {
				System.out.println("You gained " + community[randomNum].getGain() + " developers!");
				int currentDevelopers = players[turn].getDevelopers();
				int newDevelopers = currentDevelopers + chance[randomNum].getGain();
				players[turn].setDevelopers(newDevelopers);
				System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
			}

			if (community[randomNum].getLoss() > 0) {
				System.out.println("You lost " + community[randomNum].getLoss() + " developers!");
				int currentDevelopers = players[turn].getDevelopers();
				int newDevelopers = currentDevelopers - chance[randomNum].getLoss();
				players[turn].setDevelopers(newDevelopers);
				System.out.println("You now have " + players[turn].getDevelopers() + " developers left");
			}
			if (community[randomNum].getCardId() == 9) { // Advance to go card (move to pass go)
				System.out.println("Moving to Pass Go...");
				players[turn].setCurrentSquare(board[0]);
			}

			if (community[randomNum].getCardId() == 13) { // Server repair card
				System.out.println("You can use this card to reboot the server if it crashes");
				players[turn].setRebootCard(true);
			}

			if (community[randomNum].getCardId() == 16) { // Server crash card
				serverCrash();
			}

		}

	}

	private static void serverCrash() {
		System.out.println("Oh no! The server has crashed!");
		Scanner input = new Scanner(System.in);
		String response = "";
		boolean inputValid = false;
		if (players[turn].getRebootCard() == true) {
			while (inputValid != true) {
				System.out.println("You have a server reboot card, would you like to use it now? (Y/N)");
				try {
					response = input.nextLine();

					if (response.equals("Y") || response.equals("y")) {
						System.out.println("The server has been rebooted");
						players[turn].setRebootCard(false);
						inputValid = true;

					} else if (response.equals("N") || response.equals("n")) {
						System.out.println("Ok, server reboot not used");
						players[turn].setServerCrash(true);
						players[turn].setCurrentSquare(board[10]);
						inputValid = true;
					} else {
						System.out.println("Invalid input, try again: ");
					}

				} catch (Exception exception) {
					System.out.println("Invalid input");
				}
			}

		} else {
			players[turn].setServerCrash(true);
			players[turn].setCurrentSquare(board[10]);

		}
	}

	private static void trade() {
		boolean traders = false;
		if (players[turn].getDevelopers() > 0) {
			for (int i = 0; i < players.length; i++) {
				if (players[i].getOwnedSquares().size() > 0 && players[i] != players[turn]) {
					traders = true;
					break;
				}
			}
			if (players.length == 0) {
				System.out.println("Please register players first");
			} else if (traders == true) {
				String header = "\nSelect the player you wish to trade with: ";
				Player currentPlayer = players[turn];
				ArrayList<Player> otherPlayers = new ArrayList<Player>();

				for (int i = 0; i < players.length; i++) {
					if (players[i] != currentPlayer && players[i].getOwnedSquares().size() > 0) {
						otherPlayers.add(players[i]);
					}
				}

				String[] options = new String[otherPlayers.size()];

				for (int i = 0; i < otherPlayers.size(); i++) {
					options[i] = otherPlayers.get(i).getName();
				}
				Menu playerSelect = new Menu(options, header);
				int choice = playerSelect.getChoice();

				Player ownerPlayer = otherPlayers.get(choice - 1);
				ArrayList<OwnableSquare> companies = ownerPlayer.getOwnedSquares();
				String[] companyOptions = new String[companies.size()];
				String companyHeader = "\nSelect the company you wish to make an offer for: ";

				for (int i = 0; i < companies.size(); i++) {
					companyOptions[i] = companies.get(i).getSquareName().toString();
				}

				Menu companySelect = new Menu(companyOptions, companyHeader);
				choice = companySelect.getChoice();
				OwnableSquare selectedCompany = companies.get(choice - 1);

				System.out.println(currentPlayer.getName() + " enter your offer amount: ");
				Scanner input = new Scanner(System.in);

				boolean valid = false;
				int offer = 0;
				while (valid == false) {
					try {
						offer = input.nextInt();
						input.nextLine();
						if (offer < 0) {
							System.out.println("You must enter an offer greater than 0");
						} else if (offer > currentPlayer.getDevelopers()) {
							System.out.println("You don't have this many developers to offer");
						} else {
							valid = true;
						}
					} catch (Exception exception) {
						System.out.println("You must enter an integer value");
					}
				}

				System.out.println(ownerPlayer.getName() + ", do you accept this offer for "
						+ selectedCompany.getSquareName().toString() + " (Y/N)");
				boolean accepted = false;
				while (accepted == false) {
					String offerChoice = input.nextLine();
					if (offerChoice.toUpperCase().equals("Y")) {
						accepted = true;
						ownerPlayer.tradeOwnableSquares(currentPlayer, offer, selectedCompany);
					} else if (offerChoice.toUpperCase().equals("N")) {
						break;
					} else {
						System.out.println("Enter 'Y' or 'N' to accept or decline the offer: ");
					}

				}
				nextTurn();

			} else {
				System.out.println("There are no players available to trade with");
			}

		} else {
			System.out.println("You don't have enough developers to trade right now");
		}
	}

	private static void develop() throws Exception {

		// checking if player has developed more than once a turn
		if (numOfDevelops == 1) {
			System.out.println("Sorry, " + players[turn].getName()
					+ " you can only attempt develop once per turn, please select roll dice or next turn");

		} else {

			if (players.length == 0) {
				System.out.println("Please register players first");
			}
			Player currentPlayer = players[turn];
			System.out.println();

			// checks if player owns any squares
			if (currentPlayer.getOwnedSquares().isEmpty() || currentPlayer.getOwnedSquares() == null) {
				System.out.println("Sorry, you currently don't own any squares");

			} else {

				// ArrayList of all squares the players owns that are type company
				ArrayList<OwnableSquare> allOCo = currentPlayer.getOwnedSquares();

				// checks array list of owned squares for type company
				for (int i = 0; i < allOCo.size(); i++) {
					if (allOCo.get(i).getSquareType() != Type.COMPANY) {
						allOCo.remove(i);
					}
				}

				// checks if player owns any companies
				if (allOCo.isEmpty() || allOCo == null) {
					System.out.println("Sorry, you currently don't own any companies");
				} else {

					System.out.println();

					// Player can choose tool or product
					String[] choices = { "Tool", "Product" };
					String title = ("Choose what you'd like to develop:");
					Menu menu = new Menu(choices, title);
					int option;
					option = menu.getChoice();
					System.out.println();

					if (option == 1) {

						ArrayList<Company> allAvailableComps = new ArrayList<Company>();
						ArrayList<Company> allComps = new ArrayList<Company>();
						for (int i = 0; i < allOCo.size(); i++) {
							allAvailableComps.add((Company) allOCo.get(i));
							allComps.add((Company) allOCo.get(i));
						}
						// If the player already has tools
						if (players[turn].getTool() != null) {
							ArrayList<Company> allTools = players[turn].getTool();
							allAvailableComps.removeAll(allTools);
						}

						// Initialisation of list of companies to develop tools from
						String[] list = new String[allAvailableComps.size()];

						// adds each square name and type to list array
						for (int i = 0; i < allAvailableComps.size(); i++) {
							Company ownComp = allAvailableComps.get(i);
							list[i] = ownComp.getSquareName() + ", Type: " + ownComp.getCompanyType();

						}

						if (allAvailableComps.isEmpty() || allAvailableComps == null) {
							System.out.println(
									"Sorry, you currently don't own any companies without tools developed on them already");
						} else {
							String header = "Choose one of your companies to develop a tool: ";
							Menu menu2 = new Menu(list, header);
							int choice2;
							choice2 = menu2.getChoice();
							choice2 = choice2 - 1;
							System.out.println("You choose: " + allAvailableComps.get(choice2).getSquareName());
							Company com = (Company) allAvailableComps.get(choice2);

							int numOfType = 0;

							for (int i = 0; i < players[turn].getOwnedSquares().size(); i++) {
								Company allCom = (Company) (players[turn].getOwnedSquares().get(i));
								if (allCom.getCompanyType() == com.getCompanyType()) {
									numOfType++;
								}
							}

							if (numOfType >= 3 || ((com.getCompanyType() == CompanyTypes.CYBERSECURITY
									|| com.getCompanyType() == CompanyTypes.ARCHITECTURE) && numOfType >= 2)) {
								System.out.println();
								System.out.println("The cost of a tool on this company is: " + com.getToolCost());
								System.out.println("You have " + players[turn].getDevelopers() + " developers");
								if (players[turn].getDevelopers() >= com.getToolCost()) {
									System.out.println("\nWould you like to purchase this tool? (Y/N)");
									Scanner input = new Scanner(System.in);
									String answer = "";
									boolean validInput = false;
									while (validInput != true) {
										try {
											answer = input.nextLine();
											if (answer.equals("Y")) {
												players[turn].addTools(com);
												numOfDevelops = 1;
												int newDevelAmount = players[turn].getDevelopers() - com.getToolCost();
												players[turn].setDevelopers(newDevelAmount);
												int newRent = com.getRentCost() + 50;
												com.setRentCost(newRent);
												System.out.println("You have purchased a tool on the square "
														+ com.getSquareName()
														+ ", the rent for this square has went up by 50 developers");
												System.out.println("You now have " + players[turn].getDevelopers()
														+ " developers remaining");
												validInput = true;
											} else if (answer.equals("N")) {
												System.out.println("Ok, tool not purchased");
												validInput = true;
											}
										} catch (Exception exception) {
											System.out.println("Invalid input");
										}
									}

								} else {
									System.out.println("Sorry, you don't have enough developers to purchase this tool");
								}

							} else {
								System.out.println("Sorry, You need to own all of the companies of type "
										+ com.getCompanyType() + " to develop a tool with it");
							}
						}

					} else if (option == 2) {

						if (currentPlayer.getTool().isEmpty()) { // check if player has tools first
							System.out.println("Sorry, you currently don't own an tools to develop products from");
						} else {

							ArrayList<Company> allAvailableTools = new ArrayList<Company>(players[turn].getTool());
							ArrayList<Company> allTools = new ArrayList<Company>();
							allTools = players[turn].getTool();

							if (players[turn].getProducts() != null) {
								ArrayList<Company> allProducts = players[turn].getProducts();
								allAvailableTools.removeAll(allProducts);

							}

							String header = "Choose one of your companies you own tools on to develop a product: ";
							String[] listOfTools = new String[allAvailableTools.size()];
							for (int i = 0; i < allAvailableTools.size(); i++) {
								listOfTools[i] = allAvailableTools.get(i).getSquareName() + ", Type: "
										+ allAvailableTools.get(i).getCompanyType();
							}

							Menu menu2 = new Menu(listOfTools, header);
							int choice2;
							choice2 = menu2.getChoice();
							choice2 = choice2 - 1;
							System.out.println("You choose: " + allAvailableTools.get(choice2).getSquareName());
							Company tool = (Company) allAvailableTools.get(choice2);

							int numOfToolType = 0;

							Company comTool = (Company) allTools.get(choice2);

							for (int i = 0; i < players[turn].getTool().size(); i++) {
								Company allOwnTools = (Company) (players[turn].getTool().get(i));
								if (allOwnTools.getCompanyType() == tool.getCompanyType()) {
									numOfToolType++;
								}
							}

							ArrayList<Company> products = players[turn].getProducts();
							int numOfPro = products.size();

							if (numOfToolType >= 3 || ((comTool.getCompanyType() == CompanyTypes.CYBERSECURITY
									|| comTool.getCompanyType() == CompanyTypes.ARCHITECTURE) && numOfToolType >= 2)) {
								System.out.println();
								System.out.println(
										"The cost of a product on this company is: " + comTool.getProductCost());
								System.out.println("You have " + players[turn].getDevelopers() + " developers");
								System.out.println("\nWould you like to purchase this product? (Y/N)");
								Scanner input = new Scanner(System.in);
								String answer = "";
								answer = input.nextLine();
								if (answer.equals("Y")) {
									if (players[turn].getDevelopers() >= comTool.getProductCost()) {
										players[turn].addProducts(comTool);
										numOfPro++;
										numOfDevelops = 1;
										if (numOfPro >= 3 || ((comTool.getCompanyType() == CompanyTypes.CYBERSECURITY
												|| comTool.getCompanyType() == CompanyTypes.ARCHITECTURE)
												&& numOfToolType >= 2)) {
											winGame();
											exit();
										} else {
											int newDevelAmount = players[turn].getDevelopers()
													- comTool.getProductCost();
											players[turn].setDevelopers(newDevelAmount);
											int newRent = comTool.getRentCost() + 100;
											comTool.setRentCost(newRent);

											System.out.println("You have purchased a product on the square "
													+ comTool.getSquareName()
													+ ", the rent for this square has went up by 100 developers");
											System.out.println("You now have " + players[turn].getDevelopers()
													+ " developers remaining");
										}
									} else {
										System.out.println(
												"Sorry, you don't have enough developers to purchase this product");
									}
								} else if (answer.equals("N") || answer.equals("n")) {
									System.out.println("Ok, product not purchased");
								} else {
									System.out.println("Input error");
								}

							} else {
								System.out.println("Sorry, You need to own tools on all of the companies of type "
										+ comTool.getCompanyType() + " to develop a product with it");
							}
						}

					}

				}

			}
		}
	}

	private static void sell() {
		Player currentPlayer = players[turn];

		if (currentPlayer.getOwnedSquares().isEmpty() || currentPlayer.getOwnedSquares() == null) { // check if player
			System.out.println("Sorry, you currently don't own any squares to sell");

		} else {
			ArrayList<OwnableSquare> allOwnSqu = currentPlayer.getOwnedSquares();
			String header = "Choose a square you own that you want to give up: ";
			String[] list = new String[allOwnSqu.size()];

			for (int i = 0; i < allOwnSqu.size(); i++) {
				list[i] = allOwnSqu.get(i).getSquareName();
			}

			Menu menu = new Menu(list, header);
			int choice;
			choice = menu.getChoice();
			choice = choice - 1;
			System.out.println("\nYou choose: " + allOwnSqu.get(choice).getSquareName());
			OwnableSquare chosenSqu = allOwnSqu.get(choice);
			System.out.println("\n" + chosenSqu.getSquareName() + " is being sold back to the board");
			System.out.println("You gain " + chosenSqu.getCost() + " developers back");
			int newDevelopers = chosenSqu.getCost() + currentPlayer.getDevelopers();
			currentPlayer.setDevelopers(newDevelopers);
			currentPlayer.removeOwnedSquare(chosenSqu);
			System.out.println("You now have " + players[turn].getDevelopers() + " developers");

		}
	}

	private static void winGame() {
		ArrayList<Company> allProducts = players[turn].getProducts();
		Company[] allProductsArr = allProducts.toArray(new Company[allProducts.size()]);
		int CS = 0, VG = 0, Ro = 0, Gr = 0, CC = 0, DM = 0, Co = 0, Ar = 0;

		for (int i = 0; i < allProductsArr.length; i++) {
			Company product = allProductsArr[i];
			CompanyTypes prodType = product.getCompanyType();

			if (prodType == CompanyTypes.CYBERSECURITY) {
				CS++;
			} else if (prodType == CompanyTypes.VIDEOGAME) {
				VG++;
			} else if (prodType == CompanyTypes.ROBOTICS) {
				Ro++;
			} else if (prodType == CompanyTypes.GRAPHICS) {
				Gr++;
			} else if (prodType == CompanyTypes.CLOUDCOMPUTING) {
				CC++;
			} else if (prodType == CompanyTypes.DIGITALMARKETING) {
				DM++;
			} else if (prodType == CompanyTypes.COMPUTERSOFTWARE) {
				Co++;
			} else if (prodType == CompanyTypes.ARCHITECTURE) {
				Ar++;
			}
		}

		if (CS == 2 || Ar == 2 || VG == 3 || Ro == 3 || Gr == 3 || CC == 3 || DM == 3 || Co == 3) {
			System.out.println("\nCongradulations, " + players[turn].getName() + "! You won the game of Technopoly!");
			System.out.println("You have developed a product on each square of a company type! Well Done!");
			// endCredits();
		} else {
			return;
		}

	}

	private static void endCredits() {

		for (int l = 0; l < players.length; l++) {
			String name = players[l].getName();
			Player currentPlayer = players[l];

			System.out.println("\nShowing: " + name + "'s Final Statistics!" + "\n");
			System.out.println("Developers: " + currentPlayer.getDevelopers());

			ArrayList<OwnableSquare> ownedSquare = currentPlayer.getOwnedSquares();
			if (ownedSquare != null) {
				String squareString = "";
				for (int i = 0; i < ownedSquare.size(); i++) {
					squareString += ownedSquare.get(i).getSquareName().toString();

					if (i < ownedSquare.size() - 1) {
						squareString += ", ";
					}
				}
				System.out.println("Owned Squares: " + squareString);
			}

			// Owned tools

			ArrayList<Company> ownedTool = currentPlayer.getTool();
			Company[] allOwnTool = ownedTool.toArray(new Company[ownedTool.size()]);
			if (ownedTool != null) {
				String toolString = "";
				for (int i = 0; i < allOwnTool.length; i++) {
					toolString += allOwnTool[i].getSquareName().toString() + " ";
				}

				System.out.println("Owned Tools: " + toolString);
			}

			// Owned Products
			ArrayList<Company> ownedProducts = currentPlayer.getProducts();
			Company[] allOwnPro = ownedProducts.toArray(new Company[ownedProducts.size()]);
			if (ownedProducts != null) {
				String productString = "";
				for (int i = 0; i < allOwnPro.length; i++) {
					productString += allOwnPro[i].getSquareName().toString() + " ";
				}
				System.out.println("Owned Products: " + productString);
			}

		}

	}

	private static void playerStats() {

		String name = players[turn].getName();

		if (players.length == 0) {
			System.out.println("Please register players first");
		} else {

			Player currentPlayer = players[turn];
			Square currentSquare = currentPlayer.getCurrentSquare();
			if (currentSquare != null) {
				System.out.println("\nShowing: " + name + "'s Statistics!" + "\n");
				if (currentSquare.getSquareType() == Type.COMPANY) {
					Company currentCompany = (Company) currentPlayer.getCurrentSquare();
					System.out.println("Current square Name: " + currentSquare.getSquareName() + ", Current Square ID: "
							+ currentSquare.getSquareId() + ", Current Square Type: " + currentSquare.getSquareType()
							+ " , Current company Type: " + currentCompany.getCompanyType());
				} else {
					System.out.println("Current square Name: " + currentSquare.getSquareName() + ", Current Square ID: "
							+ currentSquare.getSquareId() + ", Current Square Type: " + currentSquare.getSquareType());
				}
				System.out.println("Developers: " + currentPlayer.getDevelopers());

				// Owned Square
				ArrayList<OwnableSquare> ownedSquare = currentPlayer.getOwnedSquares();
				if (ownedSquare != null) {
					String squareString = "";
					for (int i = 0; i < ownedSquare.size(); i++) {
						squareString += ownedSquare.get(i).getSquareName().toString();

						if (i < ownedSquare.size() - 1) {
							squareString += ", ";
						}
					}
					System.out.println("Owned Squares: " + squareString);
				} else {
					System.out.println("null");
				}

				// Owned tools

				ArrayList<Company> ownedTool = currentPlayer.getTool();
				Company[] allOwnTool = ownedTool.toArray(new Company[ownedTool.size()]);
				if (ownedTool != null) {
					String toolString = "";
					for (int i = 0; i < allOwnTool.length; i++) {
						toolString += allOwnTool[i].getSquareName().toString() + " ";
					}

					System.out.println("Owned Tools: " + toolString);
				}

				// Owned Products
				ArrayList<Company> ownedProducts = currentPlayer.getProducts();
				Company[] allOwnPro = ownedProducts.toArray(new Company[ownedProducts.size()]);
				if (ownedProducts != null) {
					String productString = "";
					for (int i = 0; i < allOwnPro.length; i++) {
						productString += allOwnPro[i].getSquareName().toString() + " ";
					}
					System.out.println("Owned Products: " + productString);
				}
			}

			System.out.println("Player has reboot card?: " + currentPlayer.getRebootCard());
		}

	}

	private static void rules() throws Exception {
		System.out.println("\n   Rules:");
		System.out.println("============");
		System.out.println(
				"How to win: The first player to develop a product on each of the same type of company wins the game");
		System.out.println(
				"How to develop product: The player has to develop a tool of each of the same type of company to start before developing a product on those companies");
		System.out.println("How to develop tool: The player must own all of the same type of that company");
		System.out.println(
				"How to repair server: The player can roll a double, use a server reboot card, or, they can pay developers after 3 tries at rolling a double");

		boolean done = false;
		do {
			Scanner input = new Scanner(System.in);
			String ans = "";

			System.out.println("\nDone?: (Y/N)");
			ans = input.nextLine();
			if (ans.equals("Y") || ans.equals("y")) {
				done = true;
				System.out.println();
				return;

			} else {

			}
		} while (done == false);
	}

	private static void nextTurn() {
		numOfDevelops = 0;
		if (turn < players.length - 1) {
			System.out.println("\nNext turn is: " + players[turn + 1].getName());
			turn = turn + 1;
		} else {
			turn = 0;
			System.out.println("\nNext turn is: " + players[turn].getName());
		}
	}

	private static void exit() {
		if (players != null) {
			endCredits();
		}
		System.out.println("\nThank you for playing Technopoly!");
		System.out.println("Exiting Game...");
		finished = true;

	}

	private static void saveGame() {
		try {
			File myObj = new File("save.txt");
			if (myObj.createNewFile()) {
				System.out.println("Save File Created: " + myObj.getName());
				writeSaveData();
				System.out.println("Saved Successfully");
			} else {
				clearSaveData();
				writeSaveData();
				System.out.println("Saved Successfully");
			}
		} catch (IOException e) {
			System.out.println("Error: Failed to Save");
			e.printStackTrace();
		}
	}

	// writes save data to local file in src package save.txt -John
	private static void writeSaveData() {
		for (int i = 0; i < players.length; i++) {
			int developers = players[i].getDevelopers();
			String name = players[i].getName();
			Square currentSquare = players[i].getCurrentSquare();
			int squareID = players[i].getSquareID();
			ArrayList<OwnableSquare> ownedSquares = players[i].getOwnedSquares();
			ArrayList<Company> tools = players[i].getTool();
			ArrayList<Company> products = players[i].getProducts();

			boolean turn = players[i].getTurn();

			try {
				FileWriter saveWriter = new FileWriter("save.txt", true);
				saveWriter.write(i + 1 + "_" + "name" + "_" + name + "\n");
				saveWriter.write(i + 1 + "_" + "developers" + "_" + developers + "\n");
				saveWriter.write(i + 1 + "_" + "currentSquare" + "_" + squareID + "\n");
				saveWriter.write(i + 1 + "_" + "ownedCompanies" + "_" + ownedSquares + "\n"); // will implement parser
				saveWriter.write(i + 1 + "_" + "tools" + "_" + tools + "\n");
				saveWriter.write(i + 1 + "_" + "products" + "_" + products + "\n");
				saveWriter.write(i + 1 + "_" + "isTurn" + "_" + turn + "\n");
				saveWriter.close();
			} catch (IOException e) {
				System.out.println("Error: Failed to Save");
				e.printStackTrace();
			}
			System.out.println("Saved player:" + name);
		}
	}

	// clears save file before writing new save data -John
	private static void clearSaveData() {
		try {
			FileWriter clearSave = new FileWriter("save.txt", false);
			PrintWriter pw = new PrintWriter(clearSave, false);
			pw.flush();
			pw.close();
			clearSave.close();
			System.out.println("Cleared Save Successfully");
		} catch (IOException e) {
			System.out.println("Error: Failed to Save");
			e.printStackTrace();
		}

	}

	private static void loadGame() throws Exception {
		File f = new File("save.txt");
		if (f.exists() && !f.isDirectory()) {
			loadSave();
		} else {
			System.out.println("No Save file found \n");
		}
	}

	private static void loadSave() throws Exception {
		addGameData();
		Player[] temp = new Player[8];
		String checker = "";
		// int playerID = 0;
		int playerCount = 0;
		ArrayList<String> list = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader("save.txt"))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				list.add(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < list.size(); i++) {
			checker = list.get(i);
			if (checker.contains("name")) {
				Player player = new Player(checker.substring(7));
				temp[playerCount] = player;
				playerCount++;
			} else if (checker.contains("developers")) {
				String devCount = checker.substring(13);
				int devs = Integer.parseInt(devCount);
				temp[playerCount - 1].setDevelopers(devs);
			} else if (checker.contains("currentSquare")) {
				String currentSquare = checker.substring(16);
				int square = Integer.valueOf(currentSquare);
				temp[playerCount - 1].setSquareID(square);
				temp[playerCount - 1].setCurrentSquare(board[square]);

			} else if (checker.contains("ownedCompanies")) {
				for (int j = 0; j < 39; j++) {
					String num = String.valueOf(j);
					if (checker.contains("[" + num + ",") || checker.contains(" " + num + ",")) {
						temp[playerCount - 1].addOwnedSquare((OwnableSquare) board[j - 1]);
						((OwnableSquare) board[j - 1]).setOwner(temp[playerCount - 1]);
					}
				}
			} else if (checker.contains("tools")) {
				int multiplier = 0;
				for (int j = 0; j < 39; j++) {
					String num = String.valueOf(j);

					multiplier = checker.split(num, -1).length - 1;

					if (checker.contains("[" + num + ",") || checker.contains(" " + num + ",")) {
						Square squ = board[j - 1];
						Company com = (Company) squ;
						int curRent = com.getRentCost();
						// System.out.println("Multiplier: " + multiplier);
						com.setRentCost(curRent + (multiplier * 50));

						temp[playerCount - 1].addTools(com);
					}
				}
			} else if (checker.contains("products")) {
				int multiplier = 0;
				for (int j = 0; j < 39; j++) {
					String num = String.valueOf(j);

					multiplier = checker.split(num, -1).length - 1;

					if (checker.contains("[" + num + ",") || checker.contains(" " + num + ",")) {
						Square squ = board[j - 1];
						Company com = (Company) squ;
						int curRent = com.getRentCost();
						com.setRentCost(curRent + (multiplier * 100));

						temp[playerCount - 1].addProducts(com);
					}
				}
			} else if (checker.contains("isTurn") && checker.contains("true")) {
				temp[playerCount - 1].setTurn(true);
				turn = playerCount - 1;
			}
		}
		players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++) {
			players[i] = temp[i];
		}

		System.out.println("LOADED SAVE SUCCESSFULLY");
		beginGame();
	}

	private static void addGameData() throws Exception {

		Square squ0 = new Square("Pass to Go", Type.GO, 0);
		Company squ1 = new Company("SecureIT", Type.COMPANY, CompanyTypes.CYBERSECURITY, 15, 25, 125, 2);
		compInfo[0] = squ1;

		Square squ2 = new Square("Community Card", Type.COMCHEST, 0);
		Company squ3 = new Company("CyberTech", Type.COMPANY, CompanyTypes.CYBERSECURITY, 15, 25, 125, 3);
		compInfo[1] = squ3;

		Square squ4 = new Square("Developer Strike!", Type.LOSEDEVELOPERS, 15);
		InternetDomain squ5 = new InternetDomain("Internet Domain", Type.INTERNETDOMAINS, 50, 20, 40, 80, 100);
		internetDInfo[0] = squ5;
		Company squ6 = new Company("Electronic Games", Type.COMPANY, CompanyTypes.VIDEOGAME, 25, 25, 185, 10);
		compInfo[2] = squ6;

		Square squ7 = new Square("Chance Card", Type.CHANCE, 0);
		Company squ8 = new Company("Activisor Snow", Type.COMPANY, CompanyTypes.VIDEOGAME, 25, 25, 185, 10);
		compInfo[3] = squ8;

		Company squ9 = new Company("Ninetendoe", Type.COMPANY, CompanyTypes.VIDEOGAME, 30, 25, 185, 11);
		compInfo[4] = squ9;

		Square squ10 = new Square("Check Server", Type.CHECKSERVER, 0);
		Company squ11 = new Company("GreyApple", Type.COMPANY, CompanyTypes.ROBOTICS, 35, 50, 375, 30);
		compInfo[5] = squ11;

		VPNISP squ12 = new VPNISP("ISP", Type.ISP, 35, 75, 100);
		vpnIspInfo[0] = squ12;
		Company squ13 = new Company("Catronics", Type.COMPANY, CompanyTypes.ROBOTICS, 35, 50, 375, 35);
		compInfo[6] = squ13;

		Company squ14 = new Company("LeftHand Robots", Type.COMPANY, CompanyTypes.ROBOTICS, 40, 50, 375, 40);
		compInfo[7] = squ14;

		InternetDomain squ15 = new InternetDomain("Internet Domain", Type.INTERNETDOMAINS, 50, 20, 40, 80, 100);
		internetDInfo[1] = squ15;
		Company squ16 = new Company("Mvidia", Type.COMPANY, CompanyTypes.GRAPHICS, 45, 50, 375, 45);
		compInfo[8] = squ16;

		Square squ17 = new Square("Community Card", Type.COMCHEST, 0);
		Company squ18 = new Company("DisplayConnect", Type.COMPANY, CompanyTypes.GRAPHICS, 45, 50, 375, 50);
		compInfo[9] = squ18;

		Company squ19 = new Company("Quick Begin Digital", Type.COMPANY, CompanyTypes.GRAPHICS, 50, 50, 375, 52);
		compInfo[10] = squ19;

		Square squ20 = new Square("Coffee Break", Type.COFFEEBREAK, 0);
		Company squ21 = new Company("Cloudpaths", Type.COMPANY, CompanyTypes.CLOUDCOMPUTING, 55, 55, 560, 80);
		compInfo[11] = squ21;

		Company squ22 = new Company("Server Capacity", Type.COMPANY, CompanyTypes.CLOUDCOMPUTING, 55, 55, 560, 85);
		compInfo[12] = squ22;

		Square squ23 = new Square("Chance Card", Type.CHANCE, 0);
		Company squ24 = new Company("Server Hosting", Type.COMPANY, CompanyTypes.CLOUDCOMPUTING, 60, 55, 560, 90);
		compInfo[13] = squ24;

		InternetDomain squ25 = new InternetDomain("Internet Domain", Type.INTERNETDOMAINS, 50, 20, 40, 80, 100);
		internetDInfo[2] = squ25;
		Company squ26 = new Company("ViralCorp", Type.COMPANY, CompanyTypes.DIGITALMARKETING, 65, 55, 560, 95);
		compInfo[14] = squ26;

		Company squ27 = new Company("WebDotCom", Type.COMPANY, CompanyTypes.DIGITALMARKETING, 65, 55, 560, 100);
		compInfo[15] = squ27;

		VPNISP squ28 = new VPNISP("VPN", Type.VPN, 35, 75, 100);
		vpnIspInfo[1] = squ28;
		Company squ29 = new Company("TrustSite", Type.COMPANY, CompanyTypes.DIGITALMARKETING, 70, 55, 560, 105);
		compInfo[16] = squ29;

		Square squ30 = new Square("Server Crash", Type.SERVERCRASH, 0);
		Company squ31 = new Company("Soshiba", Type.COMPANY, CompanyTypes.COMPUTERSOFTWARE, 75, 100, 750, 250);
		compInfo[17] = squ31;

		Company squ32 = new Company("Ray", Type.COMPANY, CompanyTypes.COMPUTERSOFTWARE, 75, 100, 750, 260);
		compInfo[18] = squ32;

		Square squ33 = new Square("Community Card", Type.COMCHEST, 0);
		Company squ34 = new Company("Macrosoft", Type.COMPANY, CompanyTypes.COMPUTERSOFTWARE, 80, 100, 750, 275);
		compInfo[19] = squ34;

		InternetDomain squ35 = new InternetDomain("Internet Domain", Type.INTERNETDOMAINS, 50, 20, 40, 80, 100);
		internetDInfo[3] = squ35;
		Square squ36 = new Square("Chance Card", Type.CHANCE, 0);
		Company squ37 = new Company("Pentagon IT Solutions", Type.COMPANY, CompanyTypes.ARCHITECTURE, 85, 100, 500,
				300);
		compInfo[20] = squ37;

		Square squ38 = new Square("Developers laid off", Type.LOSEDEVELOPERS, 20);
		Company squ39 = new Company("Sparkl Systems", Type.COMPANY, CompanyTypes.ARCHITECTURE, 100, 100, 500, 350);
		compInfo[21] = squ39;

		board[0] = squ0;
		board[1] = squ1;
		board[2] = squ2;
		board[3] = squ3;
		board[4] = squ4;
		board[5] = squ5;
		board[6] = squ6;
		board[7] = squ7;
		board[8] = squ8;
		board[9] = squ9;
		board[10] = squ10;
		board[11] = squ11;
		board[12] = squ12;
		board[13] = squ13;
		board[14] = squ14;
		board[15] = squ15;
		board[16] = squ16;
		board[17] = squ17;
		board[18] = squ18;
		board[19] = squ19;
		board[20] = squ20;
		board[21] = squ21;
		board[22] = squ22;
		board[23] = squ23;
		board[24] = squ24;
		board[25] = squ25;
		board[26] = squ26;
		board[27] = squ27;
		board[28] = squ28;
		board[29] = squ29;
		board[30] = squ30;
		board[31] = squ31;
		board[32] = squ32;
		board[33] = squ33;
		board[34] = squ34;
		board[35] = squ35;
		board[36] = squ36;
		board[37] = squ37;
		board[38] = squ38;
		board[39] = squ39;

		// Square for when cards don't move player
		Square noMove = new Square("Doesn't move", Type.CHECKSERVER, 0);
		// Square for when cards move player back 3
		Square move3Squ = new Square("Move Three Squares", Type.CHECKSERVER, 0);

		Card card1 = new Card("Advance to Go", Type.CHANCE, 50, 0, squ0);
		// Need to change square in parameter below
		Card card2 = new Card("Advance to nearest Internet Domain", Type.CHANCE, 0, 0, move3Squ);
		Card card3 = new Card("Advance to ISP or VPN", Type.CHANCE, 0, 0, move3Squ);
		Card card4 = new Card("You gain 50 employees", Type.CHANCE, 50, 0, noMove);
		Card card5 = new Card("Free server repair", Type.CHANCE, 0, 0, noMove);
		Card card6 = new Card("Go back 3 squares", Type.CHANCE, 0, 0, move3Squ);
		Card card7 = new Card("Server crash", Type.CHANCE, 0, 0, squ10);
		Card card8 = new Card("Your company is growing. You gained 75 employees", Type.CHANCE, 75, 0, noMove);

		// community cards:
		Card comcard1 = new Card("Advance to Go", Type.COMCHEST, 50, 0, squ0);
		Card comcard2 = new Card("You gained 100 employees", Type.COMCHEST, 100, 0, noMove);
		Card comcard3 = new Card("Strike. You lost 25 employees", Type.COMCHEST, 0, 25, noMove);
		Card comcard4 = new Card("Recent advertisement has paid off, you gain 50 employees", Type.COMCHEST, 50, 0,
				noMove);
		Card comcard5 = new Card("Free server repair", Type.COMCHEST, 0, 0, noMove);
		Card comcard6 = new Card("Management error. You lose 25 employees", Type.COMCHEST, 0, 25, noMove);
		Card comcard7 = new Card("Major management error. You lose 50 employees", Type.COMCHEST, 0, 50, noMove);
		Card comcard8 = new Card("Server Crash", Type.COMCHEST, 0, 0, squ10);

		chance[0] = card1;
		chance[1] = card2;
		chance[2] = card3;
		chance[3] = card4;
		chance[4] = card5;
		chance[5] = card6;
		chance[6] = card7;
		chance[7] = card8;

		community[0] = comcard1;
		community[1] = comcard2;
		community[2] = comcard3;
		community[3] = comcard4;
		community[4] = comcard5;
		community[5] = comcard6;
		community[6] = comcard7;
		community[7] = comcard8;

	}

}
