package com.example.kafkademo.consumer;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @version 0.0.1
 * @program: kafkademo
 * @description: kafka消费者
 * @packname: com.example.kafkademo.consumer
 * @author: wzp
 * @create: 2019-10-23 10:23
 */
public class KafkaConsumer extends Thread {
    private final ConsumerConnector consumer;
    private  String topic = "test";

    public KafkaConsumer(String topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig());
        this.topic = topic;
    }
    private static ConsumerConfig createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "127.0.0.1:2181");
        props.put("group.id", "topic");
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "10000");
        return new ConsumerConfig(props);
    }
    public void run() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println(new String("1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(new String(it.next().message()));
                consumer.commitOffsets();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public static void main(String[] args) {
        KafkaConsumer consumerThread = new KafkaConsumer("topic");
        consumerThread.start();
    }
}
