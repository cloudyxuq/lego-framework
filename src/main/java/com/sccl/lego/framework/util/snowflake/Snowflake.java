package com.sccl.lego.framework.util.snowflake;




import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @Title:
 * @Package: com.sccl.lego.framework.util.snowflake
 * @Description: (用一句话描述该文件做什么)
 * @Author: tinker
 * @Date: 2020-03-17
 * @Version: V1.0
 */
public class Snowflake {
    /**
     * 时间部分所占长度
     */
    private static final int TIME_LEN = 41;
    /**
     * 数据中心id所占长度
     */
    private static final int DATA_LEN = 5;
    /**
     * 机器id所占长度
     */
    private static final int WORK_LEN = 5;
    /**
     * 毫秒内序列所占长度
     */
    private static final int SEQ_LEN = 12;
    /**
     * 时间部分向左移动的位数 22
     */
    private static final int TIME_LEFT_BIT = 64 - 1 - TIME_LEN;
    /**
     * 数据中心id最大值 31
     */
    private static final int DATA_MAX_NUM = ~(-1 << DATA_LEN);
    /**
     * 机器id最大值 31
     */
    private static final int WORK_MAX_NUM = ~(-1 << WORK_LEN);
    /**
     * 随机获取数据中心id的参数 32
     */
    private static final int DATA_RANDOM = DATA_MAX_NUM + 1;
    /**
     * 随机获取机器id的参数 32
     */
    private static final int WORK_RANDOM = WORK_MAX_NUM + 1;
    /**
     * 数据中心id左移位数 17
     */
    private static final int DATA_LEFT_BIT = TIME_LEFT_BIT - DATA_LEN;
    /**
     * 机器id左移位数 12
     */
    private static final int WORK_LEFT_BIT = DATA_LEFT_BIT - WORK_LEN;
    /**
     * 毫秒内序列的最大值 4095
     */
    private static final long SEQ_MAX_NUM = ~(-1 << SEQ_LEN);
    private static Snowflake snowflake = null;
    /**
     * 定义起始时间 2015-01-01 00:00:00
     */
    private long startTime = 1420041600000L;
    /**
     * 上次生成ID的时间截
     */
    private long lastTimeStamp = -1L;
    /**
     * 自动获取数据中心id（可以手动定义 0-31之间的数）
     */
    private long DATA_ID = getDataId();
    /**
     * 自动机器id（可以手动定义 0-31之间的数）
     */
    private long WORK_ID = getWorkId();
    /**
     * 上一次的毫秒内序列值
     */
    private long sequence = 0L;

    private Snowflake() {
    }

    public static synchronized Snowflake getInstance() {
        if (snowflake == null) {
            snowflake = new Snowflake();
        }
        return snowflake;
    }

    /**
     * 获取下一不同毫秒的时间戳，不能与最后的时间戳一样
     */
    private static long nextMillis(long lastMillis) {
        long now = SystemClock.now();
        while (now <= lastMillis) {
            now = SystemClock.now();
        }
        return now;
    }

    /**
     * 获取字符串s的字节数组，然后将数组的元素相加，对（max+1）取余
     */
    private static int getHostId(String s, int max) {
        byte[] bytes = s.getBytes();
        int sums = 0;
        for (int b : bytes) {
            sums += b;
        }
        return sums % (max + 1);
    }

    /**
     * 根据 host address 取余，发生异常就获取 0到31之间的随机数
     */
    private static int getWorkId() {
        try {
            return getHostId(Inet4Address.getLocalHost().getHostAddress(), WORK_MAX_NUM);
        } catch (UnknownHostException e) {
            return new Random().nextInt(WORK_RANDOM);
        }
    }

    /**
     * 根据 host name 取余，发生异常就获取 0到31之间的随机数
     */
    private static int getDataId() {
        try {
            return getHostId(Inet4Address.getLocalHost().getHostName(), DATA_MAX_NUM);
        } catch (UnknownHostException e) {
            return new Random().nextInt(DATA_RANDOM);
        }
    }

    public synchronized long generateId() {
        long now = SystemClock.now();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (now < lastTimeStamp) {
            long offset = lastTimeStamp - now;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    now = SystemClock.now();
                    if (now < lastTimeStamp) {
                        throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }
        }

        if (now == lastTimeStamp) {
            sequence = (sequence + 1) & SEQ_MAX_NUM;
            if (sequence == 0) {
                now = nextMillis(lastTimeStamp);
            }
        } else {
            sequence = 0;
        }

        //上次生成ID的时间截
        lastTimeStamp = now;

        return ((now - startTime) << TIME_LEFT_BIT) | (DATA_ID << DATA_LEFT_BIT) | (WORK_ID << WORK_LEFT_BIT) | sequence;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDATA_ID() {
        return DATA_ID;
    }

    public void setDATA_ID(long DATA_ID) {
        this.DATA_ID = DATA_ID;
    }

    public long getWORK_ID() {
        return WORK_ID;
    }

    public void setWORK_ID(long WORK_ID) {
        this.WORK_ID = WORK_ID;
    }


}
