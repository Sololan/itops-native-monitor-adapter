import com.alibaba.fastjson.JSONObject;
import io.github.sololan.zabbix.api.DefaultZabbixConnect;
import io.github.sololan.zabbix.request.RequestEntity;
import io.github.sololan.zabbix.request.RequestEntityBuilder;

public class test {
    public static void main(String[] args) {
        DefaultZabbixConnect defaultZabbixConnect = DefaultZabbixConnect.newZabbixConnect("http://192.170.1.39/zabbix/api_jsonrpc.php").init()
                .login("Admin", "zabbix");
        String host = "192.170.1.91";
        JSONObject filter = new JSONObject();
        filter.put("host", new String[] { host });
        JSONObject search = new JSONObject();
        search.put("key_", "system.cpu.util[,system]");
        System.out.println(defaultZabbixConnect.call(RequestEntityBuilder.newBuilder().method("item.get").paramEntry("filter",filter).build()));
        defaultZabbixConnect.destroy();
    }
}
