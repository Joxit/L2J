import java.util.Arrays;

/*
 * Copyright (C) 2013-2015 Joxit
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
public class Util {
	public static boolean isIn(char c, Character... chars) {
		return chars == null ? false : Arrays.stream(chars).anyMatch(cur_char -> cur_char.equals(c));
	}

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
