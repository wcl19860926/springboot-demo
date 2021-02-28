package com.study.oauth2.server.mybaties.type.handler;

import com.study.oauth2.server.enums.Gender;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenderTypeHandler implements TypeHandler<Gender> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Gender parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Gender.getValue(parameter));
    }

    @Override
    public Gender getResult(ResultSet rs, String columnName) throws SQLException {
        return Gender.getGender(rs.getString(columnName));
    }

    @Override
    public Gender getResult(ResultSet rs, int columnIndex) throws SQLException {
        return Gender.getGender(rs.getString(columnIndex));
    }

    @Override
    public Gender getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Gender.getGender(cs.getString(columnIndex));
    }
}
