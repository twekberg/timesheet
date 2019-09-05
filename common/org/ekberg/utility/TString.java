//  @(#) $Id:  $  

//  *********************************************************************
// 
//    Copyright (c) 2006 Tom Ekberg.
//    All Rights Reserved
// 
//    The information contained herein is confidential to and the
//    property of Tom Ekberg. and is not to be disclosed
//    to any third party without prior express written permission
//    of Tom Ekberg.  Tom Ekberg., as the
//    author and owner under 17 U.S.C. Sec. 201(b) of this work made
//    for hire, claims copyright in this material as an unpublished 
//    work under 17 U.S.C. Sec.s 102 and 104(a)   
// 
//  ******************************************************************* 

package org.ekberg.utility;


import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;


/**
 * This class adds methods that are not in the current String class. This class
 * extends String by using StringMirror. Note that String is a final class and
 * so it not directly extendable.
 *
 * The methods defined here generally fit into one of the following categories:
 * split, join, pad (replicate fits here), replace, trim, capitalization,
 * pluralization, hexadecimal dump, tab handling, extending Character.isDigit
 * to a String object and various methods in the String class that don't have
 * an IgnoreCase method.
 */
public class TString extends StringMirror
{
    /** Default spacing for the TAB character. */
    public static final int DEFAULT_TAB_SPACING = 8;

    /** Simply an empty string, so one need not be created every time. */
    protected static final TString EMPTY_TSTRING = new TString("");
    
    /** The following are special words that can't be pluralized by appending
     * an "s". Most certainly there are others, but this is a good start.*/
    protected static final String[] singleWords = {"is",  "this",  "was",
                                                   "it",   "has"};
    protected static final String[] pluralWords = {"are", "these", "were",
                                                   "they", "have"};


    public TString(String theString) {
        super(theString);
    }


