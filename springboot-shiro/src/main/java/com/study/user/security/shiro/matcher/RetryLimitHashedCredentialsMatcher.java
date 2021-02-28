package com.study.user.security.shiro.matcher;


import com.study.user.security.shiro.matcher.AbstractCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class RetryLimitHashedCredentialsMatcher extends AbstractCredentialsMatcher {
    @Autowired
    @Lazy
    //private  UserService userService;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    protected void lockUser(String userId, String accountName) {
       /* List<Integer> accountList = new ArrayList<>();
        accountList.add(Integer.parseInt(userId));
        userService.lockUserByUserIds(accountList);
        passwordRetryCache.remove(accountName);
        PrincipalLogUtils.addOperationLogNew(ModuleNames.SYS_BASE, ModuleNames.SYS_LOGIN, UserActions.LOCK,userId,
                accountName + "输入错误次数过多，被系统锁定");*/
    }

    @Override
    protected String getPasswordRetryCacheName() {
        return "1";

    }

    @Override
    protected int getDefaultLoginAttemptTimes() {
        return 0;
    }

    @Override
    protected int getDefaultForErrPwdTimes() {
        return 0;
    }
}
