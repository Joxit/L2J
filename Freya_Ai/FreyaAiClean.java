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
import java.util.ArrayList;

/**
 * @author Joxit
 */
public class FreyaAiClean {
	final static String freya_srcipt_dir = "freya_scripts";
	final static String freya_aiScript = freya_srcipt_dir + File.separator + "ai-freya-symbol.txt";
	final static String freya_npc_pch = freya_srcipt_dir + File.separator + "npc_pch.txt";
	final static String freya_quest_pch = freya_srcipt_dir + File.separator + "quest_pch.txt";
	final static String freya_item_pch = freya_srcipt_dir + File.separator + "item_pch.txt";
	final static String freya_html_dir = freya_srcipt_dir + File.separator + "html-en";

	public static void main(final String args[]) throws IOException {
		final String Q0025name = PchFinder.getNameById(freya_quest_pch, "25");
		//getItemsIdsByQuest(PchFinder.getNameById(freya_quest_pch, "25"));
		printNpcsIdsByQuest(freya_aiScript, Q0025name);
		aiByQuest(freya_aiScript, freya_srcipt_dir + "Q0025_" + Q0025name, Q0025name);
	}

	public static void aiByName(final String freya_aiScript, final String save_dir,
			final ArrayList<String> name) throws IOException {
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
				if ((statut == 2) && name.contains(buff)) {
					System.out.println(buff);
					final FileWriter w = new FileWriter(save_dir + File.separator + buff + ".c");

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
	}

	/**
	 * Give all the npc in relationship with this quest name from the Freya AI
	 * and save their scripts in a special folder.
	 *
	 * @param freya_aiScript path of the freya ai script
	 * @param save path where we will save all files (folder like Q[id]_[name]
	 *            for exemple)
	 * @param quest name of the quest (same as quest_pch)
	 * @throws IOException
	 */
	public static void aiByQuest(final String freya_aiScript, final String save, final String quest)
			throws IOException {
		final ArrayList<String> npcs = getQuestNpcs(freya_aiScript, quest);
		final File dir = new File(save);
		if (!dir.mkdir() && !dir.exists()) {
			System.out.println("File " + save + " does not exist");
			return;
		}
		aiByName(freya_aiScript, save, npcs);
	}

	/**
	 * Give all the npc IDs in relationship with this quest name. TODO
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
					if (items.contains(buff)) {
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
							if (Character.isLetterOrDigit(c) || (c == '_')) {
								buff += c;
							}
							break;
						case 2:
							if (Character.isDigit(c)) {
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

	/**
	 * @param quest where we want all npcs
	 * @return ArrayList of all npcs
	 */
	public static ArrayList<String> getQuestNpcs(final String freya_aiScript, final String quest)
			throws IOException {
		final FileReader r = new FileReader(freya_aiScript);
		String buff = "", name = "";
		final ArrayList<String> npcs = new ArrayList<String>();
		char c = (char) r.read();
		int statut = -1;
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
		r.close();
		statut = 0;
		return npcs;
	}

	/**
	 * Print all the npc IDs in relationship with this quest name.
	 *
	 * @param quest
	 * @throws IOException
	 */
	public static void printNpcsIdsByQuest(final String freya_aiScript, final String quest)
			throws IOException {
		final FileReader r = new FileReader(freya_npc_pch);
		String buff = "", name = "";
		final ArrayList<String> npcs;
		int statut = -1;
		System.out.println("Quete : " + quest);
		npcs = getQuestNpcs(freya_aiScript, quest);
		System.out.println(npcs);
		/* on affiche l'id de tous les npcs */
		while (r.ready()) {
			final char c = (char) r.read();
			switch (c) {
				case '[':
					buff = "";
					statut = 1;
					break;
				case ']':
					if (npcs.contains(buff)) {
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
							if (Character.isLetterOrDigit(c) || (c == '_')) {
								buff += c;
							}
							break;
						case 2:
							if (Character.isDigit(c)) {
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
	}
}
