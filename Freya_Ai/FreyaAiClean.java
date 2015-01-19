/*
 * Copyright (C) 2013 Joxit This file is part of Joxit Projects. Joxit Projects
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version. Joxit
 * Projects is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;

/**
 * @author Joxit
 */
public class FreyaAIClean {
	final static String freya_srcipt_dir = "freya_scripts";
	final static String freya_aiScript = freya_srcipt_dir + File.separator + "ai-freya-symbol.txt";
	final static String freya_html_dir = freya_srcipt_dir + File.separator + File.separator + "html-en";
	final static String freya_npc_pch = freya_srcipt_dir + File.separator + File.separator + "npc_pch.txt";
	final static String freya_quest_pch = freya_srcipt_dir + File.separator + File.separator + "quest_pch.txt";
	final static String save_dir = "save";
	static boolean byQuest = false;
	final static String save_byQuest = "quests";
	final static String freya_item_pch = freya_srcipt_dir + File.separator + "item_pch.txt";

	public static void main(final String args[]) throws IOException {
		getItemsIdsByQuest(getQuestName("25"));
		//getNpcsIdsByQuest(getQuestName("25"));
		//aiByQuest(getQuestName("25"));
	}

	/**
	 * Format all freya htmls (add new line) and save in new directory
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

	/**
	 * Give the name of de quest by his id.
	 *
	 * @param id of the quest
	 * @return quest name which has this id (name found in quest_pch)
	 * @throws IOException
	 */
	public static String getQuestName(final String id) throws IOException {
		final FileReader r = new FileReader(freya_quest_pch);
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
					if (isNumber(c)) {
						quest[1] += c;
					}
					c = (char) r.read();
				}
				if (quest[1].equals(id)) {
					r.close();
					System.out.println("Quest name : " + quest[0]);
					return quest[0];
				}
			}
		}
		r.close();
		return null;
	}

	/**
	 * Give the id of de quest by his name.
	 *
	 * @param id of the quest
	 * @return quest name which has this id (name found in quest_pch)
	 * @throws IOException
	 */
	public static String getQuestId(final String name) throws IOException {
		final FileReader r = new FileReader(freya_quest_pch);
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
					if (isNumber(c)) {
						quest[1] += c;
					}
					c = (char) r.read();
				}
				if (quest[0].endsWith(name)) {
					r.close();
					return quest[1];
				}
			}
		}
		r.close();
		return null;
	}

	/**
	 * Give all names of npcs by their ids
	 *
	 * @param ids of npcs
	 * @return npcs names
	 * @throws IOException
	 */
	public static ArrayList<String> getNpcName(final String... ids) throws IOException {
		final FileReader r = new FileReader(freya_npc_pch);
		final ArrayList<String> res = new ArrayList<String>();
		while (r.ready()) {
			char c = (char) r.read();
			if (c == '[') {
				final String[] npc = new String[2];
				npc[0] = new String();
				c = (char) r.read();
				while ((c != ']') && r.ready()) {
					if (c != 0) {
						npc[0] += c;
					}
					c = (char) r.read();
				}
				npc[1] = new String();
				while ((c != '\n') && r.ready()) {
					if (isNumber(c)) {
						npc[1] += c;
					}
					c = (char) r.read();
				}
				System.out.println(npc[1]);
				if (containsEndsWith(ids, npc[1])) {
					res.add(npc[0]);
				}
			}
		}
		r.close();

		return res;
	}

	public static boolean isNumber(final char c) {
		if ((c >= '0') && (c <= '9')) {
			return true;
		}
		return false;
	}

	public static boolean isLetter(final char c) {
		if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))) {
			return true;
		}
		return false;
	}

	/**
	 * Give all the npc in relationship with this quest name from the Freya AI
	 * and save their scripts in a special folder.
	 *
	 * @param quest name of the quest (same as quest_pch)
	 * @throws IOException
	 */
	public static void aiByQuest(final String quest) throws IOException {
		final FileReader r = new FileReader(freya_aiScript);
		String buff = "", name = "";
		final ArrayList<String> npcs = new ArrayList<String>();
		char c = (char) r.read();
		int statut = -1;
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if (statut == 2) {
					name = buff;
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						if (c == '{') {
							parent++;
						}
					}
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						if ((c == ' ') || (c == '\n')) {
							if (buff.endsWith(quest)) {
								npcs.add(name);
								break;
							}
							buff = "";
						} else if (c == '{') {
							parent++;
						} else if (c == '}') {
							parent--;
						}
						if (buff.equals(" ")) {
							buff = "";
						}

						buff = buff + c;
					}
					statut = -1;
				}
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			c = (char) r.read();
		}
		r.close();
		save_byQuest += "\\" + "Q" + getQuestId(quest) + "_" + quest;
		final File dir = new File(save_byQuest);
		if (!dir.mkdir() && !dir.exists()) {
			System.out.println("File " + save_byQuest + " does not exist");
			return;
		}
		byQuest = true;
		aiByName(npcs);
	}

	/**
	 * Give all the npc IDs in relationship with this quest name.
	 *
	 * @param quest
	 * @throws IOException
	 */
	public static void getNpcsIdsByQuest(final String quest) throws IOException {
		final FileReader r = new FileReader(freya_aiScript);
		final FileReader r2 = new FileReader(freya_npc_pch);
		String buff = "", name = "";
		final ArrayList<String> npcs = new ArrayList<String>();
		char c = (char) r.read();
		int statut = -1;
		System.out.println("Quete : " + quest);
		/* on cherche tous les npcs de la quete */
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if (statut == 2) {
					name = buff;
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						if (c == '{') {
							parent++;
						}
					}
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						if ((c == ' ') || (c == '\n')) {
							if (buff.endsWith(quest)) {
								npcs.add(name);
								break;
							}
							buff = "";
						} else if (c == '{') {
							parent++;
						} else if (c == '}') {
							parent--;
						}
						if (buff.equals(" ")) {
							buff = "";
						}

						buff = buff + c;
					}
					statut = -1;
				}
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			c = (char) r.read();
		}
		statut = 0;
		System.out.println(npcs);
		/* on affiche l'id de tous les npcs */
		while (r2.ready()) {
			c = (char) r2.read();
			switch (c) {
				case '[':
					buff = "";
					statut = 1;
					break;
				case ']':
					if (contains(npcs, buff)) {
						name = buff;
						statut = 2;
						buff = "";
					} else {
						statut = 0;
					}
					break;
				default:
					switch (statut) {
						case 0:
							break;
						case 1:
							if (isNumber(c) || isLetter(c) || (c == '_')) {
								buff += c;
							}
							break;
						case 2:
							if (isNumber(c)) {
								buff += c;
							} else if ((c == '\n') || (c == '\r')) {
								System.out.println(name + " = " + buff);
								statut = 0;
							}
							break;
					}
					break;
			}
		}
		r.close();
		r2.close();
	}

	public static void aiByName(final ArrayList<String> name) throws IOException {
		final FileReader r = new FileReader(freya_aiScript);
		String buff = "";
		char c = (char) r.read();
		int statut = -1;
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				if ((statut == 2) && contains(name, buff)) {
					System.out.println(buff);
					FileWriter w;
					if (!byQuest) {
						w = new FileWriter(save_dir + File.separator + "npcs" + File.separator
								+ buff + ".c");
					} else {
						w = new FileWriter(save_byQuest + File.separator + buff + ".c");
					}

					w.write(buff);
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						w.write(c);
						if (c == '{') {
							parent++;
						}
					}
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						if (c == ' ') {
							c = (char) r.read();
							if (c == ' ') {
								w.write('\t');
							} else {
								w.write(" " + c);
							}
						} else {
							w.write(c);
						}
						if (c == '{') {
							parent++;
						} else if (c == '}') {
							parent--;
						}
						w.flush();
					}
					w.write('\n');
					statut = -1;
					w.close();
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			c = (char) r.read();
		}
		r.close();
		byQuest = false;
		save_byQuest = save_dir;
	}

	private static boolean containsEndsWith(final String[] res, final String buff) {
		for (final String s : res) {
			if (buff.endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	private static boolean contains(final AbstractCollection<String> res, final String buff) {
		for (final String s : res) {
			if (s.equals(buff)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Give all the npc IDs in relationship with this quest name.
	 *
	 * @param quest
	 * @throws IOException
	 */
	public static void getItemsIdsByQuest(final String quest) throws IOException {
		FileReader r = new FileReader(freya_aiScript);
		final FileReader r2 = new FileReader(freya_item_pch);
		String buff = "", name = "";
		final ArrayList<String> npcs = new ArrayList<String>();
		final ArrayList<String> items = new ArrayList<String>();
		char c = (char) r.read();
		int statut = -1;
		System.out.println("Quete : " + quest);
		/* on cherche tous les npcs de la quete */
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if (statut == 2) {
					name = buff;
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						if (c == '{') {
							parent++;
						}
					}
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						if ((c == ' ') || (c == '\n')) {
							if (buff.endsWith(quest)) {
								npcs.add(name);
								break;
							}
							buff = "";
						} else if (c == '{') {
							parent++;
						} else if (c == '}') {
							parent--;
						}
						if (buff.equals(" ")) {
							buff = "";
						}

						buff = buff + c;
					}
					statut = -1;
				}
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			c = (char) r.read();
		}
		System.out.println(npcs);
		r.close();
		statut = -1;
		r = new FileReader(freya_aiScript);
		while (r.ready()) {
			if (((c == ' ') || (c == '\n') || (c == '>')) && !buff.equals(" ")) {
				if (statut == 3) {
					name = buff;
					int parent = 0;
					while (r.ready() && (parent == 0)) {
						c = (char) r.read();
						if (c == '{') {
							parent++;
						}
					}
					while (r.ready() && (parent != 0)) {
						c = (char) r.read();
						if ((c == ' ') || (c == '\n')) {
							if (buff.contains("Item")) {
								statut = 4;
							}
							buff = "";
						} else if (c == '{') {
							parent++;
						} else if (c == '}') {
							parent--;
						} else if ((statut == 4) && (c == '@')) {
							statut = 5;
							buff = "";
						}
						if ((statut == 5) && (c == ' ')) {
							items.add(buff);
						}
						if (buff.equals(" ")) {
							buff = "";
						}

						buff = buff + c;
					}
					statut = -1;
				}
				if ((statut == -1) && buff.endsWith("class")) {
					statut = 1;
				}
				if ((statut == 1) && buff.endsWith("0")) {
					statut = 2;
				}
				if ((statut == 2) && npcs.contains(buff)) {
					statut = 3;
				}
				buff = "";
			} else if (buff.equals(" ")) {
				buff = "";
			} else {
				buff = buff + c;
			}
			if ((statut == 2) && (c == '\n')) {
				statut = -1;
			}
			c = (char) r.read();
		}
		statut = 0;
		System.out.println(items);
		/* on affiche l'id de tous les npcs */
		while (r2.ready()) {
			c = (char) r2.read();
			switch (c) {
				case '[':
					buff = "";
					statut = 1;
					break;
				case ']':
					if (contains(items, buff)) {
						name = buff;
						statut = 2;
						buff = "";
					} else {
						statut = 0;
					}
					break;
				default:
					switch (statut) {
						case 0:
							break;
						case 1:
							if (isNumber(c) || isLetter(c) || (c == '_')) {
								buff += c;
							}
							break;
						case 2:
							if (isNumber(c)) {
								buff += c;
							} else if ((c == '\n') || (c == '\r')) {
								System.out.println(name + " = " + buff);
								statut = 0;
							}
							break;
					}
					break;
			}
		}
		r.close();
		r2.close();
	}
}
