package com.microservices.common.generator;

public class SnowflakeIdService {
    private Snowflake snowflake;

    public SnowflakeIdService()
    {
        this.snowflake = new Snowflake();
    }

    public SnowflakeIdService(Snowflake iSnowflake)
    {
        this.snowflake = iSnowflake;
    }

    public String getId()
    {
        return String.format("%019d", new Object[] { getLongId() });
    }

    public Long getLongId()
    {
        return Long.valueOf(this.snowflake.nextId());
    }
}
