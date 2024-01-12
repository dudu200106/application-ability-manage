package com.dsj.csp.manage.util;


//雪花算法uuid
public class SnowflakeIdGenerator {
    private static final long EPOCH = 1632280972000L; // 设置一个起始时间点，通常是系统上线时间
    private static final long MACHINE_BITS = 10L;//jiqi idweishu
    private static final long SEQUENCE_BITS = 12L;//xuliehao id weishu

    private static final long MAX_MACHINE_ID = -1L ^ (-1L << MACHINE_BITS);  //max
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS); //max

    private long machineId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeIdGenerator(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("Machine ID must be between 0 and " + MAX_MACHINE_ID);
        }
        this.machineId = machineId;
    }

    public synchronized long generateId() {
        long timestamp = System.currentTimeMillis() - EPOCH;

        if (timestamp < 0) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate an ID.");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 如果同一毫秒内的序列号用尽，等待下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp << (MACHINE_BITS + SEQUENCE_BITS)) |
                (machineId << SEQUENCE_BITS) |
                sequence);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis() - EPOCH;
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis() - EPOCH;
        }
        return timestamp;
    }

    public static String createId(){

        long machineId = 1L;
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(machineId);
        long id = idGenerator.generateId();
        return Long.toString(id);
    }

    public static void main(String[] args) {
        //时间戳
        //机器id
        //序列号
        // 在实际应用中，你需要为每个机器分配一个唯一的machineId
        long machineId = 1L;
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(machineId);

        // 生成一些标识符示例
        for (int i = 0; i < 5; i++) {
            long id = idGenerator.generateId();
            System.out.println("Generated ID: " + id);

            System.out.println(Long.bitCount(id));
        }
    }
}

