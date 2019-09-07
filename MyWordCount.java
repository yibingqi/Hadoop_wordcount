import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MyWordCount {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{
    private final static IntWritable one = new IntWritable(1);

    private Text word = new Text();
    private boolean caseSensitive = false;

	private static final String[] stopWords = {"a","about","all","above","after","again","against","am","an","and",
		"any","are","aren't","as","at","be","because","been","before","being","below",
		"between","both","but","by","can't","cannot","could","couldn't","did",
		"didn't","do","does","doesn't","doing","don't","down","during","each",
		"few","for","from","further","had","hadn't","has","hasn't","have","haven't",
		"having","he","he'd","he'll","he's","her","here","here's","hers","herself",
		"him","himself","his","how","how's","i","i'd","i'll","i'm","i've","if",
		"in","into","is","isn't","it","it's","its","itself","let's","me","more",
		"most","mustn't","my","myself","no","nor","not","of","off","on","once",
		"only","or","other","ought","our","ours","ourselves","out","over","own",
		"same","shan't","she","she'd","she'll","she's","should","shouldn't","so",
		"some","such","than","that","that's","the","their","theirs","them","themselves",
		"then","there","there's","these","they","they'd","they'll","they're","they've",
		"this","those","through","to","too","under","up","very","was","wasn't",
		"we","we'd","we'll","we're","we've","were","weren't","what","what's","when",
		"when's","where","where's","which","while","who","who's","whom","why","why's",
	    "with","won't","would","wouldn't","you","you'd","you'll","you're","you've",
	    "your","yours","yourself","yourselves"};
	static Set<String> stopWordsSet = new HashSet<>();
	 static {
		  for (String s : stopWords) {
		   stopWordsSet.add(s);
		  }
		 }
	public boolean isStopWord(String s) {
		   String s1 = s; 
		   return stopWordsSet.contains(s1.toLowerCase());
		  }

    @Override
    protected void setup(Context context)
            throws IOException, InterruptedException {

        Configuration conf = context.getConfiguration();
        caseSensitive = conf.getBoolean("case.sensitive", false);
    }
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
    	String line = caseSensitive ? value.toString().replaceAll("[^a-zA-Z0-9 ]", " ") : value.toString().replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase();
    	StringTokenizer itr = new StringTokenizer(line);


      while (itr.hasMoreTokens()) {
    	String words = itr.nextToken();
    	if (isStopWord(words)){continue;}
        word.set(words);
        context.write(word, one);
      }
      
      
    }
  }

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    /*static enum ReduceCounters {
        UNIQUE_WORDS_COUNTED
    }*/
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
      //context.getCounter(ReduceCounters.UNIQUE_WORDS_COUNTED).increment(1L);
    }
  }

  public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    //job.setNumReduceTasks(Integer.parseInt(args[0]));
    FileInputFormat.addInputPaths(job, new String(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}