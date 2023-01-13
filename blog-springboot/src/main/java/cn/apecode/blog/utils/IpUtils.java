package cn.apecode.blog.utils;

import cn.apecode.blog.dto.IpSourceDto;
import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.apecode.blog.constant.CommonConst.PROVINCE;
import static cn.apecode.blog.constant.CommonConst.AREA;
import static cn.apecode.blog.constant.CommonConst.CITY;
/**
 * @description: IP工具类
 * @author: apecode
 * @date: 2022-05-27 18:55
 **/
@SuppressWarnings("all")
@Component
public class IpUtils implements EnvironmentAware {

    private static Environment env;
    private static final double EARTH_RADIUS = 6371000; //赤道半径

    /**
     * @description: 获取用户ip地址
     * @param request
     * @return {@link String}
     * @auther apecode
     * @date 2022/5/27 18:56
    */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * @description: 解析ip地址
     * @param ipAddress
     * @return {@link String}
     * @auther apecode
     * @date 2022/5/27 18:58
    */
    public static String getIpSource(String ipAddress) {
        try {
            URL url = new URL("http://opendata.baidu.com/api.php?query=" + ipAddress + "&co=&resource_id=6006&oe=utf8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            Map map = JSON.parseObject(result.toString(), Map.class);
            List<Map<String, String>> data = (List) map.get("data");
            return data.get(0).get("location").split(" ")[0];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @description: 通过高德获取城市信息
     * @param ipAddress
     * @return {@link IpSourceDto}
     * @auther apecode
     * @date 2022/6/10 18:09
    */
    public static IpSourceDto getIpSourceFromAmap(String ipAddress) {
        try {
            URL url = new URL("https://restapi.amap.com/v5/ip?key=" + env.getProperty("amap.key") + "&type=4&ip=" + ipAddress);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            String line = null;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            Map map = JSON.parseObject(result.toString(), Map.class);
            return IpSourceDto.builder()
                    .country((String) map.get("country"))
                    .province((String) map.get("province"))
                    .city((String) map.get("city"))
                    .district((String) map.get("district"))
                    .location((String) map.get("location"))
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @description: 获取访问设备
     * @param request
     * @return {@link UserAgent}
     * @auther apecode
     * @date 2022/5/27 18:58
    */
    public static UserAgent getUserAgent(HttpServletRequest request){
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    /**
     * @description: 切割省份
     * @param province
     * @return {@link String}
     * @auther apecode
     * @date 2022/5/27 18:59
    */
    public static String cutProvince(String province) {
        if (province.indexOf(PROVINCE) != -1) {
            return province.substring(0,province.indexOf(PROVINCE)+1);
        } else if (province.indexOf(AREA) != -1) {
            return province.substring(0,province.indexOf(AREA)+1);
        } else if (province.indexOf(CITY) != -1) {
            return province.substring(0,province.indexOf(CITY)+1);
        } else {
            return province;
        }
    }

    public static String cutProvince(IpSourceDto ipSource) {
        StringBuffer source = new StringBuffer();
        if (Objects.nonNull(ipSource.getCountry()) && ipSource.getCountry().equals("局域网")) {
            return ipSource.getCountry();
        }
        if (Objects.nonNull(ipSource.getCountry())) {
            source.append(ipSource.getCountry()).append(" ");
        } else {
            return "未知";
        }
        if (Objects.nonNull(ipSource.getProvince())) {
            source.append(ipSource.getProvince()).append(" ");
        }
        if (Objects.nonNull(ipSource.getCity())) {
            source.append(ipSource.getCity()).append(" ");
        }
        if (Objects.nonNull(ipSource.getDistrict())) {
            source.append(ipSource.getDistrict());
        }
        return source.toString();
    }

    public static String getLocation(String ipAddress) {
        IpSourceDto ipSourceFromAmap = getIpSourceFromAmap(ipAddress);
        if (Objects.nonNull(ipSourceFromAmap) && !ipSourceFromAmap.getLocation().split(",")[0].equals("null")) {
            return ipSourceFromAmap.getLocation();
        }
        return null;
    }

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    public static String getSource(String ipAddress) {
        IpSourceDto ipSourceFromAmap = getIpSourceFromAmap(ipAddress);
        if (Objects.nonNull(ipSourceFromAmap)) {
            return cutProvince(ipSourceFromAmap);
        }
        return getIpSource(ipAddress);
    }

    /**
     * @description: 获取距离
     * <br/>
     * double v = distanceUtils.GetDistance(109.371319, 22.155406, 108.009758, 21.679011);
     * @param lon1 经度
     * @param lat1 纬度
     * @param lon2 经度
     * @param lat2 纬度
     * @return {@link double}
     * @auther apecode
     * @date 2022/7/9 12:25
     *
    */
    public static double getDistance(double lon1,double lat1,double lon2, double lat2) {
        double radiansAX = Math.toRadians(lon1); // A经弧度
        double radiansAY = Math.toRadians(lat1); // A纬弧度
        double radiansBX = Math.toRadians(lon2); // B经弧度
        double radiansBY = Math.toRadians(lat2); // B纬弧度
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX) + Math.sin(radiansAY) * Math.sin(radiansBY);
        double acos = Math.acos(cos); // 反余弦值
        double v = EARTH_RADIUS * acos; // 最终结果
        return Math.rint(v/100)/10;
    }

    /**
     * @description: 获取距离
     * @param location1 经纬度1
     * @param location2 经纬度2
     * @return {@link String}
     * @auther apecode
     * @date 2022/7/9 12:42
    */
    public static String getDistance(String location1, String location2) {
        String distance = null;
        if (StringUtils.isNotBlank(location1) && StringUtils.isNotBlank(location2)) {
            String[] l1 = location1.split(",");
            String[] l2 = location2.split(",");
            distance = String.valueOf(IpUtils.getDistance(Double.parseDouble(l1[0]), Double.parseDouble(l1[1]), Double.parseDouble(l2[0]), Double.parseDouble(l2[1])));
        }
        return distance;
    }

    /**
     * @description: 获取当前访问用户的经纬度
     * @param request
     * @return {@link String}
     * @auther apecode
     * @date 2022/7/9 16:45
    */
    public static String getNowUserRectangle(HttpServletRequest request) {
        // 从header中拿经纬度
        String rectangle = request.getHeader("rectangle");
        String location;
        if (StringUtils.isNotBlank(rectangle)) {
            location = SecurityUtils.decryptLocation(rectangle);
        } else {
            String ipAddress = IpUtils.getIpAddress(request);
            location = IpUtils.getLocation(ipAddress);
        }
        return location;
    }

    /**
     * @description: 获取当前访问用户经纬度（加密）
     * @param request
     * @return {@link String}
     * @auther apecode
     * @date 2022/7/9 16:52
    */
    public static String getNowUserRectangleEncrypt(HttpServletRequest request) {
        String rectangle = request.getHeader("rectangle");
        if (StringUtils.isNotBlank(rectangle)) {
            return rectangle;
        }
        String ipAddress = IpUtils.getIpAddress(request);
        String location = IpUtils.getLocation(ipAddress);
        return SecurityUtils.encrypt(location);
    }

}
