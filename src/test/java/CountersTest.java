import hw1.mapper.HW1Mapper;
import hw1.utils.Counter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class CountersTest {

    private MapDriver<LongWritable, Text, Text, Text> mapDriver;

    private final String testBadData = "mama mila ramu";
    private final String testData = "2021-04-02 11:07:26.857439,warning,4";

    @Before
    public void setUp() {
        HW1Mapper mapper = new HW1Mapper();
        mapDriver = MapDriver.newMapDriver(mapper);
    }

    @Test
    public void testMapperCounterOne() throws IOException  {
        mapDriver
                .withInput(new LongWritable(), new Text(testBadData))
                .runTest();
        assertEquals("Expected 1 counter increment", 1, mapDriver.getCounters()
                .findCounter(Counter.BAD_ROW).getValue());
    }

    @Test
    public void testMapperCounterZero() throws IOException {
        mapDriver
                .withInput(new LongWritable(), new Text(testData))
                .withOutput(new Text("2021-04-02 11"), new Text("warning"))
                .runTest();
        assertEquals("Expected 1 counter increment", 0, mapDriver.getCounters()
                .findCounter(Counter.BAD_ROW).getValue());
    }

    @Test
    public void testMapperCounters() throws IOException {
        mapDriver
                .withInput(new LongWritable(), new Text(testData))
                .withInput(new LongWritable(), new Text(testBadData))
                .withInput(new LongWritable(), new Text(testBadData))
                .withOutput(new Text("2021-04-02 11"), new Text("warning"))
                .runTest();

        assertEquals("Expected 2 counter increment", 2, mapDriver.getCounters()
                .findCounter(Counter.BAD_ROW).getValue());
    }
}

