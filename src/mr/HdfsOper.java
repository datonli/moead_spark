package mr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import mop.MopData;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapred.JobConf;

public class HdfsOper {

	// HDFS address
	//private static final String HDFS = "hdfs://192.168.1.102:9000/";
	private static final String HDFS = "hdfs://192.168.1.102:8020/";
	// hdfs path
	private String hdfsPath;
	// Hadoop configure
	private Configuration conf;

	public HdfsOper() {
		this(HDFS, config());
	}

	
	public HdfsOper(Configuration conf) {
		this(HDFS, conf);
	}

	public HdfsOper(String hdfs, Configuration conf) {
		this.hdfsPath = hdfs;
		this.conf = conf;
	}

	// load the configure files
	public static Configuration config() {
		Configuration conf = new JobConf(HdfsOper.class);
//		conf.addResource(new Path("/home/laboratory/hadoop-1.2.1/conf/core-site.xml"));
//		conf.addResource(new Path("/home/laboratory/hadoop-1.2.1/conf/hdfs-site.xml"));
//		conf.addResource(new Path("/home/laboratory/hadoop-1.2.1/conf/mapred-site.xml"));
//		conf.addResource("classpath:/hadoop/core-site.xml");
//		conf.addResource("classpath:/hadoop/hdfs-site.xml");
//		conf.addResource("classpath:/hadoop/mapred-site.xml");
		return conf;
	}

	/**
	 * hdfs api implements including :
	 * cat,ls,mkdirs,rm,copyFile,download,createFile
	 */

	public void cat(String remoteFile) throws IOException {
		Path path = new Path(remoteFile);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		FSDataInputStream fsdis = null;
		System.out.println("cat: " + remoteFile);
		try {
			fsdis = fs.open(path);
			IOUtils.copyBytes(fsdis, System.out, 4096, false);
		} finally {
			IOUtils.closeStream(fsdis);
			fs.close();
		}
	}

