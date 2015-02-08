import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLCleaner {

	/**
	 * Format all freya htmls (add new line) and save in a new directory
	 *
	 * @param readPath directory path where are all htmls files
	 * @param savePath directory path where we save formated htmls
	 *
	 * @throws IOException
	 */
	public static void formatHtmls(final String readPath, final String savePath) throws IOException {
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
			final FileWriter w = new FileWriter(savePath + File.separator + file.getName());
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
						if (buff.equalsIgnoreCase("<br>") || buff.equalsIgnoreCase("<br1>")) {
							line += buff + '\n';
							w.write(line.replace(" \n", "\n"));
							line = "";
							buff = "";
						} else if (buff.equalsIgnoreCase("</body>")) {
							line += '\n';
							w.write(line.replace(" \n", "\n") + buff + "</html>");
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

}
