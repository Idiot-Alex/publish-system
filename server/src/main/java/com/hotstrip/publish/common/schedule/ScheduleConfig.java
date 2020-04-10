package com.hotstrip.publish.common.schedule;

import com.github.pagehelper.Page;
import com.hotstrip.publish.common.util.LocalDateUtil;
import com.hotstrip.publish.model.Agent;
import com.hotstrip.publish.service.AgentService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.ZonedDateTime;

@Component
@EnableScheduling
public class ScheduleConfig {
    private static Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);

    @Resource
    private AgentService agentService;

    /**
     * 从0分钟后开始  每5分钟执行一次
     * 更新终端在线情况
     */
    @Scheduled(cron = "0 1/5 * * * ?")
    public void updateAgentOnlineStatus(){
        logger.info("scheduled......update agent online status");
        long startTime = System.currentTimeMillis();
        // 把时间 转换为天数   更新终端在线状态
        String currentDate = LocalDateUtil.getDateTimeAsString(ZonedDateTime.now().toLocalDateTime(), LocalDateUtil.SDF_YMD_PATTERN);
        // 获取所有终端信息
        Page<Agent> list = agentService.getAgents(new RowBounds(), new Agent());
        for (Agent agent : list){
            Agent info = new Agent();
            info.setAgentId(agent.getAgentId());        //设置agentId
            info.setOnlineStatus(getOnlineStatus(currentDate, agent));  // 修改在线天数
            agentService.update(info);
        }
        long endTime = System.currentTimeMillis();
        logger.info("update agent online status task......total cost: [{}] ms", endTime - startTime);
    }

    // 计算在线状态
    private int getOnlineStatus(String currentDate, Agent agent){
        boolean flag = true;
        int day = 0;
        long minutes = 0;
        // TCP 连接方式或者 SOCKET 连接方式断线
        // 获取系统当前时间  对比终端上一次心跳时间  转换为分钟数  更新终端心跳频率
        if (agent.getLastHeartbeatTime() != null) {
            // logger.info("终端上次心跳时间：[{}]", LocalDateUtil.getDateTimeAsString(ZonedDateTime.now().toLocalDateTime(), LocalDateUtil.SIMPLE_DATE_HOURS_PATTERN));
            String nextDay = LocalDateUtil.getDateTimeAsString(LocalDateUtil.getDateTimeOfTimestamp(agent.getLastHeartbeatTime().getTime()).plusDays(1L), LocalDateUtil.SDF_YMD_PATTERN);
            // 上次心跳时间获取第二天时间  如果年月日匹配当前时间  就断线一天  不匹配就计算分钟数
            if (currentDate.equals(nextDay)) {
                day = 1;    //断线一天
                flag = false;   //不计算分钟数
            }
            minutes = Math.abs(Duration.between(ZonedDateTime.now().toLocalDateTime(), LocalDateUtil.getDateTimeOfTimestamp(agent.getLastHeartbeatTime().getTime())).toMinutes());
        } else {
            minutes = Math.abs(Duration.between(ZonedDateTime.now().toLocalDateTime(), LocalDateUtil.getDateTimeOfTimestamp(agent.getCreateTime().getTime())).toMinutes());
        }

        // 如果分钟数 大于5分钟 且大于终端心跳频率  小于24小时  则当天断线  对于大于一天的 向上取整
        if (flag && minutes > 5 && minutes > agent.getHeartbeatFrequency()) {
            day = (int) Math.ceil(minutes / 60f / 24.0);
        }
        return day;
    }
}
