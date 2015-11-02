package mr;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
 
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;
  
  public class HdfsOper{
		      private static final String HADOOP_URL="hdfs://localhost:8020";

		      public static void main(String[] args)throws Exception {
		          URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
		          final URL url=new URL(HADOOP_URL);
		          InputStream in = url.openStream();
		          OutputStream out=new FileOutputStream("hello.txt");
		          IOUtils.copyBytes(in, out, 1024,true);
		          out.close();
		          in.close();
  }
}
