package project3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
*
* Colby Bray 11/27/2023 COSC 310 Movie Hash Table Project
* 
*/

class KeyValuePair<K, V> { // class for key and value of hash table
    private final K key;
    private final V value;

    public KeyValuePair(K key, V value) { // constructors for key and value variables
        this.key = key;
        this.value = value;
    }

    public K getKey() { // method to get key
        return key;
    }

    public V getValue() { // method to get value
        return value;
    }
}

class HashTable<K, V> { // setup for hash table structure
    private int capacity = 743; // prime number for capacity to limit possible list collisions
    private List<List<KeyValuePair<K, V>>> table; // use linked list for key value pairs
    private int size;

    public HashTable() { // setup for size of hash table based on  set capacity
        table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(new ArrayList<>());
        }
        size = 0;
    }

    private int getHash(K key) { // get hash code for current key depending on table size
        return Math.abs(key.hashCode() % table.size());
    }

    public void put(K key, V value) { // put method for table
        int index = getHash(key);
        List<KeyValuePair<K, V>> chain = table.get(index); // use linked list per index in cases of collision

        for (KeyValuePair<K, V> pair : chain) {
            if (pair.getKey().equals(key)) {
                pair = new KeyValuePair<>(key, value); // updates existing value if a key exists
                return;
            }
        }

        chain.add(new KeyValuePair<>(key, value)); // add new keyvalue pair as part of chain
        size++; // table size increases
    }

    public V get(K key) { // method for getting value per key
        int index = getHash(key);
        List<KeyValuePair<K, V>> chain = table.get(index);

        for (KeyValuePair<K, V> pair : chain) { // updates existing value when a key exists
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }

        return null; // else return null as default
    }

    public void remove(K key) { // method for removing value from index
        int index = getHash(key);
        List<KeyValuePair<K, V>> chain = table.get(index);

        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).getKey().equals(key)) {
                chain.remove(i);
                size--;
                return;
            }
        }
    }

    public int size() { // method for returning table size integer
        return size;
    }
    
    public double currentLoadFactor() { // get current table load factor for other methods
        return (double) size / table.size();
    }

    public int getBuckets() { // count and return number of filled buckets in table
        int count = 0;
        for (List<KeyValuePair<K, V>> chain : table) {
            if (!chain.isEmpty()) {
                count++;
            }
        }
        return count;
    }
}

class MovieQuotesDatabase { // setup for movie quote hash table
    private HashTable<String, List<String>> quoteTable;
    private List<String> movieTitles;

    public MovieQuotesDatabase() { // constructors for hash table
        this.quoteTable = new HashTable<>();
        this.movieTitles = new ArrayList<>();
    }

    public void addQuote(String movieTitle, String quote) { // method to add quotes to hash table
        List<String> quotes = quoteTable.get(movieTitle);
        if (quotes == null) {
            quotes = new ArrayList<>();
            quotes.add(quote);
            quoteTable.put(movieTitle, quotes);
            movieTitles.add(movieTitle);
        } else {
            quotes.add(quote);
        }
    }

    public void delete() { // method for removing movie title(s) and related quotes from table
        Scanner tempScan = new Scanner(System.in);
        System.out.print("Enter a movie title to delete: ");
        String deleteTitle = tempScan.nextLine();

        List<String> matchingTitles = new ArrayList<>(); // search method for finding matching title(s) to delete
        for (String title : movieTitles) {
                if (title.toLowerCase().contains(deleteTitle.toLowerCase())) {// lower case to remove case differences
                    matchingTitles.add(title); // add to matching title list that will be removed from table
                }
        }

        if (!matchingTitles.isEmpty()) { // method for actually removing titles from table when not empty
            for (String title : matchingTitles) {
                quoteTable.remove(title);
                movieTitles.remove(title);
            }
            System.out.println(deleteTitle+" movie(s) deleted successfully.");
        } else {
            System.out.println("No matching movies found for deletion.");
        }
    }

