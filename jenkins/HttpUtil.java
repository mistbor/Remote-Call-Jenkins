package com.weiqing.snarl.app.jenkins;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

/**
 * @author miaoying
 * @date 4/2/18
 */
@Slf4j
public class HttpUtil {
    public static void close(CloseableHttpResponse rsp) {
        if (!StringUtils.isEmpty(rsp)) {
            try {
                EntityUtils.consume(rsp.getEntity());
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }
}
