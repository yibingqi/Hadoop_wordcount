# Hadoop_wordcount2.0
#### This is modified hadoop mapreduce Wordount program to satisfied the following requiremnt:

   1. Counting frequencies for punctuations and stop words is generally not useful. Also, current code will consider same word differently    with and without punctuation e.g. "baig" and "baig." will be considered two separate words.
      * Remove *punctuations* from the text
      * Also, remove *stop words* from the text. A list of stop words can be found [here](https://www.ranks.nl/stopwords).
   2. The program accepts 4 positional *arguments*.
      * The number of reduce tasks to be used (number of reducers);
      * True/false *Case-sensitiveness*. if the argument is true, the program takes into consideration case sensitivity. If the argument is false, the program ignores case sensitivity.
      * The HDFS *input paths* in comma separated format. The program should be able to process multiple files or directories. The number of input paths can be large.
          *e.g. filename1, filename2, filename3 and etc.
      * The HDFS *output path* for your program. Notice that you should remove the output path after every execution of your program. MapReduce cannot start a job if there exists a precreated output directory.
      
      



