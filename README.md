
# LargeCsvSorter

Библиотека LargeCsvSorter на Java предоставляет класс LargeCsvSorter для эффективной сортировки больших CSV-файлов. Она делит входной файл на более мелкие сегменты, сортирует их по отдельности, а затем объединяет отсортированные сегменты в один отсортированный файл.

## Функциональные возможности
* Сортировка больших CSV-файлов путем разделения их на более мелкие фрагменты для эффективной обработки.
* Использование параллельной обработки для более быстрой сортировки отдельных сегментов (требуется Java 8 или более поздняя версия).
* Сортировка данных CSV на основе первого целого значения в каждой строке (при условии разделения запятыми).
## Использование

**1. Импорт библиотеки:**
```Java
import org.landsreyk.sorter.LargeCsvSorter;
```
**2. Создание объекта LargeCsvSorter:**
```Java
String workDir = "/путь/к/рабочей/папке";
String fileName = "ваш_файл.csv";

LargeCsvSorter sorter = new LargeCsvSorter(workDir, fileName);
```
**3. Сортировка CSV-файла:**
```Java
sorter.sort();
```
Это отсортирует CSV-файл, расположенный по адресу `workDir + fileName`, и сохранит отсортированный результат как `workDir + result.csv`. Временные файлы будут созданы в каталоге `workDir + temp/` во время процесса сортировки и очищены после него.
## Примечания

* Эта реализация предполагает, что в данных CSV есть целое значение в первом столбце, по которому выполняется сортировка.
* Вы можете изменить метод extractKey для обработки различных критериев сортировки в зависимости от вашего конкретного формата данных.

## Тестирование производительности
JMH-тесты производительности могут быть использованы для оценки производительности этой библиотеки для разных размеров файлов и конфигураций оборудования.

## Зависимости
У этой библиотеки нет внешних зависимостей.


## Автор

- [@landsreyk](https://github.com/AndrewKozyrev)