	public void cp(String file,String anotherFile) throws IOException {
		Path path = new Path(file);
		Path anotherPath = new Path(anotherFile);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath),conf);
		FSDataInputStream fsdis = null;
		FSDataOutputStream fsdos = null;
		if(fs.exists(path)){
			try {		
				fsdis = fs.open(path);
				fsdos = fs.create(anotherPath);
				IOUtils.copyBytes(fsdis, fsdos, 4096, false);
				System.out.println("The file " + file + " has copied as " + anotherFile + " !\n");
			} finally {
				IOUtils.closeStream(fsdis);
				IOUtils.closeStream(fsdos);
				fs.close();
			}
		} else {
			System.out.println("The file " + file + " doesn't exists ! copy failed !\n");		
		}
	}

	public void ls(String folder) throws IOException {
		Path path = new Path(folder);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		FileStatus[] list = fs.listStatus(path);
		System.out.println("ls: " + folder);
		System.out
				.println("==========================================================");
		for (FileStatus f : list) {
			System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(),
					f.isDir(), f.getLen());
		}
		System.out
				.println("==========================================================");
		fs.close();
	}

	public InputStreamReader open(String file) throws IOException {
		Path filePath = new Path(file);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		return (new InputStreamReader(fs.open(filePath)));
	}

	public void appendFile(String file,String content,int writeTime) throws IOException {
		Path filePath = new Path(file);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		byte[] buff = content.getBytes();
		byte[] enter = "\n".getBytes();
		FSDataOutputStream os = null;
		try {
			os = fs.append(filePath);
			
			for(int i = 0; i < writeTime; i ++){
				os.write(buff, 0, buff.length);
				os.write(enter, 0, enter.length);
			}
			System.out.println("Append: " + file);

		} finally {
			if (os != null)
				os.close();
		}
		fs.close();
	}
	public void appendFile(String file,String content) throws IOException {
		Path filePath = new Path(file);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		byte[] buff = content.getBytes();
		FSDataOutputStream os = null;
		try {
			os = fs.append(filePath);
			
			os.write(buff, 0, buff.length);
			System.out.println("Append: " + file);

		} finally {
			if (os != null)
				os.close();
		}
		fs.close();
	}
	
	public void mkdir(String folder,short replicationNum) throws IOException {
		Path path = new Path(folder);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		fs.setReplication(path,replicationNum);
		if (!fs.exists(path)) {
			fs.mkdirs(path);
			System.out.println("Create: " + folder);
		}
		fs.close();
	}
	
	public void mkdir(String folder) throws IOException {
		Path path = new Path(folder);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		fs.setReplication(path,(short)1);
		if (!fs.exists(path)) {
			fs.mkdirs(path);
			System.out.println("Create: " + folder);
		}
		fs.close();
	}

	public void rm(String folder) throws IOException {
		Path path = new Path(folder);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		if (fs.exists(path)) {
			fs.deleteOnExit(path);
			System.out.println("Delete: " + folder);
		}
		fs.close();
	}

	public void copyFile(String local, String remote) throws IOException {
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		fs.copyFromLocalFile(new Path(local), new Path(remote));
		System.out.println("copy from: " + local + " to " + remote);
		fs.close();
	}

	public void download(String remote, String local) throws IOException {
		Path path = new Path(remote);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		fs.copyToLocalFile(path, new Path(local));
		System.out.println("download: from" + remote + " to " + local);
		fs.close();
	}
	
	public void createFile(String file, String content, int writeTime) throws IOException {
		Path filePath = new Path(file);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		byte[] buff = content.getBytes();
		byte[] enter = "\n".getBytes();
		FSDataOutputStream os = null;
		try {
			os = fs.create(filePath);
			for(int i = 0; i < writeTime; i ++){
				os.write(buff, 0, buff.length);
				os.write(enter, 0, enter.length);
			}
			System.out.println("Create: " + file);

		} finally {
			if (os != null)
				os.close();
		}
		fs.close();
	}
	
	public void createFile(String file, String content) throws IOException {
		Path filePath = new Path(file);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		byte[] buff = content.getBytes();
		FSDataOutputStream os = null;
		try {
			os = fs.create(filePath);
			
			os.write(buff, 0, buff.length);
			System.out.println("Create: " + file);

		} finally {
			if (os != null)
				os.close();
		}
		fs.close();
	}

	public void addContentFile(String file, String content, int writeTime) throws IOException {
		Path filePath = new Path(file);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		byte[] buff = content.getBytes();
		byte[] enter = "\n".getBytes();
		FSDataOutputStream os = null;
		if(!fs.exists(filePath))
		{
			try {
				
				os = fs.create(filePath);
				
			for(int i = 0; i < writeTime; i ++){
				os.write(buff, 0, buff.length);
				os.write(enter, 0, enter.length);
			}
				System.out.println("Create: " + file);
	
			} finally {
				if (os != null)
					os.close();
			}
		}
		else
		{
			try {
				os = fs.append(filePath);
				
			for(int i = 0; i < writeTime; i ++){
				os.write(buff, 0, buff.length);
				os.write(enter, 0, enter.length);
			}
				System.out.println("Append: " + file);

			} finally {
				if (os != null)
					os.close();
			}
		}
		fs.close();
	}
	public void addContentFile(String file, String content) throws IOException {
		Path filePath = new Path(file);
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		byte[] buff = content.getBytes();
		FSDataOutputStream os = null;
		if(!fs.exists(filePath))
		{
			try {
				
				os = fs.create(filePath);
				
				os.write(buff, 0, buff.length);
				System.out.println("Create: " + file);
	
			} finally {
				if (os != null)
					os.close();
			}
		}
		else
		{
			try {
				os = fs.append(filePath);
				
				os.write(buff, 0, buff.length);
				System.out.println("Append: " + file);

			} finally {
				if (os != null)
					os.close();
			}
		}
		fs.close();
	}

	public String readWholeFile(String file) throws IOException {
	    Path filePath = new Path(file);
    	FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
			//byte[] buff = new byte[fs.getLength(filePath)];
			byte[] buff = new byte[(int)fs.getFileStatus(filePath).getLen()];
			FSDataInputStream os = null;
			if(fs.exists(filePath)){
				try {
					os = fs.open(filePath);
					os.readFully(0,buff);
					System.out.println("Read " + file + " !");
				} finally {
					if ( os != null)
								os.close();
				}	
			} else {
						System.out.println("The " + file + " doesn't exists !!!" );
			}
			fs.close();
			return new String(buff);
	}

	// test use cases
	public static void main(String[] args) throws IOException {
		HdfsOper ho  = new HdfsOper();
		try {
			ho.rm("/test/a.txt");
			ho.mkdir("/test/",(short)1);
			ho.addContentFile("/test/a.txt","hello world!",4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ho.readWholeFile("/test/a.txt");
		ho.cp("/test/a.txt","/test/b.txt");
		Configuration conf = config();
		HdfsOper hdfs = new HdfsOper(conf);
		hdfs.mkdir("moead/");
		hdfs.ls("/");
//		hdfs.ls("/user/root/input/");
//		hdfs.addContentFile("/tmp/test.txt", "\ntest again!!!\n");
//		hdfs.cat("/tmp/test.txt");
//		hdfs.addContentFile("/tmp/test.txt", "\ntest again!!!\n");
//		hdfs.cat("/tmp/test.txt");
		
		hdfs.ls("moead/0/");
		
		for(int i = 0; i <= 10; i ++)
		{
			hdfs.rm("moead/" + i + "/");
			
		}
		hdfs.rm("moead/moead.txt");
	}
}
