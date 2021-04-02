package hw1;

import hw1.mapper.HW1Mapper;
import hw1.reducer.HW1Reducer;
import lombok.extern.log4j.Log4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

@Log4j
public class MapReduceApplication extends Configured implements Tool {

    /**
     * Entry point for the application
     *
     * @param args Optional arguments: InputDirectory, OutputDirectory, NumberOfReducers AggregationInterval
     * @throws Exception when ToolRunner.run() fails
     */
    public static void main(final String[] args) throws Exception {
        int result = ToolRunner.run(new Configuration(), new MapReduceApplication(), args);
        System.exit(result);
    }

    /**
     *
     * @param args additional command line arguments
     * @return 0 if jpb finished successfully
     * @throws Exception when error occured
     */
    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = getConf();
        configuration.set("mapreduce.output.textoutputformat.separator", ",");
        if (args.length != 3) {
            log.error("Usage: InputFileOrDirectory OutputDirectory NumReduceTasks");
            return 2;
        }
        String hdfsInputFileOrDirectory = args[0];
        String hdfsOutputDirectory = args[1];

        Job job = Job.getInstance(configuration);

        job.setJarByClass(getClass());
        job.setJobName(getClass().getName());

        job.setMapperClass(HW1Mapper.class);
        job.setReducerClass(HW1Reducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setNumReduceTasks(Integer.parseInt(args[2]));

        SequenceFileOutputFormat.setOutputCompressionType(job, SequenceFile.CompressionType.BLOCK);
        SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressorClass(job, org.apache.hadoop.io.compress.SnappyCodec.class);

        configuration.set("mapred.output.compression.codec","org.apache.hadoop.io.compress.SnappyCodec");

        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        FileInputFormat.addInputPath(job, new Path(hdfsInputFileOrDirectory));
        FileOutputFormat.setOutputPath(job, new Path((hdfsOutputDirectory)));

        log.info("=====================JOB STARTED=====================");
        job.waitForCompletion(true);
        log.info("=====================JOB ENDED=====================");
        Counter counter = job.getCounters().findCounter(hw1.utils.Counter.BAD_ROW);
        log.info("=====================COUNTER " + counter.getName() + ": "
                + counter.getValue() + "=====================");
        return 0;
    }
}