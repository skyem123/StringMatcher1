package uk.co.skyem.random.stringMatcher1;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.*;

import static uk.co.skyem.random.stringMatcher1.MatcherFunctions.*;

public class MatcherTests extends TestCase {
	// These are here so I can reuse them without manually copying them!
	// DO NOT CHANGE!
	private final String testString1 = "this is a test of my thing";
	/*   0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25
	   | t | h | i | s |   | i | s |   | a |   | t | e | s | t |   | o | f |   | m | y |   | t | h | i | n | g |
	   0   1   2   3   4   5   6   7   8   9   10  11  12  13  14  15  16  17  18  19  20  21  22  23  24  25  26
	   */
	private final String matchString1 = "test";
	/*
		"  t   e   s   t  "
		 | t | e | s | t |
		 | 0 | 1 | 2 | 3 |
		 0   1   2   3   4

		Bottom line is where substring "slices" a string.
		This is also how my system works.h
	 */
	private final String testString2 = "this, is a test!\\E\\Q ... :D";
	private final String matchString2 = "!\\E\\Q ... :D";
	private final String testString3 = "this 'is' a test";
	private final String testString4 = "let's test things test again";
	/* |t|h|i|s| |'|i|s|'| |a| |t|e|s|t
	   0 1 2 3 4 5 6 7 8 9*/
	private final String matchRegex3 = "'.*'";
	private final String matchString4 = "not found here";


	public void testSimpleMatch() throws Exception {
		Matches matches = match(matchString1).useOn(testString1);
		// matches should contain only one match
		assertThat(matches).containsOnly(
				new Match(matchString1, 10, 14)
		);
		System.out.println("simple match passed.");
	}

	public void testSimpleMatchWithSpecialChars() throws Exception {
		Matches matches = match(matchString2).useOn(testString2);
		// matches should contain only one match
		assertThat(matches).containsOnly(
					new Match(matchString2, 15, 27)
		);
		System.out.println("simple match with special characters passed.");
	}

	public void testInsensitiveMatch() throws Exception {
		Matches matches = matchInsensitive(matchString1.toUpperCase()).useOn(testString1);
		// matches should contain only one match
		assertThat(matches).containsOnly(
				new Match(matchString1, 10, 14)
		);
		System.out.println("case insensitive simple match passed.");
	}

	public void testEverythingMatcher() throws Exception {
		Matches matches = matchAll().useOn(testString1);
		// matches should contain only one match
		assertThat(matches).containsOnly(
				new Match(testString1, 0, testString1.length())
		);
		System.out.println("global match passed.");
	}
	
	public void testEverythingMatcherBlank() throws Exception {
		Matches matches = matchAll().useOn("");
		// matches should contain only one match
		assertThat(matches).containsOnly(
				new Match("", 0, "".length())
		);
		System.out.println("global match with blank string passed.");
	}

	public void testRegexMatcher() throws Exception {
		Matches matches = matchRegex(matchRegex3).useOn(testString3);
		assertThat(matches).containsOnly(
			new Match("'is'", 5, 9)
		);
		System.out.println("regex match passed.");
	}

	public void testMatchAllIf_None() throws Exception {
		Matches matches = matchAllIf(match(matchString4)).useOn(testString3);
		assertThat(matches).containsOnly();
		System.out.println("match all if (false) passed.");
	}

	public void testMatchAllIf() throws Exception {
		Matches matches = matchAllIf(match(matchString1)).useOn(testString1);
		assertThat(matches).containsOnly(
				new Match(testString1, 0, testString1.length())
		);
		System.out.println("match all if (true) passed.");
	}

	public void testMatchAllIfExc() throws Exception {
		Matches matches = matchAllIfExc(match(matchString1)).useOn(testString1);
		assertThat(matches).containsOnly(
				new Match("this is a ", 0, 10),
				new Match(" of my thing", 14, 26)
		);
		System.out.println("match all if exclusive passed.");
	}

