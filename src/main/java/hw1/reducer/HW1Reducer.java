package hw1.reducer;

import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reducer class
 * Input key {@link Text}
 * Input value {@link Text}
 * Output key {@link Text}
 * Output value {@link Text}
 */
public class HW1Reducer extends org.apache.hadoop.mapreduce.Reducer<Text, Text, Text, Text> {

    /**
     * Reduce function. Calculates average value in given interval
     * @param key key
     * @param values iterable of values
     * @param context reducer context
     * @throws IOException in context.write()
     * @throws InterruptedException in context.write()
     */
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        Map<String, Integer> mapping = new HashMap<>();
        String status;
        while (values.iterator().hasNext()) {
            status = values.iterator().next().toString();
            if (!mapping.containsKey(status)){
                mapping.put(status, 1);
            }
            else mapping.put(status, mapping.get(status) + 1);
        }
        context.write(key, new Text(mapping.toString()));
    }
}
