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

}
