import java.io.FileReader;
import java.io.IOException;

/**
 * Analyzer for files like item_pch, quest_pch...
 *
 * [name] = id
 *
 * @author Joxit
 *
 */
public class PchFinder {

	/**
	 * Give the name of something by his id.
	 *
	 * @param id of the quest
	 * @return quest name which has this id (name found in quest_pch)
	 * @throws IOException
	 */
	public static String getNameById(final String id, final String path) throws IOException {
		final FileReader r = new FileReader(path);
		while (r.ready()) {
			char c = (char) r.read();
			if (c == '[') {
				final String[] quest = new String[2];
				quest[0] = new String();
				c = (char) r.read();
				while ((c != ']') && r.ready()) {
					if (c != 0) {
						quest[0] += c;
					}
					c = (char) r.read();
				}
				quest[1] = new String();
				while ((c != '\n') && r.ready()) {
					if (Character.isDigit(c)) {
						quest[1] += c;
					}
					c = (char) r.read();
				}
				if (quest[1].equals(id)) {
					r.close();
					System.out.println("name : " + quest[0]);
					return quest[0];
				}
			}
		}
		r.close();
		return null;
	}

	/**
	 * Give the name of something by his id.
	 *
	 * @param id of the quest
	 * @return quest name which has this id (name found in quest_pch)
	 * @throws IOException
	 */
	public static String getIdByName(final String name, final String path) throws IOException {
		final FileReader r = new FileReader(path);
		while (r.ready()) {
			char c = (char) r.read();
			if (c == '[') {
				final String[] quest = new String[2];
				quest[0] = new String();
				c = (char) r.read();
				while ((c != ']') && r.ready()) {
					if (c != 0) {
						quest[0] += c;
					}
					c = (char) r.read();
				}
				quest[1] = new String();
				while ((c != '\n') && r.ready()) {
					if (Character.isDigit(c)) {
						quest[1] += c;
					}
					c = (char) r.read();
				}
				if (quest[0].equals(name)) {
					r.close();
					System.out.println("id : " + quest[0]);
					return quest[0];
				}
			}
		}
		r.close();
		return null;
	}

}
