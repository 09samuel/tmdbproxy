package com.sastudios.tmdbproxy.util;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipRedisSerializer<T> implements RedisSerializer<T> {

    private final RedisSerializer<T> inner;

    public GzipRedisSerializer(RedisSerializer<T> inner) {
        this.inner = inner;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return null;
        }
        byte[] uncompressed = inner.serialize(t);
        if (uncompressed == null || uncompressed.length == 0) {
            return uncompressed;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzipOut = new GZIPOutputStream(bos)) {
            gzipOut.write(uncompressed);
            gzipOut.finish();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("GZIP compression failed", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             GZIPInputStream gzipIn = new GZIPInputStream(bis)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            int len;
            while ((len = gzipIn.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return inner.deserialize(bos.toByteArray());
        } catch (IOException e) {
            throw new SerializationException("GZIP decompression failed", e);
        }
    }
}

