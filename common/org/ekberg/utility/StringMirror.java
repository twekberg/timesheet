package org.ekberg.utility;

import java.io.UnsupportedEncodingException;
import java.util.Locale;


public class StringMirror {
    protected String mirrorString;


    public StringMirror() {
        mirrorString = new String();
    }


    public StringMirror(byte[] p1) {
        mirrorString = new String(p1);
    }


    public StringMirror(byte[] p1, int p2, int p3) {
        mirrorString = new String(p1, p2, p3);
    }


    public StringMirror(byte[] p1, int p2, int p3, String p4) throws UnsupportedEncodingException {
        mirrorString = new String(p1, p2, p3, p4);
    }


    public StringMirror(byte[] p1, String p2) throws UnsupportedEncodingException {
        mirrorString = new String(p1, p2);
    }


    public StringMirror(char[] p1) {
        mirrorString = new String(p1);
    }


    public StringMirror(char[] p1, int p2, int p3) {
        mirrorString = new String(p1, p2, p3);
    }


    public StringMirror(int[] p1, int p2, int p3) {
        mirrorString = new String(p1, p2, p3);
    }


    public StringMirror(String p1) {
        mirrorString = new String(p1);
    }


    public StringMirror(StringBuffer p1) {
        mirrorString = new String(p1);
    }


    public StringMirror(StringBuilder p1) {
        mirrorString = new String(p1);
    }


    public char charAt(int p1) {
        return mirrorString.charAt(p1);
    }


    public int codePointAt(int p1) {
        return mirrorString.codePointAt(p1);
    }


    public int codePointBefore(int p1) {
        return mirrorString.codePointBefore(p1);
    }


    public int codePointCount(int p1, int p2) {
        return mirrorString.codePointCount(p1, p2);
    }


    public int compareTo(String p1) {
        return mirrorString.compareTo(p1);
    }


    public int compareToIgnoreCase(String p1) {
        return mirrorString.compareToIgnoreCase(p1);
    }


    public String concat(String p1) {
        return mirrorString.concat(p1);
    }


    public boolean contains(CharSequence p1) {
        return mirrorString.contains(p1);
    }


    public boolean contentEquals(CharSequence p1) {
        return mirrorString.contentEquals(p1);
    }


    public boolean contentEquals(StringBuffer p1) {
        return mirrorString.contentEquals(p1);
    }


    public static String copyValueOf(char[] p1) {
        return String.copyValueOf(p1);
    }


    public static String copyValueOf(char[] p1, int p2, int p3) {
        return String.copyValueOf(p1, p2, p3);
    }


    public boolean endsWith(String p1) {
        return mirrorString.endsWith(p1);
    }


    public boolean equals(Object p1) {
        return mirrorString.equals(p1);
    }


    public boolean equalsIgnoreCase(String p1) {
        return mirrorString.equalsIgnoreCase(p1);
    }


    public static String format(String p1, Object[] p2) {
        return String.format(p1, p2);
    }


    public static String format(Locale p1, String p2, Object[] p3) {
        return String.format(p1, p2, p3);
    }


    public byte[] getBytes() {
        return mirrorString.getBytes();
    }


    public byte[] getBytes(String p1) throws UnsupportedEncodingException {
        return mirrorString.getBytes(p1);
    }


    public void getChars(int p1, int p2, char[] p3, int p4) {
        mirrorString.getChars(p1, p2, p3, p4);
    }


    public int hashCode() {
        return mirrorString.hashCode();
    }


    public int indexOf(int p1, int p2) {
        return mirrorString.indexOf(p1, p2);
    }


    public int indexOf(int p1) {
        return mirrorString.indexOf(p1);
    }


    public int indexOf(String p1) {
        return mirrorString.indexOf(p1);
    }


    public int indexOf(String p1, int p2) {
        return mirrorString.indexOf(p1, p2);
    }


    public String intern() {
        return mirrorString.intern();
    }


    public int lastIndexOf(int p1) {
        return mirrorString.lastIndexOf(p1);
    }


    public int lastIndexOf(int p1, int p2) {
        return mirrorString.lastIndexOf(p1, p2);
    }


    public int lastIndexOf(String p1, int p2) {
        return mirrorString.lastIndexOf(p1, p2);
    }


    public int lastIndexOf(String p1) {
        return mirrorString.lastIndexOf(p1);
    }


    public int length() {
        return mirrorString.length();
    }


    public boolean matches(String p1) {
        return mirrorString.matches(p1);
    }


    public int offsetByCodePoints(int p1, int p2) {
        return mirrorString.offsetByCodePoints(p1, p2);
    }


    public boolean regionMatches(int p1, String p2, int p3, int p4) {
        return mirrorString.regionMatches(p1, p2, p3, p4);
    }


    public boolean regionMatches(boolean p1, int p2, String p3, int p4, int p5) {
        return mirrorString.regionMatches(p1, p2, p3, p4, p5);
    }


    public String replace(CharSequence p1, CharSequence p2) {
        return mirrorString.replace(p1, p2);
    }


    public String replace(char p1, char p2) {
        return mirrorString.replace(p1, p2);
    }


    public String replaceAll(String p1, String p2) {
        return mirrorString.replaceAll(p1, p2);
    }


    public String replaceFirst(String p1, String p2) {
        return mirrorString.replaceFirst(p1, p2);
    }


    public String[] split(String p1) {
        return mirrorString.split(p1);
    }


    public String[] split(String p1, int p2) {
        return mirrorString.split(p1, p2);
    }


    public boolean startsWith(String p1) {
        return mirrorString.startsWith(p1);
    }


    public boolean startsWith(String p1, int p2) {
        return mirrorString.startsWith(p1, p2);
    }


    public CharSequence subSequence(int p1, int p2) {
        return mirrorString.subSequence(p1, p2);
    }


    public String substring(int p1, int p2) {
        return mirrorString.substring(p1, p2);
    }


    public String substring(int p1) {
        return mirrorString.substring(p1);
    }


    public char[] toCharArray() {
        return mirrorString.toCharArray();
    }


    public String toLowerCase() {
        return mirrorString.toLowerCase();
    }


    public String toLowerCase(Locale p1) {
        return mirrorString.toLowerCase(p1);
    }


    public String toString() {
        return mirrorString.toString();
    }


    public String toUpperCase() {
        return mirrorString.toUpperCase();
    }


    public String toUpperCase(Locale p1) {
        return mirrorString.toUpperCase(p1);
    }


    public String trim() {
        return mirrorString.trim();
    }


    public static String valueOf(char[] p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(char p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(int p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(long p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(float p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(double p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(Object p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(boolean p1) {
        return String.valueOf(p1);
    }


    public static String valueOf(char[] p1, int p2, int p3) {
        return String.valueOf(p1, p2, p3);
    }
}
