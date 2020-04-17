package com.wejoyclass.itops.local.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wejoyclass.core.util.RedisUtil;
import com.wejoyclass.itops.entity.Warning;
import com.wejoyclass.itops.local.service.WebHookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class WebHookServiceImpl implements WebHookService {

    @Override
    public void publishWarning(JSONObject warningJSON) {
        log.info("---接受到告警---");
        Warning warning = null;
        try {
            warning = constructWarning(warningJSON);
        } catch (ParseException ex) {
            throw new IllegalStateException("ParseException", ex);
        }
        //publish
        String warningKey = "warning";
        String warningString = JSON.toJSONString(warning);
        RedisUtil.publish(warningKey, warningString);
    }

    private Warning constructWarning(JSONObject warningJSON) throws ParseException {
        String subject = (String) warningJSON.get("Subject");
        log.info(subject);
        if (subject.length() > 500) { //todo info too long
            subject = subject.substring(0, 500);
        }
        String msg = (String) warningJSON.get("Message");
        JSONObject msgObject = JSON.parseObject(msg);
        Long priority = Long.valueOf((String) msgObject.get("priority"));
        String triggerStatus = (String) msgObject.get("triggerStatus");
        String triggerName;
        if(msgObject.getString("triggerName") == null || msgObject.getString("triggerName") == ""){
            triggerName = (String) msgObject.get("eventName");
        }else {
            triggerName = (String) msgObject.get("triggerName");
        }
        String hostName = (String) msgObject.get("hostName");

        Warning warning = Warning.builder()
                .warningCode((String) msgObject.get("eventId"))
                .warningLevel(priority > 3 ? 3 : priority)
                .warningInfo(subject)
                .triggerName(triggerName)
                .hostName(hostName.trim())
                .build();

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        if (triggerStatus.equals("PROBLEM")) {
            warning.setWarningStatus("0");      //未关闭
            warning.setIpAddress((String) msgObject.get("hostIp"));
            warning.setWarningTime(dateFormat.parse((String) msgObject.get("alartTime")));
            warning.setCreateTime(warning.getWarningTime());
            warning.setCreateUser(1000L);
            warning.setCreateUsername("zabbix");
        } else if (triggerStatus.equals("OK")) {
            warning.setWarningStatus("1");  // 已关闭
            warning.setCloseTime(dateFormat.parse((String) msgObject.get("recoveryTime")));
        }

        return warning;
    }

}