    public List<String> searchTitle(String inputTitle) {// method for matching movie titles into an array for later use in search method
        List<String> matchingTitles = new ArrayList<>();
        for (String title : movieTitles) {
            
                if (title.toLowerCase().contains(inputTitle.toLowerCase())) { // lower case to remove case differences
                    matchingTitles.add(title);// if current hash table title is contained in input title, add to matching array
                }
            }
       
        return matchingTitles;
    }

    public void search() { // method for searching table for matching title(s) string
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a movie title to search: ");
        String inputTitle = input.nextLine();

        List<String> matchingTitles = searchTitle(inputTitle);
        if (!matchingTitles.isEmpty()) {
            for (String title : matchingTitles) {
                List<String> quotes = quoteTable.get(title);
                if (quotes != null) {
                    System.out.println("Movie: " + title);
                    for (String quote : quotes) {
                        System.out.println("  - '" + quote+"'");
                    }
                }
            }
        } else {
            System.out.println("Movie not found."); // default when no match found
        }
    }
    
    public void printDatabase() { // method for printing current hash table database
        for (String title : movieTitles) {
            List<String> quotes = quoteTable.get(title);
            if (quotes != null) {
                System.out.println("Movie: " + title);
                for (String quote : quotes) {
                    System.out.println("  - '" + quote+"'");
                }
            }
        }
    }

    public int countItems() { // return current number of items on table
        return quoteTable.size();
    }
    
    public int countBuckets() { // return current number of movie titles
        return quoteTable.getBuckets();
    }

    public List<String> getAllMovieTitles() { // test to get current list of movie titles
        return movieTitles;
    }

    public double currentLoadFactor() { // use load factor method to return current load factor.
        return quoteTable.currentLoadFactor();
    }

    public double maxLoadFactor() { // set max load within current hash table class
    	double maxLoad = 0.8;
        return maxLoad;
    }
}

