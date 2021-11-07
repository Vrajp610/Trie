package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null);

		if (allWords.length	== 0) 
			return root;
		
		root.firstChild = new TrieNode(new Indexes(0, (short)0, (short) (allWords[0].length() - 1)), null, null);
		TrieNode pointer = root.firstChild;
		TrieNode previous = null;
		
		for (int i = 1; i < allWords.length; i++) {
			String prefix = "";
			String insWord = allWords[i];
			
			while (pointer != null){
				int charIndex = pointer.substr.wordIndex;
				int iniIndex = pointer.substr.startIndex;
				int finIndex = pointer.substr.endIndex;
				String nPref = allWords[charIndex].substring(iniIndex, finIndex + 1);
				
				if (insWord.indexOf(prefix + nPref) == 0){
					prefix += nPref;
					previous = pointer;
					pointer = pointer.firstChild;
					continue;
				}
				
				if (insWord.charAt(iniIndex) != nPref.charAt(0)){
					previous = pointer;
					pointer = pointer.sibling;
					continue;
				}
				
				int total = 0;
				
				for (int j = 0; j < nPref.length(); j++){
					if (insWord.charAt(iniIndex + j) == nPref.charAt(j)) total++;
					else break;
				}
				
				int attached = iniIndex + total;
				
				pointer.firstChild = new TrieNode(new Indexes(charIndex, (short)attached, (short)finIndex), pointer.firstChild, null);
				pointer.substr.endIndex = (short)(attached - 1);
				prefix += allWords[charIndex].substring(iniIndex, attached);
				previous = pointer;
				pointer = pointer.firstChild;
			}
				
			Indexes fIndexes = new Indexes(i, (short)(prefix.length()), (short)(insWord.length() - 1));
			TrieNode addNode = new TrieNode(fIndexes, null, null);
			previous.sibling = addNode;
			pointer = root.firstChild;
			previous = null;
		}

		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		ArrayList<TrieNode> completionList = new ArrayList<>();
		TrieNode pointer = root.firstChild,previous = null;
		String prefTotal = "";
		while (prefix.length() > prefTotal.length() && pointer != null){
			int charIndex = pointer.substr.wordIndex;
			int iniIndex = pointer.substr.startIndex;
			int finIndex = pointer.substr.endIndex;
			
			previous = pointer;
			
			String prefCurrent = allWords[charIndex].substring(iniIndex, finIndex + 1);
			
			if (prefix.indexOf(prefTotal + prefCurrent) == 0){
				prefTotal = prefTotal + prefCurrent;
				pointer = pointer.firstChild;
				continue; 
			}
			
			if ((prefTotal + prefCurrent).indexOf(prefix) == 0){
				pointer = pointer.firstChild;
				break;
			}

			pointer = pointer.sibling;

		}
		if (pointer == null){
			if (allWords[previous.substr.wordIndex].indexOf(prefix) == 0){
				completionList.add(previous);
				return completionList;
			}
			return null;
		}

		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		addToTrie(completionList, pointer);
		return completionList;
	}
	
	private static void addToTrie(ArrayList<TrieNode> completionList, TrieNode initialInsert){
		if(initialInsert == null) 
			return;
		
		if(initialInsert.firstChild == null){
			completionList.add(initialInsert);
			addToTrie(completionList, initialInsert.sibling);
			return;
		}

		addToTrie(completionList, initialInsert.firstChild);
		addToTrie(completionList, initialInsert.sibling);
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
}