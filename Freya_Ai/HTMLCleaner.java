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
		final File dir = new File(readPath);
		for (final File file : dir.listFiles()) {
			if ((i % 500) == 0) {
				System.out.println(i);
			}
			final FileReader r = new FileReader(file.getPath());
			final FileWriter w = new FileWriter(savePath + file.getName());
			String buff;
			buff = "";
			while (r.ready()) {
				final char c = (char) r.read();
				switch (c) {
					case '<':
						w.write(buff);
						buff = "" + c;
						break;
					case ' ':
						w.write(buff + c);
						buff = "";
						break;
					case '>':
						buff += c;
						if (buff.equalsIgnoreCase("<br>")) {
							w.write(buff + '\n');
							buff = "";
						} else if (buff.equalsIgnoreCase("</body>")) {
							w.write('\n' + buff);
							buff = "";
						} else if (buff.equalsIgnoreCase("<head>")
								|| buff.equalsIgnoreCase("</head>")) {
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
				w.flush();
			}
			r.close();
			w.close();
			i++;
		}
		System.out.println("total : " + i);
	}

}
