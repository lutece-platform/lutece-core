package fr.paris.lutece.util.parser;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.paris.lutece.portal.business.editor.ParserComplexElement;
import fr.paris.lutece.portal.business.editor.ParserElement;
/**
 * 
 *  This class provides parser utils  for BBCODE parsing
 *
 */
public class BbcodeUtil {

	private static final String CR_LF = "(?:\r\n|\r|\n)?";


	/**
	 * Parse BBCODE text and return HTML text
	 * @param value the value of the text
	 * @param listParserElement the list of simple parser element using for parsing
	 * @param listParserComplexElement the list of complex  parser element using for parsing
	 * @return HTML Text
	 */
	public static String parse(String value,
			List<ParserElement> listParserElement,
			List<ParserComplexElement> listParserComplexElement) {
		value = value.replaceAll("(\r\n|\n\r|\n|\r)", "<br>");

		StringBuffer buffer = new StringBuffer(value);

		// complex Element

		if (listParserComplexElement != null) {
			for (ParserComplexElement element : listParserComplexElement) {

				processNestedTags(buffer, element.getTagName(), element
						.getOpenSubstWithParam(), element
						.getCloseSubstWithParam(), element
						.getOpenSubstWithoutParam(), element
						.getCloseSubstWithoutParam(), element
						.getInternalSubst(), element.isProcessInternalTags(),
						element.isAcceptParam(), element
								.isRequiresQuotedParam());

			}

		}

		String str = buffer.toString();

		// SimpleElement
		if (listParserElement != null) {
			for (ParserElement element : listParserElement) {
				str = str.replaceAll(element.getCode(), element.getValue());

			}

		}

		return str;
	}