	// This has no shorthand function, as it is a NOP
	public void testStartEndNop() throws Exception {
		Matches matches = new StartEndMatcher(match(matchString1), false, false, false).useOn(testString1);
		assertThat(matches).containsOnly(
			new Match(matchString1, 10, 14)
		);
		System.out.println("StartEndMatcher passthrough NOP passed");
	}

	// This has no shorthand function, as it is a NOP
	public void testStartEndNoneNop() throws Exception {
		Matches matches = new StartEndMatcher(match(matchString1), false, false, true).useOn(testString1);
		assertThat(matches).isEmpty();
		System.out.println("StartEndMatcher passthrough NOP passed");
	}

	public void testStartsWith1() throws Exception {
		Matches matches = startsWith(match(matchString1)).useOn(testString4);
		assertThat(matches).containsOnly(
				new Match("test things test again", 6, 28)
		);
		System.out.println("starts with passed (1).");
	}

	public void testStartsWith2() throws Exception {
		Matches matches = startsWith(match(matchString1)).useOn(testString1);
		assertThat(matches).containsOnly(
				new Match("test of my thing", 10, 26)
		);
		System.out.println("starts with passed (2).");
	}

	public void testEndsWith1() throws Exception {
		Matches matches = endsWith(match(matchString1)).useOn(testString4);
		assertThat(matches).containsOnly(
			 new Match("let's test things test", 0, 22)
		);
		System.out.println("ends with passed (1).");
	}

	public void testEndsWith2() throws Exception {
		Matches matches = endsWith(match(matchString1)).useOn(testString1);
		assertThat(matches).containsOnly(
				new Match("this is a test", 0, 14)
		);
		System.out.println("ends with passed (2).");
	}

	public void testStartsWithExc1() throws Exception {
		Matches matches = startsWithExc(match(matchString1)).useOn(testString4);
		assertThat(matches).containsOnly(
				new Match(" things test again", 10, 28)
		);
		System.out.println("exclusive starts with passed (1).");
	}

	public void testStartsWithExc2() throws Exception {
		Matches matches = startsWithExc(match(matchString1)).useOn(testString1);
		assertThat(matches).containsOnly(
				new Match(" of my thing", 14, 26)
		);
		System.out.println("exclusive starts with passed (2).");
	}

	public void testEndsWithExc1() throws Exception {
		Matches matches = endsWithExc(match(matchString1)).useOn(testString4);
		assertThat(matches).containsOnly(
				new Match("let's test things ", 0, 18)
		);
		System.out.println("exclusive ends with passed (1).");
	}

	public void testEndsWithExc2() throws Exception {
		Matches matches = endsWithExc(match(matchString1)).useOn(testString1);
		assertThat(matches).containsOnly(
				new Match("this is a ", 0, 10)
		);
		System.out.println("exclusive ends with passed (2).");
	}

	public void testInverter() throws Exception {
		Matches matches = match(matchString1).invert().useOn(testString1);
		assertThat(matches).containsOnly(
			new Match("this is a ", 0, 10),
			new Match(" of my thing", 14, 26)
		);
		System.out.println("inverter works");
	}

	public void testMatcherEquals() throws Exception {
		assertThat(matchRegex(".")).isEqualTo(matchRegex("."));
		assertThat(matchRegex(".*")).isNotEqualTo(matchRegex("."));
		System.out.println("regex matcher equality works.");

		assertThat(new RegexMatcher(".*", true)).isEqualTo(matchAll());
		assertThat(matchRegex(".")).isNotEqualTo(matchAll());
		System.out.println("regex .* and matchAll equality works.");

		assertThat(match("hello").invert()).isEqualTo(match("hello").invert());
		assertThat(match("hello").invert()).isNotEqualTo(match("world").invert());
		System.out.println("inversion equality works");
	}

	public void testOtherEquals() throws Exception {
		assertThat(matchAll().useOn(testString1)).isEqualTo(matchAll().useOn(testString1));
		System.out.println("match list / array / collection equality works.");
	}
}

