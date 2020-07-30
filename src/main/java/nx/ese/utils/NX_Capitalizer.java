package nx.ese.utils;

public class NX_Capitalizer {
	
	 public static String capitalizer(String word){
	        String[] words = word.split(" ");
	        StringBuilder sb = new StringBuilder();
	        if (words[0].length() > 0) {
	            sb.append(Character.toUpperCase(words[0].charAt(0))).append(words[0].subSequence(1, words[0].length()).toString().toLowerCase());
	            for (int i = 1; i < words.length; i++) {
	                sb.append(" ");
	                sb.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].subSequence(1, words[i].length()).toString().toLowerCase());
	            }
	        }
	        return  sb.toString();

	    }

}