	/**
	 * 
	 * Method using for parsing complex element
	 * @param buffer value to parse
	 * @param tagName tagName
	 * @param openSubstWithParam openSubstWithParam 
	 * @param closeSubstWithParam closeSubstWithParam
	 * @param openSubstWithoutParam openSubstWithoutParam
	 * @param closeSubstWithoutParam closeSubstWithoutParam
	 * @param internalSubst internalSubst
	 * @param processInternalTags processInternalTags
	 * @param acceptParam acceptParam
	 * @param requiresQuotedParam requiresQuotedParam
	 */
	private static void processNestedTags(StringBuffer buffer, String tagName,
			String openSubstWithParam, String closeSubstWithParam,
			String openSubstWithoutParam, String closeSubstWithoutParam,
			String internalSubst, boolean processInternalTags,
			boolean acceptParam, boolean requiresQuotedParam) {
		String str = buffer.toString();

		Stack openStack = new Stack();
		Set subsOpen = new HashSet();
		Set subsClose = new HashSet();
		Set subsInternal = new HashSet();

		String openTag = CR_LF
				+ "\\["
				+ tagName
				+ (acceptParam ? (requiresQuotedParam ? "(?:=\"(.*?)\")?"
						: "(?:=\"?(.*?)\"?)?") : "") + "\\]" + CR_LF;
		String closeTag = CR_LF + "\\[/" + tagName + "\\]" + CR_LF;
		String internTag = CR_LF + "\\[\\*\\]" + CR_LF;

		String patternString = "(" + openTag + ")|(" + closeTag + ")";

		if (processInternalTags) {
			patternString += "|(" + internTag + ")";
		}

		Pattern tagsPattern = Pattern.compile(patternString);
		Matcher matcher = tagsPattern.matcher(str);

		int openTagGroup;
		int paramGroup;
		int closeTagGroup;
		int internalTagGroup;

		if (acceptParam) {
			openTagGroup = 1;
			paramGroup = 2;
			closeTagGroup = 3;
			internalTagGroup = 4;
		} else {
			openTagGroup = 1;
			paramGroup = -1; // INFO
			closeTagGroup = 2;
			internalTagGroup = 3;
		}

		while (matcher.find()) {
			int length = matcher.end() - matcher.start();
			MutableCharSequence matchedSeq = new MutableCharSequence(str,
					matcher.start(), length);

			// test opening tags
			if (matcher.group(openTagGroup) != null) {
				if (acceptParam && (matcher.group(paramGroup) != null)) {
					matchedSeq.param = matcher.group(paramGroup);
				}

				openStack.push(matchedSeq);

				// test closing tags
			} else if ((matcher.group(closeTagGroup) != null)
					&& !openStack.isEmpty()) {
				MutableCharSequence openSeq = (MutableCharSequence) openStack
						.pop();

				if (acceptParam) {
					matchedSeq.param = openSeq.param;
				}

				subsOpen.add(openSeq);
				subsClose.add(matchedSeq);

				// test internal tags
			} else if (processInternalTags
					&& (matcher.group(internalTagGroup) != null)
					&& (!openStack.isEmpty())) {
				subsInternal.add(matchedSeq);
			} else {
				// assert (false);
			}
		}

		LinkedList subst = new LinkedList();
		subst.addAll(subsOpen);
		subst.addAll(subsClose);
		subst.addAll(subsInternal);

		Collections.sort(subst, new Comparator() {
			public int compare(Object o1, Object o2) {
				MutableCharSequence s1 = (MutableCharSequence) o1;
				MutableCharSequence s2 = (MutableCharSequence) o2;
				return -(s1.start - s2.start);
			}
		});

		buffer.delete(0, buffer.length());

		int start = 0;

		while (!subst.isEmpty()) {
			MutableCharSequence seq = (MutableCharSequence) subst.removeLast();
			buffer.append(str.substring(start, seq.start));

			if (subsClose.contains(seq)) {
				if (seq.param != null) {
					buffer.append(closeSubstWithParam);
				} else {
					buffer.append(closeSubstWithoutParam);
				}
			} else if (subsInternal.contains(seq)) {
				buffer.append(internalSubst);
			} else if (subsOpen.contains(seq)) {
				Matcher m = Pattern.compile(openTag).matcher(
						str.substring(seq.start, seq.start + seq.length));

				if (m.matches()) {
					if (acceptParam && (seq.param != null)) {
						buffer.append( //
								openSubstWithParam.replaceAll(
										"\\{BBCODE_PARAM\\}", seq.param));
					} else {
						buffer.append(openSubstWithoutParam);
					}
				}
			}

			start = seq.start + seq.length;
		}

		buffer.append(str.substring(start));
	}

	/**
	 * 
	 * Inner class MutableCharSequence
	 *
	 */
	static class MutableCharSequence implements CharSequence {
		/** */
		public CharSequence base;

		/** */
		public int start;

		/** */
		public int length;

		/** */
		public String param = null;

		/**
	         */
		public MutableCharSequence() {
			//
		}

		/**
		 * @param base
		 * @param start
		 * @param length
		 */
		public MutableCharSequence(CharSequence base, int start, int length) {
			reset(base, start, length);
		}

		/**
		 * @see java.lang.CharSequence#length()
		 */
		public int length() {
			return this.length;
		}

		/**
		 * @see java.lang.CharSequence#charAt(int)
		 */
		public char charAt(int index) {
			return this.base.charAt(this.start + index);
		}

		/**
		 * @see java.lang.CharSequence#subSequence(int, int)
		 */
		public CharSequence subSequence(int pStart, int end) {
			return new MutableCharSequence(this.base, this.start + pStart,
					this.start + (end - pStart));
		}

		/**
		 * @param pBase
		 * @param pStart
		 * @param pLength
		 * @return -
		 */
		public CharSequence reset(CharSequence pBase, int pStart, int pLength) {
			this.base = pBase;
			this.start = pStart;
			this.length = pLength;

			return this;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer();

			for (int i = this.start; i < (this.start + this.length); i++) {
				sb.append(this.base.charAt(i));
			}

			return sb.toString();
		}

	}

}
