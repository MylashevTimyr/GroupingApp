package com.example;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class GroupingApp {
	public static void main(String[] args) {
		// Проверка наличия аргументов командной строки
		if (args.length == 0) {
			System.out.println("Please provide the file path as an argument.");
			return;
		}

		BufferedReader reader = null;
		try (FileWriter fileWriter = new FileWriter("./output.txt", false)) {
			long startTime = System.currentTimeMillis();

			// Определение типа файла и его открытие
			if (args[0].endsWith(".gz")) {
				reader = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(new FileInputStream(args[0])), StandardCharsets.UTF_8));
			} else if (args[0].endsWith(".7z")) {
				SevenZFile sevenZFile = new SevenZFile(new File(args[0]));
				SevenZArchiveEntry entry = sevenZFile.getNextEntry();
				if (entry != null && !entry.isDirectory()) {
					reader = new BufferedReader(new InputStreamReader(sevenZFile.getInputStream(entry), StandardCharsets.UTF_8));
				}
			} else {
				reader = new BufferedReader(new FileReader(args[0]));
			}

			// Если файл не удалось открыть
			if (reader == null) {
				System.out.println("Failed to open the file.");
				return;
			}

			// Список групп строк
			List<Set<String>> groups = new ArrayList<>();
			// Список частей строк для определения принадлежности к группам
			List<Map<String, Integer>> parts = new ArrayList<>();

			String line = reader.readLine();
			while (line != null) {
				// Разбиваем строку на столбцы
				String[] columns = getColumnsOf(line);
				Integer groupNumber = null;

				// Определяем, к какой группе принадлежит строка
				for (int i = 0; i < Math.min(parts.size(), columns.length); i++) {
					Integer groupNumber2 = parts.get(i).get(columns[i]);
					if (groupNumber2 != null) {
						if (groupNumber == null) {
							groupNumber = groupNumber2;
						} else if (!Objects.equals(groupNumber, groupNumber2)) {
							// Объединение групп, если есть пересечения
							for (String line2 : groups.get(groupNumber2)) {
								groups.get(groupNumber).add(line2);
								apply(getColumnsOf(line2), groupNumber, parts);
							}
							groups.set(groupNumber2, new HashSet<>());
						}
					}
				}

				// Если строка не принадлежит ни к одной группе, создаем новую группу
				if (groupNumber == null) {
					if (Arrays.stream(columns).anyMatch(s -> !s.isEmpty())) {
						groups.add(new HashSet<>(List.of(line)));
						apply(columns, groups.size() - 1, parts);
					}
				} else {
					groups.get(groupNumber).add(line);
					apply(columns, groupNumber, parts);
				}
				line = reader.readLine();
			}
			reader.close();

			// Запись результатов в файл
			fileWriter.write("Number of groups with more than one element: " + groups.stream().filter(s -> s.size() > 1).count() + "\n");
			groups.sort(Comparator.comparingInt(s -> -s.size()));
			int i = 0;
			for (Set<String> group : groups) {
				if (!group.isEmpty()) {
					i++;
					fileWriter.write("\nGroup " + i + "\n");
					for (String val : group) {
						fileWriter.write(val + "\n");
					}
				}
			}

			long endTime = System.currentTimeMillis();
			System.out.println("Execution time (seconds): " + ((endTime - startTime) / 1000.0));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Метод для разбивки строки на столбцы с учетом кавычек
	private static String[] getColumnsOf(String line) {
		List<String> columns = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean inQuotes = false;
		for (char c : line.toCharArray()) {
			if (c == '\"') {
				inQuotes = !inQuotes;
			} else if (c == ';' && !inQuotes) {
				columns.add(sb.toString());
				sb.setLength(0);
			} else {
				sb.append(c);
			}
		}
		columns.add(sb.toString());
		return columns.toArray(new String[0]);
	}

	// Метод для обновления информации о частях строк для каждой группы
	private static void apply(String[] newValues, int groupNumber, List<Map<String, Integer>> parts) {
		for (int i = 0; i < newValues.length; i++) {
			if (newValues[i].isEmpty()) {
				continue;
			}
			if (i < parts.size()) {
				parts.get(i).put(newValues[i], groupNumber);
			} else {
				HashMap<String, Integer> map = new HashMap<>();
				map.put(newValues[i], groupNumber);
				parts.add(map);
			}
		}
	}
}
