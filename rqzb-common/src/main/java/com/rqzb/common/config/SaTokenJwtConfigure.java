package com.rqzb.common.config;

import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaTokenJwtConfigure {

    @Bean
    public StpLogic stpLogic() {
        return new StpLogicJwtForStateless();
    }
}
