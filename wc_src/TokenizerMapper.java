/**
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *   *  you may not use this file except in compliance with the License.
 *    *  You may obtain a copy of the License at
 *     *
 *      *      http://www.apache.org/licenses/LICENSE-2.0
 *       *
 *        *  Unless required by applicable law or agreed to in writing, software
 *         *  distributed under the License is distributed on an "AS IS" BASIS,
 *          *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *           *  See the License for the specific language governing permissions and
 *            *  limitations under the License.
 *             */


package org.apache.hadoop.examples;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


  public class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
      }
    }
  }
  
 