public class MovieHashTable {
    public static void main(String[] args) {
        MovieQuotesDatabase quotesDatabase = new MovieQuotesDatabase();
        
        ArrayList<String> movieList = new ArrayList<String>(); // set up arrayList
		File movieFile = new File("movie_quotes.csv"); // Note: This is name of movie quotes text file

		//int i = 0; // used to increment up scanner limiter
		try { // This process reads text file for movie related information
			Scanner sc1 = new Scanner(movieFile); // scanner reads text line, ends at enter

			sc1.nextLine(); // skip first line
			while (sc1.hasNext()) { // stop before index exception for movie quotes

				// for testing use counter to move down list, get movie title and quotes one at a time

				movieList.add(sc1.nextLine());
				//System.out.println(movieList); // temp for what is inserted

				String movieTotal = String.join(",", movieList); // temporary string

				String[] movieSplitArray = movieTotal.split(","); // organize String, split to two arrays
				
				String Quote = movieSplitArray[0];
				String Title = movieSplitArray[1];	// set up node data from splitArray
				
				quotesDatabase.addQuote(Title, Quote);
				// insert information into hash table, title acts as key
				//count++;
				//System.out.println("Count is: "+count); // temp for testing insertion methods
				 //i++; // temp	for scanner loop limit for debugging
				
				movieList.clear(); // prepare for next line
			} // should loop to next line of movie quotes list while scanner hasNext

		} // end of try
		catch (FileNotFoundException e) {
			System.out.println("Error, File not found."); // in case file is misplaced
		} // end of catch
	    // end of Read QuoteList

		Scanner startup = new Scanner(System.in); // scanner for startup input from user
		
		while (true) {// the printed out commands for user input
			System.out.println("\nCommands:");
			System.out.println("add - Add a movie title and then a related quote");
			System.out.println("delete - Remove a movie and all related quotes");
			System.out.println("find - Search for a movie title and related quote");
			System.out.println("printHT - Print out current hash table movie quote database");
			System.out.println("load - Print current load factor and max load factor for hash table.");
			System.out.println("count - Return current number of items in hash table");
			System.out.println("buckets - Return the current number of movies, or buckets of the hash table");
			System.out.println("who - Provide program author name");
			System.out.println("help or ? - Provide program information");
			System.out.println("exit - Exit program \n");
			System.out.print("Please enter your command: ");
			String response = startup.nextLine();

			switch (response.toLowerCase()) { // response will accept any case, but spelling must be correct
			case "add":
				System.out.print("Enter the Title of the movie quote to add: ");
				String newStringTitle = startup.nextLine();
				System.out.print("Enter the movie quote to be added: ");
				String newStringQuote = startup.nextLine();
				quotesDatabase.addQuote(newStringTitle, newStringQuote); // just insert title, no data
				System.out.println("Movie added to database!");
				break;
			case "delete":
				quotesDatabase.delete(); // use method for deleting related titles based on first two words match
				break;
			case "find":
				quotesDatabase.search(); // use method for finding movie titles related to first two words and present their quotes
				break;
			case "load": // get current table load factor and max load limit
				System.out.println("Current Load Factor: " + quotesDatabase.currentLoadFactor()
				+"\nMax Load Factor: "+quotesDatabase.maxLoadFactor());
				break;
			case "count": // get current number of items in table
				System.out.println("Number of Movies in table: " + quotesDatabase.countItems());
				break;
			case "buckets": // get current number of buckets, movie titles in our case
				System.out.println("Number of buckets in the hash table: " + quotesDatabase.countBuckets());
				break;
			case "printht": // print current hash table
				System.out.println("PRINTING MOVIE QUOTE DATABASE: \n "); 
				quotesDatabase.printDatabase();
				break;
			case "who":// print out program author name
				System.out.println("Program author is Colby Bray written for COSC 310 Project");
				break;
			case "help":// print output to inform user of all program functions and related inputs
				System.out.print("\nProgram Help - \n This program creates a Hash Table of movie titles and movie quotes with multi-mapping."
						+ "\n The program also has various commands for a user starting with"
						+ " the find command for the user to search the table for specific movie titles by matching the initial first two words (or less) inputted by user,"
						+ "\n the add command to add movie titles to the table as well as a related quote,"
						+ "\n the delete command to remove a matching movie title as well as any related quotes,"
						+ "\n the load command to see the current load factor of the table as well as get the maximum load of the hash table,"
						+ "\n the count commmand to see the current number of items in the hash table,"
						+ "\n the buckets command to get the number of bucket, which would be movie titles in this case,"
						+ "\n the printHT command that prints the current hash table,"
						+ "\n the who command to give the program author name,"
						+ "\n and finally an 'exit' command to exit program ('x' or 'q' also work).\n");
				break;
			case "?":// alternate input for help to inform user of all program functions and related inputs
				System.out.print("\nProgram Help - \n This program creates a Hash Table of movie titles and movie quotes with multi-mapping."
						+ "\n The program also has various commands for a user starting with"
						+ " the find command for the user to search the table for specific movie titles by matching the initial first two words (or less) inputted by user,"
						+ "\n the add command to add movie titles to the table as well as a related quote,"
						+ "\n the delete command to remove a matching movie title as well as any related quotes,"
						+ "\n the load command to see the current load factor of the table as well as get the maximum load of the hash table,"
						+ "\n the count commmand to see the current number of items in the hash table,"
						+ "\n the buckets command to get the number of bucket, which would be movie titles in this case,"
						+ "\n the printHT command that prints the current hash table,"
						+ "\n the who command to give the program author name,"
						+ "\n and finally an 'exit' command to exit program ('x' or 'q' also work).\n");
				break;
			case "exit": // method to exit program and exit's alternate commands x or q
				System.out.println("Exiting program. Goodbye!");
				startup.close();
				System.exit(0);
			case "x":
				System.out.println("Exiting program. Goodbye!");
				startup.close();
				System.exit(0);
			case "q":
				System.out.println("Exiting program. Goodbye!");
				startup.close();
				System.exit(0);
			default: // if invalid command is inputed, will ask for another valid one, will not exit program
				System.out.println("Invalid command. Please enter a valid command.");
			} // end of cases
		}
        
    }// end of try
} // end of main MovieHashTable