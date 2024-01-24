package com.dsj.csp.manage.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/24 17:04
 * @Todo:
 */
public class TimeTolong {

    public static long timetolong(Date datetime) {
        long time = datetime.getTime();
        return time;
    }
}
