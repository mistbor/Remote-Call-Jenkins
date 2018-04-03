package com.weiqing.snarl.app.jenkins;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author miaoying
 * @date 4/2/18
 */
public class JenkinsDemo {

    public static void main(String[] args) {
        JenkinsBuildService jenkinsBuildService = new JenkinsBuildService();
        String jobName = "Brawn_wireless";
        Map<String, String> map = new HashMap<>();
        map.put("type", "external");
        map.put("version", "201804021853_test");
        map.put("engine", "2018-03-23_WIFI_bctc_case_list_Release_Koopa_1.1.40.tar.gz");
        map.put("count", "01");
        map.put("password", "weiqing");
        map.put("LICENSE_INFO", "wireless black vulnerability detection system trial edition");
        map.put("LICENSE_PRODUCT", "11");
        map.put("cve_file_path", "2018-03-23_WIFI_bctc_case_list/2018-03-23_WIFI_bctc_case_list_Snapshot_Koopa_1.1.39.txt");
        map.put("token", "Brawn_wireless");

        jenkinsBuildService.build(jobName, map);
    }
}
