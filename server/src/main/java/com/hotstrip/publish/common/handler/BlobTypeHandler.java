package com.hotstrip.publish.common.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobTypeHandler extends BaseTypeHandler<String> {
    private static Logger logger = LoggerFactory.getLogger(BlobTypeHandler.class);

    // 指定字符集
    private static final String UTF8_charset = "UTF-8";

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
        byte[] bytes;
        try {
            // 把String转化成byte流
            bytes = s.getBytes(UTF8_charset);
        } catch (UnsupportedEncodingException e) {
            logger.error("String to blob failed...msg: [{}]", e.getMessage());
            throw new RuntimeException("Blob Encoding Error!");
        }
        preparedStatement.setBytes(i, bytes);
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if(resultSet.getBytes(s) == null){
            return null;
        }
        byte[] returnValue = resultSet.getBytes(s);
        try {
            // 把byte转化成string
            return new String(returnValue, UTF8_charset);
        } catch (UnsupportedEncodingException e) {
            logger.error("blob to String failed...msg: [{}]", e.getMessage());
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
        if(resultSet.getBytes(i) == null) {
            return null;
        }
        byte[] returnValue = resultSet.getBytes(i);
        try {
            return new String(returnValue, UTF8_charset);
        } catch (UnsupportedEncodingException e) {
            logger.error("blob to String failed...msg: [{}]", e.getMessage());
            throw new RuntimeException("Blob Encoding Error!");
        }
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int i) {
        return null;
    }
}
