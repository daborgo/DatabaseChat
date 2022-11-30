package application;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides a screen with a command-line interface that is displayed when opening a new chat application.
 * Checks for authentication of the user accessing the app, and creates a new User with database entry.
 */
public class Authentication {
	DatabaseAccessor db;
	Scanner scan = new Scanner(System.in);
	
	
	/**
	 * Constructs the new Authentication screen. Assigns parameter to variable.
	 * 
	 * @param db DatabaseAccessor taken from main to be used in Authentication methods.
	 */
	public Authentication(DatabaseAccessor db) {
		this.db = db;
	}
	
	/**
	 * Initial screen that calls either register() or login() screens.
	 * 
	 * @return User taken from either register() or login() to be used in determining authentication in other screens.
	 */
	public User authScreen() {
		User ret = null;
		boolean bool = true;
		System.out.print("Welcome to *generic chat app name*! Enter a command:\n - /register \n - /login");
		while(bool) {
			System.out.print("\n\n> ");
			String s = scan.nextLine();
			if(s.equals("/register")) {
				ret = register();
				bool = false;
			} else if(s.equals("/login")) {
				ret = login();
				bool = false;
			} else {
				System.out.print("\nInvalid: Unknown Command.");
			}
		}
		return ret;
	}
	
	/**
	 * Gets command-line prompts from the user (username, password) and searches the database.
	 * Checks if inputs exist and determines authentication.
	 * If authenticated, returns a new user with unique id and given parameters.
	 *
	 * @return User that mirrors database entry.
	 */
	public User login() {
		User u = null;
		boolean b = true;
		int tempid = -1;
		while(b) {
			boolean exists = false;
			System.out.print("\nUsername: ");
			String un = scan.nextLine();
			Object[] keys = db.getKeys().toArray();
			for(int i = 0; i<keys.length; i++) {
				Object[] names = db.get((int)keys[i]).toArray();
				if(names[0].toString().equals(un)) {
					exists = true;
					tempid = (int)keys[i];
				}
			}
			if(exists) {
				boolean valid = false;
				while(!valid) {
					System.out.print("Password: ");
					String p = scan.nextLine();
					Object[] names = db.get(tempid).toArray();
					if(names[1].toString().equals(p)) {	
						u = new User(keys.length, un, p);
						System.out.println("\nLogging you in...\n");
						valid = true;
						b = false;
					} else {
						System.out.println("\nInvalid: Incorrect Password.\n");
					}
				}
			} else {
				System.out.print("\nInvalid: Username doesn't exist.\n");
			}
		}
		return u;
	}
	
	/**
	 * Gets command-line prompts from the user(username, password) and updates the database.
	 * Checks if inputs are unique.
	 * If unique, returns a new user with unique id and given parameters.
	 *
	 * @return User that mirrors database entry.
	 */
	public User register() {
		User u = null;
		boolean b = true;
		System.out.println("\nRegistering new user...");
		while(b) {
			boolean valid = true;
			System.out.print("\nUsername: ");
			String un = scan.nextLine();
			Object[] keys = db.getKeys().toArray();
			for(int i = 0; i<keys.length; i++) {
				Object[] names = db.get((int)keys[i]).toArray();
				if(names[0].toString().equals(un))
					valid = false;
			}
			if(valid) {
				System.out.print("Password: ");
				String p = scan.nextLine();
				ArrayList<Object> al = new ArrayList<Object>();
				if(keys.length==0)
					al.add(0);
				else
					al.add(keys.length);
				al.add(un);
				al.add(p);
				db.add(al);
				u = new User(keys.length, un, p);
				System.out.println("\nSuccessfully registered. Logging you in...\n");
				b = false;
			} else {
				System.out.print("\nInvalid: Username already taken.\n");
			}
		}
		return u;
	}

}