    //-------------------------------------------------------------------------
    /**
     * Creation method for TString.
     *
     * @param  s  the string.
     *
     * @return
     *   TString - the TString equivalent of s.
     */
    //-------------------------------------------------------------------------
    public static TString create(String theString) {
    	return new TString(theString);
    }
  
    
    //-------------------------------------------------------------------------
    /**
     * Return a string.
     *
     * @param  s  the string candidate.
     *
     * @return
     *   String - if s is null return an empty string, otherwise return s.
     */
    //-------------------------------------------------------------------------
    private String noNull(String s) {
        return (s == null
                    ? ""
                    : s);
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string into a Collection.
     *
     * @param  separator  field separator. If the length of this string is
     * greater than one then any of the single characters in the string can act
     * as a field separator.
     *
     * @return
     *   Collection - the string in the form of a Collection.
     */
    // ------------------------------------------------------------------------
    public Collection<String> splitToCollection(String separator)
    {
        Collection<String> c = (Collection<String>)(new Vector<String>());
        if (mirrorString != null) {
            StringTokenizer st = new StringTokenizer(mirrorString, separator);
            while(st.hasMoreTokens())
            {
                c.add(st.nextToken());
            }
        }
        return c;
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string into an Enumeration.
     *
     * @param  separator  field separator. If the length of this string is
     * greater than one then any of the single characters in the string can act
     * as a field separator.
     *
     * @return
     *   Enumeration - the string in the form of an Enumeration.
     */
    // ------------------------------------------------------------------------
    public Enumeration splitToEnumeration(String separator)
    {
        StringTokenizer st = new StringTokenizer(noNull(mirrorString), separator);
        // Boy this is simple. A StringTokenizer is an Enumeration.
        return st;
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string into an array.
     *
     * @param  separator  field separator. If the length of this string is
     * greater than one then any of the single characters in the string can act
     * as a field separator.
     *
     * @return
     *   String[] - the string in the form of an array.
     */
    // ------------------------------------------------------------------------
    public String[] splitToArray(String separator)
    {
        StringTokenizer st = new StringTokenizer(noNull(mirrorString), separator);
        String[] a = new String[st.countTokens()];
        int i = 0;

        while(st.hasMoreTokens())
        {
            a[i++] = st.nextToken();
        }
        return a;
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string into an array of ints.
     *
     * @param  separator  field separator. If the length of this string is
     * greater than one then any of the single characters in the string can act
     * as a field separator.
     *
     * @return
     *   int[] - the string in the form of an int array.
     */
    // ------------------------------------------------------------------------
    public int[] splitToIntArray(String separator)
    {
        StringTokenizer st = new StringTokenizer(noNull(mirrorString), separator);
        int[] a = new int[st.countTokens()];
        int i = 0;

        while(st.hasMoreTokens())
        {
            a[i++] = Integer.parseInt(st.nextToken());
        }
        return a;
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string into an array of longs.
     *
     * @param  separator  field separator. If the length of this string is
     * greater than one then any of the single characters in the string can act
     * as a field separator.
     *
     * @return
     *   long[] - the string in the form of a long array.
     */
    // ------------------------------------------------------------------------
    public long[] splitToLongArray(String separator)
    {
    	long[] a = null;
    	if (noNull(mirrorString).equals("")) {
    		// An empty string implies no longs.
    		a = new long[0];
    	}
    	else {
    		StringTokenizer st = new StringTokenizer(noNull(mirrorString), separator);
    		a = new long[st.countTokens()];
    		int i = 0;

    		while(st.hasMoreTokens())
    		{
    			a[i++] = Long.parseLong(st.nextToken());
    		}
        }
        return a;
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string into an array of booleans.
     *
     * @param  separator  field separator. If the length of this string is
     * greater than one then any of the single characters in the string can act
     * as a field separator.
     *
     * @return
     *   boolean[] - the string in the form of a boolean array.
     */
    // ------------------------------------------------------------------------
    public boolean[] splitToBooleanArray(String separator)
    {
        StringTokenizer st = new StringTokenizer(noNull(mirrorString), separator);
        boolean[] a = new boolean[st.countTokens()];
        int i = 0;

        while(st.hasMoreTokens())
        {
            a[i++] = Boolean.valueOf(st.nextToken()).booleanValue();
        }
        return a;
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string into a Hashtable (implements Map).
     *
     * @param  separator  field separator. If the length of this string is
     * greater than one then any of the single characters in the string can act
     * as a field separator.
     *
     * @return
     *   Hashtable - the string in the form of a Hashtable. The keys are from
     *   the string. The value is not important, but will be non-null for a
     *   valid key.
     */
    // ------------------------------------------------------------------------
    public Hashtable<String, String> splitToHashtable(String separator)
    {
        StringTokenizer st = new StringTokenizer(noNull(mirrorString), separator);
        Hashtable<String, String> a = new Hashtable<String, String>();

        while(st.hasMoreTokens())
        {
            a.put(st.nextToken(), "yes");
        }
        return a;
    }


    // ------------------------------------------------------------------------
    /**
     * Split a string from a CSV file. The separator is always a comma. This
     * method differs from the other split methods in that this handles quoted
     * strings with a CSV line.
     *
     * @return
     *   Collection - the string in the form of a Collection.
     */
    // ------------------------------------------------------------------------
    public Collection splitCSV()
    {
        Collection<String> c = (Collection<String>)(new Vector<String>());
        boolean inString = false; // true if the current value is a string.
        int index;
        int stringLength = mirrorString.length();
        char ch;
        int startValue = 0;

        for(index=0; index < stringLength; index++) {
            ch = mirrorString.charAt(index);
            if (index == startValue) {
                // Make a determination of the value based on the first
                // character.
                inString = ch == '"';
            }
            else if (inString) {
                if (ch == '"') {
                    if ((index+1 < stringLength) && (mirrorString.charAt(index+1) == '"')) {
                        // Have two adjacent double quotes. These are
                        // data. Skip over both of them.
                        index++;
                    }
                    else {
                        // Have found the matching trailing double quote.
                        inString = false;
                    }
                }

            }
            if ( !inString && (ch == ',')) {
                // Have hit the end of the current field.
                c.add(mirrorString.substring(startValue, index));
                startValue = index + 1;
            }
        }
        // Get the last value.
        c.add(mirrorString.substring(startValue, stringLength));
        return c;
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of a Collection together.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined. If an element is null then
     * it is assumed that it is not there.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, Collection elements)
    {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            Iterator i = elements.iterator();
            Object element;
            boolean firstTime = true;
            while(i.hasNext())
            {
                element = i.next();
                if (element != null) {
                    if (firstTime) {
                        firstTime = false;
                    }
                    else {
                        sb.append(separator);
                    }
                    sb.append(element.toString());
                }
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of an Iterator together.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined. If an element is null then
     * it is assumed that it is not there.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, Iterator elements) {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            Object element;
            boolean firstTime = true;
            while(elements.hasNext()) {
                element = elements.next();
                if (element != null) {
                    if (firstTime) {
                        firstTime = false;
                    }
                    else {
                        sb.append(separator);
                    }
                    sb.append(element.toString());
                }
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of an enumeration together.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined. If an element is null then
     * it is assumed that it is not there.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, Enumeration elements) {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            Object element;
            boolean firstTime = true;
            while(elements.hasMoreElements()) {
                element = elements.nextElement();
                if (element != null) {
                    if (firstTime) {
                        firstTime = false;
                    }
                    else {
                        sb.append(separator);
                    }
                    sb.append(element.toString());
                }
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of an Object array together.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined. If an element is null then
     * it is assumed that it is not there.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, Object[] elements)
    {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            int nElements = elements.length;
            Object element;
            boolean firstTime = true;
            for(int i=0; i<nElements; i++)
            {
                element = elements[i];
                if (element != null) {
                    if (firstTime) {
                        firstTime = false;
                    }
                    else {
                        sb.append(separator);
                    }
                    sb.append(element.toString());
                }
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of an int  array together.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, int[] elements)
    {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            int nElements = elements.length;
            int element;
            boolean firstTime = true;
            for(int i=0; i<nElements; i++)
            {
                element = elements[i];
                if (firstTime) {
                    firstTime = false;
                }
                else {
                    sb.append(separator);
                }
                sb.append(String.valueOf(element));
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of a long array together.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, long[] elements)
    {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            int nElements = elements.length;
            long element;
            boolean firstTime = true;
            for(int i=0; i<nElements; i++)
            {
                element = elements[i];
                if (firstTime) {
                    firstTime = false;
                }
                else {
                    sb.append(separator);
                }
                sb.append(String.valueOf(element));
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of a boolean array together.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, boolean[] elements)
    {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            int nElements = elements.length;
            boolean element;
            boolean firstTime = true;
            for(int i=0; i<nElements; i++)
            {
                element = elements[i];
                if (firstTime) {
                    firstTime = false;
                }
                else {
                    sb.append(separator);
                }
                sb.append(String.valueOf(element));
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Join elements of a Map together. Note that if one does a
     * splitToMap followed by this join method, the order of the values
     * in the original string may not be the same as the order of the values
     * returned by this join method.
     *
     * @param  separator  field separator. The length of this string should be
     * 1 so that the result is compatible with the split methods. If the length
     * of this string exceeds one then the entire separator string is the field
     * separator making the result incompatible with the split methods.
     *
     * @param  elements  the elements to be joined. If an element is null then
     * it is assumed that it is not there.
     *
     * @return
     *   String - the string with elements from elements.
     */
    // ------------------------------------------------------------------------
    public static String join(String separator, Map elements)
    {
        StringBuffer sb = new StringBuffer();
        // Handle the null case gracefully.
        if (elements != null) {
            Iterator i = elements.keySet().iterator();
            Object key;
            Object value;
            String valueString;
            String element;
            boolean firstTime = true;
            while(i.hasNext()) {
                key = i.next();
                // If the key is null, remove from the list. According to the
                // JavaDocs, neither the key nor the value can be null.
                if (key != null) {
                    value = elements.get(key);
                    valueString = value.toString();
                    if (valueString.length() > 0)
                        // Only show -->value if the value string is not empty.
                        element = key + "-->" + valueString;
                    else
                        element = key.toString();
                    if (firstTime) {
                        firstTime = false;
                    }
                    else {
                        sb.append(separator);
                    }
                    sb.append(element);
                }
            }
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Apply a method to a Collection of elements. If the method returns null
     * then that element is ignored. Otherwise the return value is used to form
     * a new collection. This is somewhat similar to the Lisp mapcar function
     * that applies a function to each element in list.
     *
     * @param  applier  implementation of the Applier interface that defines
     * the method.
     *
     * @param  elements  the elements to be applied. If an element is null then
     * it is assumed that it is not there. Also, if the application to an
     * element causes an exception then the element is ignored.
     *
     * @return
     *   Collection - the result of applying each element to the original
     * elements.
     */
    // ------------------------------------------------------------------------
    public static Collection apply(Applier applier, Collection elements)
    {
        Collection result = null;
        // Handle the null case gracefully.
        if (elements != null) {
            result = new Vector();
            Object element = null;
            Iterator i = elements.iterator();
            while(i.hasNext()) {
                element = i.next();
                if (element != null) {
                    try {
                        element = applier.apply(element);
                    }
                    catch (Exception e) {
                        element = null;
                    }
                }
                if (element != null) {
                    result.add(element);
                }
            }
        }
        return result;
    }


    // ------------------------------------------------------------------------
    /**
     * Apply a method to an array of elements. If the method returns null
     * then that element is ignored. Otherwise the return value is used to form
     * a new collection. This is somewhat similar to the Lisp mapcar function
     * that applies a function to each element in list.
     *
     * @param  applier  implementation of the Applier interface that defines
     * the method.
     *
     * @param  elements  the elements to be applied. If an element is null then
     * it is assumed that it is not there. Also, if the application to an
     * element causes an exception then the element is ignored.
     *
     * @return
     *   Object[] - the result of applying each element to the original
     * elements.
     */
    // ------------------------------------------------------------------------
    public static Object[] apply(Applier applier, Object[] elements)
    {
        Object[] result = null;
        // Handle the null case gracefully.
        if (elements != null) {
            Collection c = new Vector();
            Object element = null;
            int nElements = elements.length;
            for(int i=0; i<nElements; i++) {
                element = elements[i];
                if (element != null) {
                    try {
                        element = applier.apply(element);
                    }
                    catch (Exception e) {
                        element = null;
                    }
                }
                if (element != null) {
                    c.add(element);
                }
            }
            result = c.toArray();
        }
        return result;
    }


    // ------------------------------------------------------------------------
    /**
     * Apply a method to an Enumeration of elements. If the method returns null
     * then that element is ignored. Otherwise the return value is used to form
     * a new enumeration. This is somewhat similar to the Lisp mapcar function
     * that applies a function to each element in list.
     *
     * @param  applier  implementation of the Applier interface that defines
     * the method.
     *
     * @param  elements  the elements to be applied. If an element is null then
     * it is assumed that it is not there. Also, if the application to an
     * element causes an exception then the element is ignored.
     *
     * @return
     *   Enumeration - the result of applying each element to the original
     * elements.
     */
    // ------------------------------------------------------------------------
    public static Enumeration apply(Applier applier, Enumeration elements)
    {
        Vector result = null;
        // Handle the null case gracefully.
        if (elements != null) {
            result = new Vector();
            Object element = null;
            while(elements.hasMoreElements()) {
                element = elements.nextElement();
                if (element != null) {
                    try {
                        element = applier.apply(element);
                    }
                    catch (Exception e) {
                        element = null;
                    }
                }
                if (element != null) {
                    result.add(element);
                }
            }
        }
        return result == null
                    ? null
                    : result.elements();
    }


    //-------------------------------------------------------------------------
    /**
     * More general form of toArray(new String[]). Handles nulls and uses the
     * toString method to get the string.
     *
     * @param  objects  the objects.
     *
     * @return
     *   String[] - the objects in array format.
     */
    //-------------------------------------------------------------------------
    public static String[] toStringArray(Collection objects) {
        String[] ret = null;
        if (objects == null)
            ret = new String[0];
        else {
            ret = new String[objects.size()];
            Iterator it = objects.iterator();
            int i = 0;
            Object o;
            while(it.hasNext()) {
                o = it.next();
                if (o == null)
                    ret[i] = null;
                else
                    ret[i] = o.toString();
                i++;
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * More general form of toArray(new String[]). Handles nulls and uses the
     * toString method to get the string.
     *
     * @param  objects  the objects.
     *
     * @return
     *   String[] - the objects in array format.
     */
    //-------------------------------------------------------------------------
    public static String[] toStringArray(Enumeration objects) {
        String[] ret = null;
        if (objects == null)
            ret = new String[0];
        else {
            // There is no way to determine the length of the enumeration, so
            // we create a Collection and call the other method.
            Vector vector = new Vector();
            while(objects.hasMoreElements()) {
                vector.add(objects.nextElement());
            }
            ret = toStringArray(vector);
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Pad the value with leading "0"s to the specified width.
     *
     * @param  width  the minimum number of character positions in the result.
     *
     * @return
     *   String - value padded to width characters.
     */
    //-------------------------------------------------------------------------
    public String pad0(int width) {
        return pad(width, "0");
    }


    //-------------------------------------------------------------------------
    /**
     * Pad the value with leading characters to the specified width.
     *
     * @param  width  the minimum number of character positions in the result.
     *
     * @param  pad  the character to pad with. Normally this contains one
     * character, but that is not required.
     *
     * @return
     *   String - value padded to width characters. If pad contains more than
     * one character, the returned result may be longer than width.
     */
    //-------------------------------------------------------------------------
    public String pad(int width, String pad) {
        String ret = mirrorString;
        while(ret.length() < width)
            ret = pad + ret;
        return ret;
    }


    // ------------------------------------------------------------------------
    /**
     * Pad the value on the right with trailing " "s to the specified width.
     *
     * @param  width  the minimum number of character positions in the result.
     *
     * @return
     *   String - value padded to width characters.
     */
    // ------------------------------------------------------------------------
    public String rpad(int width) {
        return rpad(width, " ");
    }


    // ------------------------------------------------------------------------
    /**
     * Pad the value on the right with trailing delimiters to the specified
     * width.
     *
     * @param  width  the minimum number of character positions in the result.
     *
     * @param  delimiter  the trailing values.
     *
     * @return
     *   String - value padded to width characters.
     */
    // ------------------------------------------------------------------------
    public String rpad(int width, String delimiter) {
        StringBuffer ret = new StringBuffer(mirrorString);
        while(ret.length() < width)
            ret.append(delimiter);
        return ret.toString();
    }


    //-------------------------------------------------------------------------
    /**
     * Replicate a string a specified number of times. This has the same effect
     * as TextUtil.join(value, new String[nTimes+1]), but is much more
     * efficient.
     *
     * @param  nTimes  the number of times.
     *
     * @return
     *   String - value, replicated nTimes.
     */
    //-------------------------------------------------------------------------
    public String replicate(int nTimes) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<nTimes; i++) {
            sb.append(mirrorString);
        }
        return sb.toString();
    }


    // ------------------------------------------------------------------------
    /**
     * Replace one set of substrings with another set. This works by iterating
     * through searches and calling the simpler replace method. Note that there
     * is only one version of this (none for Collection, Iterator, etc.)
     * because only arrays can be created in-line.
     *
     * @param  searches  look for these Strings.
     *
     * @param  replacements  replace a match in searches with the corresponding
     * string in this array.
     *
     * @return
     *   String - look through source and change all occurrances of searches to
     * replacements.
     */
    // ------------------------------------------------------------------------
    public String replace(String[] searches, String[] replacements) {
        int nSearches = Math.min(searches.length, replacements.length);
        String source = mirrorString; // We don't want to modify this object.
        for(int i=0; i<nSearches; i++) {
            source = source.replace(searches[i], replacements[i]);
        }
        return source;
    }


    // ------------------------------------------------------------------------
    /**
     * Replace one substring with another, ignoring case.
     *
     * @param  search  look for this String.
     *
     * @param  replacement  replace with this String.
     *
     * @return
     *   String - look through source and change all occurrances of search to
     * replacement.
     */
    // ------------------------------------------------------------------------
    public String replaceIgnoreCase(String search, String replacement) {
        int searchLength;
        int i;
        String source = mirrorString; // We don't want to modify this object.
        String part;            // Part of source.

        searchLength = search.length();
        for(i=0; i < source.length() - searchLength + 1; i++) {
            part = source.substring(i, i + searchLength);
            if (part.compareToIgnoreCase(search) == 0) {
                if (i+searchLength == source.length()) {
                    // At end of source.
                    source = source.substring(0, i) + replacement;
                }
                else if (i == 0) {
                    // At beginning of source.
                    source = replacement + source.substring(searchLength);
                }
                else {
                    // Somewhere in the middle.
                    source = source.substring(0,i) + replacement + source.substring(i+searchLength);
                }
            }
        }
        return source;
    }


    //-------------------------------------------------------------------------
    /**
     * Similar to String.trim(), except this only removes one leading and/or
     * trailing instance of the trim string.
     *
     * @param  trimmer  the value to trim out of s.
     *
     * @return
     *   String - the string with a leading and/or trailing trimmer removed.
     */
    //-------------------------------------------------------------------------
    public String trim1(String trimmer) {
        StringBuffer sb = new StringBuffer(mirrorString);
        int lenTrimmer = trimmer.length();

        if (sb.toString().startsWith(trimmer)) {
            sb.delete(0, lenTrimmer);
        }
        if (sb.toString().endsWith(trimmer)) {
            sb.setLength(sb.length() - lenTrimmer);
        }
        return sb.toString();
    }


    //-------------------------------------------------------------------------
    /**
     * Similar to String.trim(), except this only removes all trailing
     * whitespace.
     *
     * @return
     *   String - the string with the trailing whitespaceremoved.
     */
    //-------------------------------------------------------------------------
    public String rtrim() {
        StringBuffer sb = new StringBuffer(mirrorString);

        while(Character.isWhitespace(sb.toString().charAt(sb.length()-1))) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }


    //-------------------------------------------------------------------------
    /**
     * true if the character sequence represented by the "ending" argument is a
     * suffix of the character sequence represented by this, ignoring case;
     * false otherwise. Note that the result will be true if the "ending"
     * argument is the empty string or is equal to the this object as determined
     * by the equalsIgnoreCase(Object) method.
     *
     * @param  ending  the possible suffix of this.
     *
     * @return
     *   boolean - true if ending is a suffix of this, ignoring case.
     */
    //-------------------------------------------------------------------------
    public boolean endsWithIgnoreCase(String ending) {
        int lenS = mirrorString.length();
        int lenEnding = ending.length();
        boolean ret = false;
        
        if (lenEnding <= lenS) {
            if (lenEnding == 0)
                ret = true;
            else {
                String part = mirrorString.substring(lenS - lenEnding);
                ret = part.equalsIgnoreCase(ending);
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * true if the character sequence represented by the "starting" argument is
     * a prefix of the character sequence represented by this, ignoring case;
     * false otherwise. Note that the result will be true if the "starting"
     * argument is the empty string or is equal to the this object as determined
     * by the equalsIgnoreCase(Object) method.
     *
     * @param  starting  the possible prefix of this.
     *
     * @return
     *   boolean - true if "starting" is a prefix of this, ignoring case.
     */
    //-------------------------------------------------------------------------
    public boolean startsWithIgnoreCase(String starting) {
        return startsWithIgnoreCase(starting, 0);
    }


    //-------------------------------------------------------------------------
    /**
     * true if the character sequence represented by the "starting" argument is
     * a prefix of the character sequence represented by this, ignoring case,
     * starting at the indicated offset within this; false otherwise. Note that
     * the result will be true if the "starting" argument is the empty string
     * or is equal to the the substring of this starting at the indicated
     * offset, as determined by the equalsIgnoreCase(Object) method.
     *
     * @param  starting  the possible prefix of this.
     *
     * @param  offset  where to begin looking in the string.
     *
     * @return
     *   boolean - true if "starting" is a prefix of this, ignoring case.
     */
    //-------------------------------------------------------------------------
    public boolean startsWithIgnoreCase(String starting, int offset) {
        int lenS = mirrorString.length();
        int lenStarting = starting.length();
        boolean ret = false;
        
        if (lenStarting <= lenS - offset) {
            if (lenStarting == 0)
                ret = true;
            else {
                String part = mirrorString.substring(offset, offset + lenStarting);
                ret = part.equalsIgnoreCase(starting);
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, ignoring case. The integer returned is the smallest
     * value k such that: 
     * <PRE>
     *   match.equalsIgnoreCase(this.substring(k, k+match.length()))
     * </PRE>
     * is true.
     *
     * @param  match  the substring to search for.
     *
     * @return
     *   int - if the string argument occurs as a substring within this,
     * then the index of the first character of the first such substring is
     * returned; if it does not occur as a substring, -1 is returned.
     */
    //-------------------------------------------------------------------------
    public int indexOfIgnoreCase(String match) {
        return indexOfIgnoreCase(match, 0);
    }


    //-------------------------------------------------------------------------
    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index, ignoring case. The
     * integer returned is the smallest value k such that:
     * <PRE>
     *   match.equalsIgnoreCase(this.substring(k, k+match.length())) && (k >= fromIndex)
     * </PRE>
     * is true.
     * There is no restriction on the value of fromIndex. If it is negative, it
     * has the same effect as if it were zero: the entire string may be
     * searched. If it is greater than the length of this string, it has the same
     * effect as if it were equal to the length of this string: -1 is returned. 
     *
     * @param  match  the substring to search for.
     *
     * @param  fromIndex  the index to start the search from.
     *
     * @return
     *   int - If the match argument occurs as a substring within this
     * at a starting index no smaller than fromIndex, then the index of the
     * first character of the first such substring is returned. If it does not
     * occur as a substring starting at fromIndex or beyond, -1 is returned.
     */
    //-------------------------------------------------------------------------
    public int indexOfIgnoreCase(String match, int fromIndex) {
        int retIndex = -1;
        if ((mirrorString != null) && (match != null)) {
            int lenSource = mirrorString.length();
            int lenMatch = match.length();
            int i = fromIndex < 0
                        ? 0
                        : fromIndex;

            while(lenSource - i >= lenMatch) {
                if (match.equalsIgnoreCase(mirrorString.substring(i, i+lenMatch))) {
                    retIndex = i;
                    break;
                }
                i++;
            }
        }
        return retIndex;
    }


    //-------------------------------------------------------------------------
    /**
     * Searches for the first occurence of this in array testing for equality
     * using the equalsIgnoreCase method. This basically is a simple array
     * lookup, looking for a match with this, ignoring case. No order of the
     * array is assumed.
     *
     * Note that the match argument is this, which also differs from other
     * indexOf methods.
     *
     * @param  source  the array being searched through.
     *
     * @return
     *   int - the index of the first occurrence of this in the source
     * array. More specifically, this is the smallest value k such that
     * this.equalsIgnoreCase(source[k]) is true; returns -1 if the object is
     * not found.
     */
    //-------------------------------------------------------------------------
    public int indexOfIgnoreCase(String[] source) {
        int ret = -1;
        for(int i=0; i<source.length; i++) {
            if (source[i].equalsIgnoreCase(mirrorString)) {
                ret = i;
                break;
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Searches for the first occurence of this in array testing for equality
     * using the equals method. This basically is a simple array
     * lookup, looking for a match with this, ignoring case. No order of the
     * array is assumed.
     *
     * Note that the match argument is this, which also differs from other
     * indexOf methods.
     *
     * @param  source  the array being searched through.
     *
     * @return
     *   int - the index of the first occurrence of this in the source
     * array. More specifically, this is the smallest value k such that
     * this.equalsIgnoreCase(source[k]) is true; returns -1 if the object is
     * not found.
     */
    //-------------------------------------------------------------------------
    public int indexOf(String[] source) {
        int ret = -1;
        for(int i=0; i<source.length; i++) {
            if (source[i].equals(mirrorString)) {
                ret = i;
                break;
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Convert the initial character of a string to lower case.
     *
     * @return
     *   String - if the initial character of the this string is upper case,
     * convert it to lower case.
     */
    //-------------------------------------------------------------------------
    public String lowerInitial() {
        String string = mirrorString;
        if (string.length() >= 1) {
            char ch = string.charAt(0);
            if (Character.isUpperCase(ch))
                string = Character.toLowerCase(ch) + string.substring(1);
        }
        return string;
    }


    //-------------------------------------------------------------------------
    /**
     * Convert the initial character of a string to upper case.
     *
     * @return
     *   String - if the initial character of the this string is lower case,
     * convert it to upper case.
     */
    //-------------------------------------------------------------------------
    public String upperInitial() {
        String string = mirrorString;
        if (string.length() >= 1) {
            char ch = string.charAt(0);
            if (Character.isLowerCase(ch))
                string = Character.toUpperCase(ch) + string.substring(1);
        }
        return string;
    }

    
    //-------------------------------------------------------------------------
    /**
     * Convert a Java name to a name suitable as a label. For example,
     * "javaToName" would become "Java To Name".
     *
     * @Return
     *   String - the Java name converted to a more user-friendly name.
     */
    //-------------------------------------------------------------------------
    public String javaToName() {
    	StringBuilder string = new StringBuilder(upperInitial());
    	for(int i=1,max=string.length(); i<max; i++) {
            char ch = string.charAt(i);
            if (Character.isUpperCase(ch)) {
            	string.insert(i, ' ');
                i++;
                max++;
            }
    	}
    	return string.toString();
    }
    

    //-------------------------------------------------------------------------
    /**
     * Convert a name suitable as a label to its equivalent Java name. For
     * example, "Name To Java" would become "nameToJava".
     *
     * @Return
     *   String - user-friendly name converted to its Java name.
     */
    //-------------------------------------------------------------------------
    public String nameToJava() {
    	StringBuilder string = new StringBuilder(lowerInitial());
    	for(int i=1,max=string.length(); i<max; i++) {
            char ch = string.charAt(i);
            if (ch == ' ') {
            	string.deleteCharAt(i);
                i--;
                max--;
            }
    	}
    	return string.toString();
    }
    

    //-------------------------------------------------------------------------
    /**
     * Capitalize the first word, then separate all words with an initial
     * capital with a blank. This normally displays a value to the user.
     *
     * @return
     *   String - looks like a Java class name with each word separated by a
     *   blank.
     */
    //-------------------------------------------------------------------------
    public String separateWords() {
		return upperInitial().replaceAll("([A-Z])([a-z]+)", "$1$2 ").trim();
    }

    // ------------------------------------------------------------------------
    /**
     * Count the number of times search appears in this.
     *
     * @param  search  look for this String.
     *
     * @return
     *   int - the number of times.
     */
    // ------------------------------------------------------------------------
    public int countMatches(String search) {
        int searchLength;
        int i;
        int count = 0;

        searchLength = search.length();
        for(i=mirrorString.indexOf(search); i >= 0;
            i=mirrorString.indexOf(search, i+searchLength)) {
            count++;
        }
        return count;
    }


    // ------------------------------------------------------------------------
    /**
     * Perform a hexadecimal dump of this string.
     *
     * @return
     *   String - the hexadecimal dump.
     */
    // ------------------------------------------------------------------------
    public String hexDump() {
        String ret = "";
        try {
            byte[] bytes = mirrorString.getBytes("ISO-8859-1");
            ret = TString.hexDump(bytes);
        }
        catch (UnsupportedEncodingException e) {}
        return ret;
    }


    // ------------------------------------------------------------------------
    /**
     * Perform a hexadecimal dump of a byte array.
     *
     * @param  bytes  the bytes to perform the dump on.
     *
     * @return
     *   String - the hexadecimal dump.
     */
    // ------------------------------------------------------------------------
    public static String hexDump(byte[] bytes) {
        return hexDump(bytes, bytes.length);
    }


    // ------------------------------------------------------------------------
    /**
     * Perform a hexadecimal dump of a byte array.
     *
     * @param  bytes  the bytes to perform the dump on.
     *
     * @return
     *   String - the hexadecimal dump.
     */
    // ------------------------------------------------------------------------
    public static String hexDump(byte[] bytes, int length) {
        int i;
        int j;
        StringBuffer sb = new StringBuffer();
        char ch;

        // Calculate the number of digits needed based on bytes.length.
        int ndigits = (length == 0)
                    ? 1
                    : (int)(Math.ceil(Math.log((double)(bytes.length)) / Math.log(16.0)));
        for(i=0; i<length; i++) {
            if ((i % 16) == 0) {
                if (i > 0) {
                    sb.append("  ");
                    for(j=i-16; j<i; j++) {
                        ch = (char)bytes[j];
                        sb.append(charToHex(ch));
                    }
                    sb.append("\n");
                }
                sb.append(padToHex((long)i, ndigits) + " ");
            }
            else if ((i > 0) && ((i % 4) == 0)) {
                sb.append(" ");
            }
            sb.append(padToHex((long)bytes[i] & 0xFF, 2));
        }

        // For the last line, determine the number of blanks to write out
        // before writing out the ASCII values.
        int remainder = length % 16;
        i = remainder;
        if (i == 0)
            i = 16;
        i = (16 - i);
        j = i / 4;
        i = i * 2 + 2 + j;
        while(i-->0)
            sb.append(" ");

        // Display the ASCII values for the last line.
        for(j=Math.max(0, length -
                    (remainder == 0
                                ? 16
                                : remainder));
            j<length;
            j++) {
            ch = (char)bytes[j];
            sb.append(charToHex(ch));
        }
        sb.append("\n");
        return sb.toString();
    }


    //-------------------------------------------------------------------------
    /**
     * Convert a single character to the printable notation for a hex dump.
     *
     * @param  ch  a character
     *
     * @return
     *   char - if ch is printable then return that, otherwise return period.
     */
    //-------------------------------------------------------------------------
    protected static char charToHex(char ch) {
        return (((ch >= 'A') && (ch <= 'Z')) ||
                    ((ch >= 'a') && (ch <= 'z')) ||
                    ((ch >= '0') && (ch <= '9')) ||
                    (ch == '-') || (ch == '_') ||
                    (ch == '+') || (ch == '=') ||
                    (ch == ' '))
                    ? ch
                    : '.';
    }


    //-------------------------------------------------------------------------
    /**
     * Convert the value to hexadecimal and pad the result with leading "0"s to
     * the specified width.
     *
     * @param  value  the decimal value to be padded.
     *
     * @param  width  the minimum number of decimal digits in the result.
     *
     * @return
     *   String - value in hex padded to width characters.
     */
    //-------------------------------------------------------------------------
    public static String padToHex(long value, int width) {
        return (new TString(Long.toHexString(value).toUpperCase())).pad0(width);
    }


    // ------------------------------------------------------------------------
    /**
     * Extension of the Character.isDigit method that works for an entire
     * string.
     *
     * @return
     *   boolean - false if this string contains non-digits, true
     * otherwise. The distinction here is that an empty string returns true.
     */
    // ------------------------------------------------------------------------
    public boolean isDigits() {
        int len = mirrorString.length();
        boolean ret = true;
        
        for(int i=0; i<len; i++) {
            if (!Character.isDigit(mirrorString.charAt(i))) {
                ret = false;
                break;
            }
        }
        return ret;
    }


    //-------------------------------------------------------------------------
    /**
     * Replace tabs with spaces. Uses the constant DEFAULT_TAB_SPACING as the
     * default value.
     *
     * @return
     *   String - the untabified result.
     */
    //-------------------------------------------------------------------------
    public String untabify() {
        return untabify(DEFAULT_TAB_SPACING);
    }


    //-------------------------------------------------------------------------
    /**
     * Replace tabs with spaces.
     *
     * @param  tabSpacing  the amount of spacing for a TAB character.
     *
     * @return
     *   String - the untabified result.
     */
    //-------------------------------------------------------------------------
    public String untabify(int tabSpacing) {
        String string = mirrorString;
        for(int index=0; index<string.length(); index++) {
            if (string.charAt(index) == '\t')
                string = string.substring(0, index) +
                            EMPTY_TSTRING.pad(tabSpacing - (index % tabSpacing), " ") +
                            string.substring( index + 1 );
        }
        return string;
    }


    //-------------------------------------------------------------------------
    /**
     * Simplistic approach to pluralize a word based upon a count. Doesn't
     * handle all cases but does a reasonable job on the common cases. The
     * singleWords and pluralWords arrays are used to handle the special cases.
     * Note that if the initial letter of the word is upper case then the
     * initial letter of the result will also be in upper case.
     *
     * It doesn't matter which case of the word is being used: singular or
     * plural.
     *
     * @param  count  the count.
     *
     * @return
     *   String - this string pluralized properly according to count.
     */
    //-------------------------------------------------------------------------
    public String pluralize(int count) {
        String ret = null;
        int loc = indexOfIgnoreCase(singleWords);

        if (loc >= 0) {
            ret = (count == 1)
                        ? mirrorString
                        : pluralWords[loc];
        }
        else {
            loc = indexOfIgnoreCase(pluralWords);
            if (loc >= 0) {
                ret = count == 1
                            ? singleWords[loc]
                            : mirrorString;
            }
            else {
                if (mirrorString.endsWith("s")) {
                    ret = (count == 1
                                ? mirrorString.substring(0, mirrorString.length() - 1)
                                : mirrorString);
                }
                else {
                    ret = mirrorString + (count == 1
                                ? ""
                                : "s");
                }
            }
        }
        if (Character.isUpperCase(mirrorString.charAt(0)))
            ret = (new TString(ret)).upperInitial();
        return ret;
    }
}
