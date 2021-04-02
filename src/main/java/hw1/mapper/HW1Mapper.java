package hw1.mapper;

import hw1.utils.Counter;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Mapper class
 * Input key {@link LongWritable}
 * Input value {@link Text}
 * Output key {@link Text}
 * Output value {@link Text}
 */
public class HW1Mapper extends org.apache.hadoop.mapreduce.Mapper<LongWritable, Text, Text, Text> {
    /**
     * Regex pattern to split csv row
     */
    private final static Pattern splitStr = Pattern.compile(",");

    /**
     * Regex pattern to split hours
     */
    private final static Pattern hourSplit = Pattern.compile(":");


    private final static Map<Integer, String> dict = new HashMap<Integer, String>(){{
        put(7, "debug");
        put(6, "info");
        put(5, "notice");
        put(4, "warning");
        put(3, "error");
        put(2, "crit");
        put(1, "alert");
        put(0, "panic");
    }};


    /**
     * Map function. Truncates timestamp according to interval for aggregating. Checks data for correctness.
     * Uses counters {@link Counter}
     * @param key input key
     * @param value input value
     * @param context mapper context
     * @throws IOException from context.write()
     * @throws InterruptedException from context.write()
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        int code;
        String timestamp;
        try {
            String [] data = splitStr.split(value.toString());
            String status = "";
            if (data.length == 3) {
                timestamp = hourSplit.split(data[0])[0];
                code = Integer.parseInt(data[2]);
                status = dict.get(code);
                context.write(new Text(timestamp), new Text(status));
            }
            else context.getCounter(Counter.BAD_ROW).increment(1);
        }
        catch (RuntimeException error){
            context.getCounter(Counter.BAD_ROW).increment(1);
        }
    }
}
