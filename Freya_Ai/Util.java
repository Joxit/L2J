public class Util {
	public static boolean isDigit(final String text) {
		if ((text == null) || text.isEmpty()) {
			return false;
		}
		for (final char c : text.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isLetter(final String text) {
		if ((text == null) || text.isEmpty()) {
			return false;
		}
		for (final char c : text.toCharArray()) {
			if (!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean containsEndsWith(final String[] res, final String buff) {
		for (final String s : res) {
			if (buff.endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(final String[] res, final String buff) {
		for (final String s : res) {
			if (buff.equals(s)) {
				return true;
			}
		}
		return false;
	}

}
