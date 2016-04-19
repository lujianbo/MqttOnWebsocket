package io.github.lujianbo.mqtt.driver.common;

/**
 * 一条消息的抽象
 */
public class MQTTMessage {

    /**
     * 发送者
     * */
    public String sender;

    /**
     * 目标topic
     * */
    public String  topic;


    public int packetIdentifier;

    /**
     * 消息内容
     * */
    public byte[] playLoad;


    public MQTTMessage() {
    }

    public MQTTMessage(String sender, String topic, int packetIdentifier, byte[] playLoad) {
        this.sender = sender;
        this.topic = topic;
        this.packetIdentifier = packetIdentifier;
        this.playLoad = playLoad;
    }

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getPlayLoad() {
        return playLoad;
    }

    public void setPlayLoad(byte[] playLoad) {
        this.playLoad = playLoad;
    }
}