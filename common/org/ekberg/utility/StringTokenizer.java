//  @(#) $Id: StringTokenizer.java,v 1.1.1.1 2004/11/05 23:05:07 tekberg Exp $

package org.ekberg.utility;


import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;


/**
 * This class exists because the class java.util.StringTokenizer doesn't handle
 * empty tokens. For example, the Sequence:
 *
 * <PRE>
 *    String s = ",,,,";
 *    st = new java.util.StringTokenizer(s, ",");
 *    System.out.println("There are " + st.countTokens() + " tokens in " + s + ".");
 *    if (st.countTokens() > 0)
 *        System.out.println("First token is " + st.nextToken());
 * </PRE>
 *
 * This generates the following output:
 *
 * <PRE>
 *    There are 0 tokens in ,,,,.
 * </PRE>
 *
 * This class will indicate that there are 5 tokens, with each of them being
 * the empty string "". 
 * <P>
 * The following java.util.StringTokenizer methods are not supported by this
 * class due to their limited usefulness:
 *
 * <PRE>
 *    StringTokenizer(String str, String delim, boolean returnDelims)
 *    public String nextToken(String delim)
 * </PRE>
 *
 */
public class StringTokenizer 
    implements Enumeration
{
    /** Precomputed sequence of tokens. */
    protected Vector<String> my_tokens;
    
    /** The delimeter specified implicitly or explicitly by the caller. */
    protected String my_delim;

    /** The string being tokenized. */
    protected String my_str;
    
    /** The position within my_tokens of the next token. */
    protected int my_currentToken;


    // ------------------------------------------------------------------------
    /**
     * Constructs a string tokenizer for the specified string. The tokenizer
     * uses the default delimiter set, which is " \t\n\r\f": the space
     * character, the tab character, the newline character, the carriage-return
     * character, and the form-feed character. Delimiter characters themselves
     * will not be treated as tokens.
     *
     * @param  str  the string to be parsed.
     */
    // ------------------------------------------------------------------------
    public StringTokenizer(String str) {
        my_delim = " \t\n\r\f";
        my_str = str;
        tokenize();
    }


    // ------------------------------------------------------------------------
    /**
     * Constructs a string tokenizer for the specified string. The characters
     * in the delim argument are the delimiters for separating tokens.
     * Delimiter characters themselves will not be treated as tokens.
     *
     * @param  str  the string to be parsed.
     */
    // ------------------------------------------------------------------------
    public StringTokenizer(String str, String delim) {
        my_delim = delim;
        my_str = str;
        tokenize();
    }


    // ------------------------------------------------------------------------
    /**
     * Tokenize the string before the user asks for it. Most likely the user
     * will walk through the whole string anyway, or call countTokens().
     *
     * Note that the following cases are handled:
     * <UL>
     *   <LI> An empty string will result in 1 token which is the empty string.
     *   <LI> If two delimiters are have no intervening characters, the empty
     *        string will be the token returned as the intervening token.
     * </UL>
     */
    // ------------------------------------------------------------------------
    protected void tokenize() {
        char ch;
        String token;
        int len = my_str.length();

        my_tokens = new Vector<String>();
        token = "";
        for(int i=0; i<len; i++) {
            ch = my_str.charAt(i);
            if (my_delim.indexOf(ch) >= 0) {
                // Found one of the delimiters.
                my_tokens.add(token);
                token = "";
            }
            else {
                token += ch;
            }
        }
        // Add the last (or first if len==0) token.
        my_tokens.add(token);
        my_currentToken = 0;
    }


    // ------------------------------------------------------------------------
    /**
     * Calculates the number of times that this tokenizer's nextToken method
     * can be called before it generates an exception. The current position is
     * not advanced.
     *
     * @return
     *   int - the number of tokens remaining.
     */
    // ------------------------------------------------------------------------
    public int countTokens() {
        return my_tokens.size() - my_currentToken;
    }


    // ------------------------------------------------------------------------
    /**
     * Tests if there are more tokens available from this tokenizer's string.
     * If this method returns true, then a subsequent call to nextToken with no
     * argument will successfully return a token.
     *
     * @return
     *   boolean - true if at least one more token is available, false
     * otherwise.
     */
    // ------------------------------------------------------------------------
    public boolean hasMoreTokens() {
        return hasMoreElements();
    }


    // ------------------------------------------------------------------------
    /**
     * Returns the same value as the hasMoreTokens  method. It exists so that
     * this class can implement the Enumeration interface.
     *
     * @return
     *   boolean - true if at least one more token is available, false
     * otherwise.
     */
    // ------------------------------------------------------------------------
    public boolean hasMoreElements() {
        return countTokens() > 0;
    }

    // ------------------------------------------------------------------------
    /**
     * Returns the next token from this string tokenizer.
     *
     * @return
     *   String - the next token.
     *
     * @exception NoSuchElementException if there are no more tokens in this
     * tokenizer's string.
     */
    // ------------------------------------------------------------------------
    public String nextToken() throws NoSuchElementException {
        if (countTokens() <= 0)
            throw new NoSuchElementException("No more tokens are available.");
        else
            return my_tokens.elementAt(my_currentToken++);
    }

    // ------------------------------------------------------------------------
    /**
     * Returns the same value as the nextToken method, except that its declared
     * return value is Object rather than String. It exists so that this class
     * can implement the Enumeration interface.
     *
     * @return
     *   Object - the next token.
     *
     * @exception NoSuchElementException if there are no more tokens in this
     * tokenizer's string.
     */
    // ------------------------------------------------------------------------
    public Object nextElement() throws NoSuchElementException {
        if (countTokens() <= 0)
            throw new NoSuchElementException("No more tokens are available.");
        else
            return my_tokens.elementAt(my_currentToken++);
    }


    // ------------------------------------------------------------------------
    /**
     * Runs through test cases of using some of the methods defined here. This
     * does not perform complete coverage yet.
     *
     * @param  args  unused.
     */
    // ------------------------------------------------------------------------
    public static void main(String[] args) {
        int nTests = 0;
        int nPassed = 0;
        int n;
        int count;
        String expected = null;
        String got = null;
        boolean alreadyFailed;
        StringTokenizer st;

        nTests++;
        st = new StringTokenizer(",,,,", ",");
        if (st.countTokens() == 5) {
            nPassed++;
        }
        else {
            failed("countTokens", new Integer(5), new Integer(st.countTokens()));
        }

        nTests++;
        st = new StringTokenizer(",,,,", ",");
        expected = "";
        alreadyFailed = false;
        count = st.countTokens();
        for(n=0; n<count; n++) {
            if (st.hasMoreTokens()) {
                got = st.nextToken(); 
                if (got.length() != 0) {
                    // These tokens should all be "".
                    failed("empty token too long", expected, got);
                    alreadyFailed = true;
                    break;
                }
            }
            else {
                // Expected a token but none was found.
                failed("empty token missing", expected, "null");
                alreadyFailed = true;
                break;
            }
        }
        if (!alreadyFailed) {
            if (st.hasMoreTokens()) {
                got = st.nextToken(); 
                failed("empty token too many", "null", got);
            }
            else {
                nPassed++;
            }
        }
        

        System.out.println("Out of " + nTests + " tests, " +
                    nPassed + " tests passed.");
    }


    // ------------------------------------------------------------------------
    /**
     * Used to report test case failures.
     *
     * @param  message  a brief description of the test case.
     * @param  expected  the result that was expected.
     * @param  got  the result that was obtained.
     */
    // ------------------------------------------------------------------------
    private static void failed(String message, Object expected, Object got) {
        System.out.println("Error detected in " + message + ".");
        System.out.println("Expected " + expected + ", but got " + got + ".");
    }
}
