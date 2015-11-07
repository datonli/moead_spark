package mr;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.LineReader;

/**
 * duplicate using input file as multi-files input.
 * 
 * @author Datong Li
 * @date 2015-5-4
 */
public class MyFileInputFormat extends FileInputFormat<LongWritable, Text> //implements JobConfigurable 
{

	public static final String READFILETIME = "READFILETIMES";
	public static void setReadFileTime(JobConf job, int numLines) {
		job.setInt(READFILETIME, numLines);
	}

	public static int getNumLinesPerSplit(JobConf job) {
		return job.getInt(READFILETIME, 1);
	}
	public RecordReader<LongWritable,Text> getRecordReader(
		InputSplit genericSplit, JobConf job,
	    Reporter reporter
	) 
	throws IOException{
		String recordDelimiterStr = "";
		byte[] recordDelimiterBytes = recordDelimiterStr.getBytes("UTF-8");
		FileSplit split = (FileSplit) genericSplit;
		System.out.println("Enter getRecordReader!!!");
		return new MyFileRecordReader2(job,split,recordDelimiterBytes);
	}

	protected boolean isSplitable(JobContext context, Path filename) {
		return false;
	}

	public List<InputSplit> getSplits(JobConf job) throws IOException {
		List<InputSplit> splits = new ArrayList<InputSplit>();
		for (FileStatus status : listStatus(job)) {
			splits.addAll(getSplitsForFile(status, job,
					getNumLinesPerSplit(job)
					));
		}
		return splits;
	}

	private Collection<? extends InputSplit> getSplitsForFile(
			FileStatus status, Configuration conf, int readTimeNum)
			throws IOException {
		List<FileSplit> splits = new ArrayList<FileSplit>();
		Path fileName = status.getPath();
		FileSystem fs = fileName.getFileSystem(conf);
		LineReader lr = null;
		try {
			FSDataInputStream in = fs.open(fileName);
			lr = new LineReader(in, conf);
			Text line = new Text();
			long begin = 0;
			long length = 0;
			int num = -1;
			while ((num = lr.readLine(line)) > 0) {
				length += num;
			}
			System.out.println("getSplitForFile Enter!!!");
			for (int i = 0; i < readTimeNum; i++)
				splits.add(createFileSplit(fileName, begin, length));
		} finally {
			if (lr != null) {
				lr.close();
			}
		}
		return splits;
	}

	private FileSplit createFileSplit(Path fileName, long begin, long length) {
		return new FileSplit(fileName, begin, length - 1, new String[] {});
	}

}
