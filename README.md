# Hadoop_wordcount2.0
#### This is modified hadoop mapreduce Wordount program to satisfied the following requiremnt:

   1. Counting frequencies for punctuations and stop words is generally not useful. Also, current code will consider same word differently    with and without punctuation e.g. "baig" and "baig." will be considered two separate words.
      * Remove *punctuations* from the text
      * Also, remove *stop words* from the text. A list of stop words can be found [here](https://www.ranks.nl/stopwords).
   2. The program accepts 4 positional *arguments*.
      * The number of reduce tasks to be used (number of reducers);
      * True/false *Case-sensitiveness*. if the argument is true, the program takes into consideration case sensitivity. If the argument is false, the program ignores case sensitivity.
      *



