/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package org.apache.spark.examples;

package practise;

import scala.Tuple2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class JavaReduceByKeyTest{

  public static void main(String[] args) throws Exception {
			SparkConf sparkConf = new SparkConf().setAppName("Java ReduceByKey test");
			JavaSparkContext ctx = new JavaSparkContext(sparkConf);
			JavaRDD<String>  dataRDD = ctx.parallelize(Arrays.asList("1 2","1 30","1 4","2 4","2 1","2 10","2 30"));
			JavaPairRDD<String,Integer> dataPairRDD = dataRDD.mapToPair(
												new PairFunction<String,String,Integer>() {
													public Tuple2<String,Integer> call(String s) {
														String[] ss = s.split(" ");
														return new Tuple2<String,Integer>(ss[0],Integer.parseInt(ss[1]));
													}
												}
											);

			JavaPairRDD<String,Integer> reduceDataPairRDD = dataPairRDD.reduceByKey(
												new Function2<Integer,Integer,Integer>() {
													public Integer call(Integer i1,Integer i2){
														if(i1 > i2) return i1;
														return i2;
													}
												}
											);
			List<Tuple2<String,Integer>> output = reduceDataPairRDD.collect();
			for(Tuple2<?,?> t : output){
				System.out.println(t._1() + " : " + t._2());
			}
			ctx.stop();
  }
}
