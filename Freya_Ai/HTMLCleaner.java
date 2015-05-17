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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class HTMLCleaner {

	/**
	 * Format all freya htmls (add new line) and save in a new directory
	 *
	 * @param readPath
	 *            directory path where are all htmls files
	 * @param savePath
	 *            directory path where we save formated htmls
	 *
	 * @throws IOException
	 */
	public static void formatHtmls(final String readPath, final String savePath)
			throws IOException {
		int i = 0;
		final File dir = new File(readPath + File.separator);
		char lastChar = '\0';
		String line = "";
		if (!dir.isDirectory()) {
			throw new IOException("readPath must be a directory.");
		}
		if (!new File(savePath).isDirectory()) {
			throw new IOException("savePath must be a directory.");
		}
		for (final File file : dir.listFiles()) {
			if ((i % 500) == 0) {
				System.out.println(i);
			}
			final FileReader r = new FileReader(file.getPath());
			final BufferedWriter w = new BufferedWriter(new FileWriter(savePath
					+ File.separator + file.getName()));
			String buff;
			buff = "";
			while (r.ready()) {
				final char c = (char) r.read();
				switch (c) {
					case '<':
						line += buff;
						buff = "" + c;
						break;
					case ' ':
						if (lastChar != ' ') {
							line += buff + c;
							buff = "";
						}
						break;
					case '>':
						buff += c;
						if (buff.equalsIgnoreCase("<br>")
								|| buff.equalsIgnoreCase("<br1>")) {
							line += buff + '\n';
							w.write(line.replace(" \n", "\n").replace(" <br",
									"<br"));
							line = "";
							buff = "";
						} else if (buff.equalsIgnoreCase("</body>")) {
							line += '\n';
							w.write(line.replace(" \n", "\n") + buff
									+ "</html>");
							line = "";
							buff = "";
						} else if (buff.equalsIgnoreCase("<head>")
								|| buff.equalsIgnoreCase("</head>")
								|| buff.equalsIgnoreCase("</html>")) {
							buff = "";
						}
						break;
					case '\n':
					case '\r':
						break;
					default:
						buff += c;
						break;
				}
				lastChar = c;
				w.flush();
			}
			r.close();
			w.close();
			i++;
		}
		System.out.println("total : " + i);
	}

	/**
	 * @param readPath
	 * @param savePath
	 * @param npcs
	 * @throws IOException
	 */
	public static void renameHtmls(final String readPath,
			final String savePath, final HashMap<String, String> npcs)
			throws IOException {
		final File dir = new File(readPath + File.separator);
		if (!dir.isDirectory()) {
			throw new IOException("readPath must be a directory.");
		}
		if (!new File(savePath).isDirectory()) {
			throw new IOException("savePath must be a directory.");
		}
		Arrays.stream(dir.listFiles()).forEach(
				file -> {
					final String name = file.getName().replaceAll(
							"(.*)_q\\d{4}_\\d+\\w*\\..*", "$1");
					final String num = file.getName().replaceAll(
							".*_q\\d{4}_(\\d+\\w*)\\..*", "$1");
					final String ext = file.getName().replaceAll(
							".*_q\\d{4}_\\d+\\w*\\.(.*)", "$1");
					if (npcs.containsKey(name)) {
						try {
							final String id = npcs.get(name).replaceFirst(
									"^10", "");
							final BufferedReader r = new BufferedReader(
									new FileReader(file.getPath()));
							final BufferedWriter w = new BufferedWriter(
									new FileWriter(savePath + File.separator
											+ id + "-" + num + "." + ext));
							r.lines().forEach(
									line -> {
										write(w, r, line, name
												+ "_q\\d{4}_(\\d+\\w*)\\.htm",
												id + "-$1.htm");
									});
							while (r.ready()) {

							}
							r.close();
							w.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private static void write(BufferedWriter w, BufferedReader r, String line,
			String regex, String remplacement) {
		try {
			w.write(line.replaceAll(regex, remplacement));
			// Prevent LF at EOF
			if (r.ready()) {
				w.newLine();
			}
			w.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
