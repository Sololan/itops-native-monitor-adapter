package com.wejoyclass.itops.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wejoyclass.core.service.entity.BaseMysqlEntity;
import lombok.*;

import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Warning extends BaseMysqlEntity {

    private Long orgId; //根机构（T_BASE_ORG.ID）

    private String warningCode; //告警编号

    private Long warningLevel;  //告警级别(字典项)

    private String warningInfo; //告警内容

    private String ipAddress;   //IP地址

    private Date warningTime;   //告警时间

    private String warningStatus;   //告警状态

    private String triggerName;  //触发器名

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closeTime; //关闭时间

    private Long equipmentId;   //设备ID

    private Long monitorId; //监控ID

    @Transient
    private String hostName;    //from zabbix, 用于在t_ops_monitor表中, 查询equipmentId和monitorId
}
