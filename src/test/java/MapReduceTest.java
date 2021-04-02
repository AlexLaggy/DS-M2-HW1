import hw1.mapper.HW1Mapper;
import hw1.reducer.HW1Reducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapReduceTest {

    private MapDriver<LongWritable, Text, Text, Text> mapDriver;
    private ReduceDriver<Text, Text, Text, Text> reduceDriver;
    private MapReduceDriver<LongWritable, Text, Text, Text, Text, Text> mapReduceDriver;

    private final String testData = "2021-04-02 11:07:26.857439,warning,4";
    private final String testDataReducer = "2021-04-02 11";

    @Before
    public void setUp() {
        HW1Mapper mapper = new HW1Mapper();
        HW1Reducer reducer = new HW1Reducer();
        mapDriver = MapDriver.newMapDriver(mapper);
        reduceDriver = ReduceDriver.newReduceDriver(reducer);
        mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
    }

    @Test
    public void testMapper() throws IOException {
        mapDriver
                .withInput(new LongWritable(), new Text(testData))
                .withOutput(new Text("2021-04-02 11"), new Text("warning"))
                .runTest();
    }

    @Test
    public void testReducer() throws IOException {
        Map<String, Integer> mapping = new HashMap<>();
        List<Text> values = new ArrayList<Text>();
        values.add(new Text("warning"));
        values.add(new Text("error"));
        mapping.put("warning", 1);
        mapping.put("error", 1);
        reduceDriver
                .withInput(new Text(testDataReducer), values)
                .withOutput(new Text("2021-04-02 11"), new Text(mapping.toString()))
                .runTest();
    }

    @Test
    public void testMapReduce() throws IOException {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("warning", 2);
        mapReduceDriver
                .withInput(new LongWritable(), new Text(testData))
                .withInput(new LongWritable(), new Text(testData))
                .withOutput(new Text("2021-04-02 11"), new Text(mapping.toString()))
                .runTest();
    }
}