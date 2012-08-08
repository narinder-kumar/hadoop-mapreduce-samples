package com.mercris.hadoop.mapreduce.samples.useraccess;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class UserAccessCount {

  public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, LongWritable> {
    private Text word = new Text();
    public void map(LongWritable key, Text value, OutputCollector<Text, LongWritable> output, Reporter reporter) throws IOException {
      String line = value.toString();
      String[] inputArray = line.split("\\s+");
      word.set(inputArray[0]);
      output.collect(word, new LongWritable(Long.parseLong(inputArray[1])));
    }
  }

  public static class Reduce extends MapReduceBase implements Reducer<Text, LongWritable, Text, LongWritable> {

    public static long startTime;
    public static long endTime;
    
    @Override
    public void configure(JobConf jobConf) {
      startTime = Long.parseLong(jobConf.get("startTime"));
      endTime = Long.parseLong(jobConf.get("endTime"));
    }
    
    public void reduce(Text key, Iterator<LongWritable> values, OutputCollector<Text, LongWritable> output, Reporter reporter) throws IOException {
      int sum = 0;
      while (values.hasNext()) {
        long userLoggedInTime = values.next().get();
        if (userLoggedInTime >= startTime && userLoggedInTime <= endTime) {
          ++sum;
        }
      }
      if (sum > 0) {
        output.collect(key, new LongWritable(sum));
      }
    }
  }

  public static void main(String[] args) throws Exception {
    JobConf conf = new JobConf(UserAccessCount.class);

    if (args.length != 4) {
      System.err.println("startTime and endTime parameters must be specified");
      System.exit(0);      
    }

    String startTime = args[2];
    String endTime = args[3];

    conf.set("startTime", startTime);
    conf.set("endTime", endTime);
    conf.setJobName("useraccesscount");

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(LongWritable.class);

    conf.setMapperClass(Map.class);
    conf.setReducerClass(Reduce.class);

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.setInputPaths(conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(conf, new Path(args[1]));

    JobClient.runJob(conf);
  }
}
