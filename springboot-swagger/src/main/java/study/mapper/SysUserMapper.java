package study.mapper;

import com.github.pagehelper.Page;
import com.study.entity.SysUser;

import java.util.Map;

public interface SysUserMapper {
    /**
    sys_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
    sys_user
     *
     * @mbg.generated
     */
    int insertSelective(SysUser record);

    /**
    sys_user
     *
     * @mbg.generated
     */
    SysUser selectByPrimaryKey(Integer id);

    /**
    sys_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SysUser record);

    /**
     *
     */
    Page<SysUser> queryListPage(Map<String, Object> params);
}