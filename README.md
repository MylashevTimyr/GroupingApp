GroupingApp
Это приложение было создано как тестовое задание и добавлено в портфолио.

Описание
GroupingApp — это Java-приложение для обработки текстовых файлов, содержащих строки вида A1;B1;C1. 
Основная задача приложения заключается в нахождении уникальных строк и их разбиении на непересекающиеся группы. 
Если две строки имеют совпадающие непустые значения в одной или более колонках, они принадлежат одной группе.

Основные задачи
1) Считать файл:
Поддержка форматов .gz и .7z.
Открытие и чтение строк из файлов.

2) Найти множество уникальных строк:
Игнорирование некорректных строк.

3) Разбить строки на группы:
Группировка строк на основе совпадающих значений в колонках.
Объединение строк в группы, если они имеют совпадающие значения в одной или более колонках.

4) Вывести группы в файл:
Запись результатов в файл output.txt.
Указание количества групп с более чем одним элементом.
Сортировка групп по размеру в порядке убывания.

Требования
1) Допустимое время работы: до 30 секунд.
2) Максимальное потребление памяти: 1 ГБ (запуск с ограничением по памяти -Xmx1G).
3) Программа должна игнорировать некорректные строки.
4) Дублирующиеся строки в одной группе не выводятся.

Необходимые файлы
Перед запуском приложения скачайте необходимые файлы по следующим ссылкам:
https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz
https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz

Сборка и запуск
Сборка проекта
Для сборки проекта используйте Maven. Выполните команду:
mvn clean package

Запуск приложения
Для запуска приложения используйте следующие команды:
java -Xmx1G -jar target/GroupingApp-1.0-SNAPSHOT.jar lng-4.txt.gz
java -Xmx1G -jar target/GroupingApp-1.0-SNAPSHOT.jar lng-big.7z

Формат ввода
Пример входных данных:
A1;B1;C1
A2;B2;C2
A3;B3
...

Пример группировки
Строки:
111;123;222
200;123;100
300;;100
принадлежат одной группе, так как первые две строки имеют одинаковое значение 123 во второй колонке,
а две последние — одинаковое значение 100 в третьей колонке.

Строки:
100;200;300
200;300;100
не должны быть в одной группе, так как значение 200 находится в разных колонках.

Формат вывода
Полученные группы выводятся в файл в следующем формате:

Группа 1
строчка1
строчка2
строчка3

Группа 2
строчка1
строчка2
строчка3
В начале вывода указывается количество групп с более чем одним элементом. Группы располагаются в порядке убывания размера.

Пример использования
Сборка проекта:
mvn clean package
Запуск приложения для файла lng-4.txt.gz:
java -Xmx1G -jar target/GroupingApp-1.0-SNAPSHOT.jar lng-4.txt.gz

Запуск приложения для файла lng-big.7z:
java -Xmx1G -jar target/GroupingApp-1.0-SNAPSHOT.jar lng-big.7z
Результаты выполнения будут записаны в файл output.txt в текущей директории.